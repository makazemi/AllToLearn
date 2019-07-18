package com.parsclass.android.alltolearn.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;

import android.view.View;
import android.widget.Toast;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.offline.Download;
import com.google.android.exoplayer2.offline.DownloadCursor;
import com.google.android.exoplayer2.offline.DownloadHelper;
import com.google.android.exoplayer2.offline.DownloadIndex;
import com.google.android.exoplayer2.offline.DownloadManager;
import com.google.android.exoplayer2.offline.DownloadRequest;
import com.google.android.exoplayer2.offline.DownloadService;
import com.google.android.exoplayer2.offline.StreamKey;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.Util;
import com.parsclass.android.alltolearn.R;
import com.parsclass.android.alltolearn.config.MyApplication;
import com.parsclass.android.alltolearn.services.MediaDownloadService;
import com.parsclass.android.alltolearn.view.StartActivity;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;


public class DownloadTracker implements DownloadManager.Listener {

    /** Listens for changes in the tracked downloads. */
    public interface Listener {

        /** Called when the tracked downloads changed. */
        void onDownloadsChanged(DownloadManager downloadManager,Download download);
    }

    private static final String TAG = "DownloadTracker";

    private final Context context;
    private final DataSource.Factory dataSourceFactory;
    private final CopyOnWriteArraySet<Listener> listeners;
    private final HashMap<Uri, Download> downloads;
    private final DownloadIndex downloadIndex;
    @Nullable
    private StartDownloadDialogHelper startDownloadDialogHelper;


    public DownloadTracker(
            Context context,
            DataSource.Factory dataSourceFactory,
            DownloadManager downloadManager) {
        this.context = context.getApplicationContext();
        this.dataSourceFactory = dataSourceFactory;
        listeners = new CopyOnWriteArraySet<>();
        downloads = new HashMap<>();
        downloadIndex = downloadManager.getDownloadIndex();
        downloadManager.addListener(new DownloadManagerListener());
        loadDownloads();
    }



    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    public void removeListener(Listener listener) {
        listeners.remove(listener);
    }


    public  boolean isDownloaded(Uri uri) {
        Download download = downloads.get(uri);
        return download != null && download.state != Download.STATE_FAILED;
    }

    @SuppressWarnings("unchecked")
    public List<StreamKey> getOfflineStreamKeys(Uri uri) {
        Download download = downloads.get(uri);
        return download != null && download.state != Download.STATE_FAILED
                ? download.request.streamKeys
                : Collections.emptyList();
    }

    public void toggleDownload(
            FragmentManager fragmentManager,
            String name,
            Uri uri,
            String extension,
            RenderersFactory renderersFactory,
            Activity activity) {
        Download download = downloads.get(uri);
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(activity);
        builder.setCancelable(true);
        builder.setMessage(context.getString(R.string.are_you_sure_remove_download_media));
        builder.setTitle(context.getString(R.string.remove_download));
        builder.setPositiveButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (download != null) {
                    DownloadService.sendRemoveDownload(
                            context, MediaDownloadService.class, download.request.id, /* foreground= */ false);
                }
                dialogInterface.dismiss();
                return;
            }
        });
        builder.setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                return;
            }
        });
        final AlertDialog dialog = builder.create();
        if(isDownloaded(uri)){
            Log.e(TAG,"isDownload");
            dialog.show();
            return;
            //Toast.makeText(context, context.getString(R.string.is_downloaded_media), Toast.LENGTH_SHORT).show();
            //return;
        }
        if (download != null) {
            DownloadService.sendRemoveDownload(
                    context, MediaDownloadService.class, download.request.id, /* foreground= */ false);
        } else {
            if (startDownloadDialogHelper != null) {
                startDownloadDialogHelper.release();
            }
            startDownloadDialogHelper =
                    new StartDownloadDialogHelper(
                            fragmentManager, getDownloadHelper(uri, extension, renderersFactory), name,activity);
        }
    }

    private void loadDownloads() {
        try (DownloadCursor loadedDownloads = downloadIndex.getDownloads()) {
            while (loadedDownloads.moveToNext()) {
                Download download = loadedDownloads.getDownload();
                downloads.put(download.request.uri, download);
            }
        } catch (IOException e) {
            Log.w(TAG, "Failed to query downloads", e);
        }
    }

    private DownloadHelper getDownloadHelper(
            Uri uri, String extension, RenderersFactory renderersFactory) {
        int type = Util.inferContentType(uri, extension);
        switch (type) {
            case C.TYPE_DASH:
                return DownloadHelper.forDash(uri, dataSourceFactory, renderersFactory);
            case C.TYPE_SS:
                return DownloadHelper.forSmoothStreaming(uri, dataSourceFactory, renderersFactory);
            case C.TYPE_HLS:
                return DownloadHelper.forHls(uri, dataSourceFactory, renderersFactory);
            case C.TYPE_OTHER:
                Log.e(TAG,"forProgressive");
                return DownloadHelper.forProgressive(uri);
            default:
                throw new IllegalStateException("Unsupported type: " + type);
        }
    }

    private class DownloadManagerListener implements DownloadManager.Listener {

        @Override
        public void onDownloadChanged(DownloadManager downloadManager, Download download) {
            downloads.put(download.request.uri, download);
            for (Listener listener : listeners) {
                listener.onDownloadsChanged(downloadManager,download);
            }
        }

        @Override
        public void onDownloadRemoved(DownloadManager downloadManager, Download download) {
            downloads.remove(download.request.uri);
            for (Listener listener : listeners) {
                listener.onDownloadsChanged(downloadManager,download);

            }
        }
    }

    private final class StartDownloadDialogHelper
            implements DownloadHelper.Callback {

        private final FragmentManager fragmentManager;
        private final DownloadHelper downloadHelper;
        private final String name;

       // private TrackSelectionDialog trackSelectionDialog;
       // private MappingTrackSelector.MappedTrackInfo mappedTrackInfo;

        private Activity activity;
        private  AlertDialog.Builder builder;

        public StartDownloadDialogHelper(
                FragmentManager fragmentManager, DownloadHelper downloadHelper, String name,Activity activity) {
            this.fragmentManager = fragmentManager;
            this.downloadHelper = downloadHelper;
            this.name = name;
            downloadHelper.prepare(this);
            this.activity=activity;
        }

        public void release() {
            downloadHelper.release();
//            if (trackSelectionDialog != null) {
//                trackSelectionDialog.dismiss();
//            }
        }

        // DownloadHelper.Callback implementation.

        @Override
        public void onPrepared(DownloadHelper helper) {

            builder = new AlertDialog.Builder(activity);
            builder.setCancelable(true);
            builder.setMessage(context.getString(R.string.are_you_sure_download_media));
            builder.setTitle(context.getString(R.string.exo_download_description));
            builder.setPositiveButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    startDownload();
                    downloadHelper.release();
                    dialogInterface.dismiss();
                }
            });
            builder.setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    downloadHelper.release();
                    dialogInterface.dismiss();
                    return;
                }
            });
            final AlertDialog dialog = builder.create();
            dialog.show();

//            if (helper.getPeriodCount() == 0) {
//                Log.d(TAG, "No periods found. Downloading entire stream.");
//                startDownload();
//                downloadHelper.release();
//                return;
//            }
           // mappedTrackInfo = downloadHelper.getMappedTrackInfo(/* periodIndex= */ 0);
//            if (!TrackSelectionDialog.willHaveContent(mappedTrackInfo)) {
//                Log.d(TAG, "No dialog content. Downloading entire stream.");
//                startDownload();
//                downloadHelper.release();
//                return;
//            }
//            trackSelectionDialog =
//                    TrackSelectionDialog.createForMappedTrackInfoAndParameters(
//                            /* titleId= */ R.string.exo_download_description,
//                            mappedTrackInfo,
//                            /* initialParameters= */ DownloadHelper.DEFAULT_TRACK_SELECTOR_PARAMETERS,
//                            /* allowAdaptiveSelections =*/ false,
//                            /* allowMultipleOverrides= */ true,
//                            /* onClickListener= */ this,
//                            /* onDismissListener= */ this);
            //trackSelectionDialog.show(fragmentManager, /* tag= */ null);
        }

        @Override
        public void onPrepareError(DownloadHelper helper, IOException e) {
            Toast.makeText(
                    context.getApplicationContext(), R.string.download_start_error, Toast.LENGTH_LONG)
                    .show();
            Log.e(TAG, "Failed to start download", e);
        }

        // DialogInterface.OnClickListener implementation.

//        @Override
//        public void onClick(DialogInterface dialog, int which) {
//            for (int periodIndex = 0; periodIndex < downloadHelper.getPeriodCount(); periodIndex++) {
//                downloadHelper.clearTrackSelections(periodIndex);
//                for (int i = 0; i < mappedTrackInfo.getRendererCount(); i++) {
//                    if (!trackSelectionDialog.getIsDisabled(/* rendererIndex= */ i)) {
//                        downloadHelper.addTrackSelectionForSingleRenderer(
//                                periodIndex,
//                                /* rendererIndex= */ i,
//                                DownloadHelper.DEFAULT_TRACK_SELECTOR_PARAMETERS,
//                                trackSelectionDialog.getOverrides(/* rendererIndex= */ i));
//                    }
//                }
//            }
//            DownloadRequest downloadRequest = buildDownloadRequest();
//            if (downloadRequest.streamKeys.isEmpty()) {
//                // All tracks were deselected in the dialog. Don't start the download.
//                return;
//            }
//            startDownload(downloadRequest);
//        }
//
//        // DialogInterface.OnDismissListener implementation.
//
//        @Override
//        public void onDismiss(DialogInterface dialogInterface) {
//            trackSelectionDialog = null;
//            downloadHelper.release();
//        }

        // Internal methods.

        private void startDownload() {
            startDownload(buildDownloadRequest());
        }

        private void startDownload(DownloadRequest downloadRequest) {
            DownloadService.sendAddDownload(
                    context, MediaDownloadService.class, downloadRequest, /* foreground= */ false);
        }

        private DownloadRequest buildDownloadRequest() {
            return downloadHelper.getDownloadRequest(Util.getUtf8Bytes(name));
        }
    }



//
//    private final class StartDownloadDialogHelper
//            implements DownloadHelper.Callback, DialogInterface.OnClickListener {
//
//        private final DownloadHelper downloadHelper;
//        private final String name;
//
//        private final AlertDialog.Builder builder;
//        private final View dialogView;
//        private final List<TrackKey> trackKeys;
//        private final ArrayAdapter<String> trackTitles;
//        private final ListView representationList;
//        private int taskId;
//
//        public StartDownloadDialogHelper(
//                Activity activity, DownloadHelper downloadHelper, String name) {
//            this.downloadHelper = downloadHelper;
//            this.name = name;
//            builder =
//                    new AlertDialog.Builder(activity)
//                            .setTitle(R.string.exo_download_description)
//                            .setPositiveButton(R.string.ok, this)
//                            .setNegativeButton(R.string.cancel, null);
//
//            // Inflate with the builder's context to ensure the correct style is used.
//            LayoutInflater dialogInflater = LayoutInflater.from(builder.getContext());
//            dialogView = dialogInflater.inflate(R.layout.start_download_dialog, null);
//
//            trackKeys = new ArrayList<>();
//            trackTitles =
//                    new ArrayAdapter<>(
//                            builder.getContext(), android.R.layout.simple_list_item_multiple_choice);
//            representationList = dialogView.findViewById(R.id.representation_list);
//            representationList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
//            representationList.setAdapter(trackTitles);
//        }
//
//        public void prepare() {
//            downloadHelper.prepare(this);
//
//        }
//
//        @Override
//        public void onPrepared(DownloadHelper helper) {
//
//            for (int i = 0; i < downloadHelper.getPeriodCount(); i++) {
//                TrackGroupArray trackGroups = downloadHelper.getTrackGroups(i);
//                for (int j = 0; j < trackGroups.length; j++) {
//                    TrackGroup trackGroup = trackGroups.get(j);
//                    for (int k = 0; k < trackGroup.length; k++) {
//                        trackKeys.add(new TrackKey(i, j, k));
//                        trackTitles.add(trackNameProvider.getTrackName(trackGroup.getFormat(k)));
//                    }
//                }
//            }
//            if (!trackKeys.isEmpty()) {
//                builder.setView(dialogView);
//            }
//            builder.create().show();
//        }
//
//        @Override
//        public void onPrepareError(DownloadHelper helper, IOException e) {
//            Toast.makeText(
//                    context.getApplicationContext(), R.string.download_start_error, Toast.LENGTH_LONG)
//                    .show();
//            Log.e(TAG, "Failed to start download", e);
//        }
//
//        @Override
//        public void onClick(DialogInterface dialog, int which) {
//            ArrayList<TrackKey> selectedTrackKeys = new ArrayList<>();
//            for (int i = 0; i < representationList.getChildCount(); i++) {
//                if (representationList.isItemChecked(i)) {
//                    selectedTrackKeys.add(trackKeys.get(i));
//                }
//            }
//            if (!selectedTrackKeys.isEmpty() || trackKeys.isEmpty()) {
//                // We have selected keys, or we're dealing with single stream content.
//                DownloadAction downloadAction =
//                        downloadHelper.getDownloadAction(Util.getUtf8Bytes(name), selectedTrackKeys);
//                taskId=MyApplication.getInstance().getDownloadManager().handleAction(downloadAction);
//
//                startDownload(downloadAction);
//            }
//        }
//
//        public int getTaskId(){
//            return this.taskId;
//        }
//
//    }
}