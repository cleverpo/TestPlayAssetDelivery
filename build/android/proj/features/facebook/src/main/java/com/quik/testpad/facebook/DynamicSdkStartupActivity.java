package com.quik.testpad.facebook;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.android.play.core.splitcompat.SplitCompat;

public class DynamicSdkStartupActivity extends Activity {
    private static final String TAG = DynamicSdkStartupActivity.class.getSimpleName();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamicsdk_startup_layout);
        finish();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);

        boolean result = SplitCompat.installActivity(this);

        Log.d(TAG, "SplitCompat.installActivity " + result);
    }
}
