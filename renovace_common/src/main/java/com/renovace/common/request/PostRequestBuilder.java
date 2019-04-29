package com.renovace.common.request;

import com.renovace.config.Config;

/**
 * @author athou
 * @date 2017/12/13
 */

public class PostRequestBuilder extends RequestBuilder<PostRequestBuilder> {

    public PostRequestBuilder(Class<? extends Config> clientConfig) {
        super(Method.Post, clientConfig);
    }
}
