package com.athou.renovace.demo;

import com.athou.renovace.IRenovaceCallBack;
import com.athou.renovace.RenovaceException;

/**
 * Created by athou on 2016/12/16.
 */

public abstract class HttpCallback<R> implements IRenovaceCallBack<R> {

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
    public void onProgress(long curDownSize, long totalSize) {

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
