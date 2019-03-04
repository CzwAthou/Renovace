package com.pince.renovace2.request.upload;

import android.support.annotation.WorkerThread;
import android.text.TextUtils;
import android.util.Pair;

import com.pince.renovace2.Renovace;
import com.pince.renovace2.config.Config;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author athou
 * @date 2017/12/13
 */
public class UploadRequest {

    private String url;
    private Map<String, String> params;
    private Set<FileInput> fileInputs;

    private OkHttpClient client;
    private UploadListener uploadListener;

    public static class FileInput {
        String filePath;
        File file;
        MediaType mediaType = MediaType.parse("multipart/form-data");
        String buket;

        public FileInput(String filePath, String buket, MediaType mediaType) {
            this.filePath = filePath;
            this.buket = buket;
            this.mediaType = mediaType;
        }

        public FileInput(File file, String buket, MediaType mediaType) {
            this.file = file;
            this.mediaType = mediaType;
        }
    }

    public UploadRequest(Class<? extends Config> configClass) {
        this.client = Renovace.getClient(configClass);
        if (client != null) {
            client.newBuilder()
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request original = chain.request();
                            Request request = original.newBuilder()
                                    .method(original.method(), new ProgressRequestBody(original.body(), uploadListenerProxy))
                                    .build();
                            return chain.proceed(request);
                        }
                    })
                    .connectTimeout(5000, TimeUnit.MILLISECONDS)
                    .build();
        }
    }

    public UploadRequest url(String url) {
        this.url = url;
        return this;
    }

    public UploadRequest withParams(String key, String value) {
        if (params == null) {
            params = new HashMap<>();
        }
        params.put(key, value);
        return this;
    }

    public UploadRequest addFile(FileInput fileInput) {
        if (fileInputs == null) {
            fileInputs = new HashSet<>();
        }
        fileInputs.add(fileInput);
        return this;
    }

    /**
     * 进度回调接口
     */
    private UploadListener uploadListenerProxy = new UploadListener() {
        @Override
        public void onStart() {
            if (uploadListener != null) {
                uploadListener.onStart();
            }
        }

        @Override
        public void onProgress(long currentLength, long total, boolean done) {
            if (uploadListener != null) {
                uploadListener.onProgress(currentLength, total, done);
            }
        }

        @Override
        public void onError(Throwable throwable) {
            if (uploadListener != null) {
                uploadListener.onError(throwable);
            }
        }

        @Override
        public void onSuccess(Response response) {
            if (uploadListener != null) {
                uploadListener.onSuccess(response);
            }
        }
    };


    /**
     * 包装OkHttpClient，用于上传文件的回调
     *
     * @return 包装后的OkHttpClient
     */
    private OkHttpClient getClient() {
        if (client != null) {
            return client;
        }
        return new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();
                        Request request = original.newBuilder()
                                .method(original.method(), new ProgressRequestBody(original.body(), uploadListenerProxy))
                                .build();
                        return chain.proceed(request);
                    }
                })
                .retryOnConnectionFailure(true)
                .connectTimeout(5000, TimeUnit.MILLISECONDS)
                .build();
    }

    private okhttp3.Call createCall() {
        //构造上传请求，类似web表单
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            builder.addFormDataPart(key, value);
        }
        if (fileInputs != null) {
            for (FileInput fileInput : fileInputs) {
                File file = null;
                if (fileInput.file != null) {
                    file = fileInput.file;
                } else if (!TextUtils.isEmpty(fileInput.filePath)) {
                    file = new File(fileInput.filePath);
                }
                if (file != null && file.exists() && !TextUtils.isEmpty(fileInput.buket)) {
                    builder.addFormDataPart(fileInput.buket, file.getName(), RequestBody.create(fileInput.mediaType, file));
                }
            }
        }
        RequestBody requestBody = builder.build();
        Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .post(requestBody);
        return getClient()
                .newCall(requestBuilder.build());
    }

    @WorkerThread
    public Response uploadSync(UploadListener uploadListener) throws IOException {
        this.uploadListener = uploadListener;
        if (uploadListenerProxy != null) {
            uploadListenerProxy.onStart();
        }
        return createCall().execute();
    }

    public void uploadAsync(UploadListener uploadListener) {
        this.uploadListener = uploadListener;
        if (uploadListenerProxy != null) {
            uploadListenerProxy.onStart();
        }
        createCall().enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (uploadListenerProxy != null) {
                    uploadListenerProxy.onError(e);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //do something?
                uploadListenerProxy.onSuccess(response);
            }
        });
    }

    public Observable<Pair<Long, Long>> upload() {
        return Observable.create(new ObservableOnSubscribe<Pair<Long, Long>>() {
            @Override
            public void subscribe(final ObservableEmitter<Pair<Long, Long>> emitter) throws Exception {
                uploadAsync(new UploadListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onProgress(long currentLength, long total, boolean done) {
                        if (emitter != null) {
                            emitter.onNext(new Pair<Long, Long>(currentLength, total));
                            if (done) {
                                emitter.onComplete();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        if (emitter != null) {
                            emitter.onError(throwable);
                        }
                    }

                    @Override
                    public void onSuccess(Response response) {
                    }
                });
            }
        });
    }
}
