package com.quik.testpad;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.quik.testpad.dagger.dynamicfeature.DFManager;

public class MainAppActivity extends Activity {
    public final static String TAG = MainAppActivity.class.getSimpleName();
    private static int CONFIRMATION_REQUEST_CODE = 1;

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.btn_admob_init){
                if(dfManager.adModule == null){
                    //初始化没完成
                    Log.i(TAG, "初始化没完成");
                }else{
                    dfManager.adModule.provideAdTool().init(MainAppActivity.this);
                }
            }else if(v.getId() == R.id.btn_admob_load){
                if(dfManager.adModule == null){
                    //初始化没完成
                    Log.i(TAG, "初始化没完成");
                }else{
                    dfManager.adModule.provideAdTool().loadRewardedAd();
                }
            }else if(v.getId() == R.id.btn_admob_show){
                if(dfManager.adModule == null){
                    //初始化没完成
                    Log.i(TAG, "初始化没完成");
                }else{
                    dfManager.adModule.provideAdTool().showRewardedVideo();
                }
            }else if(v.getId() == R.id.btn_facebook){
                if(dfManager.facebookLoginModule == null){
                    //初始化没完成
                    Log.i(TAG, "初始化没完成");
                }else{
                    dfManager.facebookLoginModule.provideLoginTool().signIn();
                }
            }
        }
    };

    private DFManager dfManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_admob_init).setOnClickListener(onClickListener);
        findViewById(R.id.btn_admob_load).setOnClickListener(onClickListener);
        findViewById(R.id.btn_admob_show).setOnClickListener(onClickListener);
        findViewById(R.id.btn_applovin).setOnClickListener(onClickListener);
        findViewById(R.id.btn_facebook).setOnClickListener(onClickListener);

        dfManager = DFManager.getInstance();
        dfManager.init(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        dfManager.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d(TAG, "onPause()");
        dfManager.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CONFIRMATION_REQUEST_CODE) {
            // Handle the user's decision. For example, if the user selects "Cancel",
            // you may want to disable certain functionality that depends on the module.
            if (resultCode == Activity.RESULT_CANCELED) {
                Log.i(TAG, "REQUIRES_USER_CONFIRMATION_CANCEL");
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
