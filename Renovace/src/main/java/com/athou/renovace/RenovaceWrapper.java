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
    public static int DEFAULT_TIME_OUT = 5; //default timeout is 5s
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
                //set an OkHttpClient for Retrofit
                .client(httpClient.getHttpClient())
                //add Gson
                .addConverterFactory(GsonConverterFactory.create(Renovace.getInstance().getGson()))
                //add RxJava support
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                //When calling {@link #create} on the resulting {@link Retrofit} instance, eagerly validate
                //the configuration of all methods in the supplied interface.
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
                    //Interceptor's sort must be RenovaceInterceptor first,and then CacheInterceptor and so on...
                    //must add RenovaceInterceptor, otherwize some function(like dynamic header and cache) will be not useful
                    .addInterceptor(new RenovaceInterceptor())
                    //add cache Interceptor
                    .addInterceptor(new CacheInterceptor(context.get()))
                    //add log Interceptor
                    .addInterceptor(new RenovaceLog())
                    //set cache file path
                    .cache(RenovaceCache.getCache(context.get()))
                    .retryOnConnectionFailure(true)
                    //set timeout
                    .connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
                    .build();
            return mHttpClient;
        }
    }
}