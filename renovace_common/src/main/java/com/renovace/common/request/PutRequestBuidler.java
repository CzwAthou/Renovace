package com.renovace.common.request;

import com.renovace.config.Config;

/**
 * @author athou
 * @date 2017/12/13
 */

public class PutRequestBuidler extends RequestBuilder<PutRequestBuidler> {

    public PutRequestBuidler(Class<? extends Config> clientConfig) {
        super(Method.Put, clientConfig);
    }
}
