package com.pince.renovace2.interceptor;

import com.pince.renovace2.Util.RenovaceLogUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * @author athoucai
 * @date 2019/2/12
 */
public class RenovaceLogInterceptor implements Interceptor {

    private HttpLoggingInterceptor httpLoggingInterceptor;

    public RenovaceLogInterceptor() {
        httpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                RenovaceLogUtil.logD(message);
            }
        });
        httpLoggingInterceptor.setLevel(RenovaceLogUtil.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        return httpLoggingInterceptor.intercept(chain);
    }
}
