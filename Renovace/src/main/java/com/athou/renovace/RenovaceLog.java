package com.athou.renovace;

import com.athou.renovace.util.Utils;

import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by athou on 2016/12/16.
 */

public class RenovaceLog implements HttpLoggingInterceptor.Logger {

    @Override
    public void log(String message) {
        Utils.logI(message);
    }
}