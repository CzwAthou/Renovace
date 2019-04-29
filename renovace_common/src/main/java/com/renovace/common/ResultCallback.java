package com.renovace.common;

import android.arch.lifecycle.LifecycleOwner;

import java.lang.ref.WeakReference;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;

/**
 * @author cc
 * @date 2017/11/3
 */

public abstract class ResultCallback<T> {

    private WeakReference<LifecycleOwner> lifecycleOwner;

    public ResultCallback() {
    }

    public ResultCallback(LifecycleOwner lifecycleOwner) {
        this.lifecycleOwner = new WeakReference<LifecycleOwner>(lifecycleOwner);
    }

    public void onStart() {
    }

    public abstract void onSuccess(T result);

    public void onError(int code, Throwable err) {
    }

    public Type getType(StructType structType) {
        Type type = Utils.findNeedType(getClass());
        if (type == null) {
            type = ResponseBody.class;
        }
        return type;
    }

    public LifecycleOwner getLifecycleOwner() {
        if (lifecycleOwner != null) {
            return lifecycleOwner.get();
        }
        return null;
    }
}
