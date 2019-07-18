package com.parsclass.android.alltolearn.Utils;

import android.content.Context;

import com.google.android.exoplayer2.offline.DownloadManager;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.Cache;
import com.google.android.exoplayer2.upstream.cache.NoOpCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;
import com.google.android.exoplayer2.util.Util;
import com.parsclass.android.alltolearn.R;

import java.io.File;

public class DownloadUtil {
    private static Cache cache;
    private static DownloadManager downloadManager;

    public static synchronized Cache getCache(Context context) {
        if (cache == null) {
            File cacheDirectory = new File(context.getExternalFilesDir(null), "downloads");
            cache = new SimpleCache(cacheDirectory, new NoOpCacheEvictor());
        }
        return cache;
    }

//    public static synchronized DownloadManager getDownloadManager(Context context) {
//        if (downloadManager == null) {
//            File actionFile = new File(context.getExternalCacheDir(), "actions");
//            downloadManager =
//                    new DownloadManager(
//                            getCache(context),
//                            new DefaultDataSourceFactory(
//                                    context,
//                                    Util.getUserAgent(context, context.getString(R.string.app_name))),
//                            actionFile
//                        );
//        }
//        return downloadManager;
//    }
}
