/*
 * Copyright (c) 2017  athou(cai353974361@163.com)
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

import com.athou.renovace.util.Utils;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import rx.Subscriber;

/**
 * rxjava return data callback, it will get the type will be parsed<br>
 * Created by athou on 2016/10/27.
 *
 * @param <R>
 */
class RenovaceSubscriber<R> extends Subscriber<R> implements IRenovaceSubscription {

    private IRenovaceCallBack callBack;

    public RenovaceSubscriber(IRenovaceCallBack callBack) {
        this.callBack = callBack;
        if (this.callBack != null) {
            this.callBack.setSubscription(this);
        }
    }

    @Override
    public Type getType() {
        if (this.callBack != null) {
            return callBack.getType();
        }
        return new TypeToken<ResponseBody>() {
        }.getType();
    }

    @Override
    public void onStart() {
        if (callBack != null) {
            callBack.onStart();
        }
        super.onStart();
    }

    @Override
    public void onCompleted() {
        if (callBack != null) {
            callBack.onCompleted();
        }
    }

    @Override
    public void onError(Throwable e) {
        if (callBack != null) {
            callBack.onError(e);
        }
    }

    @Override
    public void onNext(R responseBody) {
        if (!isUnsubscribed()) {
            Utils.logI("onNext:" + responseBody.getClass());
            if (callBack != null) {
                callBack.onSuccess(responseBody);
            }
        } else {
            Utils.logE("onNext has be Cancel:" + responseBody.getClass());
        }
    }
}