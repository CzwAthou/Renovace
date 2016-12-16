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
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.athou.renovace.IRenovace;
import com.athou.renovace.IRenovaceCallBack;
import com.athou.renovace.Renovace;
import com.athou.renovace.RenovaceHttpProxy;
import com.athou.renovace.RenovaceLog;
import com.athou.renovace.demo.bean.BaiduApiBean;
import com.athou.renovace.demo.bean.BaiduApiModel;
import com.athou.renovace.demo.bean.SouguBean;
import com.athou.renovace.demo.bean.TaobaoApiBean;
import com.athou.renovace.demo.bean.TaobaoApiModel;
import com.athou.renovace.interceptor.HeaderInterceptor;
import com.athou.renovace.util.Utils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by athou on 2016/10/10.
 */

public class MainActivity extends Activity implements IDialogHandler {
    private String TAG = "Renovace";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void OnClickGetResult(View v) {
        Renovace.getInstance().init("http://apis.baidu.com", new IRenovace.IHttpClient() {
            @Override
            public OkHttpClient getHttpClient() {
                HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor(new RenovaceLog());
                logInterceptor.setLevel(Utils.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);

                HashMap<String, String> headers = new HashMap<>();
//                headers.put("apikey", "27b6fb21f2b42e9d70cd722b2ed038a9");
                headers.put("apikey", "e084abf9f93a9ec92c35e165b33bb9b3");

                return new OkHttpClient.Builder()
                        .addInterceptor(logInterceptor)
                        .addInterceptor(new HeaderInterceptor(headers))
                        .retryOnConnectionFailure(true)
                        .connectTimeout(5, TimeUnit.SECONDS)
                        .build();//设置超时;
            }
        });
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("ip", "119.75.217.109");
        Renovace.getInstance().getResult("/apistore/iplookupservice/iplookup", parameters, new RenovaceHttpProxy<BaiduApiBean<BaiduApiModel>>(
                new HttpCallback<BaiduApiModel>(this) {
                    @Override
                    public void onSuccess(BaiduApiModel response) {
                        showToast(response.toString());
                    }

                    @Override
                    public void onFinish(NetErrorBean errorBean) {
                        super.onFinish(errorBean);
                        showToast(errorBean);
                    }
                }
        ) {
        });
    }

    public void OnClickGetBean(View v) {
        Renovace.getInstance().init("http://apis.baidu.com", new IRenovace.IHttpClient() {
            @Override
            public OkHttpClient getHttpClient() {
                HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor(new RenovaceLog());
                logInterceptor.setLevel(Utils.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);

                HashMap<String, String> headers = new HashMap<>();
//                headers.put("apikey", "27b6fb21f2b42e9d70cd722b2ed038a9");
                headers.put("apikey", "e084abf9f93a9ec92c35e165b33bb9b3");

                return new OkHttpClient.Builder()
                        .addInterceptor(logInterceptor)
                        .addInterceptor(new HeaderInterceptor(headers))
                        .retryOnConnectionFailure(true)
                        .connectTimeout(5, TimeUnit.SECONDS)
                        .build();//设置超时;
            }
        });
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("ip", "119.75.217.109");
        Renovace.getInstance().getBean("/apistore/iplookupservice/iplookup", parameters, new RenovaceHttpProxy<BaiduApiBean<BaiduApiModel>>(
                new HttpCallback<BaiduApiBean<BaiduApiModel>>(this) {
                    @Override
                    public void onSuccess(BaiduApiBean<BaiduApiModel> response) {
                        showToast(response.toString());
                    }

                    @Override
                    public void onFinish(NetErrorBean errorBean) {
                        super.onFinish(errorBean);
                        showToast(errorBean);
                    }
                }
        ) {
        });
    }

    public void OnClickGetDirect(View v) {
        Renovace.getInstance().init("http://apis.baidu.com", new IRenovace.IHttpClient() {
            @Override
            public OkHttpClient getHttpClient() {
                HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor(new RenovaceLog());
                logInterceptor.setLevel(Utils.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);

                HashMap<String, String> headers = new HashMap<>();
                headers.put("apikey", "e084abf9f93a9ec92c35e165b33bb9b3");

                return new OkHttpClient.Builder()
                        .addInterceptor(logInterceptor)
                        .addInterceptor(new HeaderInterceptor(headers))
                        .retryOnConnectionFailure(true)
                        .connectTimeout(5, TimeUnit.SECONDS)
                        .build();//设置超时;
            }
        });
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("ip", "119.75.217.109");
        Renovace.getInstance().getDirect("/apistore/iplookupservice/iplookup", parameters, new HttpCallback<BaiduApiBean<BaiduApiModel>>(this) {
            @Override
            public void onSuccess(BaiduApiBean<BaiduApiModel> response) {
                showToast(response.toString());
            }

            @Override
            public void onFinish(NetErrorBean errorBean) {
                super.onFinish(errorBean);
                showToast(errorBean);
            }
        });
    }

    public void OnClickPostResult(View v) {
        Renovace.getInstance().init("http://ip.taobao.com/");
        getTaobaoApiModel("119.75.217.109", new HttpCallback<TaobaoApiModel>() {
            @Override
            public void onSuccess(TaobaoApiModel response) {
                showToast(response.toString());
            }

            @Override
            public void onFinish(NetErrorBean errorBean) {
                super.onFinish(errorBean);
                showToast(errorBean);
            }
        });
    }

    private void getTaobaoApiModel(String ip, IRenovaceCallBack<TaobaoApiModel> callBack) {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("ip", ip);
        Renovace.getInstance().postResult("service/getIpInfo.php", parameters,
                new RenovaceHttpProxy<TaobaoApiBean<TaobaoApiModel>>(callBack) {
                });
    }

    public void OnClickPostBean(View v) {
        Renovace.getInstance().init("http://ip.taobao.com/");
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("ip", "119.75.217.109");
        Renovace.getInstance().postBean("service/getIpInfo.php", parameters, new RenovaceHttpProxy<TaobaoApiBean<TaobaoApiModel>>(
                new HttpCallback<TaobaoApiBean<TaobaoApiModel>>() {
                    @Override
                    public void onSuccess(TaobaoApiBean<TaobaoApiModel> response) {
                        showToast(response.toString());
                    }

                    @Override
                    public void onFinish(NetErrorBean errorBean) {
                        super.onFinish(errorBean);
                        showToast(errorBean);
                    }
                }
        ) {
        });
    }

    public void OnClickPostDirect(View v) {
        Renovace.getInstance().init("http://ip.taobao.com/");
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("ip", "119.75.217.109");
        Renovace.getInstance().postDirect("service/getIpInfo.php", parameters, new HttpCallback<TaobaoApiBean<TaobaoApiModel>>(this) {
            @Override
            public void onSuccess(TaobaoApiBean<TaobaoApiModel> response) {
                showToast(response.toString());
            }

            @Override
            public void onFinish(NetErrorBean errorBean) {
                super.onFinish(errorBean);
                showToast(errorBean);
            }
        });
    }

    public void OnClickAPI(View v) {
        Renovace.getInstance().init("http://lbs.sougu.net.cn/");
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("m", "souguapp");
        parameters.put("c", "appusers");
        parameters.put("a", "network");

        TestApi testApi = Renovace.getInstance().create(TestApi.class);
        Renovace.getInstance().call(testApi.getSougu(parameters), new HttpCallback<SouguBean>(this) {
            @Override
            public void onSuccess(SouguBean response) {
                showToast(response.toString());
            }

            @Override
            public void onFinish(NetErrorBean errorBean) {
                super.onFinish(errorBean);
                showToast(errorBean);
            }
        });
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void showToast(NetErrorBean errorBean) {
        if (!errorBean.isSucceed()) {
            showToast(errorBean.getErrMessge());
        }
    }

    private ProgressDialog dialog = null;

    @Override
    public void showLoadDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
        dialog = ProgressDialog.show(this, "", "正在加载中...", false);
    }

    @Override
    public void hideLoadDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }
}
