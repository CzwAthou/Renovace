/*
 * Copyright (c) 2016  athou（cai353974361@163.com）.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.athou.renovace.demo;

import com.athou.frame.net.exception.CustomException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * <strong>网络异常信息类 </strong><br>
 *
 * @author 菜菜
 * @category 该类已经将常见异常对应的信息翻译出来<br>
 * 包含了各种异常信息已经对应的code
 * @see AbHttpConstant
 * @since 2015-3-6
 */
public class NetErrorBean {
    private int err_code = AbHttpConstant.ERR_CODE_NONE;
    private String err_messge = "";
    private Throwable e;

    /**
     * 空构造方法，默认没有错误信息
     */
    public NetErrorBean() {
        super();
    }

    /**
     * 包含错误信息Throwable的构造方法
     *
     * @param e
     */
    public NetErrorBean(Throwable e) {
        super();
        this.e = e;
        getErrDetailByThrowable(e);
    }

    /**
     * 包含错误详细的构造方法
     *
     * @param errMsg
     */
    public NetErrorBean(String errMsg) {
        super();
        this.e = new Exception(errMsg);
        err_messge = errMsg;
    }

    /**
     * 清除错误信息
     */
    public void clear() {
        err_code = AbHttpConstant.ERR_CODE_NONE;
        err_messge = "";
        e = null;
    }

    /**
     * 获取错误码，默认{@link AbHttpConstant#ERR_CODE_NONE}
     *
     * @return
     */
    public int getErrCode() {
        return err_code;
    }

    /**
     * 获取错误详细
     */
    public String getErrMessge() {
        return err_messge;
    }

    /**
     * 获取Throwable
     */
    public Throwable getThrowable() {
        return e;
    }

    /**
     * 设置Throwable
     */
    public NetErrorBean setThrowable(Throwable e) {
        this.e = e;
        getErrDetailByThrowable(e);
        return this;
    }

    /**
     * 设置Throwable
     */
    public NetErrorBean setThrowable(int code, String errMsg) {
        setThrowable(new CustomException(code, errMsg));
        return this;
    }

    /**
     * 根据Throwable 获取相对于的err_code和err_messge
     *
     * @param e
     */
    private void getErrDetailByThrowable(Throwable e) {
        if (e == null) {
            err_code = AbHttpConstant.ERR_CODE_UNKOWN;
            err_messge = "未知错误";
            return;
        }
        if (e instanceof CustomException) {
            int code = ((CustomException) e).getCode();
            err_code = code == AbHttpConstant.ERR_CODE_NONE ? AbHttpConstant.ERR_CODE_UNKOWN : code;
            err_messge = e.getMessage();
        } else if (e instanceof FileNotFoundException) { //IOException
            err_code = AbHttpConstant.ERR_CODE_FILENOTFOUNDEXCEPTION;
            err_messge = AbHttpConstant.ERR_MESSAGE_FILENOTFOUNDEXCEPTION;
        } else if (e instanceof MalformedURLException) { //IOException
            err_code = AbHttpConstant.ERR_CODE_MALFORMEDURLEXCEPTION;
            err_messge = AbHttpConstant.ERR_MESSAGE_MALFORMEDURLEXCEPTION;
        } else if (e instanceof UnknownHostException) { //IOException
            err_code = AbHttpConstant.ERR_CODE_UNKNOWNHOSTEXCEPTION;
            err_messge = AbHttpConstant.ERR_MESSAGE_UNKNOWNHOSTEXCEPTION;
        } else if (e instanceof HttpHostConnectException) { //ConnectException
            err_code = AbHttpConstant.ERR_CODE_HTTPHOSTCONNECTEXCEPTION;
            err_messge = AbHttpConstant.ERR_MESSAGE_HTTPHOSTCONNECTEXCEPTION;
        } else if (e instanceof ConnectException) { //SocketException
            err_code = AbHttpConstant.ERR_CODE_CONNECTEXCEPTION;
            err_messge = AbHttpConstant.ERR_MESSAGE_CONNECTEXCEPTION;
        } else if (e instanceof SocketException) { //IOException
            err_code = AbHttpConstant.ERR_CODE_SOCKETEXCEPTION;
            err_messge = AbHttpConstant.ERR_MESSAGE_SOCKETEXCEPTION;
        } else if (e instanceof ConnectTimeoutException) { //InterruptedIOException
            err_code = AbHttpConstant.ERR_CODE_CONNECTTIMEOUTEXCEPTION;
            err_messge = AbHttpConstant.ERR_MESSAGE_CONNECTTIMEOUTEXCEPTION;
        } else if (e instanceof SocketTimeoutException) { //InterruptedIOException
            err_code = AbHttpConstant.ERR_CODE_SOCKETTIMEOUTEXCEPTION;
            err_messge = AbHttpConstant.ERR_MESSAGE_SOCKETTIMEOUTEXCEPTION;
        } else if (e instanceof HttpResponseException) { //ClientProtocolException
            err_code = AbHttpConstant.ERR_CODE_HTTPRESPONSEEXCEPTION;
            err_messge = AbHttpConstant.ERR_MESSAGE_HTTPRESPONSEEXCEPTION;
        } else if (e instanceof ClientProtocolException) { //IOException
            err_code = AbHttpConstant.ERR_CODE_CLIENTPROTOCOLEXCEPTION;
            err_messge = AbHttpConstant.ERR_MESSAGE_CLIENTPROTOCOLEXCEPTION;
        } else if (e instanceof IOException) { //
            err_code = AbHttpConstant.ERR_CODE_IOEXCEPTION;
            err_messge = AbHttpConstant.ERR_MESSAGE_IOEXCEPTION;
        } else {
            err_code = AbHttpConstant.ERR_CODE_UNKOWN;
            err_messge = e.getMessage();
        }
    }

    /**
     * 判断网络请求是否成功，若err_code =AbHttpConstant.ERR_CODE_NONE，表示数据请求成功
     *
     * @return
     */
    public boolean isSucceed() {
        if (getErrCode() == AbHttpConstant.ERR_CODE_NONE) {
            return true;
        }
        return false;
    }
}
