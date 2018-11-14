package com.pince.renovace2;

import android.text.TextUtils;

import com.pince.renovace2.config.Config;

import java.util.HashMap;

import retrofit2.Retrofit;

/**
 * 网络基础类
 *
 * @author bin
 * @date 2017/12/6 18:01
 */
class Api {

    private static HashMap<String, ApiProxy> apiProxies = new HashMap<>();

    private Api() {
    }

    static ApiProxy provide(Class<? extends Config> clientConfig) {
        if (clientConfig == null) {
            throw new IllegalArgumentException("config can't be null");
        }
        String clientName = clientConfig.getCanonicalName();
        if (TextUtils.isEmpty(clientName)) {
            throw new IllegalArgumentException("class must be a Config");
        }
        ApiProxy apiProxy = apiProxies.get(clientName);
        if (apiProxy == null) {
            apiProxy = new ApiProxy(newRetrofit(clientConfig));
            apiProxies.put(clientName, apiProxy);
        }
        return apiProxy;
    }

    private static Retrofit newRetrofit(Class<? extends Config> configClass) {
        Retrofit.Builder builder = new Retrofit.Builder();
        try {
            Config config = configClass.newInstance();
            config.build(builder);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return builder.build();
    }

    static class ApiProxy {

        private final HashMap<Class, Object> apiCache;
        private Retrofit retrofit;

        ApiProxy(Retrofit retrofit) {
            apiCache = new HashMap<>();
            this.retrofit = retrofit;
        }

        public <T> T create(Class<T> tClass) {
            Object service = apiCache.get(tClass);
            if (service == null) {
                service = retrofit.create(tClass);
                apiCache.put(tClass, service);
            }
            return (T) service;
        }
    }
}
