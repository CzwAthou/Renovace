package com.renovace.common;

import com.renovace.common.request.BodyRequestBuilder;
import com.renovace.common.request.DeleteRequestBuidler;
import com.renovace.common.request.GetRequestBuidler;
import com.renovace.common.request.PostRequestBuilder;
import com.renovace.common.request.PutRequestBuidler;
import com.renovace.common.upload.UploadRequest;
import com.renovace.config.Config;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author yunshan
 * @date 2019-04-29
 */
public class RequestFactory {

    private static Class<? extends Config> mDefaultConfig;
    private static List<RespCodeInteceptor> respCodeinteceptorList = null;

    private RequestFactory() {
        respCodeinteceptorList = new CopyOnWriteArrayList<>();
    }

    public static synchronized List<RespCodeInteceptor> getRespCodeInteceptorList() {
        return respCodeinteceptorList;
    }

    public static void addRespCodeInteceptor(RespCodeInteceptor inteceptor) {
        respCodeinteceptorList.add(inteceptor);
    }

    public static void removeRespCodeInteceptor(RespCodeInteceptor inteceptor) {
        respCodeinteceptorList.remove(inteceptor);
    }

    public static void setDefaultConfig(Class<? extends Config> defaultConfig) {
        RequestFactory.mDefaultConfig = defaultConfig;
    }

    @Deprecated
    static Class<? extends Config> getDefaultConfig() {
        return mDefaultConfig;
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

    public static UploadRequest upload() {
        return upload(mDefaultConfig);
    }

    public static UploadRequest upload(Class<? extends Config> clientConfig) {
        if (clientConfig == null) {
            throw new RuntimeException("clientConfig == null");
        }
        return new UploadRequest(clientConfig);
    }
}
