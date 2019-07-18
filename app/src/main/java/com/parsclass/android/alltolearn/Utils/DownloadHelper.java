package com.parsclass.android.alltolearn.Utils;

import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;

public class DownloadHelper {
    private Uri uri;
    private long downloadId;
    private String filename;
    private Context context;
    private String path;


    public DownloadHelper(String filename, Context context, String path) {
        this.filename = filename;
        this.context = context;
        this.path = path;
    }

    public void startDownload(){
        uri=Uri.parse(path);
        DownloadManager.Request request=new DownloadManager.Request(uri);
        request.setTitle("Download Video");
        request.setDescription("downloading...");
        request.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS,filename);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        DownloadManager downloadManager=(DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);
        downloadId=downloadManager.enqueue(request);


    }

    public int getDownloadStatus(){
        DownloadManager.Query query=new DownloadManager.Query();
        query.setFilterById(downloadId);
        DownloadManager downloadManager=(DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);
        Cursor cursor=downloadManager.query(query);
        if(cursor.moveToFirst()){
            int columnInded=cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
            int status=cursor.getInt(columnInded);

            return status;
        }
        return DownloadManager.ERROR_UNKNOWN;
    }

    public int getPercentageDownload(){
        DownloadManager.Query query=new DownloadManager.Query();
        query.setFilterById(downloadId);
        int progress = 0;
        DownloadManager downloadManager=(DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);
        Cursor cursor=downloadManager.query(query);
        if (cursor.moveToFirst()) {
            int sizeIndex = cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);
            int downloadedIndex = cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);
            long size = cursor.getInt(sizeIndex);
            long downloaded =cursor.getInt(downloadedIndex);

            if (size != -1)
                progress =(int) (downloaded*100/size);
            return progress;
            // At this point you have the progress as a percentage.
        }
        return progress;
    }

    public void cancelDownload(){
        DownloadManager downloadManager=(DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);
        downloadManager.remove(downloadId);
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public long getDownloadId() {
        return downloadId;
    }

    public void setDownloadId(long downloadId) {
        this.downloadId = downloadId;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
