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

import android.content.Context;

import com.athou.renovace.bean.RenovaceBean;
import com.athou.renovace.download.DownLoadCallBack;
import com.athou.renovace.download.DownSubscriber;
import com.athou.renovace.util.Utils;
import com.google.gson.Gson;

import java.lang.reflect.Type;
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
    private Gson mGson;

    /**
     * init the renovace, you need applicationcontext and baseurl,
     * it will create default both retrofit and OkHttpClient
     *
     * @param context applicationcontext
     * @param baseUrl your baseurl
     */
    public Renovace init(Context context, String baseUrl) {
        return init(new RenovaceWrapper(context, baseUrl));
    }

    /**
     * init the renovace, you need baseurl and IHttpClient,
     * it will create a default retrofit, but the OkHttpClient you will implement IHttpClient
     *
     * @param baseUrl
     * @param httpCLient
     */
    public Renovace init(String baseUrl, IRenovace.IHttpClient httpCLient) {
        return init(new RenovaceWrapper(baseUrl, httpCLient));
    }

    /**
     * init the renovace, you need implement {@link com.athou.renovace.IRenovace}
     *
     * @param iRenovace
     */
    public Renovace init(IRenovace iRenovace) {
        this.iRenovace = iRenovace;
        this.apiManager = Utils.checkNotNull(iRenovace, "iRenovace == null").getRetrofit().create(BaseApiService.class);
        return this;
    }

    /**
     * set inner gson to parse data
     *
     * @param gson
     */
    public void setGson(Gson gson) {
        mGson = gson;
    }

    /**
     * get the gson, if null, will create a new gson
     *
     * @return
     */
    public Gson getGson() {
        if (mGson == null) {
            mGson = new Gson();
        }
        return mGson;
    }

    /**
     * user-defined interface
     *
     * @param service the apiservice
     */
    public <R> R create(Class<R> service) {
        return iRenovace.getRetrofit().create(service);
    }

    /**
     * after user-defined interface, you can call this method to start request.
     *
     * @param callBack defind callback you can implement
     */
    public <R> Subscriber call(Observable<R> observable, IRenovaceCallBack<R> callBack) {
        return call(observable, new RenovaceCallSubscriber(callBack));
    }

    /**
     * after user-defined interface, you can call this method to start request.<br>
     * And unified the format for request or other oprate, and the oprate will occur in thread
     *
     * @param subscriber the Subscriber you can extends
     */
    public <R> Subscriber call(Observable<R> observable, Subscriber<R> subscriber) {
        observable.compose(schedulersTransformer)
                .subscribe(subscriber);
        return subscriber;
    }

    /**
     * @param observable this observable's paramstype may be not same as subscriber's paramstype, so this don't add paramstype
     * @param subscriber
     */
    private <R> Subscriber toSubscribe(Observable observable, RenovaceFunc.StructType parseType, RenovaceSubscriber<R> subscriber) {
        observable.map(new RenovaceFunc<R>(subscriber, parseType))
                .compose(schedulersTransformer)
                .subscribe(subscriber);
        return subscriber;
    }

    //GET=====================================================================

    /**
     * get,the match struct is:
     * {
     * "code":0,
     * "err":"",
     * "result":{
     * "name":"renovace"
     * }
     * }<br>
     * return object is the result,code and err has be gathered in callback's err<br>
     * if your data struct's key isnot code or err or result, ofcourse you can extends {@link com.athou.renovace.bean.RenovaceBean}<br>
     * then you can override code,err,result
     */
    public <R> Subscriber getResult(String url, RenovaceHttpProxy<? extends RenovaceBean<R>, R> callBack) {
        return getResult(url, RequestParams.emptyParams, callBack);
    }

    /**
     * get request with params
     *
     * @see #getResult(String, RenovaceHttpProxy)
     */
    public <R> Subscriber getResult(String url, RequestParams maps, RenovaceHttpProxy<? extends RenovaceBean<R>, R> callBack) {
        if (callBack != null) {
            callBack.setRequestUri(url);
        }
        return toSubscribe(apiManager.get(maps.getHeader(), url, maps.getParams()), RenovaceFunc.StructType.Result, new RenovaceSubscriber<RenovaceBean<R>>(callBack));
    }

    /**
     * get, the match struct is (this support user-defined struct):
     * {
     * "code":0,
     * "name":"renovace"
     * }<br>
     * the return object is the whole bean, if code is errcode, the code and err msg gathered in callback's err<br>
     * if your data struct's key isnot code or err or result, ofcourse you can extends {@link com.athou.renovace.bean.RenovaceBean}<br>
     * then you can override code,err,result
     */
    public <R> Subscriber getBean(String url, IRenovaceCallBack<R> callBack) {
        return getBean(url, RequestParams.emptyParams, callBack);
    }

    /**
     * get request with params
     *
     * @see #getBean(String, IRenovaceCallBack)
     */
    public <R> Subscriber getBean(String url, RequestParams maps, IRenovaceCallBack<R> callBack) {
        if (callBack != null) {
            callBack.setRequestUri(url);
        }
        return toSubscribe(apiManager.get(maps.getHeader(), url, maps.getParams()), RenovaceFunc.StructType.Bean, new RenovaceSubscriber<R>(callBack));
    }

    /**
     * get,the match struct is (this completely user-defined struct):
     * {
     * "xxx":"xxx",
     * "xxx":"xxx"
     * }<br>
     * the return object is whole bean, the struct is your bean, but you must extends {@link com.athou.renovace.bean.RenovaceBean}<br>
     */
    public <R> Subscriber getDirect(String url, IRenovaceCallBack<R> callBack) {
        return getDirect(url, RequestParams.emptyParams, callBack);
    }

    /**
     * get request with params
     *
     * @see #getDirect(String, IRenovaceCallBack)
     */
    public <R> Subscriber getDirect(String url, RequestParams maps, IRenovaceCallBack<R> callBack) {
        if (callBack != null) {
            callBack.setRequestUri(url);
        }
        return toSubscribe(apiManager.get(maps.getHeader(), url, maps.getParams()), RenovaceFunc.StructType.Direct, new RenovaceSubscriber<R>(callBack));
    }

    //POST=====================================================================

    /**
     * post,the match struct is:
     * {
     * "code":0,
     * "err":"",
     * "result":{
     * "name":"renovace"
     * }
     * }<br>
     * return object is the result,code and err has be gathered in callback's err<br>
     * if your data struct's key isnot code or err or result, ofcourse you can extends {@link com.athou.renovace.bean.RenovaceBean}<br>
     * then you can override code,err,result
     */
    public <R> Subscriber postResult(String url, RenovaceHttpProxy<? extends RenovaceBean<R>, R> callBack) {
        return postResult(url, RequestParams.emptyParams, callBack);
    }

    /**
     * post request with params
     *
     * @see #postResult(String, RenovaceHttpProxy)
     */
    public <R> Subscriber postResult(String url, RequestParams params, RenovaceHttpProxy<? extends RenovaceBean<R>, R> callBack) {
        if (callBack != null) {
            callBack.setRequestUri(url);
        }
        return toSubscribe(apiManager.post(params.getHeader(), url, params.getParams()), RenovaceFunc.StructType.Result, new RenovaceSubscriber<RenovaceBean<R>>(callBack));
    }

    /**
     * post, the match struct is (this support user-defined struct):
     * {
     * "code":0,
     * "err":""
     * "name":"renovace"
     * }<br>
     * the return object is the whole bean, if code is errcode, the code and err msg gathered in callback's err<br>
     * if your data struct's key isnot code or err or result, ofcourse you can extends {@link com.athou.renovace.bean.RenovaceBean}<br>
     * then you can override code,err,result
     */
    public <R> Subscriber postBean(String url, IRenovaceCallBack<R> callBack) {
        return postBean(url, RequestParams.emptyParams, callBack);
    }

    /**
     * post request with params
     *
     * @see #postBean(String, IRenovaceCallBack)
     */
    public <R> Subscriber postBean(String url, RequestParams params, IRenovaceCallBack<R> callBack) {
        if (callBack != null) {
            callBack.setRequestUri(url);
        }
        return toSubscribe(apiManager.post(params.getHeader(), url, params.getParams()), RenovaceFunc.StructType.Bean, new RenovaceSubscriber<R>(callBack));
    }

    /**
     * post, the match struct is (this completely user-defined struct):
     * {
     * "xxx":"xxx",
     * "xxx":"xxx"
     * }<br>
     * the return object is whole bean, the struct is your bean, but you must extends {@link com.athou.renovace.bean.RenovaceBean}<br>
     */
    public <R> Subscriber postDirect(String url, IRenovaceCallBack<R> callBack) {
        return postDirect(url, RequestParams.emptyParams, callBack);
    }

    /**
     * request with params
     *
     * @see #postDirect(String, IRenovaceCallBack)
     */
    public <R> Subscriber postDirect(String url, RequestParams params, IRenovaceCallBack<R> callBack) {
        if (callBack != null) {
            callBack.setRequestUri(url);
        }
        return toSubscribe(apiManager.post(params.getHeader(), url, params.getParams()), RenovaceFunc.StructType.Direct, new RenovaceSubscriber<R>(callBack));
    }

    //muti request for merge/zip/concat =====================================================================

    /**
     * create a observable with data parse. only this parse bean is whole bean and dot not jugde the code<br>
     * this method can apply for muti request merge or zip or concat...
     *
     * @param url
     * @param params
     * @param type
     * @return
     */
    public Observable getCall(String url, RequestParams params, Type type) {
        return apiManager.get(params.getHeader(), url, params.getParams())
                .map(new RenovaceFunc(new RenovaceFunc.FuncSubscription(type), RenovaceFunc.StructType.Direct));
    }

    /**
     * @see #getCall(String, RequestParams, Type)
     */
    public Observable postCall(String url, RequestParams params, Type type) {
        return apiManager.post(params.getHeader(), url, params.getParams())
                .map(new RenovaceFunc(new RenovaceFunc.FuncSubscription(type), RenovaceFunc.StructType.Direct));
    }

    //DELETE=====================================================================

    /**
     * delete
     */
    public <R> Subscriber delete(String url, RequestParams params, IRenovaceCallBack<R> callBack) {
        if (callBack != null) {
            callBack.setRequestUri(url);
        }
        return toSubscribe(apiManager.delete(params.getHeader(), url, params.getParams()), RenovaceFunc.StructType.Direct, new RenovaceSubscriber<R>(callBack));
    }

    //PUT=====================================================================

    /**
     * put
     */
    public <R> Subscriber put(String url, RequestParams params, IRenovaceCallBack<R> callBack) {
        if (callBack != null) {
            callBack.setRequestUri(url);
        }
        return toSubscribe(apiManager.put(params.getHeader(), url, params.getParams()), RenovaceFunc.StructType.Direct, new RenovaceSubscriber<R>(callBack));
    }

    //upload file=====================================================================

    /**
     * upload sigle file
     */
    public <R> Subscriber upload(Map<String, String> header, String url, RequestBody requestBody, IRenovaceCallBack<R> callBack) {
        if (callBack != null) {
            callBack.setRequestUri(url);
        }
        return toSubscribe(apiManager.uploadFile(header, url, requestBody), RenovaceFunc.StructType.Direct, new RenovaceSubscriber<R>(callBack));
    }

    /**
     * upload multi file
     */
    public <R> Subscriber upload(Map<String, String> header, String url, String description, Map<String, RequestBody> maps, IRenovaceCallBack<R> callBack) {
        if (callBack != null) {
            callBack.setRequestUri(url);
        }
        return toSubscribe(apiManager.uploadFiles(header, url, description, maps), RenovaceFunc.StructType.Direct, new RenovaceSubscriber<R>(callBack));
    }

    /**
     * download file
     *
     * @param url
     * @return
     */
    public Subscriber download(Map<String, String> header, String url, String saveFileDir, String fileName, DownLoadCallBack callBack) {
        return call(apiManager.downloadFile(header, url), new DownSubscriber(saveFileDir, fileName, callBack));
    }

    /**
     * you can cancel the request by Subscription
     *
     * @param subscriber
     */
    public void cancel(Subscription subscriber) {
        Utils.checkNotNull(subscriber, "subscription is null").unsubscribe();
    }

    /**
     * the uniform transform
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