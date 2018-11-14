package com.pince.renovace2;

import com.pince.renovace2.Util.TypeUtil;

import java.lang.reflect.Type;

/**
 * @author cc
 * @date 2017/11/3
 */

public abstract class ResultCallback<T> {

    public void onStart() {
    }

    public abstract void onSuccess(T result);

    public void onError(int code, Throwable err) {
    }

    public Type getType(StructType structType) {
        return TypeUtil.findNeedType(getClass());
    }
}
