package com.example.shobhit.loadingfileinbackground;

import android.app.Application;

import com.example.shobhit.loadingfileinbackground.data.SharedPrefHelper;

/**
 * Created by Shobhit on 02-04-2018.
 */

public class FileDownloadApp extends Application {

    private SharedPrefHelper mSharedPrefHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        mSharedPrefHelper = new SharedPrefHelper(getApplicationContext());
    }

    public SharedPrefHelper getSharedPrefHelper() {
        return mSharedPrefHelper;
    }
}
