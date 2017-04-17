package com.athou.renovace.demo;

import com.athou.renovace.IRenovaceCallBack;
import com.athou.renovace.Renovace;
import com.athou.renovace.RenovaceHttpProxy;
import com.athou.renovace.RequestParams;
import com.athou.renovace.bean.RenovaceBean;
import com.athou.renovace.demo.bean.PhoneIpApiBean;
import com.athou.renovace.demo.bean.TaobaoApiBean;

/**
 * Created by athou on 2017/4/17.
 */

public class HttpManager {

    public static <R> void getResult(String url, RequestParams params, IRenovaceCallBack<R> callback) {
        Renovace.getInstance().getResult(url, params, new RenovaceHttpProxy<RenovaceBean<R>, R>(callback) {
        });
    }

    public static <R> void postResult(String url, RequestParams params, IRenovaceCallBack<R> callback) {
        Renovace.getInstance().postResult(url, params, new RenovaceHttpProxy<RenovaceBean<R>, R>(callback) {
        });
    }

    public static <R> void getPhoneIpResult(String url, RequestParams params, IRenovaceCallBack<R> callback) {
        Renovace.getInstance().getResult(url, params, new RenovaceHttpProxy<PhoneIpApiBean<R>, R>(callback) {
        });
    }

    public static <R> void postPhoneIpResult(String url, RequestParams params, IRenovaceCallBack<R> callback) {
        Renovace.getInstance().postResult(url, params, new RenovaceHttpProxy<PhoneIpApiBean<R>, R>(callback) {
        });
    }

    public static <R> void postTaobaoResult(String url, RequestParams params, IRenovaceCallBack<R> callback) {
        Renovace.getInstance().postResult(url, params, new RenovaceHttpProxy<TaobaoApiBean<R>, R>(callback) {
        });
    }
}
