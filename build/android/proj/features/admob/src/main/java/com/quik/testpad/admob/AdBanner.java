package com.quik.testpad.admob;

import android.util.Log;

import com.quik.testpad.dagger.dynamicfeature.modules.ad.interfaces.IAdBanner;

public class AdBanner implements IAdBanner {
    private static final String TAG = AdBanner.class.getSimpleName();
    @Override
    public void show() {
        Log.i(TAG, "show");

    }

    @Override
    public void load() {
        Log.i(TAG, "load");

    }
}
