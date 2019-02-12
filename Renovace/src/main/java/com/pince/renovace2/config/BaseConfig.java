package com.pince.renovace2.config;

import android.text.TextUtils;

import com.pince.renovace2.Renovace;
import com.pince.renovace2.cache.HttpCache;
import com.pince.renovace2.interceptor.CacheInterceptor;
import com.pince.renovace2.interceptor.LogInterceptor;
import com.pince.renovace2.interceptor.MainInterceptor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
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
        okHttpClientBuilder.addInterceptor(new MainInterceptor());
        okHttpClientBuilder.addNetworkInterceptor(new LogInterceptor());
        client(okHttpClientBuilder);
        okHttpClientBuilder.addInterceptor(new CacheInterceptor());
        okHttpClientBuilder.cache(HttpCache.getCache(Renovace.getContext().getExternalCacheDir() + "/http-cache"));
        okHttpClientBuilder.retryOnConnectionFailure(true);
        okHttpClientBuilder.connectTimeout(5000, TimeUnit.MILLISECONDS);
        return okHttpClientBuilder.build();
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