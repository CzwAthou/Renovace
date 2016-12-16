/*
 * Copyright (c) 2016  athou（cai353974361@163.com）.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.athou.renovace;

import com.athou.renovace.bean.RenovaceBean;
import com.athou.renovace.constants.RenovaceCode;
import com.athou.renovace.util.Utils;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import rx.functions.Func1;

/**
 * Created by athou on 2016/12/15.
 */

class RenovaceFunc<T> implements Func1<ResponseBody, T> {
    RenovaceSubscriber subscriber;
    RenovaceFunc.ParseType parseType;
    Type type;

    public RenovaceFunc(RenovaceSubscriber subscriber, RenovaceFunc.ParseType parseType) {
        this.subscriber = subscriber;
        this.parseType = parseType;
        this.type = subscriber.getType();
    }

    protected boolean checkUnSubscriber() {
        if (subscriber != null && subscriber.isUnsubscribed()) {
            return true;
        }
        return false;
    }

    @Override
    public T call(ResponseBody responseBody) {
        if (checkUnSubscriber()) {
            throw new RenovaceException(RenovaceCode.CODE_REQUEST_CANCEL, "请求取消");
        } else {
            return parse(responseBody);
        }
    }

    protected T parse(ResponseBody responseBody) {
        if (String.class.equals(type)) {
            try {
                return (T) responseBody.string();
            } catch (IOException e) {
                throw new RenovaceException(RenovaceCode.CODE_PARSE_ERR, "数据解析错误:" + type + "\n" + e.getMessage());
            }
        } else if (byte[].class.equals(type)) {
            try {
                return (T) responseBody.bytes();
            } catch (IOException e) {
                throw new RenovaceException(RenovaceCode.CODE_PARSE_ERR, "数据解析错误:" + type + "\n" + e.getMessage());
            }
        } else if (InputStream.class.equals(type)) {
            return (T) responseBody.byteStream();
        } else if (ResponseBody.class.equals(type)) {
            return (T) responseBody;
        } else {
            RenovaceBean<T> bean = null;
            try {
                bean = new Gson().fromJson(responseBody.string(), type);
            } catch (Exception e) {
                throw new RenovaceException(RenovaceCode.CODE_PARSE_ERR, "数据解析错误");
            }
            if (bean == null) {
                throw new RenovaceException(RenovaceCode.CODE_PARSE_ERR, "数据解析错误");
            }
            Utils.logI("解析成功 type：" + type);
            switch (parseType) {
                case Result:
                    if (!bean.isSuccess()) {
                        throw new RenovaceException(bean.getCode(), bean.getError());
                    }
                    return bean.getResult();
                case Bean:
                    if (!bean.isSuccess()) {
                        throw new RenovaceException(bean.getCode(), bean.getError());
                    }
                    return (T) bean;
                case Direct:
                    return (T) bean;
                default:
                    throw new RenovaceException(bean.getCode(), bean.getError());
            }
        }
    }

    enum ParseType {
        /**
         * 数据结构为：
         * {
         * "code":0,
         * "err":"",
         * "result":{
         * "name":"renovace"
         * }
         * }
         */
        Result,
        /**
         * 自定义数据结构为：
         * {
         * "code":0,
         * "name":"renovace"
         * }
         */
        Bean,
        /**
         * 完全自定义数据结构为：
         * {
         * "xxx":"xxx",
         * "xxx":"xxx"
         * }
         */
        Direct
    }
}
