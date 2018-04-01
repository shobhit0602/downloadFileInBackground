package com.example.shobhit.loadingfileinbackground.business;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Shobhit on 02-04-2018.
 */

public class Download  implements Parcelable {

    public Download(){}

    private int progress;
    private int currentFileSize;
    private int totalFileSize;
    private long remainingTime;

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getCurrentFileSize() {
        return currentFileSize;
    }

    public void setCurrentFileSize(int currentFileSize) {
        this.currentFileSize = currentFileSize;
    }

    public int getTotalFileSize() {
        return totalFileSize;
    }

    public void setTotalFileSize(int totalFileSize) {
        this.totalFileSize = totalFileSize;
    }


    public long getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(long remainingTime) {
        this.remainingTime = remainingTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(progress);
        dest.writeInt(currentFileSize);
        dest.writeInt(totalFileSize);
        dest.writeLong(remainingTime);
    }

    private Download(Parcel in) {

        progress = in.readInt();
        currentFileSize = in.readInt();
        totalFileSize = in.readInt();
        remainingTime = in.readLong();
    }

    public static final Creator<Download> CREATOR = new Creator<Download>() {
        public Download createFromParcel(Parcel in) {
            return new Download(in);
        }

        public Download[] newArray(int size) {
            return new Download[size];
        }
    };
}
