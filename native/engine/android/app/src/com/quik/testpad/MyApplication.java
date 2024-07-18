package com.quik.testpad;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.google.android.play.core.splitcompat.SplitCompat;

public class MyApplication extends Application {

    private static final String TAG = MyApplication.class.getSimpleName();

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //启用动态模块功能
        boolean result = SplitCompat.install(this);
        Log.d(TAG, "SplitCompat.install " + result);
    }
}
