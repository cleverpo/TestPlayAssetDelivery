package com.quik.testpad.dagger.dynamicfeature.modules.facebook;

import android.content.ContentProvider;
import android.content.Context;

import com.quik.testpad.dagger.dynamicfeature.modules.facebook.interfaces.IFacebookLoginTool;

public class FacebookLoginModule {
    private IFacebookLoginTool mFacebookLoginTool;
    public void init(Context context){
        try{
            Class clz = Class.forName("com.quik.testpad.facebook.FacebookLoginTool");
            mFacebookLoginTool = (IFacebookLoginTool)clz.newInstance();
            mFacebookLoginTool.init(context);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public ContentProvider provideAudienceNetworkContentProvider(){
        try{
            Class clz = Class.forName("com.facebook.ads.AudienceNetworkContentProvider");
            return (ContentProvider)clz.newInstance();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public ContentProvider provideFacebookContentProvider(){
        try{
            Class clz = Class.forName("com.facebook.internal.FacebookInitProvider");
            return (ContentProvider)clz.newInstance();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public IFacebookLoginTool provideLoginTool(){
        return this.mFacebookLoginTool;
    }
}
