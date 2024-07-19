package com.quik.testpad.dagger.dynamicfeature;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.play.core.splitinstall.SplitInstallException;
import com.google.android.play.core.splitinstall.SplitInstallManager;
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory;
import com.google.android.play.core.splitinstall.SplitInstallRequest;
import com.google.android.play.core.splitinstall.SplitInstallSessionState;
import com.google.android.play.core.splitinstall.SplitInstallStateUpdatedListener;
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus;
import com.google.android.play.core.splitinstall.testing.FakeSplitInstallManager;
import com.google.android.play.core.splitinstall.testing.FakeSplitInstallManagerFactory;
import com.quik.testpad.dagger.dynamicfeature.modules.ad.AdModule;
import com.quik.testpad.dagger.dynamicfeature.modules.facebook.FacebookLoginModule;
import com.quik.testpad.dagger.dynamicfeature.modules.google_auth.GoogleAuthModule;

import java.util.Timer;
import java.util.TimerTask;

public class DFManager {
    private static DFManager _instance;
    public static DFManager getInstance(){
        if(_instance == null){
            _instance = new DFManager();
        }
        return _instance;
    }

    public static final String TAG = DFManager.class.getSimpleName();
    public static final boolean IS_FAKE_SPLIT_MANAGER = true;

    private static final String MODULE_NAME_ADMOB = "admob";
    private static final String MODULE_NAME_GOOGLEAUTH = "google_auth";
    private static final String MODULE_NAME_FACEBOOK = "facebook";
    private static final String MODULE_NAME_TEST1 = "test1";

    public AdModule adModule;
    public GoogleAuthModule googleAuthModule;
    public FacebookLoginModule facebookLoginModule;

    private Activity mActivity;

    private SplitInstallManager mManager;

    private SplitInstallStateUpdatedListener mListener = new SplitInstallStateUpdatedListener(){
        @Override
        public void onStateUpdate(@NonNull SplitInstallSessionState state) {
            boolean multiInstall = state.moduleNames().size() > 1;
            boolean langsInstall = !state.languages().isEmpty();

            String names;
            if(langsInstall){
                names = state.languages().get(0);
            }else{
                names = String.join(" - ", state.moduleNames());
            }

            switch(state.status()){
                case SplitInstallSessionStatus.DOWNLOADING:{
                    Log.i(TAG, "DOWNLOADING:" + names);
                }
                break;

                case SplitInstallSessionStatus.REQUIRES_USER_CONFIRMATION:{
                    Log.i(TAG, "REQUIRES_USER_CONFIRMATION:" + names);
                }
                break;

                case SplitInstallSessionStatus.INSTALLED:{
                    Log.i(TAG, "INSTALLED:" + names);
                    onSuccessfulLoad(names);
                }
                break;

                case SplitInstallSessionStatus.INSTALLING:{
                    Log.i(TAG, "INSTALLING:" + names);
                }
                break;

                case SplitInstallSessionStatus.FAILED:{
                    Log.i(TAG, "FAILED:" + names);
                }
                break;
            }
        }
    };

    public void init(Activity activity){
        mActivity = activity;

        if(IS_FAKE_SPLIT_MANAGER){
            mManager = FakeSplitInstallManagerFactory.create(mActivity);
            ((FakeSplitInstallManager) mManager).setShouldNetworkError(false);
        }else{
            mManager = SplitInstallManagerFactory.create(mActivity);
        }

//        this.loadAndLaunchModule(MODULE_NAME_ADMOB);
        this.loadAndLaunchModule(MODULE_NAME_FACEBOOK);
//        this.loadAndLaunchModule(MODULE_NAME_GOOGLEAUTH);
//        this.loadAndLaunchModule(MODULE_NAME_TEST1);
    }

    public void onResume(){
        mManager.registerListener(mListener);
    }

    public void onPause(){
        mManager.unregisterListener(mListener);
    }

    private void loadAndLaunchModule(String name){
        if(mManager.getInstalledModules().contains(name)){
            this.onSuccessfulLoad(name);
            return;
        }
        Log.i(TAG, "Load module " + name);
        SplitInstallRequest request = SplitInstallRequest.newBuilder()
                .addModule(name)
                .build();
        mManager.startInstall(request);
    }

    private void onSuccessfulLoad(String moduleName) {
        Log.i(TAG, "onSuccessfulLoad: " + moduleName);
        try{
            Activity activity = mActivity;
            if(moduleName.equals(MODULE_NAME_ADMOB)){
                adModule = new AdModule();
            }else if(moduleName.equals(MODULE_NAME_GOOGLEAUTH)){
                googleAuthModule = new GoogleAuthModule();
            }else if(moduleName.equals(MODULE_NAME_FACEBOOK)){
                facebookLoginModule = new FacebookLoginModule();
                facebookLoginModule.init(activity);

                //模拟启动一个acitivty，因为第一个acitvity需要在attachBaseContext加上SplitCompat.installActivity(this);资源读取才有效
//                activity.startActivity(
//                        new Intent()
//                                .setClassName(activity.getPackageName(), "com.quik.testpad.facebook.DynamicSdkStartupActivity")
//                );

            }else if(moduleName.equals(MODULE_NAME_TEST1)){
//                Intent intent = new Intent();
//                intent.setClassName("com.quik.testpad", "com.quik.testpad.test1.Test3Activity");
//                mActivity.startActivity(intent);

                Timer timer = new Timer();
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        final Intent it = new Intent();
                        it.setClassName("com.quik.testpad", "com.quik.testpad.test1.Test1Activity");// 要启动的 Activity
                        mActivity.startActivity(it); // 执行启动 Activity
                    }
                };
                timer.schedule(task, 3000); // 10 秒后启动，时间可根据需求调整
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
