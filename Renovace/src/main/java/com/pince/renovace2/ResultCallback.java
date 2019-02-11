package com.pince.renovace2;

import android.arch.lifecycle.LifecycleOwner;

import com.pince.renovace2.Util.Utils;

import java.lang.ref.WeakReference;
import java.lang.reflect.Type;

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
        return Utils.findNeedType(getClass());
    }

    public LifecycleOwner getLifecycleOwner() {
        if (lifecycleOwner != null) {
            return lifecycleOwner.get();
        }
        return null;
    }
}
