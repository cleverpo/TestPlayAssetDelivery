package com.quik.testpad.dagger.dynamicfeature.modules.ad;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.quik.testpad.dagger.dynamicfeature.DFManager;

/**
 * MobileAdsInitProviderProxy
 */
public class MobileAdsInitProviderProxy extends ContentProvider {
    private ContentProvider provider;
    private ContentProvider getRealProvider(){
        if(provider == null){
            AdModule adModule = DFManager.getInstance().adModule;
            if(adModule != null){
                provider = adModule.provideAdContentProvider();
            }
        }
        return provider;
    }
    @Override
    public boolean onCreate() {
        ContentProvider provider = getRealProvider();
        if(provider == null) return false;

        return provider.onCreate();
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        ContentProvider provider = getRealProvider();
        if(provider == null) return null;

        return provider.query(uri, projection, selection, selectionArgs, sortOrder);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        ContentProvider provider = getRealProvider();
        if(provider == null) return "";

        return provider.getType(uri);
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        ContentProvider provider = getRealProvider();
        if(provider == null) return null;

        return provider.insert(uri, values);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        ContentProvider provider = getRealProvider();
        if(provider == null) return 0;

        return provider.delete(uri, selection, selectionArgs);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        ContentProvider provider = getRealProvider();
        if(provider == null) return 0;

        return provider.update(uri, values, selection, selectionArgs);
    }
}
