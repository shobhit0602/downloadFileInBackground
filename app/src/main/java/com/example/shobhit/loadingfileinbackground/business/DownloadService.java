package com.example.shobhit.loadingfileinbackground.business;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.example.shobhit.loadingfileinbackground.R;
import com.example.shobhit.loadingfileinbackground.network.ApiClient;
import com.example.shobhit.loadingfileinbackground.network.ApiInterface;
import com.example.shobhit.loadingfileinbackground.ui.filedownload.FileDownloadActivity;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Created by Shobhit on 02-04-2018.
 */

public class DownloadService extends IntentService {

    private static final String EXTRAS_DOWNLOAD = "EXTRAS_DOWNLOAD";
    private static final String TAG = DownloadService.class.getSimpleName();

    private NotificationCompat.Builder mNotificationBuilder;
    private NotificationManager mNotificationManager;
    private Download mDownload;

    public DownloadService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_download)
                .setContentTitle(getResources().getString(R.string.download))
                .setContentText(getResources().getString(R.string.downloading_file))
                .setAutoCancel(true);
        mNotificationManager.notify(0, mNotificationBuilder.build());

        initDownload();
    }

    private void initDownload(){
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<ResponseBody> request = apiInterface.downloadFile();
        try {
            downloadFile(request.execute().body());
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    private void downloadFile(ResponseBody body) throws IOException {
        mDownload = new Download();
        int count;
        byte data[] = new byte[1024 * 4];
        long fileSize = body.contentLength();
        InputStream bis = new BufferedInputStream(body.byteStream(), 1024 * 8);
        File outputFile = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS), "file.zip");
        OutputStream output = new FileOutputStream(outputFile);
        long total = 0;
        long startTime = System.currentTimeMillis();
        int timeCount = 1;
        while ((count = bis.read(data)) != -1) {

            total += count;
            int totalFileSize = (int) (fileSize / (Math.pow(1024, 2)));
            double current = Math.round(total / (Math.pow(1024, 2)));

            int progress = (int) ((total * 100) / fileSize);
            if (current != 0) {
                long currentTime = System.currentTimeMillis() - startTime;
                long totalTimeForDownloading = (currentTime * totalFileSize / (long) current);
                long remainingTime = totalTimeForDownloading - currentTime;
                mDownload.setTotalFileSize(totalFileSize);

                if (currentTime > 1000 * timeCount) {
                    mDownload.setCurrentFileSize((int) current);
                    mDownload.setProgress(progress);
                    mDownload.setRemainingTime(remainingTime);
                    sendNotification(mDownload);
                    timeCount++;
                }
            }
            output.write(data, 0, count);
        }
        onDownloadComplete();
        output.flush();
        output.close();
        bis.close();
    }

    private void sendNotification(Download download){
        sendIntent(download);
        mNotificationBuilder.setProgress(100,download.getProgress(),false);
        mNotificationBuilder.setContentText(String.format("Downloaded (%d/%d) MB",
                download.getCurrentFileSize(),download.getTotalFileSize()));
        mNotificationManager.notify(0, mNotificationBuilder.build());
    }

    private void sendIntent(Download download){
        Intent intent = new Intent(FileDownloadActivity.MESSAGE_PROGRESS);
        intent.putExtra(EXTRAS_DOWNLOAD,download);
        LocalBroadcastManager.getInstance(DownloadService.this).sendBroadcast(intent);
    }

    private void onDownloadComplete(){
        mDownload.setProgress(100);
        sendIntent(mDownload);
        mNotificationManager.cancel(0);
        mNotificationBuilder.setProgress(0,0,false);
        mNotificationBuilder.setContentText(getResources().getString(R.string.file_download_complete));
        mNotificationManager.notify(0, mNotificationBuilder.build());
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        mNotificationManager.cancel(0);
    }
}
