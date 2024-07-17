package com.quik.testpad.facebook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.quik.testpad.dagger.dynamicfeature.modules.facebook.interfaces.IFacebookLoginTool;

import java.lang.ref.WeakReference;
import java.util.Arrays;

public class FacebookLoginTool implements IFacebookLoginTool {
    private static final String TAG = FacebookLoginTool.class.getSimpleName();

    private static final int RC_SIGN_IN = 200000;

    private WeakReference<Activity> mActivityRef;

    private CallbackManager callbackManager = null;

    private ProfileTracker profileTracker = null;

    private AccessToken.AccessTokenRefreshCallback tokenRefreshCallback = new AccessToken.AccessTokenRefreshCallback() {
        @Override
        public void OnTokenRefreshed(@Nullable AccessToken accessToken) {
            AccessToken.setCurrentAccessToken(accessToken);
            if (profileTracker != null) {
                profileTracker.stopTracking();
            }
            profileTracker = new ProfileTracker() {
                @Override
                protected void onCurrentProfileChanged(@Nullable Profile oldProfile, @Nullable Profile newProfile) {
                    profileTracker.stopTracking();
                    handleSignInResult(accessToken);
                }
            };
            Profile.fetchProfileForCurrentAccessToken();
        }

        @Override
        public void OnTokenRefreshFailed(@Nullable FacebookException e) {
            onSignInFail(-1, e.getMessage());
        }
    };

    public void init(Context context){
        mActivityRef = new WeakReference<>((Activity) context);

        FacebookSdk.sdkInitialize(context);

        this.callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken.refreshCurrentAccessTokenAsync(tokenRefreshCallback);
            }

            @Override
            public void onCancel() {
                onSignInFail(-1, "cancel");
            }

            @Override
            public void onError(@NonNull FacebookException e) {
                onSignInFail(-1, e.getMessage());
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void signIn() {
        Activity activity = mActivityRef.get();
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AccessToken accessToken = AccessToken.getCurrentAccessToken();
                boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
                if (isLoggedIn) {
                    handleSignInResult(accessToken);
                } else {
                    LoginManager.getInstance().logInWithReadPermissions(activity, Arrays.asList("public_profile"));
                }
            }
        });
    }

    private void handleSignInResult(AccessToken accessToken) {
        if (accessToken == null || accessToken.isExpired()) {
            onSignInFail(-1, "access token error");
        } else {
            Profile profile = Profile.getCurrentProfile();
            if (profile == null) {
                onSignInFail(-1, "invalid profile");
            } else {
                onSignInSuccess(profile);
            }
        }
    }

    public void onSignInSuccess(Profile profile) {
        Log.i(TAG, "sign in success" + profile.getName());
    }

    public void onSignInFail(int code, String message) {
        Log.i(TAG, "onSignInFail, code=" + code + ", message=" + message);
    }

}
