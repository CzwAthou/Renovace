/*
 * Copyright (c) 2016  athou(cai353974361@163.com).
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
import com.athou.renovace.Renovace;
import com.athou.renovace.RenovaceCache;
import com.athou.renovace.RequestParams;
import com.athou.renovace.demo.bean.PhoneIpApiBean;
import com.athou.renovace.demo.bean.PhoneIpBean;
import com.athou.renovace.demo.bean.SouguBean;
import com.athou.renovace.demo.bean.TaobaoApiBean;
import com.athou.renovace.demo.bean.TaobaoApiModel;
import com.athou.renovace.demo.bean.YuminResultBean;
import com.athou.renovace.interceptor.CacheInterceptor;
import com.athou.renovace.interceptor.RenovaceInterceptor;
import com.athou.renovace.interceptor.RenovaceLog;
import com.athou.renovace.util.TypeUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import rx.Observable;
import rx.functions.Func2;

/**
 * Created by athou on 2016/10/10.
 */

public class MainActivity extends Activity implements IDialogHandler {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void OnClickGetResult(View v) {
        Renovace.getInstance().init("http://apis.juhe.cn", new IRenovace.IHttpClient() {
            @Override
            public OkHttpClient getHttpClient() {
                return new OkHttpClient.Builder()
                        //拦截器的顺序必须是先RenovaceInterceptor，然后再是CacheInterceptor等等。。。
                        //必须添加RenovaceInterceptor, 否则本框架的许多功能您将无法体验
                        .addInterceptor(new RenovaceInterceptor())
                        //添加缓存拦截器
                        .addInterceptor(new CacheInterceptor(MainActivity.this))
                        //添加日志拦截器
                        .addInterceptor(new RenovaceLog())
                        //设置缓存路径
                        .cache(RenovaceCache.getCache(MainActivity.this))
                        .retryOnConnectionFailure(true)
                        .connectTimeout(5, TimeUnit.SECONDS)
                        .build();//设置超时;
            }
        });
        RequestParams parameters = new RequestParams();
        parameters.put("phone", "13888888888");
        parameters.put("dtype", "json");
        parameters.put("key", "5682c1f44a7f486e40f9720d6c97ffe4");
        HttpManager.getPhoneIpResult("/mobile/get", parameters, new HttpCallback<PhoneIpApiBean.PhoneIpResultBean>() {
            @Override
            public void onSuccess(PhoneIpApiBean.PhoneIpResultBean response) {
                showToast(response.toString());
            }

            @Override
            public void onFinish(NetErrorBean errorBean) {
                super.onFinish(errorBean);
                showToast(errorBean);
            }
        });
    }

    public void OnClickGetResultWithCacheFirst(View view) {
        Renovace.getInstance().init(this, "http://apis.juhe.cn");
        RequestParams parameters = new RequestParams(RenovaceCache.CacheStrategy.CacheFirst);
        parameters.put("phone", "13888888888");
        parameters.put("dtype", "json");
        parameters.put("key", "5682c1f44a7f486e40f9720d6c97ffe4");
        HttpManager.getPhoneIpResult("/mobile/get", parameters, new HttpCallback<PhoneIpApiBean.PhoneIpResultBean>() {
            @Override
            public void onSuccess(PhoneIpApiBean.PhoneIpResultBean response) {
                showToast(response.toString());
            }

            @Override
            public void onFinish(NetErrorBean errorBean) {
                super.onFinish(errorBean);
                showToast(errorBean);
            }
        });
    }

    public void OnClickGetResultWithNetworkFirst(View view) {
        Renovace.getInstance().init(this, "http://apis.juhe.cn");
        RequestParams parameters = new RequestParams(RenovaceCache.CacheStrategy.NetWorkFirst);
        parameters.put("phone", "13888888888");
        parameters.put("dtype", "json");
        parameters.put("key", "5682c1f44a7f486e40f9720d6c97ffe4");
        HttpManager.getPhoneIpResult("/mobile/get", parameters, new HttpCallback<PhoneIpApiBean.PhoneIpResultBean>() {
            @Override
            public void onSuccess(PhoneIpApiBean.PhoneIpResultBean response) {
                showToast(response.toString());
            }

            @Override
            public void onFinish(NetErrorBean errorBean) {
                super.onFinish(errorBean);
                showToast(errorBean);
            }
        });
    }

    public void OnClickGetBean(View v) {
        Renovace.getInstance().init("http://www.yumingco.com", new IRenovace.IHttpClient() {
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
        parameters.put("domain", "baidu");
        parameters.put("suffix", "com");
        Renovace.getInstance().getBean("/api", parameters, new HttpCallback<YuminResultBean>() {
            @Override
            public void onSuccess(YuminResultBean response) {
                showToast(response.toString());
            }

            @Override
            public void onFinish(NetErrorBean errorBean) {
                super.onFinish(errorBean);
                showToast(errorBean);
            }
        });
    }

    public void OnClickGetDirect(View v) {
        Renovace.getInstance().init("http://www.yumingco.com", new IRenovace.IHttpClient() {
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
        parameters.put("domain", "baidu");
        parameters.put("suffix", "com");
        Renovace.getInstance().getBean("/api", parameters, new HttpCallback<YuminResultBean>() {
            @Override
            public void onSuccess(YuminResultBean response) {
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
//        Renovace.getInstance().init(this, "http://apis.juhe.cn");
//        RequestParams parameters = new RequestParams();
//        parameters.put("phone", "13888888888");
//        parameters.put("dtype", "json");
//        parameters.put("key", "5682c1f44a7f486e40f9720d6c97ffe4");
//        Renovace.getInstance().postResult("/mobile/get", parameters, new RenovaceHttpProxy<PhoneIpApiBean<PhoneIpApiBean.PhoneIpResultBean>, PhoneIpApiBean.PhoneIpResultBean>(
//                new HttpCallback<PhoneIpApiBean.PhoneIpResultBean>() {
//                    @Override
//                    public void onSuccess(PhoneIpApiBean.PhoneIpResultBean response) {
//                        showToast(response.toString());
//                    }
//
//                    @Override
//                    public void onFinish(NetErrorBean errorBean) {
//                        super.onFinish(errorBean);
//                        showToast(errorBean);
//                    }
//                }) {
//        });


        Renovace.getInstance().init(this, "http://apis.juhe.cn");
        RequestParams parameters = new RequestParams();
        parameters.put("phone", "13888888888");
        parameters.put("dtype", "json");
        parameters.put("key", "5682c1f44a7f486e40f9720d6c97ffe4");
        HttpManager.postPhoneIpResult("/mobile/get", parameters, new HttpCallback<PhoneIpApiBean.PhoneIpResultBean>() {
            @Override
            public void onSuccess(PhoneIpApiBean.PhoneIpResultBean response) {
                showToast(response.toString());
            }

            @Override
            public void onFinish(NetErrorBean errorBean) {
                super.onFinish(errorBean);
                showToast(errorBean);
            }
        });
    }

    public void OnClickPostBean(View v) {
        Renovace.getInstance().init(this, "http://ip.taobao.com/");
        RequestParams parameters = new RequestParams();
        parameters.put("ip", "119.75.217.109");
        Renovace.getInstance().postBean("service/getIpInfo.php", parameters, new HttpCallback<TaobaoApiBean<TaobaoApiModel>>() {
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
        );
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

    public void OnClickMutiRequest1(View view) {
        Renovace.getInstance().init(this, "http://apis.juhe.cn");

        RequestParams parameters1 = new RequestParams();
        parameters1.put("phone", "13888888888");
        parameters1.put("dtype", "json");
        parameters1.put("key", "5682c1f44a7f486e40f9720d6c97ffe4");

        RequestParams parameters2 = new RequestParams();
        parameters2.put("phone", "13988888888");
        parameters2.put("dtype", "json");
        parameters2.put("key", "5682c1f44a7f486e40f9720d6c97ffe4");

        Type type = TypeUtil.newParameterizedTypeWithOwner(PhoneIpApiBean.class, PhoneIpApiBean.PhoneIpResultBean.class);
        Observable ob1 = Renovace.getInstance().postCall("/mobile/get", parameters1, type);
        Observable ob2 = Renovace.getInstance().postCall("/mobile/get", parameters2, type);
        Observable<List<PhoneIpApiBean<PhoneIpApiBean.PhoneIpResultBean>>> zip = Observable.zip(ob1, ob2, new Func2<PhoneIpApiBean<PhoneIpApiBean.PhoneIpResultBean>, PhoneIpApiBean<PhoneIpApiBean.PhoneIpResultBean>, List<PhoneIpApiBean<PhoneIpApiBean.PhoneIpResultBean>>>() {
            @Override
            public List<PhoneIpApiBean<PhoneIpApiBean.PhoneIpResultBean>> call(PhoneIpApiBean<PhoneIpApiBean.PhoneIpResultBean> phoneIpBeanPhoneIpApiBean, PhoneIpApiBean<PhoneIpApiBean.PhoneIpResultBean> phoneIpBeanPhoneIpApiBean2) {
                List<PhoneIpApiBean<PhoneIpApiBean.PhoneIpResultBean>> array = new ArrayList<>();
                array.add(phoneIpBeanPhoneIpApiBean);
                array.add(phoneIpBeanPhoneIpApiBean2);
                return array;
            }
        });
        Renovace.getInstance().call(zip, new HttpCallback<List<PhoneIpApiBean<PhoneIpApiBean.PhoneIpResultBean>>>(this) {
            @Override
            public void onSuccess(List<PhoneIpApiBean<PhoneIpApiBean.PhoneIpResultBean>> response) {
                if (response != null) {
                    showToast(response.toString());
                }
            }

            @Override
            public void onFinish(NetErrorBean errorBean) {
                super.onFinish(errorBean);
                showToast(errorBean);
            }
        });
    }

    public void OnClickMutiRequest2(View view) {
        Renovace.getInstance().init(this, "http://apis.juhe.cn");

        RequestParams parameters1 = new RequestParams();
        parameters1.put("phone", "13888888888");
        parameters1.put("dtype", "json");
        parameters1.put("key", "5682c1f44a7f486e40f9720d6c97ffe4");

        RequestParams parameters2 = new RequestParams();
        parameters2.put("phone", "13988888888");
        parameters2.put("dtype", "json");
        parameters2.put("key", "5682c1f44a7f486e40f9720d6c97ffe4");

        Observable<PhoneIpBean> ob1 = Renovace.getInstance().postCall("/mobile/get", parameters1, PhoneIpBean.class);
        Observable<PhoneIpBean> ob2 = Renovace.getInstance().postCall("/mobile/get", parameters2, PhoneIpBean.class);
        Observable<List<PhoneIpBean>> zip = Observable.zip(ob1, ob2, new Func2<PhoneIpBean, PhoneIpBean, List<PhoneIpBean>>() {
            @Override
            public List<PhoneIpBean> call(PhoneIpBean phoneIpBean, PhoneIpBean phoneIpBean2) {
                List<PhoneIpBean> array = new ArrayList<>();
                array.add(phoneIpBean);
                array.add(phoneIpBean2);
                return array;
            }
        });
        Renovace.getInstance().call(zip, new HttpCallback<List<PhoneIpBean>>(this) {
            @Override
            public void onSuccess(List<PhoneIpBean> response) {
                if (response != null) {
                    showToast(response.toString());
                }
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
