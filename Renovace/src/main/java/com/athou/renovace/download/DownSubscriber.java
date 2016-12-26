package com.athou.renovace.download;

import android.content.Context;

import okhttp3.ResponseBody;
import rx.Subscriber;

/**
 * DownSubscriber
 * Created by Tamic on 2016-08-03.
 */
public class DownSubscriber extends Subscriber<ResponseBody> {
    private Context context;
    private String path;
    private String name;
    private DownLoadCallBack callBack;

    public DownSubscriber(Context context, String path, String name, DownLoadCallBack callBack) {
        this.context = context;
        this.path = path;
        this.name = name;
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
        new RenovaceDownLoadManager(callBack).writeResponseBodyToDisk(context, responseBody, path, name);
    }
}
