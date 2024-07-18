package com.quik.testpad;

import android.app.Activity;
import android.content.Context;

import com.google.android.play.core.splitcompat.SplitCompat;

public class SplitCompatBaseActivity extends Activity {
    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        SplitCompat.installActivity(context);
    }
}
