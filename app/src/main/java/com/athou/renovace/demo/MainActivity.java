/*
 * Copyright (c) 2016  athou（cai353974361@163.com）.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.athou.renovace.demo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.athou.renovace.IRenovace;
import com.athou.renovace.Renovace;
import com.athou.renovace.RenovaceCallback;
import com.athou.renovace.demo.bean.BaiduApiBean;
import com.athou.renovace.demo.bean.BaiduApiModel;
import com.athou.renovace.demo.bean.GankIOBean;
import com.athou.renovace.demo.bean.GankIOModel;
import com.athou.renovace.demo.bean.SouguBean;
import com.athou.renovace.demo.bean.TaobaoApiBean;
import com.athou.renovace.demo.bean.TaobaoApiModel;
import com.athou.renovace.interceptor.HeaderInterceptor;
import com.athou.renovace.util.Utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import rx.Subscriber;

/**
 * Created by athou on 2016/10/10.
 */

public class MainActivity extends Activity {
    private String TAG = "Renovace";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void GetNoParamsCallback(View v) {
        Renovace.getInstance().init("http://gank.io");
        Renovace.getInstance().get("/api/history/content/2/1", new RenovaceCallback<GankIOBean<List<GankIOModel>>>(this) {
            @Override
            public <T> void onSuccees(T bean) {
                showToast(bean.toString());
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        });
    }

    public void GetNoParamsSubscribe(View v) {
        Renovace.getInstance().init("http://gank.io");
        Renovace.getInstance().get("/api/history/content/2/1", new Subscriber<GankIOBean<List<GankIOModel>>>() {
            @Override
            public void onCompleted() {
                Log.i(TAG, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onError");
            }

            @Override
            public void onNext(GankIOBean<List<GankIOModel>> bean) {
                showToast(bean.toString());
            }
        });
    }

    public void GetNoParamsNoParseCallback(View v) {
        Renovace.getInstance().init("http://gank.io");
        Renovace.getInstance().get("/api/history/content/2/1", new RenovaceCallback<ResponseBody>(this) {
            @Override
            public <T> void onSuccees(T bean) {
                try {
                    showToast(((ResponseBody) bean).string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        });
    }

    public void GetNoParamsNoParseSubscribe(View v) {
        Renovace.getInstance().init("http://gank.io");
        Renovace.getInstance().get("/api/history/content/2/1", new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {
                Log.i(TAG, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onError");
            }

            @Override
            public void onNext(ResponseBody bean) {
                try {
                    showToast(bean.string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void GetParamsParseCallback(View v) {
        Renovace.getInstance().init("http://apis.baidu.com", new IRenovace.IHttpClient() {
            @Override
            public OkHttpClient getHttpClient() {
                HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
                logInterceptor.setLevel(Utils.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);

                HashMap<String, String> headers = new HashMap<>();
                headers.put("apikey", "27b6fb21f2b42e9d70cd722b2ed038a9");

                return new OkHttpClient.Builder()
                        .addInterceptor(logInterceptor)
                        .addInterceptor(new HeaderInterceptor(headers))
                        .retryOnConnectionFailure(true)
                        .connectTimeout(5, TimeUnit.SECONDS)
                        .build();//设置超时;
            }
        });

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("ip", "117.89.35.58");
        Renovace.getInstance().get("/apistore/iplookupservice/iplookup", parameters, new RenovaceCallback<BaiduApiBean<BaiduApiModel>>(this) {
            @Override
            public <T> void onSuccees(T response) {
                showToast(response.toString());
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        });
    }

    public void GetParamsParseSubscribe(View v) {
        Renovace.getInstance().init("http://apis.baidu.com", new IRenovace.IHttpClient() {
            @Override
            public OkHttpClient getHttpClient() {
                HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
                logInterceptor.setLevel(Utils.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);

                HashMap<String, String> headers = new HashMap<>();
                headers.put("apikey", "27b6fb21f2b42e9d70cd722b2ed038a9");

                return new OkHttpClient.Builder()
                        .addInterceptor(logInterceptor)
                        .addInterceptor(new HeaderInterceptor(headers))
                        .retryOnConnectionFailure(true)
                        .connectTimeout(5, TimeUnit.SECONDS)
                        .build();//设置超时;
            }
        });

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("ip", "117.89.35.58");
        Renovace.getInstance().get("/apistore/iplookupservice/iplookup", parameters, new Subscriber<BaiduApiBean<BaiduApiModel>>() {

            @Override
            public void onCompleted() {
                Log.i(TAG, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onError");
            }

            @Override
            public void onNext(BaiduApiBean<BaiduApiModel> bean) {
                showToast(bean.toString());
            }
        });
    }

    public void GetParamsNoParseCallback(View v) {
        Renovace.getInstance().init("http://apis.baidu.com", new IRenovace.IHttpClient() {
            @Override
            public OkHttpClient getHttpClient() {
                HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
                logInterceptor.setLevel(Utils.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);

                HashMap<String, String> headers = new HashMap<>();
                headers.put("apikey", "27b6fb21f2b42e9d70cd722b2ed038a9");

                return new OkHttpClient.Builder()
                        .addInterceptor(logInterceptor)
                        .addInterceptor(new HeaderInterceptor(headers))
                        .retryOnConnectionFailure(true)
                        .connectTimeout(5, TimeUnit.SECONDS)
                        .build();//设置超时;
            }
        });

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("ip", "117.89.35.58");
        Renovace.getInstance().get("/apistore/iplookupservice/iplookup", parameters, new RenovaceCallback<ResponseBody>(this) {
            @Override
            public <T> void onSuccees(T bean) {
                try {
                    showToast(((ResponseBody) bean).string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        });
    }

    public void GetParamsNoParseSubscribe(View v) {
        Renovace.getInstance().init("http://apis.baidu.com", new IRenovace.IHttpClient() {
            @Override
            public OkHttpClient getHttpClient() {
                HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
                logInterceptor.setLevel(Utils.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);

                HashMap<String, String> headers = new HashMap<>();
                headers.put("apikey", "27b6fb21f2b42e9d70cd722b2ed038a9");

                return new OkHttpClient.Builder()
                        .addInterceptor(logInterceptor)
                        .addInterceptor(new HeaderInterceptor(headers))
                        .retryOnConnectionFailure(true)
                        .connectTimeout(5, TimeUnit.SECONDS)
                        .build();//设置超时;
            }
        });

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("ip", "117.89.35.58");
        Renovace.getInstance().get("/apistore/iplookupservice/iplookup", parameters, new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {
                Log.i(TAG, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onError");
            }

            @Override
            public void onNext(ResponseBody bean) {
                try {
                    showToast(((ResponseBody) bean).string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // Post =========================================================================================
    public void PostParamsParseCallback(View v) {
        Renovace.getInstance().init("http://ip.taobao.com/");
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("ip", "21.22.11.33");
        Renovace.getInstance().post("service/getIpInfo.php", parameters, new RenovaceCallback<TaobaoApiBean<TaobaoApiModel>>(this) {
            @Override
            public <T> void onSuccees(T bean) {
                showToast(bean.toString());
            }
        });
    }

    public void PostParamsParseSubscribe(View v) {
        Renovace.getInstance().init("http://ip.taobao.com/");
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("ip", "21.22.11.33");
        Renovace.getInstance().post("service/getIpInfo.php", parameters, new Subscriber<TaobaoApiBean<TaobaoApiModel>>() {
            @Override
            public void onCompleted() {
                Log.i(TAG, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onError");
            }

            @Override
            public void onNext(TaobaoApiBean<TaobaoApiModel> bean) {
                showToast(bean.toString());
            }
        });
    }

    public void PostParamsNoParseCallback(View v) {
        Renovace.getInstance().init("http://ip.taobao.com/");
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("ip", "21.22.11.33");
        Renovace.getInstance().post("service/getIpInfo.php", parameters, new RenovaceCallback<ResponseBody>(this) {
            @Override
            public <T> void onSuccees(T bean) {
                try {
                    showToast(((ResponseBody) bean).string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void PostParamsNoParseSubscribe(View v) {
        Renovace.getInstance().init("http://ip.taobao.com/");
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("ip", "21.22.11.33");
        Renovace.getInstance().post("service/getIpInfo.php", parameters, new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {
                Log.i(TAG, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onError");
            }

            @Override
            public void onNext(ResponseBody bean) {
                try {
                    showToast(bean.string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void OnClick17(View v) {
        Renovace.getInstance().init("http://lbs.sougu.net.cn/");
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("m", "souguapp");
        parameters.put("c", "appusers");
        parameters.put("a", "network");

        TestApi testApi = Renovace.getInstance().create(TestApi.class);
        Renovace.getInstance().call(testApi.getSougu(parameters), new Subscriber<SouguBean>() {
            @Override
            public void onCompleted() {
                Log.i(TAG, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onError");
            }

            @Override
            public void onNext(SouguBean bean) {
                showToast(bean.toString());
            }
        });
    }

    public void OnClick18(View v) {
        Renovace.getInstance().init("http://lbs.sougu.net.cn/");
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("m", "souguapp");
        parameters.put("c", "appusers");
        parameters.put("a", "network");

        TestApi testApi = Renovace.getInstance().create(TestApi.class);
        Renovace.getInstance().call(testApi.getSougu(parameters), new RenovaceCallback<SouguBean>(this) {
            @Override
            public <T> void onSuccees(T bean) {
                showToast(bean.toString());
            }
        });
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
