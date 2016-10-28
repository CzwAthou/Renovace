package com.athou.renovace.demo.bean;

import com.athou.renovace.bean.RenovaceBean;

/**
 * Created by athou on 2016/10/28.
 */

public class GankIOBean<T> extends RenovaceBean<T>{
    int count;
    T results;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public T getResult() {
        return results;
    }

    @Override
    public void setResult(T result) {
        this.results = result;
    }

    @Override
    public String toString() {
        return "GankIOBean{" +
                "count=" + count +
                ", results=" + results +
                '}';
    }
}
