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
import com.athou.renovace.RenovaceCache;
import com.athou.renovace.RenovaceHttpProxy;
import com.athou.renovace.RequestParams;
import com.athou.renovace.demo.bean.BaiduApiBean;
import com.athou.renovace.demo.bean.BaiduApiModel;
import com.athou.renovace.demo.bean.SouguBean;
import com.athou.renovace.demo.bean.TaobaoApiBean;
import com.athou.renovace.demo.bean.TaobaoApiModel;
import com.athou.renovace.interceptor.CacheInterceptor;
import com.athou.renovace.interceptor.RenovaceInterceptor;
import com.athou.renovace.interceptor.RenovaceLog;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

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
                return new OkHttpClient.Builder()
                        //拦截器的顺序必须是先RenovaceInterceptor，然后再是CacheInterceptor等等。。。
                        //添加日志拦截器
                        .addInterceptor(new RenovaceLog())
                        //必须添加RenovaceInterceptor, 否则本框架的许多功能您将无法体验
                        .addInterceptor(new RenovaceInterceptor())
                        //添加缓存拦截器
                        .addInterceptor(new CacheInterceptor(MainActivity.this))
                        //设置缓存路径
                        .cache(RenovaceCache.getCache(MainActivity.this))
                        .retryOnConnectionFailure(true)
                        .connectTimeout(5, TimeUnit.SECONDS)
                        .build();//设置超时;
            }
        });
        RequestParams parameters = new RequestParams();
        parameters.put("ip", "119.75.217.109");
        parameters.addHeader("apikey", "e084abf9f93a9ec92c35e165b33bb9b3");
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

    public void OnClickGetResultWithCacheFirst(View view) {
        Renovace.getInstance().init("http://apis.baidu.com", new IRenovace.IHttpClient() {
            @Override
            public OkHttpClient getHttpClient() {
                return new OkHttpClient.Builder()
                        .addInterceptor(new RenovaceLog())
                        .addInterceptor(new RenovaceInterceptor())
                        .addInterceptor(new CacheInterceptor(MainActivity.this))
                        .cache(RenovaceCache.getCache(MainActivity.this))
                        .retryOnConnectionFailure(true)
                        .connectTimeout(5, TimeUnit.SECONDS)
                        .build();//设置超时;
            }
        });
        RequestParams parameters = new RequestParams(RenovaceCache.CacheStrategy.CacheFirst);
        parameters.put("ip", "119.75.217.109");
        parameters.addHeader("apikey", "e084abf9f93a9ec92c35e165b33bb9b3");
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

    public void OnClickGetResultWithNetworkFirst(View view) {
        Renovace.getInstance().init("http://apis.baidu.com", new IRenovace.IHttpClient() {
            @Override
            public OkHttpClient getHttpClient() {
                return new OkHttpClient.Builder()
                        .addInterceptor(new RenovaceLog())
                        .addInterceptor(new RenovaceInterceptor())
                        .addInterceptor(new CacheInterceptor(MainActivity.this))
                        .cache(RenovaceCache.getCache(MainActivity.this))
                        .retryOnConnectionFailure(true)
                        .connectTimeout(5, TimeUnit.SECONDS)
                        .build();//设置超时;
            }
        });
        RequestParams parameters = new RequestParams(RenovaceCache.CacheStrategy.NetWorkFirst);
        parameters.put("ip", "119.75.217.109");
        parameters.addHeader("apikey", "e084abf9f93a9ec92c35e165b33bb9b3");
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
                return new OkHttpClient.Builder()
                        .addInterceptor(new RenovaceLog())
                        .addInterceptor(new RenovaceInterceptor())
                        .retryOnConnectionFailure(true)
                        .connectTimeout(5, TimeUnit.SECONDS)
                        .build();//设置超时;
            }
        });
        RequestParams parameters = new RequestParams();
        parameters.put("ip", "119.75.217.109");
        parameters.addHeader("apikey", "e084abf9f93a9ec92c35e165b33bb9b3");
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
                return new OkHttpClient.Builder()
                        .addInterceptor(new RenovaceLog())
                        .addInterceptor(new RenovaceInterceptor())
                        .retryOnConnectionFailure(true)
                        .connectTimeout(5, TimeUnit.SECONDS)
                        .build();//设置超时;
            }
        });
        RequestParams parameters = new RequestParams();
        parameters.put("ip", "119.75.217.109");
        parameters.addHeader("apikey", "e084abf9f93a9ec92c35e165b33bb9b3");
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
        Renovace.getInstance().init(this, "http://ip.taobao.com/");
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
        RequestParams parameters = new RequestParams();
        parameters.put("ip", ip);
        Renovace.getInstance().postResult("service/getIpInfo.php", parameters,
                new RenovaceHttpProxy<TaobaoApiBean<TaobaoApiModel>>(callBack) {
                });
    }

    public void OnClickPostBean(View v) {
        Renovace.getInstance().init(this, "http://ip.taobao.com/");
        RequestParams parameters = new RequestParams();
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
        Renovace.getInstance().init(this, "http://ip.taobao.com/");
        RequestParams parameters = new RequestParams();
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
        Renovace.getInstance().init(this, "http://lbs.sougu.net.cn/");
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
