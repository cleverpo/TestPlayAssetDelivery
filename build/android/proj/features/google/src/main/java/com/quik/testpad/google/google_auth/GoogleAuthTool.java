package com.quik.testpad.google.google_auth;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import java.lang.ref.WeakReference;

public class GoogleAuthTool {
    private static final String TAG = GoogleAuthTool.class.getSimpleName();

    private WeakReference<Activity> mActivityRef = null;
    private GoogleSignInClient googleSignInClient = null;

    public void init(Context context) {
        Log.d(TAG, "init");
        mActivityRef = new WeakReference<>((Activity) context);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(context, gso);
    }
}
