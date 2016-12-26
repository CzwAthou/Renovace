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

package com.athou.renovace;

import android.content.Context;

import com.athou.renovace.interceptor.CacheInterceptor;
import com.athou.renovace.interceptor.RenovaceInterceptor;
import com.athou.renovace.interceptor.RenovaceLog;
import com.athou.renovace.util.Utils;

import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by athou on 2016/10/27.
 */

final class RenovaceWrapper implements IRenovace {
    public static int DEFAULT_TIME_OUT = 5; //超时时间默认5s
    private Retrofit mRetrofit;

    private String baseUrl = null;
    private IHttpClient httpClient = null;

    public RenovaceWrapper(Context context, String baseUrl) {
        this(baseUrl, new RenovaceHttpClient(context));
    }

    public RenovaceWrapper(String baseUrl, IHttpClient httpClient) {
        this.baseUrl = baseUrl;
        this.httpClient = httpClient;
    }

    @Override
    public Retrofit getRetrofit() {
        if (mRetrofit != null) {
            return mRetrofit;
        }
        mRetrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                //制定Retrofit使用的OkHttpClient
                .client(httpClient.getHttpClient())
                //添加对Gson的转换解析
                .addConverterFactory(GsonConverterFactory.create())
                //添加对RxJava的转换解析
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                //是否在create（Class）时检测接口定义是否正确，而不是在调用方法才检测，适合在测试的时候调用
                .validateEagerly(Utils.DEBUG)
                .build();
        return mRetrofit;
    }

    static class RenovaceHttpClient implements IRenovace.IHttpClient {
        private OkHttpClient mHttpClient = null;
        private WeakReference<Context> context;

        public RenovaceHttpClient(Context context) {
            this.context = new WeakReference<Context>(context.getApplicationContext());
        }

        @Override
        public OkHttpClient getHttpClient() {
            if (mHttpClient != null) {
                return mHttpClient;
            }
            mHttpClient = new OkHttpClient.Builder()
                    //拦截器的顺序必须是先RenovaceInterceptor，然后再是CacheInterceptor等等。。。
                    //添加日志拦截器
                    .addInterceptor(new RenovaceLog())
                    //必须添加RenovaceInterceptor, 否则本框架的许多功能您将无法体验
                    .addInterceptor(new RenovaceInterceptor())
                    //添加缓存拦截器
                    .addInterceptor(new CacheInterceptor(context.get()))
                    //设置缓存路径
                    .cache(RenovaceCache.getCache(context.get()))
                    .retryOnConnectionFailure(true)
                    .connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
                    .build();//设置超时
            return mHttpClient;
        }
    }
}
