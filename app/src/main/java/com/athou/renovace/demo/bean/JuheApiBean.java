package com.athou.renovace.demo.bean;

import com.athou.renovace.bean.RenovaceBean;

/**
 * Created by athou on 2016/10/28.
 */

public class JuheApiBean<T> extends RenovaceBean<T>{
    int error_code;
    String reason;

    @Override
    public int getCode() {
        return error_code;
    }

    @Override
    public void setCode(int code) {
        error_code = code;
    }

    @Override
    public String getError() {
        return reason;
    }

    @Override
    public void setError(String error) {
        reason = error;
    }

    @Override
    public String toString() {
        return "JuheApiBean{" +
                "error_code=" + error_code +
                ", reason='" + reason + '\'' +
                '}';
    }
}
