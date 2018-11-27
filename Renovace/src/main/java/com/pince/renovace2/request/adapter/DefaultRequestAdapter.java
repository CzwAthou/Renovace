package com.pince.renovace2.request.adapter;

import com.pince.renovace2.BaseApiService;
import com.pince.renovace2.Renovace;
import com.pince.renovace2.request.BodyRequestBuilder;
import com.pince.renovace2.request.RequestBuilder;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

/**
 * @author athou
 * @date 2017/12/13
 */

public class DefaultRequestAdapter implements RequestAdapter {

    @Override
    public Observable<ResponseBody> adapt(RequestBuilder builder) {
        if (builder != null) {
            switch (builder.getMethod()) {
                case Get:
                    return Renovace.create(builder.getConfigCls(), BaseApiService.class)
                            .get(builder.getHeaders(), builder.getUrl(), builder.getParams());
                case Post:
                    return Renovace.create(builder.getConfigCls(), BaseApiService.class)
                            .post(builder.getHeaders(), builder.getUrl(), builder.getParams());
                case Put:
                    return Renovace.create(builder.getConfigCls(), BaseApiService.class)
                            .put(builder.getHeaders(), builder.getUrl(), builder.getParams());
                case Delete:
                    return Renovace.create(builder.getConfigCls(), BaseApiService.class)
                            .delete(builder.getHeaders(), builder.getUrl(), builder.getParams());
                case Body:
                    return Renovace.create(builder.getConfigCls(), BaseApiService.class)
                            .body(builder.getHeaders(), builder.getUrl(), ((BodyRequestBuilder) builder).getBody());
                default:
                    return Observable.never();
            }
        }
        return Observable.never();
    }
}
