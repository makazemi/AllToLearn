package com.parsclass.android.alltolearn.services;

import android.os.Parcel;
import android.os.Parcelable;

public class Download implements Parcelable {
    public Download(){

    }

    private String  downloadId;
    private int progress;
    private int currentFileSize;
    private int totalFileSize;
    private boolean failDownload=false;

    public String getDownloadId() {
        return downloadId;
    }

    public void setDownloadId(String downloadId) {
        this.downloadId = downloadId;
    }

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

    public boolean isFailDownload() {
        return failDownload;
    }

    public void setFailDownload(boolean failDownload) {
        this.failDownload = failDownload;
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
        dest.writeString(downloadId);

    }

    private Download(Parcel in) {

        progress = in.readInt();
        currentFileSize = in.readInt();
        totalFileSize = in.readInt();
        downloadId=in.readString();

    }

    public static final Parcelable.Creator<Download> CREATOR = new Parcelable.Creator<Download>() {
        public Download createFromParcel(Parcel in) {
            return new Download(in);
        }

        public Download[] newArray(int size) {
            return new Download[size];
        }
    };
}