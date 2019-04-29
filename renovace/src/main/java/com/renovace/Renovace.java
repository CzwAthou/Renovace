package com.renovace;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.renovace.Util.RenovaceLogUtil;
import com.renovace.config.Config;
import com.renovace.config.ServerConfig;

import io.reactivex.plugins.RxJavaPlugins;
import okhttp3.OkHttpClient;

/**
 * @author athou
 * @date 2017/12/13
 */

public final class Renovace {
    private static Context mContext;
    private static Gson mGson;

    public static void init(@NonNull Context context, boolean debug) {
        mContext = context.getApplicationContext();
        RenovaceLogUtil.DEBUG = debug;
        RxJavaPlugins.setErrorHandler(new RenovaceErrorHandler());
    }

    /**
     * reset config, maybe you want change the retrofit with the config
     *
     * @param clientConfig
     */
    public static void resetConfig(Class<? extends Config> clientConfig) {
        Api.resetConfig(clientConfig);
    }

    public static Context getContext() {
        return mContext;
    }

    /**
     * 获取OkHttpClient
     *
     * @param defaultConfig
     * @return
     */
    public static @Nullable
    OkHttpClient getClient(Class<? extends Config> defaultConfig) {
        return Api.getOkhttpClient(defaultConfig);
    }

    /**
     * set inner gson to parse data
     *
     * @param gson
     */
    public static void setGson(Gson gson) {
        Renovace.mGson = gson;
    }

    /**
     * get the gson, if null, will create a new gson
     *
     * @return
     */
    public static Gson getGson() {
        if (mGson == null) {
            mGson = new Gson();
        }
        return mGson;
    }

    public static <T> T create(@NonNull Class<T> service) {
        Class<? extends Config> configClass = getConfigClass(service);
        if (configClass == null) {
            throw new RuntimeException("configClass == null, you must set a config with service:" + service.getCanonicalName());
        }
        return Api.provide(configClass).create(service);
    }

    public static <T> T create(@NonNull Class<? extends Config> clientConfig, @NonNull Class<T> service) {
        return Api.provide(clientConfig).create(service);
    }

    private static <T> Class<? extends Config> getConfigClass(@NonNull Class<T> service) {
        ServerConfig serverConfig = service.getAnnotation(ServerConfig.class);
        if (serverConfig != null) {
            return serverConfig.config();
        }
        return null;
    }
}