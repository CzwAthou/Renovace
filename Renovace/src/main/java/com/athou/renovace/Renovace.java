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

import com.athou.renovace.util.Utils;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by athou on 2016/10/26.
 */
public class Renovace {
    private String TAG = Renovace.class.getSimpleName();

    private static Renovace unique = null;

    public static Renovace getInstance() {
        synchronized (Renovace.class) {
            if (unique == null) {
                unique = new Renovace();
            }
            return unique;
        }
    }

    private IRenovace iRenovace;
    private BaseApiService apiManager;

    /**
     * 初始化，如果传入的是一个url, 则将创建一个默认的OkHttpClient
     *
     * @param baseUrl
     */
    public void init(String baseUrl) {
        init(new RenovaceWrapper(baseUrl));
    }

    /**
     * 初始化，如果传入的是一个url已经用户自定义实现的OkHttpClient, retrofit将内部自己创建
     *
     * @param baseUrl
     */
    public void init(String baseUrl, IRenovace.IHttpClient httpCLient) {
        init(new RenovaceWrapper(baseUrl, httpCLient));
    }

    /**
     * 初始化，如果传入的是一个IRenovace, 那么用户需要自己实现这个接口
     *
     * @param iRenovace
     */
    public void init(IRenovace iRenovace) {
        this.iRenovace = iRenovace;
        this.apiManager = Utils.checkNotNull(iRenovace, "iRenovace == null").getRetrofit().create(BaseApiService.class);
    }

    /**
     * 提供用户自定义接口
     *
     * @param service
     * @param <T>
     * @return
     */
    public <T> T create(Class<T> service) {
        return iRenovace.getRetrofit().create(service);
    }

    /**
     * 自定义接口后，调用此方法进行请求
     */
    public <T> void call(Observable observable, IRenovaceCallBack<T> callBack) {
        call(observable, new RenovaceCallSubscriber(callBack));
    }

    /**
     * 自定义接口后，调用此方法进行请求
     */
    public <T> void call(Observable observable, Subscriber<T> subscriber) {
        observable.compose(schedulersTransformer)
                .subscribe(subscriber);
    }

    private <T> void toSubscribe(Observable observable, Type type, Subscriber<T> subscriber) {
        if (subscriber instanceof RenovaceSubscriber) {
            observable.map(new RenovaceResultFunc<T>(type))
                    .compose(schedulersTransformer)
                    .subscribe(subscriber);
        } else {
            observable.map(new SubscribeResultFunc<T>(type))
                    .compose(schedulersTransformer)
                    .subscribe(subscriber);
        }
    }

    //GET请求=====================================================================

    /**
     * get请求，不带参数
     */
    public <T> void get(final String url, final IRenovaceCallBack<T> callBack) {
        toSubscribe(apiManager.get(url), findNeedType(callBack.getClass()), new RenovaceSubscriber<T>(callBack));
    }

    /**
     * get请求，带参数
     */
    public <T> void get(final String url, final Map<String, String> maps, final IRenovaceCallBack<T> callBack) {
        toSubscribe(apiManager.get(url, maps), findNeedType(callBack.getClass()), new RenovaceSubscriber<T>(callBack));
    }

    /**
     * get请求，不带参数
     */
    public <T> void get(String url, Subscriber<T> subscriber) {
        toSubscribe(apiManager.get(url), findNeedType(subscriber.getClass()), subscriber);
    }

    /**
     * get请求，带参数
     */
    public <T> void get(String url, Map<String, String> maps, Subscriber<T> subscriber) {
        toSubscribe(apiManager.get(url, maps), findNeedType(subscriber.getClass()), subscriber);
    }

    //POST请求=====================================================================

    /**
     * post请求，不带参数
     */
    public <T> void post(final String url, final IRenovaceCallBack<T> callBack) {
        toSubscribe(apiManager.post(url), findNeedType(callBack.getClass()), new RenovaceSubscriber<T>(callBack));
    }

    /**
     * post请求，带参数
     */
    public <T> void post(final String url, final Map<String, String> maps, final IRenovaceCallBack<T> callBack) {
        toSubscribe(apiManager.post(url, maps), findNeedType(callBack.getClass()), new RenovaceSubscriber<T>(callBack));
    }

    /**
     * post请求，不带参数
     */
    public <T> void post(String url, Subscriber<T> subscriber) {
        toSubscribe(apiManager.post(url), findNeedType(subscriber.getClass()), subscriber);
    }

    /**
     * post请求，带参数
     */
    public <T> void post(String url, Map<String, String> parameters, Subscriber<T> subscriber) {
        toSubscribe(apiManager.post(url, parameters), findNeedType(subscriber.getClass()), subscriber);
    }

    //DELETE请求=====================================================================

    /**
     * delete请求,带参数，并已经完成解析
     */
    public <T> void delete(final String url, final Map<String, String> maps, final IRenovaceCallBack<T> callBack) {
        toSubscribe(apiManager.delete(url, maps), findNeedType(callBack.getClass()), new RenovaceSubscriber(callBack));
    }

    //PUT请求=====================================================================

    /**
     * put请求,带参数，并已经完成解析
     */
    public <T> void put(final String url, Map<String, String> maps, final IRenovaceCallBack<T> callBack) {
        toSubscribe(apiManager.put(url, maps), findNeedType(callBack.getClass()), new RenovaceSubscriber<T>(callBack));
    }

    //上传文件=====================================================================

    /**
     * upload请求，上传单个文件
     */
    public <T> void upload(String url, RequestBody requestBody, Subscriber<ResponseBody> subscriber) {
        toSubscribe(apiManager.uploadFile(url, requestBody), ResponseBody.class, subscriber);
    }

    /**
     * upload请求，上传多个文件
     */
    public <T> void upload(String url, String description, Map<String, RequestBody> maps, Subscriber<ResponseBody> subscriber) {
        toSubscribe(apiManager.uploadFiles(url, description, maps), ResponseBody.class, subscriber);
    }

    /**
     * 统一的线程转换格式
     */
    private final Observable.Transformer schedulersTransformer = new Observable.Transformer() {
        @Override
        public Object call(Object observable) {
            return ((Observable) observable).subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .doOnError(new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread());
        }
    };

    /**
     * find the type by interfaces
     *
     * @param cls
     * @param <T>
     * @return
     */
    private <T> Type findNeedType(Class<T> cls) {
        List<Type> typeList = Utils.getMethodTypes(cls);
        if (typeList == null || typeList.isEmpty()) {
            return RequestBody.class;
        }
        return typeList.get(0);
    }
}
