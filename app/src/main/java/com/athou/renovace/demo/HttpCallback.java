package com.athou.renovace.demo;

import com.athou.renovace.RenovaceException;
import com.athou.renovace.SimpleCallback;

/**
 * Created by athou on 2016/12/16.
 */

public abstract class HttpCallback<R> extends SimpleCallback<R> {

    private NetErrorBean errorBean = null;
    private IDialogHandler mDialogHandler = null;

    public HttpCallback() {
        this.errorBean = new NetErrorBean();
    }

    public HttpCallback(IDialogHandler dialogHandler) {
        this.mDialogHandler = dialogHandler;
        this.errorBean = new NetErrorBean();
    }

    @Override
    public void onStart() {
        errorBean.clear();
        if (mDialogHandler != null) {
            mDialogHandler.showLoadDialog();
        }
    }

    @Override
    public final void onCompleted() {
        onFinish(errorBean);
    }

    @Override
    public final void onError(Throwable e) {
        if (e != null) {
            e.printStackTrace();

            if (e instanceof RenovaceException) {
                errorBean.setThrowable(((RenovaceException) e).getCode(), e.getMessage());
            } else {
                errorBean.setThrowable(e);
            }
        }
        onFinish(errorBean);
    }

    public void onFinish(NetErrorBean errorBean) {
        if (mDialogHandler != null) {
            mDialogHandler.hideLoadDialog();
        }
    }
}
