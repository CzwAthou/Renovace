package com.athou.renovace.demo.bean;

import com.athou.renovace.bean.RenovaceBean;

/**
 * Created by athou on 2016/10/28.
 */

public class BaiduApiBean<T> extends RenovaceBean<T>{
    int errNum;
    String errMsg;
    T retData;

    @Override
    public int getCode() {
        return errNum;
    }

    @Override
    public void setCode(int code) {
        errNum = code;
    }

    @Override
    public String getError() {
        return errMsg;
    }

    @Override
    public void setError(String error) {
        errMsg = error;
    }

    @Override
    public T getResult() {
        return retData;
    }

    @Override
    public void setResult(T result) {
        this.retData = result;
    }

    @Override
    public String toString() {
        return "BaiduApiBean{" +
                "errNum=" + errNum +
                ", errMsg='" + errMsg + '\'' +
                ", retData=" + retData +
                '}';
    }
}
