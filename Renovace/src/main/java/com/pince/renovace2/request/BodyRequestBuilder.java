package com.pince.renovace2.request;

import com.pince.renovace2.Renovace;
import com.pince.renovace2.config.Config;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * @author athoucai
 * @date 2018/4/16
 */
public class BodyRequestBuilder extends RequestBuilder<BodyRequestBuilder> {

    public BodyRequestBuilder(Class<? extends Config> configCls) {
        super(Method.Body, configCls);
    }

    private RequestBody mBody;

    public BodyRequestBuilder body(RequestBody body) {
        this.mBody = body;
        return this;
    }

    public BodyRequestBuilder json(String jsonStr) {
        return body(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonStr));
    }

    public BodyRequestBuilder object(Object object) {
        return json(Renovace.getGson().toJson(object));
    }

    public RequestBody getBody() {
        return mBody;
    }
}