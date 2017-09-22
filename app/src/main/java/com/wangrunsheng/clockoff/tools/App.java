package com.wangrunsheng.clockoff.tools;

import android.app.Application;

/**
 * Created by Russell on 2017/9/20.
 */

public class App extends Application {
private static App mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static App getInstance() {
        return mInstance;
    }
}
