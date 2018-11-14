package com.testMaven;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.pince.renovace2.Renovace;
import com.pince.renovace2.config.BaseConfig;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Renovace.init(getApplicationContext(), true);
        Renovace.setDefaultConfig(DefaultConfig.class);
    }

    public void onClickMain(View view) {
        Log.e("Renovace", "start reqeust");
        Renovace.get().url("http://api.diaoyu-3.com/servertime")
                .bindLifecycle(this)
                .request(String.class)
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String s) throws Exception {
                        Thread.sleep(3000);
                        return s;
                    }
                })
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        Log.e("Renovace", "doOnSubscribe1  thread:" + Thread.currentThread().getName());
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        Log.e("Renovace", "doOnSubscribe2  thread:" + Thread.currentThread().getName());
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.e("Renovace", "result:" + s + "  thread:" + Thread.currentThread().getName());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e("Renovace", "Throwable:" + throwable.getMessage());
                        throwable.printStackTrace();
                    }
                });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 1000);
    }

    public static class DefaultConfig extends BaseConfig {


        @Override
        public void client(OkHttpClient.Builder clientBuilder) {

        }

        @Override
        protected String getBaseUrl() {
            return "http://api.diaoyu-3.com/";
        }
    }
}
