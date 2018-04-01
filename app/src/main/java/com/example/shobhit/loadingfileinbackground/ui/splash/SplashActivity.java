package com.example.shobhit.loadingfileinbackground.ui.splash;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.os.Bundle;

import com.example.shobhit.loadingfileinbackground.R;
import com.example.shobhit.loadingfileinbackground.ui.base.BaseActivity;
import com.example.shobhit.loadingfileinbackground.ui.filedownload.FileDownloadActivity;

/**
 * Created by Shobhit on 02-04-2018.
 */

public class SplashActivity extends BaseActivity implements SplashView {

    private SplashPresenter mSplashPresenter;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, SplashActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mSplashPresenter = new SplashPresenter();
        mSplashPresenter.onAttach(this);
        mSplashPresenter.decideNextActivity();

    }

    @Override
    public void openMainActivity() {
        Intent intent = FileDownloadActivity.getStartIntent(this);
        startActivity(intent);
        finish();
    }
}
