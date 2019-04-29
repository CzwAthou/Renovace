package com.renovace.common.download;

import android.os.Environment;
import android.support.annotation.WorkerThread;
import android.text.TextUtils;
import android.util.Pair;

import com.renovace.common.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.BufferedSource;

/**
 * @author athou
 * @date 2017/12/13
 */
public class DownloadRequest {

    private String url;
    private String savePath;

    public DownloadRequest(String url) {
        this.url = url;
    }

    public DownloadRequest(String url, String savePath) {
        this.url = url;
        savePath(savePath);
    }

    public DownloadRequest savePath(String path) {
        this.savePath = path;
        return this;
    }

    private OkHttpClient newClient(final DownloadListener downloadListener) {
        return new OkHttpClient.Builder()
                .addNetworkInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        okhttp3.Response originalResponse = chain.proceed(chain.request());
                        return originalResponse.newBuilder()
                                .body(new ProgressResponseBody(originalResponse.body(), downloadListener))
                                .build();

                    }
                })
                .retryOnConnectionFailure(true)
                .connectTimeout(5000, TimeUnit.MILLISECONDS)
                .build();
    }

    private File saveDataToFile(Response response) throws IOException {
        if (TextUtils.isEmpty(savePath)) {
            String name = Utils.MD5(url);
            savePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + name;
        }
        ResponseBody responseBody = response.body();
//                        String suffix = "." + responseBody.contentType();
//                        if (!savePath.endsWith(suffix)) {
//                            savePath = savePath + suffix;
//                        }
        File file = new File(savePath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();

        BufferedSource is = responseBody.source();
        FileOutputStream fos = new FileOutputStream(file);
        byte[] buffer = new byte[4 * 1024];
        int len = 0;
        while ((len = is.read(buffer, 0, buffer.length)) > 0) {
            fos.write(buffer, 0, len);
        }
        fos.flush();
        fos.close();
        return file;
    }

    @WorkerThread
    public File downSync(DownloadListener downloadListener) {
        if (downloadListener != null) {
            downloadListener.onStart();
        }
        final OkHttpClient client = newClient(downloadListener);
        Call call = client.newCall(new Request.Builder()
                .url(url)
                .build());
        File file = null;
        try {
            file = saveDataToFile(call.execute());
        } catch (IOException e) {
            if (downloadListener != null) {
                downloadListener.onError(e);
            }
        } finally {
            return file;
        }
    }

    public void downloadAsync(final DownloadListener downloadListener) {
        final OkHttpClient client = newClient(downloadListener);
        client.newCall(new Request.Builder()
                .url(url)
                .build())
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        if (downloadListener != null) {
                            downloadListener.onError(e);
                        }
                    }

                    @Override
                    public void onResponse(Call call, Response response) {
                        try {
                            saveDataToFile(response);
                        } catch (IOException e) {
                            if (downloadListener != null) {
                                downloadListener.onError(e);
                            }
                        }
                    }
                });

        if (downloadListener != null) {
            downloadListener.onStart();
        }
    }

    /**
     * pair<Long,Long>前面的参数为当前已经下载的进度，后面的参数为文件总大小  <br>
     *
     * @return
     */
    public Observable<Pair<Long, Long>> download() {
        return Observable.create(new ObservableOnSubscribe<Pair<Long, Long>>() {
            @Override
            public void subscribe(final ObservableEmitter<Pair<Long, Long>> emitter) throws Exception {
                downloadAsync(new DownloadListener() {

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
