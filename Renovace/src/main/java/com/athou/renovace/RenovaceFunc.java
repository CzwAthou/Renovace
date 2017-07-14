/*
 * Copyright (c) 2016  athou(cai353974361@163.com).
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

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import rx.functions.Func1;

/**
 * convert the ResponseBody to the special type, it support the whole data type, contains basicType, such as String,long...,<br>
 * and it also can parse the List and map type, so any struct data can be parsed.
 * <p>
 * Created by athou on 2016/12/15.
 */
class RenovaceFunc<T> implements Func1<ResponseBody, T> {
    /**
     * the date struct type, has 3 kinds
     */
    enum StructType {
        /**
         * struct is:{"code":0,"error":"","result":{"name":"renovace"}}
         */
        Result,
        /**
         * custom struct is: {"code":0,"name":"renovace"}
         */
        Bean,
        /**
         * full custom struct is:{"xxx":"xxx","xxx":"xxx"}
         */
        Direct
    }

    IRenovaceSubscription mSubscription;
    StructType mStructType;
    /**
     * the ResponseBody data will be parse to this type
     */
    Type type;

    public RenovaceFunc(IRenovaceSubscription subscription, StructType structType) {
        this.mSubscription = subscription;
        this.mStructType = structType;
        this.type = this.mSubscription.getType();
    }

    /**
     * check current thread is unSubscriber or not
     *
     * @return true: isUnsubscribed
     */
    protected boolean checkUnSubscriber() {
        if (mSubscription != null && mSubscription.isUnsubscribed()) {
            return true;
        }
        return false;
    }

    @Override
    public T call(ResponseBody responseBody) {
        if (checkUnSubscriber()) {
            throw new RenovaceException(RenovaceCode.CODE_REQUEST_CANCEL, "Requst Cancelled");
        } else {
            return parse(responseBody);
        }
    }

    /**
     * if parse type is child of RenovaceBean, otherwize parse the type you specify
     *
     * @param responseBody
     * @return
     */
    protected T parse(ResponseBody responseBody) {
        if (type instanceof ParameterizedType) {
            Class<T> cls = (Class) ((ParameterizedType) type).getRawType();
            //if the parse type is RenovaceBean, so call method parseRenovaceBean(),
            //otherwize call method parseBasicTypes()
            if (RenovaceBean.class.isAssignableFrom(cls)) {
                return parseRenovaceBean(responseBody);
            } else {
                return parseBasicTypes(responseBody, cls);
            }
        } else {
            return parseBasicTypes(responseBody, (Class<T>) type);
        }
    }

    /**
     * parse the bean that extends RenovaceBean
     *
     * @param responseBody
     * @return
     */
    private T parseRenovaceBean(ResponseBody responseBody) {
        RenovaceBean<T> bean = null;
        try {
            bean = Renovace.getInstance().getGson().fromJson(responseBody.string(), type);
        } catch (Exception e) {
            throw createParseException(e);
        }
        if (bean == null) {
            throw new RenovaceException(RenovaceCode.CODE_PARSE_ERR, "Json Parse error:" + type);
        }
        Utils.logI("parse succeed ==> type:" + type);
        switch (mStructType) {
            case Result:
                if (!bean.isSuccess()) {
                    throw new RenovaceException(bean.getCode(), bean.getError());
                }
                return (T) bean;
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

    /**
     * parse the basic type
     *
     * @param cls juge if the type is basic type or not
     * @see java.lang.CharSequence
     * @see java.lang.Character
     * @see java.lang.Boolean
     * @see java.lang.Double
     * @see java.lang.Long
     * @see java.lang.Short
     * @see java.lang.Float
     * @see java.lang.Integer
     * @see java.lang.Byte
     * @see java.io.InputStream
     * @see okhttp3.ResponseBody
     */
    private T parseBasicTypes(ResponseBody responseBody, Class<T> cls) {
        if (CharSequence.class.isAssignableFrom(cls)) {
            try {
                return (T) responseBody.string();
            } catch (IOException e) {
                throw createParseException(e);
            }
        } else if (Boolean.class.isAssignableFrom(cls)) {
            try {
                return (T) Boolean.valueOf(responseBody.string());
            } catch (IOException e) {
                throw createParseException(e);
            }
        } else if (Double.class.isAssignableFrom(cls)) {
            try {
                return (T) Double.valueOf(responseBody.string());
            } catch (IOException e) {
                throw createParseException(e);
            }
        } else if (Long.class.isAssignableFrom(cls)) {
            try {
                return (T) Long.valueOf(responseBody.string());
            } catch (IOException e) {
                throw createParseException(e);
            }
        } else if (Short.class.isAssignableFrom(cls)) {
            try {
                return (T) Short.valueOf(responseBody.string());
            } catch (IOException e) {
                throw createParseException(e);
            }
        } else if (Float.class.isAssignableFrom(cls)) {
            try {
                return (T) Float.valueOf(responseBody.string());
            } catch (IOException e) {
                throw createParseException(e);
            }
        } else if (Integer.class.isAssignableFrom(cls)) {
            try {
                return (T) Integer.valueOf(responseBody.string());
            } catch (IOException e) {
                throw createParseException(e);
            }
        } else if (Byte.class.isAssignableFrom(cls)) {
            try {
                return (T) Byte.valueOf(responseBody.string());
            } catch (IOException e) {
                throw createParseException(e);
            }
        } else if (InputStream.class.isAssignableFrom(cls)) {
            return (T) responseBody.byteStream();
        } else if (ResponseBody.class.isAssignableFrom(cls)) {
            return (T) responseBody;
        } else if (RenovaceBean.class.isAssignableFrom(cls)) {
            return parseRenovaceBean(responseBody);
        } else {
            try {
                //the parse data type use type not cls
                return Renovace.getInstance().getGson().fromJson(responseBody.string(), type);
            } catch (Exception e) {
                throw createParseException(e);
            }
        }
    }

    private RenovaceException createParseException(Exception e) {
        return new RenovaceException(RenovaceCode.CODE_PARSE_ERR, "Json Parse error:" + type + "\n" + e.getMessage());
    }

    public static class FuncSubscription implements IRenovaceSubscription {
        Type type;

        public FuncSubscription(Type type) {
            this.type = type;
        }

        @Override
        public Type getType() {
            return type;
        }

        @Override
        public boolean isUnsubscribed() {
            return false;
        }
    }
}