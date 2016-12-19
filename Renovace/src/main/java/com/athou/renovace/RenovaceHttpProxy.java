package com.athou.renovace;

/**
 * this class is used for getting outter parse bean's type.<br>
 * 1,if you use getResult, RenovaceHttpProxy's dynamic type is your bean that extends {@link com.athou.renovace.bean.RenovaceBean},
 * and the RenovaceBean<b><<T>T<T>></b>'s <b>T</b> will be return from the Callback that you can implements {@link IRenovaceCallBack}<br>
 * 2,if you use getBean,  RenovaceHttpProxy's dynamic type is your bean that extends {@link com.athou.renovace.bean.RenovaceBean},
 * and the RenovaceBean will be return from the Callback that you can implements {@link IRenovaceCallBack}<br>
 * 3,if you use getDirect, you should use {@link IRenovaceCallBack} to get you bean<br>
 * Created by athou on 2016/12/16.b
 */
public abstract class RenovaceHttpProxy<R> {
    private IRenovaceCallBack callBack;

    public RenovaceHttpProxy(IRenovaceCallBack callBack) {
        this.callBack = callBack;
    }

    /**
     * getResult net data start, you can show a dialog in this method
     */
    void onStart() {
        if (callBack != null) {
            callBack.onStart();
        }
    }

    /**
     * getResult net data and parse the data success, you can getResult the data from return the bean
     */
    <P> void onSuccess(P response) {
        if (callBack != null) {
            callBack.onSuccess(response);
        }
    }

    /**
     * getResult net data and parse the data complete, this method will call after {@link #onSuccess(Object)} }
     */
    void onCompleted() {
        if (callBack != null) {
            callBack.onCompleted();
        }
    }

    /**
     * an error happened when getting net data
     *
     * @param e the error
     */
    void onError(Throwable e) {
        if (callBack != null) {
            callBack.onError(e);
        }
    }
}
