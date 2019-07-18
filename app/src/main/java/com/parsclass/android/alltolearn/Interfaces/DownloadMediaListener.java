package com.parsclass.android.alltolearn.Interfaces;

import com.google.android.exoplayer2.offline.Download;
import com.google.android.exoplayer2.offline.DownloadManager;

public interface DownloadMediaListener {

    void onDownloadMediaChanged(DownloadManager downloadManager, Download download,int index);
}
