package com.pince.renovace2.request.upload;

import android.support.annotation.WorkerThread;
import android.text.TextUtils;
import android.util.Pair;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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

    private final String url;
    private String filePath;
    private String buket;
    private File file;
    private MediaType mediaType = MediaType.parse("multipart/form-data");
    private Map<String, String> params;

    public UploadRequest(String url, String filePath, String buket) {
        this.url = url;
        this.filePath = filePath;
        this.buket = buket;
    }

    public UploadRequest(String url, File file, String buket) {
        this.url = url;
        this.file = file;
        this.buket = buket;
    }

    private OkHttpClient client;
    private UploadListener uploadListener;

    public UploadRequest client(OkHttpClient client) {
        this.client = client;
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
        return this;
    }

    public UploadRequest mediaType(MediaType mediaType) {
        this.mediaType = mediaType;
        return this;
    }

    public UploadRequest withParams(String key, String value) {
        if (params == null) {
            params = new HashMap<>();
        }
        params.put(key, value);
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

    @WorkerThread
    public Response uploadSync(UploadListener uploadListener) throws IOException {
        if (file == null) {
            file = new File(filePath);
        }
        this.uploadListener = uploadListener;

        //构造上传请求，类似web表单
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            builder.addFormDataPart(key, value);
        }
        if (file.exists() && TextUtils.isEmpty(buket)) {
            builder.addFormDataPart(buket, file.getName(), RequestBody.create(mediaType, file));
        }
        RequestBody requestBody = builder.build();
        Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .post(requestBody);
        okhttp3.Call call = getClient()
                .newCall(requestBuilder.build());
        if (uploadListenerProxy != null) {
            uploadListenerProxy.onStart();
        }
        return call.execute();
    }

    public void uploadAsync(UploadListener uploadListener) {
        if (file == null) {
            file = new File(filePath);
        }
        this.uploadListener = uploadListener;

        //构造上传请求，类似web表单
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            builder.addFormDataPart(key, value);
        }
        if (file.exists() && !TextUtils.isEmpty(buket)) {
            builder.addFormDataPart(buket, file.getName(), RequestBody.create(mediaType, file));
        }
        RequestBody requestBody = builder.build();
        Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .post(requestBody);

        okhttp3.Call call = getClient()
                .newCall(requestBuilder.build());
        if (uploadListenerProxy != null) {
            uploadListenerProxy.onStart();
        }
        call.enqueue(new Callback() {
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
