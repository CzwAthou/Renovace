package com.pince.renovace2.request;

import android.text.TextUtils;

import com.pince.renovace2.config.Config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import okhttp3.ResponseBody;
import okio.BufferedSource;

/**
 * @author athou
 * @date 2017/12/13
 */
@Deprecated
public class DownloadRequestBuidler extends RequestBuilder<DownloadRequestBuidler> {

    public DownloadRequestBuidler(Class<? extends Config> clientConfig) {
        super(Method.Download, clientConfig);
    }

    private String savePath;

    public DownloadRequestBuidler savePath(String path) {
        this.savePath = path;
        return this;
    }

    public Observable<File> request() {
        return requestBody().map(new Function<ResponseBody, File>() {
            @Override
            public File apply(ResponseBody responseBody) throws Exception {
                if (TextUtils.isEmpty(savePath)) {
                    throw new IllegalArgumentException("empty path");
                }
                String suffix = "." + responseBody.contentType().subtype();
                if (!savePath.endsWith(suffix)) {
                    savePath = savePath + suffix;
                }
                File file = new File(savePath);
                if (file != null) {
                    if (!file.getParentFile().exists()) {
                        file.getParentFile().mkdirs();
                    }
                    if (file.exists()) {
                        file.delete();
                    }
                    file.createNewFile();

                    BufferedSource is = responseBody.source();
                    FileOutputStream fos = new FileOutputStream(file);
                    byte[] buffer = new byte[4 * 1024];
                    int len = 0;
                    while ((len = is.read(buffer, 0, buffer.length)) > 0) {
                        fos.write(buffer, 0, len);
                    }
                    fos.flush();
                    fos.close();
                    return file;
                }
                throw new FileNotFoundException("path:" + savePath);
            }
        });
    }
}
