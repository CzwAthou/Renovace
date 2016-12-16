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

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.athou.renovace.util.Utils;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import static android.util.Log.e;

/**
 * Created by athou on 2016/10/27.
 */

public class CaheInterceptor implements Interceptor {
    private Context context;
    private String cacheControlValue;
    private String cacheOnlineControlValue;
    //set cahe times is 3 days
    private static final int maxStale = 60 * 60 * 24 * 3;
    // read from cache for 60 s
    private static final int maxOnlineStale = 60;

    public CaheInterceptor(Context context) {
        this(context, String.format("max-stale=%d", maxOnlineStale));
    }

    public CaheInterceptor(Context context, String cacheControlValue) {
        this(context, cacheControlValue, String.format("max-stale=%d", maxStale));
    }

    public CaheInterceptor(Context context, String cacheControlValue, String cacheOnlineControlValue) {
        this.context = context;
        this.cacheControlValue = cacheControlValue;
        this.cacheOnlineControlValue = cacheOnlineControlValue;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (Utils.isNetworkAvailable(context)) {
            Response response = chain.proceed(request);
            String cacheControl = request.cacheControl().toString();
            Utils.logE(maxOnlineStale + "load cahe" + cacheControl);
            return response.newBuilder()
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    .header("Cache-Control", "public, " + cacheOnlineControlValue)
                    .build();
        } else {
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "当前无网络! 为你智能加载缓存", Toast.LENGTH_SHORT).show();
                }
            });
            Utils.logE("no network load cahe");
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
            Response response = chain.proceed(request);
            return response.newBuilder()
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    .header("Cache-Control", "public, only-if-cached, " + cacheControlValue)
                    .build();
        }
    }
}
