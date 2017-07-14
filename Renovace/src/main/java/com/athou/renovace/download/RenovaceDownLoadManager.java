package com.athou.renovace.download;

import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.athou.renovace.RenovaceException;
import com.athou.renovace.constants.RenovaceCode;
import com.athou.renovace.util.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

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

    public boolean writeResponseBodyToDisk(ResponseBody body, String saveFileDir, String fileName) {
        String type = body.contentType().toString();
        Utils.logD("contentType:>>>>" + type);
        if (type.contains(APK_CONTENTTYPE)) {
            fileSuffix = ".apk";
        } else if (type.contains(PNG_CONTENTTYPE)) {
            fileSuffix = ".png";
        } else if (type.contains(JPG_CONTENTTYPE)) {
            fileSuffix = ".jpg";
        } else {
            int index = type.indexOf("/");
            if (index == -1) {
                fileSuffix = "";
            } else {
                fileSuffix = type.substring(index);
            }
        }
        if (TextUtils.isEmpty(saveFileDir)) {
            saveFileDir = Environment.getExternalStorageDirectory() + File.separator + "DownLoads" + File.separator;
        }
        if (TextUtils.isEmpty(fileName)) {
            fileName = UUID.randomUUID().toString() + fileSuffix;
        } else {
            if (!fileName.contains(".")) {
                fileName = fileName + fileSuffix;
            }
        }
        try {
            File saveFile = new File(saveFileDir, fileName);
            if (!saveFile.getParentFile().exists()) {
                saveFile.getParentFile().mkdirs();
            }
            if (saveFile.exists()) {
                saveFile.delete();
            }
            saveFile.createNewFile();

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                final long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;
                int updateCount = 0;
                Utils.logD("file length: " + fileSize);
                inputStream = body.byteStream();
                outputStream = new FileOutputStream(saveFile);

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
                    final String finalName = fileName;
                    final String finalPath = saveFileDir + fileName;
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

    private void finalonError(final Exception e) {
        if (callBack == null) {
            return;
        }
        if (Utils.isMain()) {
            callBack.onError(new RenovaceException(RenovaceCode.CODE_DOWNLOAD_ERR, e.getMessage()));
        } else {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    callBack.onError(new RenovaceException(RenovaceCode.CODE_DOWNLOAD_ERR, e.getMessage()));
                }
            });
        }
    }
}
