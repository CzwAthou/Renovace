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

package com.athou.renovace.interceptor;

import android.content.Context;
import android.text.TextUtils;

import com.athou.renovace.RenovaceCache;
import com.athou.renovace.RenovaceException;
import com.athou.renovace.util.Utils;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by athou on 2016/10/27.
 */

public class CacheInterceptor implements Interceptor {

    private WeakReference<Context> contextWeakReference;

    public CacheInterceptor(Context context) {
        this.contextWeakReference = new WeakReference<Context>(context);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        RenovaceCache.CacheStrategy cacheStrategy = checkSetCache(request);
        if (null == cacheStrategy) {
            return chain.proceed(request);
        }
        switch (cacheStrategy) {
            case CacheFirst:
                return doCacheFirst(chain, request);
            case NetWorkFirst:
                return doNetWorkFirst(chain, request);
            default:
                return chain.proceed(request);
        }
    }

    private RenovaceCache.CacheStrategy checkSetCache(Request request) {
        String cacheHeader = request.header(RenovaceCache.Key_setCache);
        if (TextUtils.isEmpty(cacheHeader)) {
            return null;
        }
        return RenovaceCache.CacheStrategy.valueOf(cacheHeader);
    }

    /**
     * if network is available and the request during max-age time(default 60s), so get cache first<br>
     * then if  network is unavailable, get data from cache.
     */
    private Response doCacheFirst(Chain chain, Request request) throws IOException {
        if (Utils.isNetworkAvailable(contextWeakReference.get())) {
            Response response = chain.proceed(request);
            return response.newBuilder()
                    .removeHeader(RenovaceCache.Key_setCache)
                    // clear head info,because if server unsupport,it will return some useless info
                    // if you donot clear,anything behind will donot action.
                    .removeHeader("Pragma")
                    .header("Cache-Control", "public, max-age=" + RenovaceCache.maxAge)
                    .build();
        } else {
            Utils.logE("no network load cahe");
            request = request.newBuilder()
                    .removeHeader(RenovaceCache.Key_setCache)
                    .removeHeader("Pragma")
                    //set cache life cycle is RenovaceCache.maxStale second
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + RenovaceCache.maxStale)
                    .build();
            Response response = chain.proceed(request);
            if (response.code() == HttpURLConnection.HTTP_GATEWAY_TIMEOUT) {
                throw new RenovaceException(response.code(), "Connot connect server");
            }
            return response;
        }
    }

    /**
     * if network is available, so get data from network whatever<br>
     * then if  network is unavailable, get data from cache.
     */
    private Response doNetWorkFirst(Chain chain, Request request) throws IOException {
        if (Utils.isNetworkAvailable(contextWeakReference.get())) {
            Response response = chain.proceed(request);
            return response.newBuilder()
                    .removeHeader(RenovaceCache.Key_setCache)
                    .removeHeader("Pragma")
                    //when net is available, set cache max-age time is zero, asy get data from network
                    .header("Cache-Control", "public, max-age=" + 0)
                    .build();
        } else {
            Utils.logE("no network load cahe");
            request = request.newBuilder()
                    .removeHeader(RenovaceCache.Key_setCache)
                    .removeHeader("Pragma")
                    //set cache life cycle is RenovaceCache.maxStale second
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + RenovaceCache.maxStale)
                    .build();
            Response response = chain.proceed(request);
            if (response.code() == HttpURLConnection.HTTP_GATEWAY_TIMEOUT) {
                throw new RenovaceException(response.code(), "Connot connect server");
            }
            return response;
        }
    }
}