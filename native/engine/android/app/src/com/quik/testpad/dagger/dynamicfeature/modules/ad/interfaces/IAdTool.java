package com.quik.testpad.dagger.dynamicfeature.modules.ad.interfaces;

import android.app.Activity;

public interface IAdTool {
    void init(Activity activity);
    void loadRewardedAd();
    void showRewardedVideo();
}
