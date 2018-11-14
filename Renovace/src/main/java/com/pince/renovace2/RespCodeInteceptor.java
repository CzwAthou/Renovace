package com.pince.renovace2;

/**
 * errcode拦截接口
 * Created by sy-caizhaowei on 2017/8/25.
 */
public interface RespCodeInteceptor {

    boolean inteceptorRespCode(int code, String extra);
}
