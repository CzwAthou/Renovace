package com.athou.renovace;

/**
 * Created by athou on 2016/12/16.
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
