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

package com.pince.renovace2;

import com.pince.renovace2.Util.RenovaceLogUtil;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import io.reactivex.functions.Function;
import okhttp3.ResponseBody;

/**
 * convert the ResponseBody to the special type, it support the whole data type, contains basicType, such as String,long...,<br>
 * and it also can parse the List and map type, so any struct data can be parsed.
 * <p>
 *
 * @author athou
 * @date 2016/12/15
 */
public class RenovaceFunc<T> implements Function<ResponseBody, T> {


    StructType mStructType = StructType.Result;
    /**
     * the ResponseBody data will be parse to this type
     */
    Type type;

    public RenovaceFunc(Type type, StructType structType) {
        this.mStructType = structType == null ? StructType.Result : structType;
        this.type = type;
    }


    @Override
    public T apply(ResponseBody responseBody) throws Exception {
        return parse(responseBody);
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
                mStructType = StructType.Direct;
                return parseBasicTypes(responseBody, cls);
            }
        } else {
            if (RenovaceBean.class.isAssignableFrom((Class<T>) type)) {
                return parseRenovaceBean(responseBody);
            } else {
                mStructType = StructType.Direct;
                return parseBasicTypes(responseBody, (Class<T>) type);
            }
        }
    }

    private boolean inteceptorResp(int code, String extra) {
        if (Renovace.getRespCodeinteceptorList() != null && !Renovace.getRespCodeinteceptorList().isEmpty()) {
            for (RespCodeInteceptor inteceptor : Renovace.getRespCodeinteceptorList()) {
                return inteceptor.inteceptorRespCode(code, extra);
            }
        }
        return false;
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
            bean = Renovace.getGson().fromJson(responseBody.string(), type);
        } catch (Exception e) {
            throw createParseException(e);
        }
        if (bean == null) {
            throw new RenovaceException(RenovaceCode.CODE_PARSE_ERR, "Json Parse error:" + type);
        }
        RenovaceLogUtil.logI("parse succeed ==> type:" + type);
        switch (mStructType) {
            case Result:
                if (!bean.isSuccess()) {
                    if (inteceptorResp(bean.getCode(), bean.getExtra())) {
                    }
                    throw new RenovaceException(bean.getCode(), bean.getMessage());
                }
                return (T) bean.getData();
            case Bean:
                if (!bean.isSuccess()) {
                    if (inteceptorResp(bean.getCode(), bean.getExtra())) {
                    }
                    throw new RenovaceException(bean.getCode(), bean.getMessage());
                }
                return (T) bean;
            case Direct:
                return (T) bean;
            default:
                throw new RenovaceException(bean.getCode(), bean.getMessage());
        }
    }

    /**
     * parse the basic type
     *
     * @param cls juge if the type is basic type or not
     * @see CharSequence
     * @see Character
     * @see Boolean
     * @see Double
     * @see Long
     * @see Short
     * @see Float
     * @see Integer
     * @see Byte
     * @see InputStream
     * @see ResponseBody
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
                return Renovace.getGson().fromJson(responseBody.string(), type);
            } catch (Exception e) {
                throw createParseException(e);
            }
        }
    }

    private RenovaceException createParseException(Exception e) {
        return new RenovaceException(RenovaceCode.CODE_PARSE_ERR, "Json Parse error:" + type + "\n" + e.getMessage());
    }
}