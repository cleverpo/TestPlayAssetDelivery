package com.quik.testpad.dagger.dynamicfeature.modules.ad;

import android.content.ContentProvider;

import com.quik.testpad.dagger.dynamicfeature.modules.ad.interfaces.IAdBanner;
import com.quik.testpad.dagger.dynamicfeature.modules.ad.interfaces.IAdTool;

import java.lang.reflect.Method;

public class AdModule {
    public ContentProvider provideAdContentProvider(){
        try{
            Class clz = Class.forName("com.google.android.gms.ads.MobileAdsInitProvider");
            return (ContentProvider)clz.newInstance();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public IAdBanner provideAdBanner(){
        try{
            Class clz = Class.forName("com.quik.testpad.google.admob.AdBanner");
            return (IAdBanner)clz.newInstance();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public IAdTool provideAdTool(){
        try{
            Class clz = Class.forName("com.quik.testpad.google.admob.AdTool");
            Method method = clz.getMethod("getInstance");
            return (IAdTool)method.invoke(null);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
