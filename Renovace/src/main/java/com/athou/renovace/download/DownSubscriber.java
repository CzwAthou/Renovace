package com.athou.renovace.download;

import okhttp3.ResponseBody;
import rx.Subscriber;

/**
 * DownSubscriber
 * Created by Tamic on 2016-08-03.
 */
public class DownSubscriber extends Subscriber<ResponseBody> {
    private String saveFileDir;
    private String fileName;
    private DownLoadCallBack callBack;

    public DownSubscriber(String saveFileDir, String fileName, DownLoadCallBack callBack) {
        this.saveFileDir = saveFileDir;
        this.fileName = fileName;
        this.callBack = callBack;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (callBack != null) {
            callBack.onStart();
        }
    }

    @Override
    public void onCompleted() {
        if (callBack != null) {
            callBack.onCompleted();
        }
    }

    @Override
    public void onError(final Throwable e) {
        if (callBack != null) {
            callBack.onError(e);
        }
    }

    @Override
    public void onNext(ResponseBody responseBody) {
        new RenovaceDownLoadManager(callBack).writeResponseBodyToDisk(responseBody, saveFileDir, fileName);
    }
}
