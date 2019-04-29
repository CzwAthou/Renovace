package com.testMaven;

import android.app.Application;

import com.renovace.Renovace;
import com.renovace.common.RequestFactory;

/**
 * Created by czwathou on 2017/8/17.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
//        Renovace.getInstance().init(this, "http://192.168.16.28");

        Renovace.init(getApplicationContext(), true);
        RequestFactory.setDefaultConfig(MainActivity.DefaultConfig.class);
    }
}
