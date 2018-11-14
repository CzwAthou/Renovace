package com.testMaven;

import android.app.Application;

/**
 * Created by sy-caizhaowei on 2017/8/17.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
//        Renovace.getInstance().init(this, "http://192.168.16.28");
    }
}
