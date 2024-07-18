package com.quik.testpad.dagger.dynamicfeature.modules.facebook.interfaces;

import android.content.Context;
import android.content.Intent;

public interface IFacebookLoginTool {
    void init(Context context);
    void onActivityResult(int requestCode, int resultCode, Intent data);

    void signIn();
}
