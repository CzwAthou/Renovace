package com.athou.renovace;

import com.athou.renovace.bean.RenovaceBean;
import com.athou.renovace.util.TypeUtil;
import com.google.gson.internal.$Gson$Types;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import rx.Subscription;

/**
 * Response data callback, in this class, it's return data is RenovaceBean's result {@link RenovaceBean}.<br>
 * this class is used for getting outter parse bean's type and just for you call the method {@link Renovace#getResult(String, RenovaceHttpProxy)}
 * or {@link Renovace#postResult(String, RenovaceHttpProxy)}<br>
 * 1,if you use getResult, RenovaceHttpProxy's dynamic type is your bean that extends {@link com.athou.renovace.bean.RenovaceBean},
 * and the RenovaceBean<b>T</b>'s <b>T</b> will be return from the Callback that you can implements {@link IRenovaceCallBack}<br>
 * 2,if you use getBean or getDirect, you should use {@link IRenovaceCallBack} to get you bean<br>
 * Created by athou on 2016/12/16.b
 */
public abstract class RenovaceHttpProxy<T extends RenovaceBean<R>, R> implements IRenovaceCallBack<T> {
    private IRenovaceCallBack<R> callBack;

    public RenovaceHttpProxy(IRenovaceCallBack<R> callBack) {
        this.callBack = callBack;
    }

    @Override
    public Type getType() {
        Type typeArguments = null;
        if (callBack != null) {
            typeArguments = callBack.getType();
        }
        if (typeArguments == null) {
            typeArguments = ResponseBody.class;
        }
        Type rawType = TypeUtil.findNeedType(getClass());
        if (rawType instanceof ParameterizedType) {
            rawType = ((ParameterizedType) rawType).getRawType();
        }
        return $Gson$Types.newParameterizedTypeWithOwner(null, rawType, typeArguments);
    }

    @Override
    public void setRequestUri(String url) {
        if (callBack != null) {
            callBack.setRequestUri(url);
        }
    }

    /**
     * save the Subscription for IRenovaceCallBack
     *
     * @param subscription
     */
    @Override
    public void setSubscription(Subscription subscription) {
        if (callBack != null) {
            callBack.setSubscription(subscription);
        }
    }

    /**
     * getResult net data start, you can show a dialog in this method
     */
    @Override
    public void onStart() {
        if (callBack != null) {
            callBack.onStart();
        }
    }

    @Override
    public void onProgress(long curDownSize, long totalSize) {

    }

    /**
     * the request be canceled
     */
    @Override
    public void onCancel() {
        if (callBack != null) {
            callBack.onCancel();
        }
    }

    /**
     * getResult net data and parse the data success, you can getResult the data from return the bean
     */
    @Override
    public void onSuccess(T response) {
        if (callBack != null) {
            callBack.onSuccess(response.getResult());
        }
    }

    /**
     * getResult net data and parse the data complete, this method will call after {@link #onSuccess(RenovaceBean)} }
     */
    @Override
    public void onCompleted() {
        if (callBack != null) {
            callBack.onCompleted();
        }
    }

    /**
     * an error happened when getting net data
     *
     * @param e the error
     */
    @Override
    public void onError(Throwable e) {
        if (callBack != null) {
            callBack.onError(e);
        }
    }
}