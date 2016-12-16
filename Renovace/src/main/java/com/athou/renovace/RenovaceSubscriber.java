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

/**
 * Created by athou on 2016/10/27.
 */


import com.athou.renovace.util.Utils;

import java.lang.reflect.Type;
import java.util.List;

import okhttp3.RequestBody;
import rx.Subscriber;

/**
 * BaseSubscriber
 *
 * @param <T>
 */
final class RenovaceSubscriber<T> extends Subscriber<T> {

    private RenovaceHttpProxy<T> proxy;
    private Type type;

    public RenovaceSubscriber(RenovaceHttpProxy<T> proxy) {
        this.proxy = proxy;
        this.type = findNeedType(proxy.getClass());
    }

    public RenovaceSubscriber(RenovaceHttpProxy<T> proxy, Class cls) {
        this.proxy = proxy;
        this.type = Utils.findNeedType(cls);
    }

    public RenovaceSubscriber(RenovaceHttpProxy proxy, Type type) {
        this.proxy = proxy;
        this.type = type;
    }

    private <R> Type findNeedType(Class<R> cls) {
        List<Type> typeList = Utils.getMethodTypes(cls);
        if (typeList == null || typeList.isEmpty()) {
            return RequestBody.class;
        }
        return typeList.get(0);
    }

    public Type getType() {
        return type;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (proxy != null) {
            proxy.onStart();
        }
    }

    @Override
    public void onCompleted() {
        if (proxy != null) {
            proxy.onCompleted();
        }
    }

    @Override
    public void onError(Throwable e) {
        if (proxy != null) {
            proxy.onError(e);
        }
    }

    @Override
    public void onNext(T responseBody) {
        if (proxy != null) {
            proxy.onSuccess(responseBody);
        }
    }
}