package com.renovace.config;

import android.text.TextUtils;

import com.renovace.Renovace;
import com.renovace.cache.HttpCache;
import com.renovace.interceptor.CacheInterceptor;
import com.renovace.interceptor.RenovaceLogInterceptor;
import com.renovace.interceptor.RenovaceInterceptor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * description
 *
 * @author czwathou
 * @date 2017/12/6 19:30
 */
public abstract class BaseConfig implements Config {

    @Override
    public void build(Retrofit.Builder builder) {
        builder.client(client());
        if (!TextUtils.isEmpty(getBaseUrl())) {
            builder.baseUrl(getBaseUrl());
        } else if (getBaseHttpUrl() != null) {
            builder.baseUrl(getBaseHttpUrl());
        } else {
            throw new RuntimeException("you mast override method getBaseUrl or getBaseHttpUrl");
        }
        addConverterFactory(builder);
        addCallAdapterFactory(builder);
    }

    @Override
    public void reset(Retrofit.Builder builder) {
        if (!TextUtils.isEmpty(getBaseUrl())) {
            builder.baseUrl(getBaseUrl());
        } else if (getBaseHttpUrl() != null) {
            builder.baseUrl(getBaseHttpUrl());
        } else {
            throw new RuntimeException("you mast override method getBaseUrl or getBaseHttpUrl");
        }
    }

    private void addCallAdapterFactory(Retrofit.Builder builder) {
        List<CallAdapter.Factory> factories = getCallAdapterFactories();
        if (factories != null) {
            for (CallAdapter.Factory factory : factories) {
                builder.addCallAdapterFactory(factory);
            }
        }
    }

    private void addConverterFactory(Retrofit.Builder builder) {
        List<Converter.Factory> factories = getConverterFactories();
        if (factories != null) {
            for (Converter.Factory factory : factories) {
                builder.addConverterFactory(factory);
            }
        }
    }

    @Override
    public OkHttpClient client() {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        okHttpClientBuilder.addInterceptor(new RenovaceInterceptor());
        okHttpClientBuilder.addNetworkInterceptor(new RenovaceLogInterceptor());
        client(okHttpClientBuilder);
        okHttpClientBuilder.addInterceptor(new CacheInterceptor());

        okHttpClientBuilder.cache(HttpCache.getCache(cachePath()));
        okHttpClientBuilder.retryOnConnectionFailure(true);
        okHttpClientBuilder.connectTimeout(5000, TimeUnit.MILLISECONDS);
        return okHttpClientBuilder.build();
    }

    protected String cachePath() {
        return Renovace.getContext().getExternalCacheDir() + "/http-cache";
    }

    /**
     * 吧clientBuilder提供给用户，做自定义用
     *
     * @param clientBuilder
     */
    public abstract void client(OkHttpClient.Builder clientBuilder);

    protected abstract String getBaseUrl();

    protected HttpUrl getBaseHttpUrl() {
        return HttpUrl.get(getBaseUrl());
    }

    protected List<Converter.Factory> getConverterFactories() {
        List<Converter.Factory> factories = new ArrayList<>();
        factories.add(GsonConverterFactory.create(Renovace.getGson()));
        return factories;
    }

    protected List<CallAdapter.Factory> getCallAdapterFactories() {
        List<CallAdapter.Factory> factories = new ArrayList<>();
        factories.add(new ThreadCallAdapterFactory());
        return factories;
    }
}