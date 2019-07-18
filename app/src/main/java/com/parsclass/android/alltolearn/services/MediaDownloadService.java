package com.parsclass.android.alltolearn.services;

import android.app.Notification;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.android.exoplayer2.offline.Download;
import com.google.android.exoplayer2.offline.DownloadManager;
import com.google.android.exoplayer2.offline.DownloadService;
import com.google.android.exoplayer2.scheduler.PlatformScheduler;
import com.google.android.exoplayer2.scheduler.Scheduler;
import com.google.android.exoplayer2.ui.DownloadNotificationHelper;
import com.google.android.exoplayer2.util.NotificationUtil;
import com.google.android.exoplayer2.util.Util;
import com.parsclass.android.alltolearn.R;
import com.parsclass.android.alltolearn.config.MyApplication;

import java.util.List;

import static com.parsclass.android.alltolearn.Utils.ConstantUtil.DOWNLOAD_CHANNEL_ID;
import static com.parsclass.android.alltolearn.Utils.ConstantUtil.DOWNLOAD_NOTIFICATION_ID;


public class MediaDownloadService extends DownloadService {

    public static String TAG="MediaDownloadService";

    private DownloadNotificationHelper notificationHelper;
    private static final int JOB_ID = 1;
    private static final int FOREGROUND_NOTIFICATION_ID = 1;
    private static int nextNotificationId = FOREGROUND_NOTIFICATION_ID + 1;
    public MediaDownloadService() {
        super(
                DOWNLOAD_NOTIFICATION_ID,
                DEFAULT_FOREGROUND_NOTIFICATION_UPDATE_INTERVAL,
                DOWNLOAD_CHANNEL_ID,
                R.string.download_channel_name);
        nextNotificationId = FOREGROUND_NOTIFICATION_ID + 1;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        notificationHelper = new DownloadNotificationHelper(this, DOWNLOAD_CHANNEL_ID);
    }

    @Override
    protected DownloadManager getDownloadManager() {
        return MyApplication.getInstance().getDownloadManager();
    }

    @Nullable
    @Override
    protected Scheduler getScheduler() {
        return Util.SDK_INT >= 21 ? new PlatformScheduler(this, JOB_ID) : null;
    }

    @Override
    protected Notification getForegroundNotification(List<Download> downloads) {
        return notificationHelper.buildProgressNotification(
                R.drawable.ic_download, /* contentIntent= */ null, /* message= */ null, downloads);
    }

    @Override
    protected void onDownloadChanged(Download download) {
        Notification notification;
        Log.e(TAG,"progrss servie= "+download.getPercentDownloaded());
        if (download.state == Download.STATE_COMPLETED) {
            notification =
                    notificationHelper.buildDownloadCompletedNotification(
                            android.R.drawable.stat_sys_download_done,
                            /* contentIntent= */ null,
                            Util.fromUtf8Bytes(download.request.data));
            Log.e(TAG,"STATE_COMPLETED");
        } else if (download.state == Download.STATE_FAILED) {
            notification =
                    notificationHelper.buildDownloadFailedNotification(
                            android.R.drawable.stat_sys_download_done,
                            /* contentIntent= */ null,
                            Util.fromUtf8Bytes(download.request.data));
        } else {
            return;
        }
        NotificationUtil.setNotification(this, nextNotificationId++, notification);
    }
//    @Override
//    protected Notification getForegroundNotification(TaskState[] taskStates) {
//
//        return DownloadNotificationUtil.buildProgressNotification(
//                this,
//                R.drawable.exo_icon_play,
//                DOWNLOAD_CHANNEL_ID,
//                null,
//                "downloadingg",
//                taskStates);
//    }
//
//    private void sendIntent(int progress){
//        Intent intent = new Intent(ConstantUtil.MESSAGE_PROGRESS);
//        intent.putExtra("progress",progress);
//        LocalBroadcastManager.getInstance(MediaDownloadService.this).sendBroadcast(intent);
//    }
//
//    @Override
//    protected void onTaskStateChanged(TaskState taskState) {
//        if (taskState.action.isRemoveAction) {
//            return;
//        }
//
//        Notification notification = null;
//        if (taskState.state == TaskState.STATE_COMPLETED) {
//            Log.e(TAG,"STATE_COMPLETED");
//            notification =
//                    DownloadNotificationUtil.buildDownloadCompletedNotification(
//                            /* context= */ this,
//                            R.drawable.exo_controls_play,
//                            DOWNLOAD_CHANNEL_ID,
//                            /* contentIntent= */ null,
//                            Util.fromUtf8Bytes(taskState.action.data));
//        } else if (taskState.state == TaskState.STATE_FAILED) {
//            Log.e(TAG,"STATE_FAILED");
//            notification =
//                    DownloadNotificationUtil.buildDownloadFailedNotification(
//                            /* context= */ this,
//                            R.drawable.exo_controls_play,
//                            DOWNLOAD_CHANNEL_ID,
//                            /* contentIntent= */ null,
//                            Util.fromUtf8Bytes(taskState.action.data));
//        }
//        int notificationId = FOREGROUND_NOTIFICATION_ID + 1 + taskState.taskId;
//        NotificationUtil.setNotification(this, notificationId, notification);
//    }
}
