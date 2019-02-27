package com.testMaven;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;
import android.view.View;

import com.pince.renovace2.Renovace;
import com.pince.renovace2.config.BaseConfig;
import com.pince.renovace2.request.download.DownloadListener;
import com.pince.renovace2.request.download.DownloadRequest;
import com.pince.renovace2.request.upload.UploadListener;
import com.pince.renovace2.request.upload.UploadRequest;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Renovace.init(getApplicationContext(), true);
        Renovace.setDefaultConfig(DefaultConfig.class);

        new UploadRequest("", "", "")
                .client(Renovace.getClient(DefaultConfig.class))
                .mediaType(MediaType.parse(""))
                .withParams("", "")
                .uploadAsync(new UploadListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onProgress(long currentLength, long total, boolean done) {

                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }

                    @Override
                    public void onSuccess(Response response) {

                    }
                });
    }

    public void onClickSync(View view) {
//        Renovace.get().url("http://api.diaoyu-3.com/servertime")
//                .bindLifecycle(this)
//                .request(String.class)
//                .map(new Function<String, String>() {
//                    @Override
//                    public String apply(String s) throws Exception {
//                        Thread.sleep(3000);
//                        return s;
//                    }
//                })
//                .doOnSubscribe(new Consumer<Disposable>() {
//                    @Override
//                    public void accept(Disposable disposable) throws Exception {
//                        Log.e("Renovace", "doOnSubscribe1  thread:" + Thread.currentThread().getName());
//                    }
//                })
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .doOnSubscribe(new Consumer<Disposable>() {
//                    @Override
//                    public void accept(Disposable disposable) throws Exception {
//                        Log.e("Renovace", "doOnSubscribe2  thread:" + Thread.currentThread().getName());
//                    }
//                })
//                .subscribe(new Consumer<String>() {
//                    @Override
//                    public void accept(String s) throws Exception {
//                        Log.e("Renovace", "result:" + s + "  thread:" + Thread.currentThread().getName());
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//                        Log.e("Renovace", "Throwable:" + throwable.getMessage());
//                        throwable.printStackTrace();
//                    }
//                });
//
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                finish();
//            }
//        }, 1000);

        new Thread() {
            @Override
            public void run() {
                super.run();
                new DownloadRequest("http://dldir1.qq.com/foxmail/qqmail_android_5.5.5.10133788.2392_0.apk")
                        .savePath(getExternalCacheDir() + "download/test/test.apk")
                        .downSync(new DownloadListener() {
                            @Override
                            public void onStart() {
                                Log.e("download", "down onstart thread：" + Thread.currentThread().getName());
                            }

                            @Override
                            public void onProgress(long currentLength, long total, boolean done) {
                                Log.e("download", String.format("hasDown:%d  total:%d  done:%s  thread:%s", currentLength, total, String.valueOf(done), Thread.currentThread().getName()));
                            }

//                            @Override
//                            public void onComplete(File file) {
//                                Log.e("download", "down complete:" + file.getAbsolutePath());
//                            }

                            @Override
                            public void onError(Throwable throwable) {
                                Log.e("download", "down error thread：" + Thread.currentThread().getName());
                            }
                        });
            }
        }.start();
    }

    public void onClickAsync(View view) {
        new DownloadRequest("http://dldir1.qq.com/foxmail/qqmail_android_5.5.5.10133788.2392_0.apk")
                .savePath(getExternalCacheDir() + "download/test/test.apk")
                .downloadAsync(new DownloadListener() {
                    @Override
                    public void onStart() {
                        Log.e("download", "down onstart thread：" + Thread.currentThread().getName());
                    }

                    @Override
                    public void onProgress(long currentLength, long total, boolean done) {
                        Log.e("download", String.format("hasDown:%d  total:%d  done：%s  thread:%s", currentLength, total, String.valueOf(done), Thread.currentThread().getName()));
                    }

//                    @Override
//                    public void onComplete(File file) {
//                        Log.e("download", "down complete:" + file.getAbsolutePath());
//                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.e("download", "down error thread：" + Thread.currentThread().getName());
                    }
                });
    }


    public void onClickRxjava(View view) {
//        new DownloadRequest("http://dldir1.qq.com/foxmail/qqmail_android_5.5.5.10133788.2392_0.apk")
        new DownloadRequest("http://appdl.hicloud.com/dl/appdl/application/apk/74/743b3fbdc77f450f843568eca86aaec4/com.Plus.calculator.1811161101.apk?sign=portal@portal1543225723251&source=portalsite")
                .savePath(getExternalCacheDir() + "download/test/test.apk")
                .download()
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        Log.e("download", "down onstart thread：" + Thread.currentThread().getName());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Pair<Long, Long>>() {
                    @Override
                    public void accept(Pair<Long, Long> longLongPair) throws Exception {
                        if (longLongPair != null) {
                            Log.e("download", String.format("hasDown:%d  total:%d  thread:%s", longLongPair.first, longLongPair.second, Thread.currentThread().getName()));
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e("download", "down error thread：" + Thread.currentThread().getName());
                        throwable.printStackTrace();
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.e("download", "down complete thread：" + Thread.currentThread().getName());
                    }
                });
    }

    public static class DefaultConfig extends BaseConfig {


        @Override
        public void client(OkHttpClient.Builder clientBuilder) {

        }

        @Override
        protected String getBaseUrl() {
            return "http://api.diaoyu-3.com/";
        }

        @Override
        public void reset(Retrofit.Builder builder) {

        }
    }
}
