package com.pince.renovace2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.pince.renovace2.Util.RenovaceLogUtil;
import com.pince.renovace2.config.Config;
import com.pince.renovace2.request.BodyRequestBuilder;
import com.pince.renovace2.request.DeleteRequestBuidler;
import com.pince.renovace2.request.GetRequestBuidler;
import com.pince.renovace2.request.PostRequestBuilder;
import com.pince.renovace2.request.PutRequestBuidler;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import io.reactivex.plugins.RxJavaPlugins;
import okhttp3.OkHttpClient;

/**
 * @author athou
 * @date 2017/12/13
 */

public class Renovace {
    private static Context mContext;
    private static Class<? extends Config> mDefaultConfig;
    private static Gson mGson;
    private static List<RespCodeInteceptor> respCodeinteceptorList = null;

    public static void init(@NonNull Context context, boolean debug) {
        init(context, null, debug);
    }

    public static void init(@NonNull Context context, Class<? extends Config> defaultConfig, boolean debug) {
        mContext = context.getApplicationContext();
        respCodeinteceptorList = new CopyOnWriteArrayList<>();
        setDefaultConfig(defaultConfig);
        RenovaceLogUtil.DEBUG = debug;
        RxJavaPlugins.setErrorHandler(new RenovaceErrorHandler());
    }

    public static Context getContext() {
        return mContext;
    }

    public static void setDefaultConfig(Class<? extends Config> defaultConfig) {
        Renovace.mDefaultConfig = defaultConfig;
    }

    public static Class<? extends Config> getDefaultConfig() {
        return mDefaultConfig;
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

    public static synchronized List<RespCodeInteceptor> getRespCodeinteceptorList() {
        return respCodeinteceptorList;
    }

    public static void addRespCodeInteceptor(RespCodeInteceptor inteceptor) {
        respCodeinteceptorList.add(inteceptor);
    }

    public static void removeRespCodeInteceptor(RespCodeInteceptor inteceptor) {
        respCodeinteceptorList.remove(inteceptor);
    }

    public static <T> T create(Class<T> service) {
        if (mDefaultConfig == null) {
            throw new RuntimeException("mDefaultConfig == null, you must set a default config before!");
        }
        return Api.provide(mDefaultConfig).create(service);
    }

    public static <T> T create(Class<? extends Config> clientConfig, Class<T> service) {
        return Api.provide(clientConfig).create(service);
    }

    public static <T> GetRequestBuidler get() {
        return get(mDefaultConfig);
    }

    public static <T> GetRequestBuidler get(Class<? extends Config> clientConfig) {
        if (clientConfig == null) {
            throw new RuntimeException("clientConfig == null");
        }
        return new GetRequestBuidler(clientConfig);
    }

    public static <T> PostRequestBuilder post() {
        return post(mDefaultConfig);
    }

    public static <T> PostRequestBuilder post(Class<? extends Config> clientConfig) {
        if (clientConfig == null) {
            throw new RuntimeException("clientConfig == null");
        }
        return new PostRequestBuilder(clientConfig);
    }

    public static PutRequestBuidler put() {
        return put(mDefaultConfig);
    }

    public static PutRequestBuidler put(Class<? extends Config> clientConfig) {
        if (clientConfig == null) {
            throw new RuntimeException("clientConfig == null");
        }
        return new PutRequestBuidler(clientConfig);
    }

    public static DeleteRequestBuidler delete() {
        return delete(mDefaultConfig);
    }

    public static DeleteRequestBuidler delete(Class<? extends Config> clientConfig) {
        if (clientConfig == null) {
            throw new RuntimeException("clientConfig == null");
        }
        return new DeleteRequestBuidler(clientConfig);
    }

    public static BodyRequestBuilder body() {
        return body(mDefaultConfig);
    }

    public static BodyRequestBuilder body(Class<? extends Config> clientConfig) {
        if (clientConfig == null) {
            throw new RuntimeException("clientConfig == null");
        }
        return new BodyRequestBuilder(clientConfig);
    }
}