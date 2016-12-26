package com.athou.renovace.download;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.athou.renovace.RenovaceException;
import com.athou.renovace.util.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;

/**
 * Created by Tamic on 2016-07-11.
 */
public class RenovaceDownLoadManager {

    private DownLoadCallBack callBack;

    private static String APK_CONTENTTYPE = "application/vnd.android.package-archive";
    private static String PNG_CONTENTTYPE = "image/png";
    private static String JPG_CONTENTTYPE = "image/jpg";

    private static String fileSuffix = "";

    private static String defPath = "";

    private Handler handler;

    public static boolean isDownLoading = false;

    public static boolean isCancel = false;

    private String key;

    public RenovaceDownLoadManager(DownLoadCallBack callBack) {
        this.callBack = callBack;
        handler = new Handler(Looper.getMainLooper());
    }

    private static RenovaceDownLoadManager sInstance;

    /**
     * DownLoadManager getInstance
     */
    public static synchronized RenovaceDownLoadManager getInstance(DownLoadCallBack callBack) {
        if (sInstance == null) {
            sInstance = new RenovaceDownLoadManager(callBack);
        }
        return sInstance;
    }

    public boolean writeResponseBodyToDisk(Context context, ResponseBody body, String path, String name) {
        Utils.logD("contentType:>>>>" + body.contentType().toString());
        String type = body.contentType().toString();
        if (type.contains(APK_CONTENTTYPE)) {
            fileSuffix = ".apk";
        } else if (type.contains(PNG_CONTENTTYPE)) {
            fileSuffix = ".png";
        } else if (type.contains(JPG_CONTENTTYPE)) {
            fileSuffix = ".jpg";
        }

        if (!TextUtils.isEmpty(name)) {
            if (!name.contains(".")) {
                name = name + fileSuffix;
            }
        }

        if (path == null) {
            path = context.getExternalFilesDir(null) + File.separator + "DownLoads" + File.separator;
        }
        if (new File(path + name).exists()) {
            deleteFile(path);
        }
        Utils.logD("path:-->" + path);
        Utils.logD("name:->" + name);
        try {
            // todo change the file location/name according to your needs
            File futureStudioIconFile = new File(path + name);

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                final long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;
                int updateCount = 0;
                Utils.logD("file length: " + fileSize);
                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);
                    if (read == -1) {
                        break;
                    }
                    outputStream.write(fileReader, 0, read);
                    fileSizeDownloaded += read;
                    Utils.logD("file download: " + fileSizeDownloaded + " of " + fileSize);
                    final int progress = (int) (fileSizeDownloaded * 100 / fileSize);
                    Utils.logD("file download progress : " + progress);
                    if (updateCount == 0 || progress >= updateCount) {
                        updateCount += 1;//每次增长10%
                        if (callBack != null) {
                            handler = new Handler(Looper.getMainLooper());
                            final long finalFileSizeDownloaded = fileSizeDownloaded;
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    callBack.onProgress(finalFileSizeDownloaded, fileSize);
                                }
                            });
                        }
                    }
                }

                outputStream.flush();
                Utils.logD("file downloaded: " + fileSizeDownloaded + " of " + fileSize);
                isDownLoading = false;
                if (callBack != null) {
                    final String finalName = name;
                    final String finalPath = path;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callBack.onSucess(finalPath, finalName, fileSize);
                        }
                    });
                    Utils.logD("file downloaded: " + fileSizeDownloaded + " of " + fileSize);
                    Utils.logD("file downloaded: is sucess");
                }
                return true;
            } catch (IOException e) {
                finalonError(e);
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            finalonError(e);
            return false;
        }
    }

    /**
     * 删除文件
     *
     * @param filePath
     */
    private void deleteFile(String filePath) {
        try {
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void finalonError(final Exception e) {
        if (callBack == null) {
            return;
        }
        if (Utils.isMain()) {
            callBack.onError(new RenovaceException(100, e.getMessage()));
        } else {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    callBack.onError(new RenovaceException(100, e.getMessage()));
                }
            });
        }
    }
}
