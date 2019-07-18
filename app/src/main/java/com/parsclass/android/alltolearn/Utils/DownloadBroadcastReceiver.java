package com.parsclass.android.alltolearn.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.parsclass.android.alltolearn.services.Download;

import static com.parsclass.android.alltolearn.Utils.ConstantUtil.MESSAGE_PROGRESS;

public class DownloadBroadcastReceiver extends BroadcastReceiver {

    public static DownloadReceiverListener listener;
    public static final String TAG="DownloadService";

    public DownloadBroadcastReceiver() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equals(MESSAGE_PROGRESS)){

            Download download = intent.getParcelableExtra("download");
            int progress;
            if(download.isFailDownload()) {
                progress = -1000;
                Log.e(TAG,"in faial donwold");
            }
            else {
                progress = download.getProgress();
                Log.e(TAG,"in succes donwold");
            }
            String downloadId=download.getDownloadId();
            Log.e(TAG,"DoLisprogress: "+progress);
            if (listener != null) {
                listener.onProgressChanged(progress, downloadId);

                if (progress == 100) {
                    listener.onDownloadCompleted(downloadId);
                }
            }

        }

    }

    public interface DownloadReceiverListener {
        void onProgressChanged(int progress,String downloadId);
        void onDownloadCompleted(String downloadId);

    }
}
