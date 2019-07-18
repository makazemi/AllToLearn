package com.parsclass.android.alltolearn.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.session.MediaSessionCompat;

import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector;
import com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerNotificationManager;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.Util;
import com.parsclass.android.alltolearn.R;
import com.parsclass.android.alltolearn.Utils.GlideApp;
import com.parsclass.android.alltolearn.Utils.MapPosition;
import com.parsclass.android.alltolearn.config.MyApplication;
import com.parsclass.android.alltolearn.model.Course;
import com.parsclass.android.alltolearn.model.LectureList;
import com.parsclass.android.alltolearn.view.DetailMyCourseActivity;

import java.util.ArrayList;
import java.util.List;

import static com.parsclass.android.alltolearn.Utils.ConstantUtil.MEDIA_SESSION_TAG;
import static com.parsclass.android.alltolearn.Utils.ConstantUtil.PLAYBACK_CHANNEL_ID;
import static com.parsclass.android.alltolearn.Utils.ConstantUtil.PLAYBACK_NOTIFICATION_ID;
import static com.parsclass.android.alltolearn.Utils.ConstantUtil.TYPE_ARTICLE;
import static com.parsclass.android.alltolearn.Utils.ConstantUtil.TYPE_SECTION;
import static com.parsclass.android.alltolearn.base.BaseExoWithServiceActivity.KEY_AUTO_PLAY;
import static com.parsclass.android.alltolearn.base.BaseExoWithServiceActivity.KEY_IMAGE_COURSE;
import static com.parsclass.android.alltolearn.base.BaseExoWithServiceActivity.KEY_MODELS;
import static com.parsclass.android.alltolearn.base.BaseExoWithServiceActivity.KEY_OPEN_FROM_NOTIFICATION;
import static com.parsclass.android.alltolearn.base.BaseExoWithServiceActivity.KEY_POSITION;
import static com.parsclass.android.alltolearn.base.BaseExoWithServiceActivity.KEY_URIS;
import static com.parsclass.android.alltolearn.base.BaseExoWithServiceActivity.KEY_WINDOW;
import static com.parsclass.android.alltolearn.base.BaseExoWithServiceActivity.STATE_CURRENT_POSITION;
import static com.parsclass.android.alltolearn.base.BaseExoWithServiceActivity.STATE_CURRENT_WINDOW;
import static com.parsclass.android.alltolearn.view.HomeFragment.KEY_DETAIL_COURSE_ACTIVITY;

public class PlayerService extends Service {


    public static final String TAG = "PlayerService";
    public static final String KEY_SEND_BROADCAST = "KEY_SEND_BROADCAST";
    public static final String KEY_LOCATION_BROADCAST = "KEY_LOCATION_BROADCAST";
    public static final String KEY_LOCATION_RELEASE_PLAYER = "releasePlayer";
    public static final String KEY_LOCATION_INITIALIZE_PLAYER = "initializePlayer";
    public static final String ACTION_SHOW_NOTIFICATION = "ACTION_SHOW_NOTIFICATION";

    public SimpleExoPlayer player;
    private PlayerNotificationManager playerNotificationManager;
    private MediaSessionCompat mediaSession;
    private MediaSessionConnector mediaSessionConnector;
    private DefaultTrackSelector trackSelector;
    private DefaultTrackSelector.Parameters trackSelectorParameters;
    private TrackGroupArray lastSeenTrackGroupArray;
    private CacheDataSourceFactory cacheDataSourceFactory;
    private ConcatenatingMediaSource concatenatingMediaSource;

    private long startPosition;
    private int startWindow;
    private int currentWindow;
    private long currentPosition;
    private boolean startAutoPlay;
    private ArrayList<Uri> uris = new ArrayList<>();
    private List<LectureList> lectureLists = new ArrayList<>();
    private String imagePathCourse;
    private Course currentCourse;
    private Context context;
    private Bitmap bitmap;


    @Override
    public void onCreate() {
        super.onCreate();

        Log.e(TAG,"service create");
        this.context = this;
        bitmap=((BitmapDrawable) context.getResources().getDrawable(R.drawable.avator_placeholder)).getBitmap();

        //initializePlayer();

        trackSelector = new DefaultTrackSelector();
        trackSelectorParameters = trackSelector.getParameters();
        trackSelector.setParameters(trackSelectorParameters);
        lastSeenTrackGroupArray = null;
        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector);

        //player.setPlayWhenReady(true);
        mediaSession = new MediaSessionCompat(this, MEDIA_SESSION_TAG);
        mediaSession.setActive(true);

        mediaSessionConnector = new MediaSessionConnector(mediaSession);
        mediaSessionConnector.setQueueNavigator(new TimelineQueueNavigator(mediaSession) {
            @Override
            public MediaDescriptionCompat getMediaDescription(Player player, int windowIndex) {
                return LectureList.getMediaDescription(context, lectureLists.get(windowIndex), imagePathCourse);
            }
        });
        mediaSessionConnector.setPlayer(player);

        displayNotification();
    }

    @Override
    public IBinder onBind(Intent intent) {
        if (intent != null) {

            startAutoPlay = intent.getBooleanExtra(KEY_AUTO_PLAY, true);
            startPosition = intent.getLongExtra(KEY_POSITION, 0);
            startWindow = intent.getIntExtra(KEY_WINDOW, 0);
            currentWindow = intent.getIntExtra(STATE_CURRENT_WINDOW, 0);
            currentPosition = intent.getLongExtra(STATE_CURRENT_POSITION, 0);
            uris.addAll((ArrayList<Uri>) intent.getSerializableExtra(KEY_URIS));
            lectureLists.addAll((List<LectureList>) intent.getSerializableExtra(KEY_MODELS));
            imagePathCourse = intent.getStringExtra(KEY_IMAGE_COURSE);
            currentCourse = (Course) intent.getSerializableExtra(KEY_DETAIL_COURSE_ACTIVITY);
            initializePlayer();
            //displayNotification();

        }

        return (IBinder) (new VideoServiceBinder());
    }

    public ConcatenatingMediaSource buildMediaSource() {
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
                Util.getUserAgent(this, getString(R.string.app_name)));

        cacheDataSourceFactory = MyApplication.getInstance().buildDataSourceFactory();
        concatenatingMediaSource = new ConcatenatingMediaSource();
        for (int i = 0; i < uris.size(); i++) {
            MediaSource mediaSource = new  ProgressiveMediaSource.Factory(cacheDataSourceFactory).
                    createMediaSource(uris.get(i));
            concatenatingMediaSource.addMediaSource(mediaSource);
        }


        return concatenatingMediaSource;
    }

    private void sendIntent(String location) {

        Intent intent = new Intent(KEY_SEND_BROADCAST);
        intent.putExtra(KEY_LOCATION_BROADCAST, location);
        sendBroadcast(intent);
        //LocalBroadcastManager.getInstance(PlayerService.this).sendBroadcast(intent);

    }


    private void initializePlayer() {
        if (player == null) {

        }

        player.seekTo(currentWindow, currentPosition);
        player.setPlayWhenReady(startAutoPlay);
        ConcatenatingMediaSource videoSource = buildMediaSource();// data from intent
        boolean haveStartPosition = startWindow != com.google.android.exoplayer2.C.INDEX_UNSET;
        if (haveStartPosition) {

            player.seekTo(startWindow, startPosition);
        }

        player.prepare(videoSource, !haveStartPosition, true);
          player.setPlayWhenReady(true);

    }

    public final class VideoServiceBinder extends Binder {


        public final SimpleExoPlayer getExoPlayerInstance() {
            return player;
        }

        public final DefaultTrackSelector getTrackSelector() {
            return trackSelector;
        }

        public final DefaultTrackSelector.Parameters getTrackSelectorParameters() {
            return trackSelectorParameters;
        }

        public final TrackGroupArray getLastSeenTrackGroupArray() {
            return lastSeenTrackGroupArray;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent != null) {
            int action = intent.getIntExtra(ACTION_SHOW_NOTIFICATION, -1);
            switch (action) {
                case 0:
                    Log.e(TAG, "case 0");
                   // displayNotification();
                    stopForeground(false);
                    break;
                case 1:
                    Log.e(TAG, "case 1");
                    stopForeground(true);
                    break;
                case 2:
                    Log.e(TAG,"stop case");
                   // displayNotification();
                   // playerNotificationManager.setPlayer(null);
                    //releasePlayer();
                    break;
                case 3:
                    Log.e(TAG,"case 3");
                    stopForeground(false);
                    break;
                default:
                    Log.e(TAG, "default");
                    if(player!=null && playerNotificationManager!=null)
                        playerNotificationManager.setPlayer(player);
                    break;

            }
        }
        return START_NOT_STICKY;
    }

    private void releasePlayer() {
        if (player != null) {

            sendIntent(KEY_LOCATION_RELEASE_PLAYER);
            player.addVideoListener(null); // Is it necessary to remove these listeners? afraid of memory leak. OOM
            player.release();
            player = null;
            trackSelector = null;

//            mediaSession.release();
            mediaSessionConnector.setPlayer(null);
          //  displayNotification();
            Log.e(TAG,"in releas palter");
            playerNotificationManager.setPlayer(null);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    private void displayNotification() {


        playerNotificationManager=PlayerNotificationManager.createWithNotificationChannel(this,PLAYBACK_CHANNEL_ID,
                R.string.playback_channel_name,PLAYBACK_NOTIFICATION_ID, new PlayerNotificationManager.MediaDescriptionAdapter() {
                    @Override
                    public String getCurrentContentTitle(Player player) {
                        LectureList lecture = lectureLists.get(getPosition(player.getCurrentWindowIndex(), lectureLists));
                        String title = lecture.getTitle_lectureList() + " " + lecture.getNumber_lectureList() + "/" + currentCourse.getCountLectures();
                        return title;
                    }

                    @Nullable
                    @Override
                    public PendingIntent createCurrentContentIntent(Player player) {

                        Intent intent = new Intent(PlayerService.this, DetailMyCourseActivity.class);
                        intent.putExtra(KEY_DETAIL_COURSE_ACTIVITY, currentCourse);
                        intent.putExtra(KEY_OPEN_FROM_NOTIFICATION, 88);
                        return PendingIntent.getActivity(PlayerService.this, 45,
                                intent, PendingIntent.FLAG_UPDATE_CURRENT);

                    }

                    @Nullable
                    @Override
                    public String getCurrentContentText(Player player) {
                        LectureList lecture = lectureLists.get(getPosition(player.getCurrentWindowIndex(), lectureLists));
                        String content = currentCourse.getTitle();
                        if (lecture.getTypeMedia_lectureList().equals(TYPE_ARTICLE)) {
                            playerNotificationManager.setUsePlayPauseActions(false);
                            content += System.lineSeparator() + getString(R.string.txt_non_playable_notification);
                            playerNotificationManager.setUseChronometer(false);

                        } else {
                            playerNotificationManager.setUsePlayPauseActions(true);
                            playerNotificationManager.setUseChronometer(true);
                        }

                        return content;
                    }

                    @Nullable
                    @Override
                    public Bitmap getCurrentLargeIcon(Player player, PlayerNotificationManager.BitmapCallback callback) {

                        GlideApp.with(context)
                                .asBitmap()
                                .override(150, 150)
                                .load(imagePathCourse)
                                .into(new CustomTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                        bitmap = resource;
                                        // android.util.Log.e(TAG,"onResourceReady ");
                                    }

                                    @Override
                                    public void onLoadCleared(@Nullable Drawable placeholder) {
                                        //android.util.Log.e(TAG,"onLoadCleared");
                                    }
                                });
                        return bitmap;
                    }

                },

                new PlayerNotificationManager.NotificationListener() {
                    @Override
                    public void onNotificationCancelled(int notificationId, boolean dismissedByUser) {
                        // if(dismissedByUser) {
                        stopForeground(false);
                        stopSelf();
                        Log.e(TAG,"onNotificationCancelled dismissuser: "+dismissedByUser);

                        //}
                    }

                    @Override
                    public void onNotificationPosted(int notificationId, Notification notification, boolean ongoing) {
                        Log.e(TAG,"onNotificationPosted ongoing: "+ongoing);
                        startForeground(notificationId, notification);


                    }


                });
//        playerNotificationManager =new PlayerNotificationManager(
//                this,
//                PLAYBACK_CHANNEL_ID,
//                PLAYBACK_NOTIFICATION_ID,
//                new PlayerNotificationManager.MediaDescriptionAdapter() {
//                    @Override
//                    public String getCurrentContentTitle(Player player) {
//                        LectureList lecture = lectureLists.get(getPosition(player.getCurrentWindowIndex(), lectureLists));
//                        String title = lecture.getTitle_lectureList() + " " + lecture.getNumber_lectureList() + "/" + currentCourse.getCountLectures();
//                        return title;
//                    }
//
//                    @Nullable
//                    @Override
//                    public PendingIntent createCurrentContentIntent(Player player) {
//
//                        Intent intent = new Intent(PlayerService.this, DetailMyCourseActivity.class);
//                        intent.putExtra(KEY_DETAIL_COURSE_ACTIVITY, currentCourse);
//                        intent.putExtra(KEY_OPEN_FROM_NOTIFICATION, 88);
//                        return PendingIntent.getActivity(PlayerService.this, 45,
//                                intent, PendingIntent.FLAG_UPDATE_CURRENT);
//                    }
//
//                    @Nullable
//                    @Override
//                    public String getCurrentContentText(Player player) {
//                        LectureList lecture = lectureLists.get(getPosition(player.getCurrentWindowIndex(), lectureLists));
//                        String content = currentCourse.getTitle();
//                        if (lecture.getTypeMedia_lectureList().equals(TYPE_ARTICLE)) {
//                            playerNotificationManager.setUsePlayPauseActions(false);
//                            content += System.lineSeparator() + getString(R.string.txt_non_playable_notification);
//                            playerNotificationManager.setUseChronometer(false);
//
//                        } else {
//                            playerNotificationManager.setUsePlayPauseActions(true);
//                            playerNotificationManager.setUseChronometer(true);
//                        }
//
//                        return content;
//                    }
//
//                    @Nullable
//                    @Override
//                    public Bitmap getCurrentLargeIcon(Player player, PlayerNotificationManager.BitmapCallback callback) {
//
//                        GlideApp.with(context)
//                                .asBitmap()
//                                .override(150, 150)
//                                .load(imagePathCourse)
//                                .into(new CustomTarget<Bitmap>() {
//                                    @Override
//                                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
//                                        bitmap = resource;
//                                        // android.util.Log.e(TAG,"onResourceReady ");
//                                    }
//
//                                    @Override
//                                    public void onLoadCleared(@Nullable Drawable placeholder) {
//                                        //android.util.Log.e(TAG,"onLoadCleared");
//                                    }
//                                });
//                        return bitmap;
//                    }
//
//                },
//
//                new PlayerNotificationManager.NotificationListener() {
//                    @Override
//                    public void onNotificationCancelled(int notificationId, boolean dismissedByUser) {
//                       // if(dismissedByUser) {
//                           // stopForeground(false);
//                            //stopSelf();
//                            Log.e(TAG,"onNotificationCancelled dismissuser: "+dismissedByUser);
//
//                        //}
//                    }
//
//                    @Override
//                    public void onNotificationPosted(int notificationId, Notification notification, boolean ongoing) {
//                        Log.e(TAG,"onNotificationPosted ongoing: "+ongoing);
//                        startForeground(notificationId, notification);
//
//                    }
//                }
//        );


        playerNotificationManager.setPlayer(player);
        playerNotificationManager.setFastForwardIncrementMs(0);
        playerNotificationManager.setRewindIncrementMs(0);
        // playerNotificationManager.setOngoing(false);

        playerNotificationManager.setMediaSessionToken(mediaSession.getSessionToken());



    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {

        super.onTaskRemoved(rootIntent);

    }

    public static int getPosition(int currentWindow,List<LectureList> list) {
        ArrayList<Integer> start = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getTypeLecture_lectureList() == TYPE_SECTION) {
                start.add(i);
            }
        }

        MapPosition mapPosition = new MapPosition(start, list.size());
        int position = mapPosition.getPosition(currentWindow);
        return position;
    }
}
