package com.athou.renovace.demo.bean;

import com.athou.renovace.bean.RenovaceBean;

/**
 * Created by athou on 2016/10/28.
 */

public class TaobaoApiBean<T> extends RenovaceBean<T> {
    private T data;

    @Override
    public T getResult() {
        return data;
    }

    @Override
    public void setResult(T result) {
        data = result;
    }

    @Override
    public String toString() {
        return "TaobaoApiBean{" +
                "data=" + data +
                '}';
    }
}
