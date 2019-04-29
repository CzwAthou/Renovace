package com.renovace.common;

/**
 * errcode拦截接口
 * Created by czwathou on 2017/8/25.
 */
public interface RespCodeInteceptor {

    boolean inteceptorRespCode(int code, String extra);
}
