package com.example.shobhit.loadingfileinbackground.ui.filedownload;

import com.example.shobhit.loadingfileinbackground.ui.base.BaseView;

/**
 * Created by Shobhit on 02-04-2018.
 */

public interface FileDownloadView extends BaseView {

    void showProgress(int currentFileSize, int totalFileSize, long remainingTime);

    void startFileDownload();

    void showProgressComplete();
}
