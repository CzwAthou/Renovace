package com.pince.renovace2.request;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.util.Log;

import com.pince.renovace2.RenovaceBean;
import com.pince.renovace2.RenovaceCode;
import com.pince.renovace2.RenovaceException;
import com.pince.renovace2.RenovaceFunc;
import com.pince.renovace2.ResultCallback;
import com.pince.renovace2.StructType;
import com.pince.renovace2.Util.Utils;
import com.pince.renovace2.cache.CacheStrategy;
import com.pince.renovace2.config.Config;
import com.pince.renovace2.header.HeaderKey;
import com.pince.renovace2.request.adapter.DefaultRequestAdapter;
import com.pince.renovace2.request.adapter.RequestAdapter;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.CheckReturnValue;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.subjects.PublishSubject;
import okhttp3.ResponseBody;

/**
 * @author athou
 * @date 2017/11/10
 */
public abstract class RequestBuilder<B extends RequestBuilder> implements LifecycleObserver {
    private String mUrl;
    private Map<String, Object> mHeaders;
    private Map<String, Object> mParams;
    private Method mMethod;
    public StructType mStructType = StructType.Result;
    public int mRetryCount = 0;
    private Class<? extends Config> mConfigCls;

    private LifecycleOwner lifecycleOwner = null;
    private PublishSubject<Lifecycle.Event> lifecycleSubject = PublishSubject.create();

    public RequestBuilder(Method method, Class<? extends Config> configCls) {
        mMethod = method;
        mConfigCls = configCls;
        mHeaders = new LinkedHashMap<>();
        mParams = new LinkedHashMap<>();
    }

    public B url(String url) {
        this.mUrl = url;
        return (B) this;
    }

    public B params(Map<String, Object> params) {
        if (mParams == null) {
            mParams = new LinkedHashMap<>();
        }
        mParams.putAll(params);
        return (B) this;
    }

    public B addParam(String key, Object val) {
        if (mParams == null) {
            mParams = new LinkedHashMap<>();
        }
        mParams.put(key, val);
        return (B) this;
    }

    public B headers(Map<String, Object> headers) {
        if (mHeaders == null) {
            mHeaders = new LinkedHashMap<>();
        }
        this.mHeaders = headers;
        return (B) this;
    }

    public B addHeader(String key, Object val) {
        if (mHeaders == null) {
            mHeaders = new LinkedHashMap<>();
        }
        mHeaders.put(key, val);
        return (B) this;
    }

    public B structType(StructType structType) {
        this.mStructType = structType;
        return (B) this;
    }

    public B cache(@NonNull CacheStrategy cacheStrategy) {
        return addHeader(HeaderKey.Cache, cacheStrategy.getValue());
    }

    public B readTimeOut(int readTimeOutMils) {
        return addHeader(HeaderKey.ReadTimeOut, readTimeOutMils);
    }

    public B writeTimeOut(int writeTimeOutMils) {
        return addHeader(HeaderKey.WriteTimeOut, writeTimeOutMils);
    }

    public B connectTimeOut(int connectTimeOutMils) {
        return addHeader(HeaderKey.ConnectTimeOut, connectTimeOutMils);
    }

    public B retry(int retryCount) {
        this.mRetryCount = retryCount;
        return (B) this;
    }

    public B bindLifecycle(LifecycleOwner lifecycleOwner) {
        this.lifecycleOwner = lifecycleOwner;
        if (lifecycleOwner != null) {
            lifecycleOwner.getLifecycle().addObserver(this);
        }
        return (B) this;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void onDestroy() {
        Log.e("Renovace", "OnLifecycleEvent  ON_DESTROY");
        if (lifecycleSubject != null) {
            lifecycleSubject.onNext(Lifecycle.Event.ON_DESTROY);
        }
        if (lifecycleOwner != null) {
            lifecycleOwner.getLifecycle().removeObserver(this);
        }
        dispose();
    }

    @Deprecated
    private void cancel() {
        dispose();
    }

    @Deprecated
    private void dispose() {

    }

    public Map<String, Object> getHeaders() {
        return mHeaders;
    }

    public Map<String, Object> getParams() {
        return mParams;
    }

    public String getUrl() {
        return mUrl;
    }

    public Method getMethod() {
        return mMethod;
    }

    public Class<? extends Config> getConfigCls() {
        if (mConfigCls == null) {
            throw new RuntimeException("config not be null");
        }
        return mConfigCls;
    }

    private class LifecycleTransformer<T> implements ObservableTransformer<T, T> {
        final Observable<?> observable;

        LifecycleTransformer(@NonNull Observable<?> observable) {
            this.observable = observable;
        }

        @Override
        public ObservableSource<T> apply(Observable<T> upstream) {
            return upstream.takeUntil(observable);
        }
    }

    @CheckReturnValue
    private <T> LifecycleTransformer<T> bindUntilEvent(@NonNull final Lifecycle.Event lifeCycleEvent) {
        Observable<Lifecycle.Event> compareLifecycleObservable = lifecycleSubject.filter(new Predicate<Lifecycle.Event>() {
            @Override
            public boolean test(Lifecycle.Event event) throws Exception {
                Log.e("Renovace", "bindUntilEvent filter:" + event);
                return lifeCycleEvent.equals(event);
            }
        });
        return new LifecycleTransformer<>(compareLifecycleObservable);
    }

    public <T> Observable<T> request(@NonNull RequestAdapter adapter) {
        Observable observable = adapter.adapt(this);
        if (mRetryCount > 0) {
            observable = observable.retry(mRetryCount);
        }
        return (Observable<T>) observable.compose(bindUntilEvent(Lifecycle.Event.ON_DESTROY));
    }

    public Observable<ResponseBody> requestBody() {
        return request(new DefaultRequestAdapter());
    }

    public <R> Observable<R> request(Class<R> rspClass) {
        return requestBody().map(new RenovaceFunc<R>(rspClass, mStructType));
    }

    public <R> Observable<R> request(Type rspType) {
        return requestBody().map(new RenovaceFunc<R>(rspType, mStructType));
    }

    public <R> Observable<R> request(Class<? extends RenovaceBean> rawCls, Class<R> resultCls) {
        return requestBody().map(new RenovaceFunc<R>(Utils.newParameterizedTypeWithOwner(rawCls, resultCls), StructType.Result));
    }

    public <R> Disposable request(final ResultCallback<R> callback) {
        Type type = null;
        if (callback != null) {
            type = callback.getType(mStructType);
        } else {
            type = ResponseBody.class;
        }
        return requestBody().map(new RenovaceFunc<R>(type, mStructType)).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Consumer<Object>() {
                            @Override
                            public void accept(Object result) throws Exception {
                                if (callback != null) {
                                    if (result != null) {
                                        if (result instanceof RenovaceBean) {
                                            RenovaceBean temp = (RenovaceBean) result;
                                            if (temp.isSuccess()) {
                                                callback.onSuccess((R) result);
                                            } else {
                                                callback.onError(temp.getCode(), new RenovaceException(temp.getCode(), temp.getMessage()));
                                            }
                                        } else {
                                            callback.onSuccess((R) result);
                                        }
                                    } else {
                                        callback.onError(RenovaceCode.CODE_PARSE_ERR, new RenovaceException(RenovaceCode.CODE_PARSE_ERR, "parse error"));
                                    }
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                if (callback != null) {
                                    callback.onError(RenovaceCode.CODE_ERR_IO, new RenovaceException(RenovaceCode.CODE_ERR_IO, throwable.getMessage()));
                                }
                            }
                        });
    }

    public enum Method {
        /**
         * get request
         */
        Get,
        /**
         * post request
         */
        Post,
        /**
         * put request
         */
        Put,
        /**
         * delete request
         */
        Delete,
        /**
         * body request
         */
        Body
    }
}