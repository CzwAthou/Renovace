package com.pince.renovace2.request;

import com.pince.renovace2.config.Config;

/**
 * @author athou
 * @date 2017/12/13
 */

public class DeleteRequestBuidler extends RequestBuilder<DeleteRequestBuidler> {

    public DeleteRequestBuidler(Class<? extends Config> clientConfig) {
        super(Method.Delete, clientConfig);
    }
}
