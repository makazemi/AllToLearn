package com.parsclass.android.alltolearn.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.parsclass.android.alltolearn.Utils.ConstantUtil;

public class Lecture implements Parcelable {

    //private int id;
    private String number;
    private String title_lecture;
    private String typeMedia;
    private String Lduration;
    private boolean hasPreview;
    private boolean hasCompleted;
    private boolean hasPublic;
    private String urlPath;
    private int downloadStatus= ConstantUtil.DOWNLOAD_NOT_START_STATUS;
    private int progressDownload;
    private int index_dataSource;
    private boolean selected=false;



    public Lecture() {
    }

    public Lecture(String number, String title_lecture, String typeMedia, String Lduration, boolean hasPreview, boolean hasCompleted, boolean hasPublic, String urlPath) {
        // this.id = id;
        this.number = number;
        this.title_lecture = title_lecture;
        this.typeMedia = typeMedia;
        this.Lduration = Lduration;
        this.hasPreview = hasPreview;
        this.hasCompleted = hasCompleted;
        this.hasPublic = hasPublic;
        this.urlPath = urlPath;
    }

//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTitle_lecture() {
        return title_lecture;
    }

    public void setTitle_lecture(String title_lecture) {
        this.title_lecture = title_lecture;
    }

    public String getTypeMedia() {
        return typeMedia;
    }

    public void setTypeMedia(String typeMedia) {
        this.typeMedia = typeMedia;
    }

    public boolean isHasPreview() {
        return hasPreview;
    }

    public void setHasPreview(boolean hasPreview) {
        this.hasPreview = hasPreview;
    }

    public boolean isHasCompleted() {
        return hasCompleted;
    }

    public void setHasCompleted(boolean hasCompleted) {
        this.hasCompleted = hasCompleted;
    }

    public boolean isHasPublic() {
        return hasPublic;
    }

    public void setHasPublic(boolean hasPublic) {
        this.hasPublic = hasPublic;
    }

    public String getUrlPath() {
        return urlPath;
    }

    public void setUrlPath(String urlPath) {
        this.urlPath = urlPath;
    }

    public String getLduration() {
        return Lduration;
    }

    public void setLduration(String lduration) {
        this.Lduration = lduration;
    }

    public int getIndex_dataSource() {
        return index_dataSource;
    }

    public void setIndex_dataSource(int index_dataSource) {
        this.index_dataSource = index_dataSource;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getDownloadStatus() {
        return downloadStatus;
    }

    public void setDownloadStatus(int downloadStatus) {
        this.downloadStatus = downloadStatus;
    }

    public int getProgressDownload() {
        return progressDownload;
    }

    public void setProgressDownload(int progressDownload) {
        this.progressDownload = progressDownload;
    }

    protected Lecture(Parcel in) {
        title_lecture = in.readString();
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title_lecture);
    }
    public static final Creator<Lecture> CREATOR = new Creator<Lecture>() {
        @Override
        public Lecture createFromParcel(Parcel in) {
            return new Lecture(in);
        }

        @Override
        public Lecture[] newArray(int size) {
            return new Lecture[size];
        }
    };
}
