package com.pince.renovace2.request.upload;

import okhttp3.Response;

/**
 * @author athoucai
 * @date 2018/11/26
 */
public interface UploadListener {
    void onStart();

    void onProgress(long currentLength, long total, boolean done);

    void onError(Throwable throwable);

    void onSuccess(Response response);
}
