package com.renovace.common.request;

import com.renovace.config.Config;

/**
 * @author athou
 * @date 2017/12/13
 */

public class GetRequestBuidler extends RequestBuilder<GetRequestBuidler> {

    public GetRequestBuidler(Class<? extends Config> clientConfig) {
        super(Method.Get, clientConfig);
    }
}
