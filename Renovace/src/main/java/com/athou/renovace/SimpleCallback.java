package com.athou.renovace;

import com.athou.renovace.util.TypeUtil;

import java.lang.reflect.Type;

import rx.Subscription;

/**
 * a simple IRenovaceCallBack impl class, the only method onSuccess will be called!<br>
 * Created by athou on 2017/4/19.
 */
public abstract class SimpleCallback<R> implements IRenovaceCallBack<R> {

    private Subscription subscription;

    @Override
    public Type getType() {
        return TypeUtil.findNeedType(getClass());
    }

    @Override
    public void setRequestUri(String url) {

    }

    @Override
    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onProgress(long curDownSize, long totalSize) {

    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onCompleted() {
        onFinish(null);
    }

    @Override
    public void onError(Throwable e) {
        onFinish(e);
    }

    public void onFinish(Throwable e) {

    }
}