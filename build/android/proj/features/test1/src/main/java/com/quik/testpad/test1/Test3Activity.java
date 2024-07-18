package com.quik.testpad.test1;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.quik.testpad.SplitCompatBaseActivity;

public class Test3Activity extends SplitCompatBaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        finish();
    }
}
