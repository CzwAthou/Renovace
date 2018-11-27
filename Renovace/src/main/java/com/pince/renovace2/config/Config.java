package com.pince.renovace2.config;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * @author athou
 * @date 2017/12/13
 */

public interface Config {

    void build(Retrofit.Builder builder);

    OkHttpClient client();
}
