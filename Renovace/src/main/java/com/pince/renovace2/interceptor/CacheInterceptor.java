///*
// * Copyright (c) 2016  athou(cai353974361@163.com).
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *     http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package com.pince.renovace2.interceptor;
//
//import android.content.Context;
//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;
//import android.text.TextUtils;
//
//import com.pince.renovace2.RenovaceException;
//import com.pince.renovace2.Util.RenovaceLogUtil;
//import com.pince.renovace2.cache.CacheStrategy;
//import com.pince.renovace2.cache.HttpCache;
//import com.pince.renovace2.header.HeaderKey;
//
//import java.io.IOException;
//import java.lang.ref.WeakReference;
//import java.net.HttpURLConnection;
//
//import okhttp3.Interceptor;
//import okhttp3.Request;
//import okhttp3.Response;
//
///**
// * @author athou
// * @date 2016/10/27
// */
//
//public class CacheInterceptor implements Interceptor {
//
//    private WeakReference<Context> contextWeakReference;
//
//    public CacheInterceptor(Context context) {
//        this.contextWeakReference = new WeakReference<Context>(context);
//    }
//
//    @Override
//    public Response intercept(Chain chain) throws IOException {
//        Request request = chain.request();
//        CacheStrategy cacheStrategy = checkSetCache(request);
//        switch (cacheStrategy) {
//            case CacheFirst:
//                return doCacheFirst(chain, request);
//            case NetWorkFirst:
//                return doNetWorkFirst(chain, request);
//            case None:
//            default:
//                return chain.proceed(request);
//        }
//    }
//
//    private CacheStrategy checkSetCache(Request request) {
//        String cacheHeader = request.header(HeaderKey.Cache);
//        if (TextUtils.isEmpty(cacheHeader)) {
//            return CacheStrategy.None;
//        }
//        return CacheStrategy.valueOf(cacheHeader);
//    }
//
//    /**
//     * if network is available and the request during max-age time(default 60s), so get cache first<br>
//     * then if  network is unavailable, get data from cache.
//     */
//    private Response doCacheFirst(Chain chain, Request request) throws IOException {
//        if (isNetworkAvailable(contextWeakReference.get())) {
//            Response response = chain.proceed(request);
//            return response.newBuilder()
//                    .removeHeader(HeaderKey.Cache)
//                    // clear head info,because if server unsupport,it will return some useless info
//                    // if you donot clear,anything behind will donot action.
//                    .removeHeader("Pragma")
//                    .header("Cache-Control", "public, max-age=" + HttpCache.maxAge)
//                    .build();
//        } else {
//            RenovaceLogUtil.logE("no network load cahe");
//            request = request.newBuilder()
//                    .removeHeader(HeaderKey.Cache)
//                    .removeHeader("Pragma")
//                    //set cache life cycle is HttpCache.maxStale second
//                    .header("Cache-Control", "public, only-if-cached, max-stale=" + HttpCache.maxStale)
//                    .build();
//            Response response = chain.proceed(request);
//            if (response.code() == HttpURLConnection.HTTP_GATEWAY_TIMEOUT) {
//                throw new RenovaceException(response.code(), "Connot connect server");
//            }
//            return response;
//        }
//    }
//
//    /**
//     * if network is available, so get data from network whatever<br>
//     * then if  network is unavailable, get data from cache.
//     */
//    private Response doNetWorkFirst(Chain chain, Request request) throws IOException {
//        if (isNetworkAvailable(contextWeakReference.get())) {
//            Response response = chain.proceed(request);
//            return response.newBuilder()
//                    .removeHeader(HeaderKey.Cache)
//                    .removeHeader("Pragma")
//                    //when net is available, set cache max-age time is zero, asy get data from network
//                    .header("Cache-Control", "public, max-age=" + 0)
//                    .build();
//        } else {
//            RenovaceLogUtil.logE("no network load cahe");
//            request = request.newBuilder()
//                    .removeHeader(HeaderKey.Cache)
//                    .removeHeader("Pragma")
//                    //set cache life cycle is HttpCache.maxStale second
//                    .header("Cache-Control", "public, only-if-cached, max-stale=" + HttpCache.maxStale)
//                    .build();
//            Response response = chain.proceed(request);
//            if (response.code() == HttpURLConnection.HTTP_GATEWAY_TIMEOUT) {
//                throw new RenovaceException(response.code(), "Connot connect server");
//            }
//            return response;
//        }
//    }
//
//    /**
//     * check NetworkAvailable
//     *
//     * @param context
//     * @return
//     */
//    private static boolean isNetworkAvailable(Context context) {
//        ConnectivityManager manager = (ConnectivityManager) context.getApplicationContext().getSystemService(
//                Context.CONNECTIVITY_SERVICE);
//        if (null == manager)
//            return false;
//        NetworkInfo info = manager.getActiveNetworkInfo();
//        if (null == info || !info.isAvailable())
//            return false;
//        return true;
//    }
//}