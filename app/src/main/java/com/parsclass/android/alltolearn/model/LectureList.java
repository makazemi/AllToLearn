package com.parsclass.android.alltolearn.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;

import com.parsclass.android.alltolearn.R;
import com.parsclass.android.alltolearn.Utils.ConstantUtil;
import com.parsclass.android.alltolearn.Utils.DateConverter;

import java.io.Serializable;
import java.util.Date;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "lecture_list_table",
        foreignKeys = @ForeignKey(entity = Course.class,
                parentColumns = "id",
                childColumns = "courseId",
                onDelete = CASCADE)
        ,indices = {@Index("courseId")})
@TypeConverters({DateConverter.class})
public class LectureList implements Serializable {


    @PrimaryKey
    private int local_id;
    private String server_id;
    private String number_lectureList;
    private String title_lectureList;
    private String typeMedia_lectureList;
    private String Lduration_lectureList;
    private boolean hasPreview_lectureList=false;
    private boolean hasCompleted_lectureList=false;
    private String urlPath_lectureList;
    private int downloadStatus_lectureList= ConstantUtil.DOWNLOAD_NOT_START_STATUS;
    private int progressDownload_lectureList;
    private int index_dataSource_lectureList;
   // private boolean selected_lectureList=false;
    private int typeLecture_lectureList;
    private int courseId;
    private int numberSection_lectureList;
    private String extension;
    private String name_section;
    private boolean Playing=false;
    private Date lastRefresh;

    public LectureList(int local_id,String number_lectureList, String title_lectureList,
                     String typeMedia_lectureList, String Lduration_lectureList,String urlPath_lectureList,int typeLecture_lectureList,
                     int courseId) {
        this.local_id=local_id;
        this.number_lectureList = number_lectureList;
        this.title_lectureList = title_lectureList;
        this.typeMedia_lectureList = typeMedia_lectureList;
        this.Lduration_lectureList = Lduration_lectureList;
        this.urlPath_lectureList = urlPath_lectureList;
        this.typeLecture_lectureList = typeLecture_lectureList;
        this.courseId = courseId;

    }

    @Ignore
    public LectureList(int local_id, String number_lectureList, String title_lectureList, int typeLecture_lectureList, int courseId) {
        this.local_id = local_id;
        this.number_lectureList = number_lectureList;
        this.title_lectureList = title_lectureList;
        this.typeLecture_lectureList = typeLecture_lectureList;
        this.courseId = courseId;
    }

    //    public LectureList(int id,String number, String title_lecture,int typeLecture, int courseId) {
//        this.local_id=id;
//        this.number_lectureList = number;
//        this.title_lectureList = title_lecture;
//        this.typeLecture_lectureList=typeLecture;
//        this.courseId=courseId;
//    }

    public int getLocal_id() {
        return local_id;
    }

    public void setLocal_id(int local_id) {
        this.local_id = local_id;
    }

    public String getServer_id() {
        return server_id;
    }

    public void setServer_id(String server_id) {
        this.server_id = server_id;
    }

    public String getNumber_lectureList() {
        return number_lectureList;
    }

    public void setNumber_lectureList(String number_lectureList) {
        this.number_lectureList = number_lectureList;
    }

    public String getTitle_lectureList() {
        return title_lectureList;
    }

    public void setTitle_lectureList(String title_lectureList) {
        this.title_lectureList = title_lectureList;
    }

    public String getTypeMedia_lectureList() {
        return typeMedia_lectureList;
    }

    public void setTypeMedia_lectureList(String typeMedia_lectureList) {
        this.typeMedia_lectureList = typeMedia_lectureList;
    }

    public String getLduration_lectureList() {
        return Lduration_lectureList;
    }

    public void setLduration_lectureList(String lduration_lectureList) {
        Lduration_lectureList = lduration_lectureList;
    }

    public boolean isHasPreview_lectureList() {
        return hasPreview_lectureList;
    }

    public void setHasPreview_lectureList(boolean hasPreview_lectureList) {
        this.hasPreview_lectureList = hasPreview_lectureList;
    }

    public boolean isHasCompleted_lectureList() {
        return hasCompleted_lectureList;
    }

    public void setHasCompleted_lectureList(boolean hasCompleted_lectureList) {
        this.hasCompleted_lectureList = hasCompleted_lectureList;
    }

    public String getUrlPath_lectureList() {
        return urlPath_lectureList;
    }

    public void setUrlPath_lectureList(String urlPath_lectureList) {
        this.urlPath_lectureList = urlPath_lectureList;
    }

    public int getDownloadStatus_lectureList() {
        return downloadStatus_lectureList;
    }

    public void setDownloadStatus_lectureList(int downloadStatus_lectureList) {
        this.downloadStatus_lectureList = downloadStatus_lectureList;
    }

    public int getProgressDownload_lectureList() {
        return progressDownload_lectureList;
    }

    public void setProgressDownload_lectureList(int progressDownload_lectureList) {
        this.progressDownload_lectureList = progressDownload_lectureList;
    }

    public int getIndex_dataSource_lectureList() {
        return index_dataSource_lectureList;
    }

    public void setIndex_dataSource_lectureList(int index_dataSource_lectureList) {
        this.index_dataSource_lectureList = index_dataSource_lectureList;
    }

//    public boolean isSelected_lectureList() {
//        return selected_lectureList;
//    }
//
//    public void setSelected_lectureList(boolean selected_lectureList) {
//        this.selected_lectureList = selected_lectureList;
//    }

    public int getTypeLecture_lectureList() {
        return typeLecture_lectureList;
    }

    public void setTypeLecture_lectureList(int typeLecture_lectureList) {
        this.typeLecture_lectureList = typeLecture_lectureList;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getNumberSection_lectureList() {
        return numberSection_lectureList;
    }

    public void setNumberSection_lectureList(int numberSection_lectureList) {
        this.numberSection_lectureList = numberSection_lectureList;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getName_section() {
        return name_section;
    }

    public void setName_section(String name_section) {
        this.name_section = name_section;
    }

    public boolean isPlaying() {
        return Playing;
    }

    public void setPlaying(boolean playing) {
        Playing = playing;
    }

    public Date getLastRefresh() {
        return lastRefresh;
    }

    public void setLastRefresh(Date lastRefresh) {
        this.lastRefresh = lastRefresh;
    }

    public static Bitmap getBitmapFromURL(String src, Context context) {
//        try {
//            URL url = new URL(src);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setDoInput(true);
//            connection.connect();
//            InputStream input = connection.getInputStream();
//            Bitmap myBitmap = BitmapFactory.decodeStream(input);
//            return myBitmap;
//        } catch (IOException e) {
//            // Log exception
//            return BitmapFactory.decodeResource(context.getResources(), R.drawable.img_itm_1_start_viewpager);
//        }

        return ((BitmapDrawable) context.getResources().getDrawable(R.drawable.avator_placeholder)).getBitmap();
    }

    public static MediaDescriptionCompat getMediaDescription(Context context,LectureList lectureList,String urlImage) {
        Bundle extras = new Bundle();
        Bitmap bitmap = getBitmapFromURL(urlImage,context);
        extras.putParcelable(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, bitmap);
        extras.putParcelable(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON, bitmap);
        return new MediaDescriptionCompat.Builder()
                .setMediaId(lectureList.getServer_id())
                .setIconBitmap(bitmap)
                .setTitle(lectureList.getTitle_lectureList())
                .setDescription("")
                .setExtras(extras)
                .build();
    }

    @Override
    public String toString() {
        return "LectureList{" +
                "local_id=" + local_id +
                ", title_lectureList='" + title_lectureList + '\'' +
                ", typeMedia_lectureList='" + typeMedia_lectureList + '\'' +
                ", hasCompleted_lectureList=" + hasCompleted_lectureList +
                ", typeLecture_lectureList=" + typeLecture_lectureList +
                ", lastRefresh=" + lastRefresh +
                '}';
    }
}
