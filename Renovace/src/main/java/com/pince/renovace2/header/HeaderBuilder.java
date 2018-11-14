package com.pince.renovace2.header;

import com.pince.renovace2.cache.CacheStrategy;

import java.util.LinkedHashMap;
import java.util.Map;

import io.reactivex.annotations.NonNull;

/**
 * @author athoucai
 * @date 2018/11/14
 */
public class HeaderBuilder {
    private Map<String, Object> mHeaders;

    public HeaderBuilder() {
        mHeaders = new LinkedHashMap<>();
    }

    public HeaderBuilder cache(@NonNull CacheStrategy cacheStrategy) {
        return addHeader(HeaderKey.Cache, cacheStrategy.getValue());
    }

    public HeaderBuilder readTimeOut(int readTimeOutMils) {
        return addHeader(HeaderKey.ReadTimeOut, readTimeOutMils);
    }

    public HeaderBuilder writeTimeOut(int writeTimeOutMils) {
        return addHeader(HeaderKey.WriteTimeOut, writeTimeOutMils);
    }

    public HeaderBuilder connectTimeOut(int connectTimeOutMils) {
        return addHeader(HeaderKey.ConnectTimeOut, connectTimeOutMils);
    }

    public HeaderBuilder addHeader(String key, Object val) {
        if (mHeaders == null) {
            mHeaders = new LinkedHashMap<>();
        }
        mHeaders.put(key, val);
        return this;
    }

    public String build() {
        return String.format("%s:%s", HeaderKey.Header, mHeaders.toString());
    }
}
