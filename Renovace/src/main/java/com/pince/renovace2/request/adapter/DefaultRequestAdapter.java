package com.pince.renovace2.request.adapter;

import com.pince.renovace2.BaseApiService;
import com.pince.renovace2.Renovace;
import com.pince.renovace2.request.BodyRequestBuilder;
import com.pince.renovace2.request.RequestBuilder;
import com.pince.renovace2.request.UploadRequestBuidler;

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
                case UploadFile:
                    BaseApiService apiService = Renovace.create(builder.getConfigCls(), BaseApiService.class);
                    UploadRequestBuidler uploadRequestBuidler = (UploadRequestBuidler) builder;
                    if (uploadRequestBuidler.isMultiFile()) {
                        return apiService.uploadFiles(builder.getHeaders(), builder.getUrl(), uploadRequestBuidler.getParts());
                    } else {
                        return apiService.uploadFile(builder.getHeaders(), builder.getUrl(), uploadRequestBuidler.getDescription(), uploadRequestBuidler.getPart());
                    }
                case Download:
                    return Renovace.create(builder.getConfigCls(), BaseApiService.class)
                            .download(builder.getHeaders(), builder.getUrl());
                default:
                    return Observable.never();
            }
        }
        return Observable.never();
    }
}
