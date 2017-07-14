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

import com.athou.renovace.util.Utils;

import rx.Subscriber;

/**
 * BaseSubscriber
 */
final class RenovaceCallSubscriber<T> extends Subscriber<T> {

    private IRenovaceCallBack<T> callBack;

    public RenovaceCallSubscriber(IRenovaceCallBack<T> callBack) {
        this.callBack = callBack;
        if (this.callBack != null) {
            this.callBack.setSubscription(this);
        }
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
    public void onNext(T responseBody) {
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