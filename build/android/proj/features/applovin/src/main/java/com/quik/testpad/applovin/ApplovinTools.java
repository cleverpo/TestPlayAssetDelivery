package com.quik.testpad.applovin;

import android.app.Activity;
import android.util.Log;

import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinSdkConfiguration;

public class ApplovinTools {
    private static final String TAG = "ApplovinTools";
    private static ApplovinTools _instance;

    public static ApplovinTools getInstance(){
        if(_instance == null){
            _instance = new ApplovinTools();
        }
        return _instance;
    }

    public void init(Activity context){
        AppLovinSdk.getInstance( context ).setMediationProvider( "max" );
        AppLovinSdk.initializeSdk( context, new AppLovinSdk.SdkInitializationListener() {
            @Override
            public void onSdkInitialized(final AppLovinSdkConfiguration configuration)
            {
                // AppLovin SDK is initialized, start loading ads
                Log.i(TAG, "init");
            }
        } );
    }
}
