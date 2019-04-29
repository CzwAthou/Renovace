package com.renovace.common.upload;

import okhttp3.Response;

/**
 * @author athoucai
 * @date 2018/11/26
 */
public abstract class UploadListener {
    public void onStart() {

    }

    public void onProgress(long currentLength, long total, boolean done) {

    }

    public void onError(Throwable throwable) {

    }

    public abstract void onSuccess(Response response);
}
