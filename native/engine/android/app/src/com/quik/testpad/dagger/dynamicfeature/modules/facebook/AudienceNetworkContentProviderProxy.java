package com.quik.testpad.dagger.dynamicfeature.modules.facebook;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.quik.testpad.dagger.dynamicfeature.DFManager;

/**
 * AudienceNetworkContentProviderProxy
 */
public class AudienceNetworkContentProviderProxy extends ContentProvider {
    private static final String TAG = AudienceNetworkContentProviderProxy.class.getSimpleName();
    private ContentProvider provider;
    private ContentProvider getRealProvider(){
        if(provider == null){
            FacebookLoginModule module = DFManager.getInstance().facebookLoginModule;
            if(module != null){
                provider = module.provideAudienceNetworkContentProvider();
            }
        }
        return provider;
    }
    @Override
    public boolean onCreate() {
        ContentProvider provider = getRealProvider();
        Log.i(TAG, "onCreate real:" + (provider != null));
        if(provider == null) return false;

        return provider.onCreate();
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        ContentProvider provider = getRealProvider();
        Log.i(TAG, "query real:" + (provider != null));
        if(provider == null) return null;

        return provider.query(uri, projection, selection, selectionArgs, sortOrder);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        ContentProvider provider = getRealProvider();
        Log.i(TAG, "getType real:" + (provider != null));
        if(provider == null) return "";

        return provider.getType(uri);
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        ContentProvider provider = getRealProvider();
        Log.i(TAG, "insert real:" + (provider != null));
        if(provider == null) return null;

        return provider.insert(uri, values);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        ContentProvider provider = getRealProvider();
        Log.i(TAG, "delete real:" + (provider != null));
        if(provider == null) return 0;

        return provider.delete(uri, selection, selectionArgs);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        ContentProvider provider = getRealProvider();
        Log.i(TAG, "update real:" + (provider != null));
        if(provider == null) return 0;

        return provider.update(uri, values, selection, selectionArgs);
    }
}
