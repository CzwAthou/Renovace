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

package com.athou.renovace.interceptor;

import android.content.Context;
import android.text.TextUtils;

import com.athou.renovace.RenovaceCache;
import com.athou.renovace.util.Utils;

import java.io.IOException;
import java.lang.ref.WeakReference;

import okhttp3.CacheControl;
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
                    .removeHeader("Pragma") // 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                    .header("Cache-Control", "public, max-age=" + RenovaceCache.maxAge)
                    .build();
        } else {
            Utils.logE("no network load cahe");
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
            Response response = chain.proceed(request);
            return response.newBuilder()
                    .removeHeader(RenovaceCache.Key_setCache)
                    .removeHeader("Pragma")
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + RenovaceCache.maxStale)
                    .build();
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
                    .header("Cache-Control", "public, max-age=" + 0) //有网络时，设置缓存超时时间0个小时，即从网络获取数据
                    .build();
        } else {
            Utils.logE("no network load cahe");
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
            Response response = chain.proceed(request);
            return response.newBuilder()
                    .removeHeader(RenovaceCache.Key_setCache)
                    .removeHeader("Pragma")
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + RenovaceCache.maxStale)
                    .build();
        }
    }
}
