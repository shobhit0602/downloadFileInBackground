package com.example.shobhit.loadingfileinbackground.ui.filedownload;

import com.example.shobhit.loadingfileinbackground.ui.base.BasePresenter;

/**
 * Created by Shobhit on 02-04-2018.
 */

public class FileDownloadPresenter<V extends FileDownloadView> extends BasePresenter<V> {

    public FileDownloadPresenter() {
        super();
    }

    void startFileDownload() {
        getMvpView().startFileDownload();
    }

    void showProgressComplete() {
        getMvpView().showProgressComplete();
    }

    void showDownloadProgress(int currentFileSize, int totalFileSize, long remainingTime ) {
        getMvpView().showProgress(currentFileSize,totalFileSize,remainingTime);
    }
}
