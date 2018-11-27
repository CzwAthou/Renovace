package com.pince.renovace2.request.upload;

import android.support.annotation.WorkerThread;
import android.util.Pair;

import java.io.File;
import java.io.IOException;
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
    private final String filePath;

    public UploadRequest(String url, String filePath) {
        this.url = url;
        this.filePath = filePath;
    }

    /**
     * 包装OkHttpClient，用于上传文件的回调
     *
     * @param uploadListener 进度回调接口
     * @return 包装后的OkHttpClient
     */
    private OkHttpClient newClient(final UploadListener uploadListener) {
        return new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();
                        Request request = original.newBuilder()
                                .method(original.method(), new ProgressRequestBody(original.body(), uploadListener))
                                .build();
                        return chain.proceed(request);
                    }
                })
                .retryOnConnectionFailure(true)
                .connectTimeout(5000, TimeUnit.MILLISECONDS)
                .build();
    }

    @WorkerThread
    public Response uoloadSync(UploadListener uploadListener) throws IOException {
        File file = new File(filePath);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        okhttp3.Call call = newClient(uploadListener)
                .newCall(new Request.Builder()
                        .post(body.body())
                        .build());
        if (uploadListener != null) {
            uploadListener.onStart();
        }
        return call.execute();
    }

    public void uoloadAsync(final UploadListener uploadListener) {
        File file = new File(filePath);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        okhttp3.Call call = newClient(uploadListener)
                .newCall(new Request.Builder()
                        .post(body.body())
                        .build());
        if (uploadListener != null) {
            uploadListener.onStart();
        }
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (uploadListener != null) {
                    uploadListener.onError(e);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //do something?
            }
        });
    }

    public Observable<Pair<Long, Long>> upload() {
        return Observable.create(new ObservableOnSubscribe<Pair<Long, Long>>() {
            @Override
            public void subscribe(final ObservableEmitter<Pair<Long, Long>> emitter) throws Exception {
                uoloadAsync(new UploadListener() {
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
                });
            }
        });
    }
}
