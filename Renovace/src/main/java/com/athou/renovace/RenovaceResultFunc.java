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

import android.util.Log;

import com.athou.renovace.bean.RenovaceBean;
import com.athou.renovace.constants.RenovaceCode;
import com.google.gson.Gson;

import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import rx.functions.Func1;

/**
 * Created by athou on 2016/10/26.
 */

class RenovaceResultFunc<T> implements Func1<ResponseBody, T> {

    Type type;

    public RenovaceResultFunc(Type type) {
        this.type = type;
    }

    @Override
    public T call(ResponseBody responseBody) {
        if (ResponseBody.class.equals(type)){
            RenovaceBean<ResponseBody> bean = new RenovaceBean<>();
            bean.setResult(responseBody);
            return (T) bean.getResult();
        }
        RenovaceBean<T> baseResponse = null;
        try {
            baseResponse = new Gson().fromJson(responseBody.string(), type);
        } catch (Exception e) {
            throw new RenovaceException(RenovaceCode.CODE_PARSE_ERR, "数据解析错误");
        }
        if (baseResponse == null) {
            throw new RenovaceException(RenovaceCode.CODE_PARSE_ERR, "数据解析错误");
        }
        Log.i("Renovace", "解析成功 type：" + type);
        if (!baseResponse.isSuccess()) {
            throw new RenovaceException(baseResponse.getCode(), baseResponse.getError());
        }
        return baseResponse.getResult();
    }
}
