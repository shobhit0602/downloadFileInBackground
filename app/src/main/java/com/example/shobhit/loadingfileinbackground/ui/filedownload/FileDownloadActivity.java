package com.example.shobhit.loadingfileinbackground.ui.filedownload;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shobhit.loadingfileinbackground.R;
import com.example.shobhit.loadingfileinbackground.business.Download;
import com.example.shobhit.loadingfileinbackground.business.DownloadService;
import com.example.shobhit.loadingfileinbackground.ui.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Shobhit on 02-04-2018.
 */

public class FileDownloadActivity extends BaseActivity implements FileDownloadView {

    public static final String MESSAGE_PROGRESS = "MESSAGE_PROGRESS";
    private static final String EXTRAS_DOWNLOAD = "EXTRAS_DOWNLOAD";
    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final String BUNDLE_CURRENT_FILE_SIZE = "BUNDLE_CURRENT_FILE_SIZE";
    private static final String BUNDLE_TOTAL_FILE_SIZE = "BUNDLE_TOTAL_FILE_SIZE";
    private static final String BUNDLE_REMAINING_TIME = "BUNDLE_REMAINING_TIME";

    private FileDownloadPresenter mPresenter;

    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;

    @BindView(R.id.progressStatus)
    TextView mProgressText;

    @BindView(R.id.remainingTime)
    TextView mProgressTime;

    @BindView(R.id.firstNameEditText)
    EditText mFirstName;

    @BindView(R.id.lastNameEditText)
    EditText mLastName;

    @BindView(R.id.outerRelativeLayout)
    RelativeLayout mOuterLayout;

    @BindView(R.id.downloadButton)
    Button mDownloadButton;

    private int mCurrrentFileSize;
    private int mTotalFileSize;
    private long mRemainingTime;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, FileDownloadActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mPresenter = new FileDownloadPresenter();
        mPresenter.onAttach(this);
        registerReceiver();

        if (savedInstanceState != null) {
            long remainingTime = savedInstanceState.getLong(BUNDLE_REMAINING_TIME);
            int minutes = (int) (remainingTime / (60 * 1000));
            int seconds = (int) ((remainingTime / 1000) % 60);
            mProgressText.setText(String.format("Downloaded (%d/%d) MB",
                    savedInstanceState.getInt(BUNDLE_CURRENT_FILE_SIZE),
                    savedInstanceState.getInt(BUNDLE_TOTAL_FILE_SIZE)));
            mProgressTime.setText(String.format("Time left %d:%02d", minutes, seconds));
        }
    }

    @OnClick(R.id.downloadButton)
    public void downloadFile(){
        if(checkPermission()){
            mPresenter.startFileDownload();
            mDownloadButton.setEnabled(false);
        } else {
            requestPermission();
        }
    }

    private boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mPresenter.startFileDownload();
                } else {
                    Toast.makeText(FileDownloadActivity.this,
                            getResources().getString(R.string.permission_denied),
                            Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void registerReceiver(){
        LocalBroadcastManager bManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MESSAGE_PROGRESS);
        bManager.registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    public void showProgress(int currentFileSize, int totalFileSize, long remainingTime) {
        int minutes = (int) (remainingTime / (60 * 1000));
        int seconds = (int) ((remainingTime / 1000) % 60);
        mProgressText.setText(String.format("Downloaded (%d/%d) MB",currentFileSize,totalFileSize));
        mProgressTime.setText(String.format("Time left %d:%02d", minutes, seconds));
        mCurrrentFileSize = currentFileSize;
        mTotalFileSize = totalFileSize;
        mRemainingTime = remainingTime;
    }

    @Override
    public void startFileDownload() {
        Intent intent = new Intent(FileDownloadActivity.this,DownloadService.class);
        startService(intent);
    }

    @Override
    public void showProgressComplete() {
        mProgressText.setText(getResources().getString(R.string.file_download_complete));
        mDownloadButton.setEnabled(true);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(BUNDLE_CURRENT_FILE_SIZE, mCurrrentFileSize);
        outState.putInt(BUNDLE_TOTAL_FILE_SIZE, mTotalFileSize);
        outState.putLong(BUNDLE_REMAINING_TIME, mRemainingTime);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction() !=  null && intent.getAction().equals(MESSAGE_PROGRESS)){
                Download download = intent.getParcelableExtra(EXTRAS_DOWNLOAD);
                mProgressBar.setProgress(download.getProgress());
                if(download.getProgress() == 100){
                    mPresenter.showProgressComplete();
                } else {
                    mPresenter.showDownloadProgress(download.getCurrentFileSize(),
                            download.getTotalFileSize(),download.getRemainingTime());
                }
            }
        }
    };
}
