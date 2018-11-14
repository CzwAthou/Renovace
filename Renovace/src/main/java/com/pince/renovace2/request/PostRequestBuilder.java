package com.pince.renovace2.request;

import com.pince.renovace2.config.Config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author athou
 * @date 2017/12/13
 */

public class PostRequestBuilder extends RequestBuilder<PostRequestBuilder> {

    public PostRequestBuilder(Class<? extends Config> clientConfig) {
        super(Method.Post, clientConfig);
    }
}
