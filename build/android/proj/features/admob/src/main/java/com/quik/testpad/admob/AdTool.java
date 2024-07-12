package com.quik.testpad.admob;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.quik.testpad.dagger.dynamicfeature.modules.ad.interfaces.IAdTool;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

public class AdTool implements IAdTool {
    private static final String AD_UNIT_ID = "ca-app-pub-3940256099942544/5224354917";
    private static final String TAG = "AdmobTools";
    public static final String TEST_DEVICE_HASHED_ID = "ABCDEF012345";

    private WeakReference<Activity> mActivityRef;

    private final AtomicBoolean isMobileAdsInitializeCalled = new AtomicBoolean(false);

    private RewardedAd rewardedAd;
    boolean isLoading;

    private static AdTool _instance;
    public static AdTool getInstance() {
        if(_instance == null){
            _instance = new AdTool();
        }
        return _instance;
    }

    public void init(Activity activity){
        mActivityRef = new WeakReference<>(activity);

        initializeMobileAdsSdk();
    }

    private void initializeMobileAdsSdk() {
        if (isMobileAdsInitializeCalled.getAndSet(true)) {
            return;
        }

        // Set your test devices.
        MobileAds.setRequestConfiguration(
                new RequestConfiguration.Builder()
                        .setTestDeviceIds(Arrays.asList(TEST_DEVICE_HASHED_ID))
                        .build());

        new Thread(
                () -> {
                    Activity activity = mActivityRef.get();
                    // Initialize the Google Mobile Ads SDK on a background thread.
                    MobileAds.initialize(activity, initializationStatus -> {});
                })
                .start();
    }


    public void loadRewardedAd() {
        if (rewardedAd == null) {
            isLoading = true;
            AdRequest adRequest = new AdRequest.Builder().build();
            RewardedAd.load(
                    AdTool.getInstance().mActivityRef.get(),
                    AD_UNIT_ID,
                    adRequest,
                    new RewardedAdLoadCallback() {
                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            // Handle the error.
                            Log.d(TAG, loadAdError.getMessage());
                            rewardedAd = null;
                            AdTool.getInstance().isLoading = false;

                            Toast.makeText(AdTool.getInstance().mActivityRef.get(), "onAdFailedToLoad", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                            AdTool.getInstance().rewardedAd = rewardedAd;
                            Log.d(TAG, "onAdLoaded");
                            AdTool.getInstance().isLoading = false;
                            Toast.makeText(AdTool.getInstance().mActivityRef.get(), "onAdLoaded", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    public void showRewardedVideo() {
        if (rewardedAd == null) {
            Log.d("TAG", "The rewarded ad wasn't ready yet.");
            return;
        }
        Activity activity = mActivityRef.get();
        rewardedAd.setFullScreenContentCallback(
                new FullScreenContentCallback() {
                    @Override
                    public void onAdShowedFullScreenContent() {
                        // Called when ad is shown.
                        Log.d(TAG, "onAdShowedFullScreenContent");
                        Toast.makeText(activity, "onAdShowedFullScreenContent", Toast.LENGTH_SHORT)
                                .show();
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                        // Called when ad fails to show.
                        Log.d(TAG, "onAdFailedToShowFullScreenContent");
                        // Don't forget to set the ad reference to null so you
                        // don't show the ad a second time.
                        rewardedAd = null;
                        Toast.makeText(activity, "onAdFailedToShowFullScreenContent", Toast.LENGTH_SHORT)
                                .show();
                    }

                    @Override
                    public void onAdDismissedFullScreenContent() {
                        // Called when ad is dismissed.
                        // Don't forget to set the ad reference to null so you
                        // don't show the ad a second time.
                        rewardedAd = null;
                        Log.d(TAG, "onAdDismissedFullScreenContent");
                        Toast.makeText(activity, "onAdDismissedFullScreenContent", Toast.LENGTH_SHORT)
                                .show();
                        loadRewardedAd();
                    }
                });
        rewardedAd.show(
                activity,
                new OnUserEarnedRewardListener() {
                    @Override
                    public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                        // Handle the reward.
                        Log.d("TAG", "The user earned the reward.");
                        Toast.makeText(activity, "onUserEarnedReward", Toast.LENGTH_SHORT)
                                .show();
                    }
                });
    }
}
