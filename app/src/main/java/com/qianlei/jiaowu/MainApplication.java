package com.qianlei.jiaowu;

import android.app.Application;

/**
 * @author qianlei
 */
public class MainApplication extends Application {

    private static MainApplication instance;

    public static MainApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
