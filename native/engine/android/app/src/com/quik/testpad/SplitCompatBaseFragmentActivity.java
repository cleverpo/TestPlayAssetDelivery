package com.quik.testpad;

import android.app.Activity;
import android.content.Context;

import com.google.android.play.core.splitcompat.SplitCompat;
import androidx.fragment.app.FragmentActivity;

public class SplitCompatBaseFragmentActivity extends FragmentActivity {
    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        SplitCompat.installActivity(context);
    }
}
