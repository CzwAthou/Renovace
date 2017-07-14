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

import java.lang.reflect.Type;

import rx.Subscription;

/**
 * IRenovaceCallBack Support your custom data model<br>
 * Created by athou on 2016/10/27.
 */
public interface IRenovaceCallBack<R> {

    /**
     * when your data struct is {@link com.athou.renovace.RenovaceFunc.StructType#Result} ,
     * and you use getResult/postResult method for getting http-data, the type is the {@link com.athou.renovace.bean.RenovaceBean}
     * 's ParameterizedType. otherwize the type is the whole bean will be parsed.
     *
     * @return
     */
    Type getType();

    /**
     * setRequestUri for saving the url
     *
     * @param url
     */
    void setRequestUri(String url);

    /**
     * save Subscription, when your loading dialog is cancel,so your equest can be cancled auto!
     *
     * @param subscription
     */
    void setSubscription(Subscription subscription);

    /**
     * getResult net data start, you can show a dialog in this method
     */
    void onStart();

    /**
     * the progress tfor download file
     *
     * @param curDownSize has downloaded file size
     * @param totalSize   the total file size
     */
    void onProgress(long curDownSize, long totalSize);

    /**
     * the request be canceled
     */
    void onCancel();

    /**
     * getResult net data and parse the data success, you can getResult the data from return the bean
     */
    void onSuccess(R response);

    /**
     * getResult net data and parse the data complete, this method will call after {@link #onSuccess(Object)}
     */
    void onCompleted();

    /**
     * an error happened when getting net data
     *
     * @param e the error
     */
    void onError(Throwable e);
}