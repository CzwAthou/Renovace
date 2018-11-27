package com.pince.renovace2.request.download;

import java.io.File;

/**
 * @author athoucai
 * @date 2018/11/26
 */
public interface DownloadListener {

    void onStart();

    void onProgress(long currentLength, long total, boolean done);

    void onError(Throwable throwable);
}
