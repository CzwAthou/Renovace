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

import android.content.Context;

import com.athou.renovace.download.DownLoadCallBack;
import com.athou.renovace.download.DownSubscriber;
import com.athou.renovace.util.Utils;

import java.util.Map;

import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by athou on 2016/10/26.
 */
public class Renovace {
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
    public void init(Context context, String baseUrl) {
        init(new RenovaceWrapper(context, baseUrl));
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
     * @param <R>
     * @return
     */
    public <R> R create(Class<R> service) {
        return iRenovace.getRetrofit().create(service);
    }

    /**
     * 自定义接口后，调用此方法进行请求
     */
    public <R> Subscriber call(Observable observable, IRenovaceCallBack<R> callBack) {
        return call(observable, new RenovaceCallSubscriber(callBack));
    }

    /**
     * 自定义接口后，调用此方法进行请求
     */
    public <R> Subscriber call(Observable observable, Subscriber<R> subscriber) {
        observable.compose(schedulersTransformer)
                .subscribe(subscriber);
        return subscriber;
    }

    private <R> Subscriber toSubscribe(Observable observable, RenovaceFunc.ParseType parseType, RenovaceSubscriber<R> subscriber) {
        observable.map(new RenovaceFunc<R>(subscriber, parseType))
                .compose(schedulersTransformer)
                .subscribe(subscriber);
        return subscriber;
    }

    public <R> Subscriber toSubscribe(Observable observable, Subscriber<R> subscriber) {
        observable.compose(schedulersTransformer)
                .subscribe(subscriber);
        return subscriber;
    }

    //GET请求=====================================================================

    /**
     * get请求，适用数据结构：
     * {
     * "code":0,
     * "err":"",
     * "result":{
     * "name":"renovace"
     * }
     * }<br>
     * 返回数据为result， code以及err已经收集在回调的err里面<br>
     * 如果你的数据结构里面的关键字不是code,err,result,当然你也可以继承renovacebean后，重写code,err,result
     */
    public <R> Subscriber getResult(String url, RenovaceHttpProxy<R> callBack) {
        return getResult(url, RequestParams.emptyParams, callBack);
//        return toSubscribe(apiManager.get(RequestParams.emptyHeader, url), RenovaceFunc.ParseType.Result, new RenovaceSubscriber<R>(callBack));
    }

    /**
     * get请求,带参数
     *
     * @see #getResult(String, RenovaceHttpProxy)
     */
    public <R> Subscriber getResult(String url, RequestParams maps, RenovaceHttpProxy<R> callBack) {
        return toSubscribe(apiManager.get(maps.getHeader(), url, maps.getParams()), RenovaceFunc.ParseType.Result, new RenovaceSubscriber<R>(callBack));
    }

    /**
     * get请求，自定义数据结构
     * {
     * "code":0,
     * "name":"renovace"
     * }<br>
     * 返回数据为整个bean，如果code是异常code, code以及err已经收集在回调的err里面<br>
     * 如果你的数据结构里面的关键字不是code,err,result,当然你也可以继承renovacebean后，重写code,err,result
     */
    public <R> Subscriber getBean(String url, RenovaceHttpProxy<R> callBack) {
        return getBean(url, RequestParams.emptyParams, callBack);
//        return toSubscribe(apiManager.get(null, url), RenovaceFunc.ParseType.Bean, new RenovaceSubscriber<R>(callBack));
    }

    /**
     * get请求，带参数, 自定义数据结构
     *
     * @see #getBean(String, RenovaceHttpProxy)
     */
    public <R> Subscriber getBean(String url, RequestParams maps, RenovaceHttpProxy<R> callBack) {
        return toSubscribe(apiManager.get(maps.getHeader(), url, maps.getParams()), RenovaceFunc.ParseType.Bean, new RenovaceSubscriber<R>(callBack));
    }

    /**
     * get请求，完全自定义数据结构
     * {
     * "xxx":"xxx",
     * "xxx":"xxx"
     * }<br>
     * 返回数据为整个bean，数据结构为你自己自定义的数据，但是需要继承renovacebean<br>
     */
    public <R> Subscriber getDirect(String url, IRenovaceCallBack<R> callBack) {
        return getDirect(url, RequestParams.emptyParams, callBack);
//        return toSubscribe(apiManager.get(null, url), RenovaceFunc.ParseType.Direct, new RenovaceSubscriber<R>(new RenovaceHttpProxy<R>(callBack) {
//        }, Utils.findNeedType(callBack.getClass())));
    }

    /**
     * get请求，带参数, 完全自定义数据结构
     * {
     * "xxx":"xxx",
     * "xxx":"xxx"
     * }<br>
     * 返回数据为整个bean，数据结构为你自己自定义的数据，但是需要继承renovacebean<br>
     */
    public <R> Subscriber getDirect(String url, RequestParams maps, IRenovaceCallBack<R> callBack) {
        return toSubscribe(apiManager.get(maps.getHeader(), url, maps.getParams()), RenovaceFunc.ParseType.Direct, new RenovaceSubscriber<R>(new RenovaceHttpProxy<R>(callBack) {
        }, Utils.findNeedType(callBack.getClass())));
    }

    //POST请求=====================================================================

    /**
     * post请求，适用数据结构：
     * {
     * "code":0,
     * "err":"",
     * "result":{
     * "name":"renovace"
     * }
     * }<br>
     * 返回数据为result， code以及err已经收集在回调的err里面<br>
     * 如果你的数据结构里面的关键字不是code,err,result,当然你也可以继承renovacebean后，重写code,err,result
     */
    public <R> Subscriber postResult(String url, RenovaceHttpProxy<R> callBack) {
        return postResult(url, RequestParams.emptyParams, callBack);
//        return toSubscribe(apiManager.post(url), RenovaceFunc.ParseType.Result, new RenovaceSubscriber<R>(callBack));
    }

    /**
     * post请求，带参数
     *
     * @see #postResult(String, RenovaceHttpProxy)
     */
    public <R> Subscriber postResult(String url, RequestParams params, RenovaceHttpProxy<R> callBack) {
        return toSubscribe(apiManager.post(params.getHeader(), url, params.getParams()), RenovaceFunc.ParseType.Result, new RenovaceSubscriber<R>(callBack));
    }

    /**
     * post请求，自定义数据结构
     * {
     * "code":0,
     * "err":""
     * "name":"renovace"
     * }<br>
     * 返回数据为整个bean，如果code是异常code, code以及err已经收集在回调的err里面<br>
     * 如果你的数据结构里面的关键字不是code,err,result,当然你也可以继承renovacebean后，重写code,err,result
     */
    public <R> Subscriber postBean(String url, RenovaceHttpProxy<R> callBack) {
        return postBean(url, RequestParams.emptyParams, callBack);
//        return toSubscribe(apiManager.post(url), RenovaceFunc.ParseType.Bean, new RenovaceSubscriber<R>(callBack));
    }

    /**
     * post请求，带参数, 自定义数据结构
     *
     * @see #postBean(String, RenovaceHttpProxy)
     */
    public <R> Subscriber postBean(String url, RequestParams params, RenovaceHttpProxy<R> callBack) {
        return toSubscribe(apiManager.post(params.getHeader(), url, params.getParams()), RenovaceFunc.ParseType.Bean, new RenovaceSubscriber<R>(callBack));
    }

    /**
     * post请求，完全自定义数据结构
     * {
     * "xxx":"xxx",
     * "xxx":"xxx"
     * }<br>
     * 返回数据为整个bean，数据结构为你自己自定义的数据，但是需要继承renovacebean<br>
     */
    public <R> Subscriber postDirect(String url, IRenovaceCallBack<R> callBack) {
        return postDirect(url, RequestParams.emptyParams, callBack);
//        return toSubscribe(apiManager.post(url), RenovaceFunc.ParseType.Direct, new RenovaceSubscriber<R>(new RenovaceHttpProxy<R>(callBack) {
//        }, Utils.findNeedType(callBack.getClass())));
    }

    /**
     * post请求，带参数, 自定义数据结构
     *
     * @see #postDirect(String, IRenovaceCallBack)
     */
    public <R> Subscriber postDirect(String url, RequestParams params, IRenovaceCallBack<R> callBack) {
        return toSubscribe(apiManager.post(params.getHeader(), url, params.getParams()), RenovaceFunc.ParseType.Direct, new RenovaceSubscriber<R>(new RenovaceHttpProxy<R>(callBack) {
        }, Utils.findNeedType(callBack.getClass())));
    }

    //DELETE请求=====================================================================

    /**
     * delete请求,带参数
     */
    public <R> Subscriber delete(String url, RequestParams params, IRenovaceCallBack<R> callBack) {
        return toSubscribe(apiManager.delete(params.getHeader(), url, params.getParams()), RenovaceFunc.ParseType.Direct, new RenovaceSubscriber<R>(new RenovaceHttpProxy<R>(callBack) {
        }, Utils.findNeedType(callBack.getClass())));
    }

    //PUT请求=====================================================================

    /**
     * put请求,带参数，并已经完成解析
     */
    public <R> Subscriber put(String url, RequestParams params, IRenovaceCallBack<R> callBack) {
        return toSubscribe(apiManager.put(params.getHeader(), url, params.getParams()), RenovaceFunc.ParseType.Direct, new RenovaceSubscriber<R>(new RenovaceHttpProxy<R>(callBack) {
        }, Utils.findNeedType(callBack.getClass())));
    }

    //上传文件=====================================================================

    /**
     * upload请求，上传单个文件
     */
    public <R> Subscriber upload(Map<String, String> header, String url, RequestBody requestBody, IRenovaceCallBack<R> callBack) {
        return toSubscribe(apiManager.uploadFile(header, url, requestBody), RenovaceFunc.ParseType.Direct, new RenovaceSubscriber<R>(new RenovaceHttpProxy<R>(callBack) {
        }, Utils.findNeedType(callBack.getClass())));
    }

    /**
     * upload请求，上传多个文件
     */
    public <R> Subscriber upload(Map<String, String> header, String url, String description, Map<String, RequestBody> maps, IRenovaceCallBack<R> callBack) {
        return toSubscribe(apiManager.uploadFiles(header, url, description, maps), RenovaceFunc.ParseType.Direct, new RenovaceSubscriber<R>(new RenovaceHttpProxy<R>(callBack) {
        }, Utils.findNeedType(callBack.getClass())));
    }

    /**
     * 下载文件
     *
     * @param url
     * @return
     */
    public Subscriber download(Context context, Map<String, String> header, String url, String path, String fileName, DownLoadCallBack callBack) {
        return toSubscribe(apiManager.downloadFile(header, url), new DownSubscriber(context, path, fileName, callBack));
    }

    /**
     * 由于结合Rxjava 所以取消请求只能通过Subscriber取消订阅，达到取消的目的
     *
     * @param subscriber
     */
    public void cancel(Subscription subscriber) {
        Utils.checkNotNull(subscriber, "subscription is null").unsubscribe();
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
}
