package com.parsclass.android.alltolearn.base;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;

import com.parsclass.android.alltolearn.Utils.ConnectivityReceiver;
import com.parsclass.android.alltolearn.Utils.DownloadTracker;
import com.parsclass.android.alltolearn.Utils.speedPlaybackDialog;

import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.util.Pair;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.analytics.AnalyticsListener;
import com.google.android.exoplayer2.offline.DownloadService;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory;
import com.google.android.exoplayer2.util.ErrorMessageProvider;
import com.google.android.exoplayer2.util.Util;
import com.parsclass.android.alltolearn.R;
import com.parsclass.android.alltolearn.config.MyApplication;
import com.parsclass.android.alltolearn.model.Course;
import com.parsclass.android.alltolearn.model.LectureList;
import com.parsclass.android.alltolearn.services.MediaDownloadService;
import com.parsclass.android.alltolearn.services.PlayerService;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

import javax.security.auth.login.LoginException;

import static com.parsclass.android.alltolearn.Utils.ConstantUtil.ERROR_NO_CONNECTION;
import static com.parsclass.android.alltolearn.Utils.ConstantUtil.ERROR_NO_PLAYABLE;
import static com.parsclass.android.alltolearn.Utils.ConstantUtil.TYPE_ARTICLE;
import static com.parsclass.android.alltolearn.services.PlayerService.ACTION_SHOW_NOTIFICATION;
import static com.parsclass.android.alltolearn.services.PlayerService.KEY_LOCATION_BROADCAST;
import static com.parsclass.android.alltolearn.services.PlayerService.KEY_LOCATION_RELEASE_PLAYER;
import static com.parsclass.android.alltolearn.services.PlayerService.KEY_SEND_BROADCAST;
import static com.parsclass.android.alltolearn.services.PlayerService.getPosition;
import static com.parsclass.android.alltolearn.view.HomeFragment.KEY_DETAIL_COURSE_ACTIVITY;


public abstract class BaseExoWithServiceActivity extends BaseActivity implements View.OnClickListener,
        speedPlaybackDialog.NoticeDialogListener,
        DownloadTracker.Listener,
        ConnectivityReceiver.ConnectivityReceiverListener{

    private static final String TAG = "PlayerActivity";
    public static final String KEY_WINDOW = "window";
    public static final String KEY_POSITION = "position";
    public static final String KEY_AUTO_PLAY = "auto_play";
    public static final String KEY_URIS = "KEY_URIS";
    public static final String KEY_MODELS = "KEY_MODELS";
    public static final String KEY_IMAGE_COURSE = "KEY_IMAGE_COURSE";
    public static final String ACTION_STOP_SERVICE = "ACTION_STOP_SERVICE";
    public static final String KEY_OPEN_FROM_NOTIFICATION = "KEY_OPEN_FROM_NOTIFICATION";
    private static final String STATE_PLAYER_FULLSCREEN = "playerFullscreen";
    public static final String STATE_SPEED_VALUE = "stateSpeedValue";
    public static final String STATE_SPEED_LABEL = "stateSpeedLabel";
    public static final String STATE_CURRENT_WINDOW = "stateCurrentWindow";
    public static final String STATE_CURRENT_POSITION = "stateCurrentPosition";
    private float currentSpeedValue = 1.0f;
    public String currentSpeedLabel = "1.0X";


    protected SimpleExoPlayer player;
    protected PlayerView mPlayerView;
    private DefaultTrackSelector trackSelector;
    private DefaultTrackSelector.Parameters trackSelectorParameters;
    private TrackGroupArray lastSeenTrackGroupArray;

    protected LinearLayout mPlayPauseLayout;
    protected AVLoadingIndicatorView mProgressBar;
    protected TextView txtSpeed;
    protected ImageView imgFullscreen;
    protected LinearLayout mBottomLayout;
    protected Button btnOpenDoc;
    protected ImageView imgBackground;
    protected ConstraintLayout rootLayout;
   // protected RelativeLayout rootLayoutMain;
    protected CoordinatorLayout rootLayoutMain;

    private long startPosition;
    protected int startWindow;
    protected boolean startAutoPlay;
    protected boolean mExoPlayerFullscreen = false;

    protected CacheDataSourceFactory cacheDataSourceFactory;
    protected ConcatenatingMediaSource concatenatingMediaSource;


    private Dialog mFullScreenDialog;
    protected int currentWindow = 0;
    protected long currentPosition = 0;
    boolean isDocument=false;
    boolean isConnected=true;
    boolean isInUpdateNetwork=false;
    boolean windowsChanged=false;
    protected int myReason=1000;
    protected Snackbar snackbar;

    //private boolean playSingle=false;
    private enum playSingle{
        notClick,docClick,
        otherClick;
    }
    playSingle playSingleState=playSingle.notClick;
    private int stateDoc=2000;

    protected DownloadTracker downloadTracker;

    private ConnectivityReceiver connectivityReceiver = new ConnectivityReceiver();

    protected boolean isDestroyed=false;

    protected serviceBroadCastReceiver serviceBroadCastReceiver;
    protected boolean bounded=false;
    protected ServiceConnection connection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            if (service instanceof PlayerService.VideoServiceBinder) {
                player=((PlayerService.VideoServiceBinder) service).getExoPlayerInstance();
                trackSelector=((PlayerService.VideoServiceBinder) service).getTrackSelector();
                trackSelectorParameters=((PlayerService.VideoServiceBinder) service).getTrackSelectorParameters();
                lastSeenTrackGroupArray=((PlayerService.VideoServiceBinder) service).getLastSeenTrackGroupArray();
                mPlayerView.setPlayer(player);
                player.addListener(new BaseExoWithServiceActivity.PlayerEventListener());
                mProgressBar.setVisibility(View.GONE);
                mProgressBar.hide();


            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        registerPlayerBroadcast();

        isDestroyed=false;
        MyApplication.prefHelper.putIsDestroyedPlayerActivity(false);


        setupWidget();

        setupClickListeners();


        if (savedInstanceState != null) {
            startAutoPlay = savedInstanceState.getBoolean(KEY_AUTO_PLAY);
            startWindow = savedInstanceState.getInt(KEY_WINDOW);
            startPosition = savedInstanceState.getLong(KEY_POSITION);
            mExoPlayerFullscreen = savedInstanceState.getBoolean(STATE_PLAYER_FULLSCREEN);
            currentSpeedValue = savedInstanceState.getFloat(STATE_SPEED_VALUE);
            currentSpeedLabel = savedInstanceState.getString(STATE_SPEED_LABEL);
            currentWindow = savedInstanceState.getInt(STATE_CURRENT_WINDOW);
            currentPosition = savedInstanceState.getLong(STATE_CURRENT_POSITION);
        } else {
            trackSelectorParameters = new DefaultTrackSelector.ParametersBuilder().build();
            clearStartPosition();
        }

        MyApplication application = (MyApplication) getApplication();
        downloadTracker = application.getDownloadTracker();

        try {
            DownloadService.start(this, MediaDownloadService.class);
        } catch (IllegalStateException e) {
            DownloadService.startForeground(this, MediaDownloadService.class);
        }


      //  init();

    }

    @Override
    protected void onNewIntent(Intent intent) {
        //releasePlayer();
       // clearStartPosition();
        setIntent(intent);

    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.getInstance().setConnectivityListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        init();
        initFullscreenDialog();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(connectivityReceiver, intentFilter);
        downloadTracker.addListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(connectivityReceiver);

    }

    @Override
    protected void onDestroy() {
        unbindSafely();
        unregisterReceiver(serviceBroadCastReceiver);
        MyApplication.prefHelper.putIsDestroyedPlayerActivity(true);
        isDestroyed=true;
//        Intent intent = new Intent(this, PlayerService.class);
//        intent.putExtra(ACTION_SHOW_NOTIFICATION, 0);
//        startService(intent);
        super.onDestroy();
    }

    private void unbindSafely() {
        if (bounded) {
            unbindService(connection);
            bounded = false;
        }
    }


    private void init(){
        final Intent intent = new Intent(this, PlayerService.class);
        intent.putExtra(KEY_AUTO_PLAY, startAutoPlay);
        intent.putExtra(KEY_POSITION, startPosition);
        intent.putExtra(KEY_WINDOW, startWindow);
        intent.putExtra(KEY_URIS, getUris());
        intent.putExtra(KEY_MODELS,getModels());
        intent.putExtra(KEY_DETAIL_COURSE_ACTIVITY,getCurrentCourse());
        intent.putExtra(KEY_IMAGE_COURSE,getImageCourse());

        Util.startForegroundService(this,intent);
        //startService(intent);
        if(!bounded) {
            bindService(intent, connection, Context.BIND_AUTO_CREATE);
            bounded=true;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        updateTrackSelectorParameters();
        updateStartPosition();
        outState.putBoolean(KEY_AUTO_PLAY, startAutoPlay);
        outState.putInt(KEY_WINDOW, startWindow);
        outState.putLong(KEY_POSITION, startPosition);
        outState.putBoolean(STATE_PLAYER_FULLSCREEN, mExoPlayerFullscreen);
        outState.putFloat(STATE_SPEED_VALUE, currentSpeedValue);
        outState.putString(STATE_SPEED_LABEL, currentSpeedLabel);
        outState.putInt(STATE_CURRENT_WINDOW, currentWindow);
        outState.putLong(STATE_CURRENT_POSITION, currentPosition);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }

    private void initFullscreenDialog() {

        mFullScreenDialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
            public void onBackPressed() {
                if (mExoPlayerFullscreen)
                    closeFullscreenDialog();
                super.onBackPressed();
            }
        };
    }

    private void openFullscreenDialog() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        ((ViewGroup) mPlayerView.getParent()).removeView(mPlayerView);
        mFullScreenDialog.addContentView(mPlayerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        imgFullscreen.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_full_screen_skrink));
        mExoPlayerFullscreen = true;
        mFullScreenDialog.show();
    }


    private void closeFullscreenDialog() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ((ViewGroup) mPlayerView.getParent()).removeView(mPlayerView);
        ((FrameLayout) findViewById(R.id.main_media_frame)).addView(mPlayerView);
        imgFullscreen.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_fullscreen_expand));
        mExoPlayerFullscreen = false;
        mFullScreenDialog.dismiss();

    }

    private void releasePlayer() {
        if (player != null) {

            updateStartPosition();
            player.removeListener(new PlayerEventListener());
            player.addVideoListener(null); // Is it necessary to remove these listeners? afraid of memory leak. OOM
            player.release();
            player = null;
            trackSelector = null;

        }
    }

    private void setupClickListeners() {
        txtSpeed.setOnClickListener(this);
        imgFullscreen.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.exo_speed:
                showNoticeDialog();
                break;
            case R.id.exo_fullscreen_icon:
                if (!mExoPlayerFullscreen) {
                    //setPlayerViewDimensionsForLandScapeMode();
                    openFullscreenDialog();
                    // imgFullscreen.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_full_screen_skrink));
                } else {
                    //setPlayerViewDimensionsForPortraitMode();
                    closeFullscreenDialog();
                    // imgFullscreen.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_fullscreen_expand));
                }
                break;
            default:
                break;
        }
    }


    public abstract ArrayList<Uri> getUris();
    public abstract ArrayList<LectureList> getModels();

    public void setupWidget() {
        mProgressBar = findViewById(R.id.progress_bar);
        mPlayerView = findViewById(R.id.player_view);
        txtSpeed = findViewById(R.id.exo_speed);
        imgFullscreen = findViewById(R.id.exo_fullscreen_icon);
        mPlayPauseLayout = findViewById(R.id.linear_layout_play_pause);
        mBottomLayout = findViewById(R.id.linear_layout_bottom);
        imgBackground=findViewById(R.id.img_background_player);
        rootLayout=findViewById(R.id.root_layout);
        rootLayoutMain=findViewById(R.id.coordinatorLayout);
        mPlayerView.setErrorMessageProvider(new PlayerErrorMessageProvider());
        txtSpeed.setText(currentSpeedLabel);


    }

    public abstract int getLayoutId();

    public void playSingleVideo(int index,int position) {
        if (player != null) {
            player.seekTo(index, 0);
            player.setPlayWhenReady(true);
            if(getModels().get(position).getUrlPath_lectureList().contains("pdf")){
                playSingleState=playSingle.docClick;
            }
            else {
                playSingleState=playSingle.otherClick;
            }

        }

    }

    private void updateStartPosition() {
        if (player != null) {
            startAutoPlay = player.getPlayWhenReady();
            startWindow = player.getCurrentWindowIndex();
            startPosition = Math.max(0, player.getContentPosition());
        }
    }

    private void clearStartPosition() {
        startAutoPlay = true;
        startWindow = C.INDEX_UNSET;
        startPosition = C.TIME_UNSET;
    }

    private void updateTrackSelectorParameters() {
        if (trackSelector != null) {
            trackSelectorParameters = trackSelector.getParameters();
        }
    }

    public void showNoticeDialog() {

        DialogFragment dialog = new speedPlaybackDialog();
        dialog.show(getSupportFragmentManager(), "NoticeDialogFragment");
    }


    @Override
    public void onSpeedChange(String speed) {

        final String s1 = "0.5X";
        final String s2 = "0.75X";
        final String s3 = "1.0X";
        final String s4 = "1.25X";
        final String s5 = "1.5X";
        final String s6 = "1.75X";
        final String s7 = "2.0X";

        currentSpeedLabel = speed;

        switch (speed) {
            case s1:
                player.setPlaybackParameters(new PlaybackParameters(0.5f));
                txtSpeed.setText(s1);
                currentSpeedValue = 0.5f;

                break;
            case s2:
                player.setPlaybackParameters(new PlaybackParameters(0.75f));
                txtSpeed.setText(s2);
                currentSpeedValue = 0.75f;
                break;
            case s3:
                player.setPlaybackParameters(new PlaybackParameters(1.0f));
                txtSpeed.setText(s3);
                currentSpeedValue = 1.0f;
                break;
            case s4:
                player.setPlaybackParameters(new PlaybackParameters(1.25f));
                txtSpeed.setText(s4);
                currentSpeedValue = 1.25f;
                break;
            case s5:
                player.setPlaybackParameters(new PlaybackParameters(1.75f));
                txtSpeed.setText(s5);
                currentSpeedValue = 1.5f;
                break;
            case s6:
                player.setPlaybackParameters(new PlaybackParameters(1.75f));
                txtSpeed.setText(s6);
                currentSpeedValue = 1.75f;
                break;
            case s7:
                player.setPlaybackParameters(new PlaybackParameters(2.0f));
                txtSpeed.setText(s7);
                currentSpeedValue = 2.0f;
                break;
        }
    }

    public abstract void changeCurrentWindow(int currentWindow, long currentPosition,int reason,boolean windowsChanged);

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

//        if(!isDocument) {
//            updateNetworkVisibility(isConnected);
//
//        }
       this.isConnected = isConnected;
        if(player!=null) {
            if (!isDocument && isConnected && player.getPlaybackState() == Player.STATE_IDLE) {
                updateNetworkVisibility(true);
                //player.retry();

            }
        }

        if (!isConnected) {
            showSnackBar();
        } else {
            if (snackbar != null)
                snackbar.dismiss();
        }

    }


    private class PlayerEventListener implements Player.EventListener, AnalyticsListener {
        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            String stateString;
            switch (playbackState) {
                case Player.STATE_IDLE: // The player does not have any media to play.
                    stateString = "Player.STATE_IDLE";
                    if(!isDocument && !isInUpdateNetwork ) {
                        mProgressBar.setVisibility(View.VISIBLE);
                        mProgressBar.show();
                    }
                    else if(isDocument || isInUpdateNetwork ) {
                        mProgressBar.setVisibility(View.GONE);
                        mProgressBar.hide();
                    }
                    mPlayerView.hideController();
                    //Log.e(TAG, "STATE_IDLE");
                    break;
                case Player.STATE_BUFFERING: // The player needs to load media before playing.
                    stateString = "Player.STATE_BUFFERING";
                    if(!isDocument) {
                        mProgressBar.setVisibility(View.VISIBLE);
                        mProgressBar.show();
                    }
                    else {
                        mProgressBar.setVisibility(View.GONE);
                        mProgressBar.hide();
                    }
                    mPlayPauseLayout.setVisibility(View.GONE);
                    //Log.e(TAG, "STATE_BUFFERING");
                    break;
                case Player.STATE_READY: // The player is able to immediately play from its current position.
                    stateString = "Player.STATE_READY";
                    mProgressBar.setVisibility(View.GONE);
                    mProgressBar.hide();
                    mPlayPauseLayout.setVisibility(View.VISIBLE);
                    if(!player.getPlayWhenReady()){
                        Intent intent=new Intent(BaseExoWithServiceActivity.this,PlayerService.class);
                        intent.putExtra(ACTION_SHOW_NOTIFICATION,3);
                        //startService(intent);
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                            startForegroundService(intent);
                        else
                            startService(intent);
                        //Util.startForegroundService(BaseExoWithServiceActivity.this,intent);
                    }
                    //Log.e(TAG, stateString);
                    break;
                case Player.STATE_ENDED: // The player has e playing the media.
                    stateString = "Player.STATE_ENDED";
                   // Log.e(TAG, stateString);
                    break;

                default:
                    stateString = "UNKNOWN_STATE";
                    //Log.e(TAG, stateString);
                    break;
            }

        }

        @Override
        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
            // updateButtonVisibilities();
            if (trackGroups != lastSeenTrackGroupArray) {
                MappingTrackSelector.MappedTrackInfo mappedTrackInfo = trackSelector.getCurrentMappedTrackInfo();
                if (mappedTrackInfo != null) {
                    if (mappedTrackInfo.getTypeSupport(C.TRACK_TYPE_VIDEO)
                            == MappingTrackSelector.MappedTrackInfo.RENDERER_SUPPORT_UNSUPPORTED_TRACKS) {
                        Toast.makeText(BaseExoWithServiceActivity.this, getString(R.string.error_unsupported_video), Toast.LENGTH_SHORT).show();
                    }
                    if (mappedTrackInfo.getTypeSupport(C.TRACK_TYPE_AUDIO)
                            == MappingTrackSelector.MappedTrackInfo.RENDERER_SUPPORT_UNSUPPORTED_TRACKS) {
                        Toast.makeText(BaseExoWithServiceActivity.this, getString(R.string.error_unsupported_audio), Toast.LENGTH_SHORT).show();
                    }
                    if (mappedTrackInfo.getTypeSupport(C.TRACK_TYPE_VIDEO) !=
                            MappingTrackSelector.MappedTrackInfo.RENDERER_SUPPORT_PLAYABLE_TRACKS) {
                        //Toast.makeText(BaseExoWithServiceActivity.this, "RENDERER_SUPPORT_PLAYABLE_TRACKS", Toast.LENGTH_SHORT).show();
                    }


                }
                lastSeenTrackGroupArray = trackGroups;
            }
        }

        @Override
        public void onPositionDiscontinuity(int reason) {
            int tempCurrentWindow=currentWindow;
            currentWindow = player.getCurrentWindowIndex();
            currentPosition = player.getCurrentPosition();


          //  Log.e(TAG,"reason: "+reason);
            myReason=reason;

            Log.e(TAG, "onPositionDiscontinuity window= "+currentWindow+" reason: "+reason);

            if(tempCurrentWindow!=currentWindow) {
             //Log.e(TAG, "onPositionDiscontinuity window changed= "+currentWindow+" reason: "+reason);
                updateButtonVisibilities(false,reason);
                //updateNetworkVisibility(true);
                player.retry();
                isDocument=false;
                windowsChanged=true;

            }
            else {
                windowsChanged=false;
                //Log.e(TAG, "onPositionDiscontinuity window not changed = "+currentWindow+" reason: "+reason);
            }
            changeCurrentWindow(currentWindow,currentPosition,reason,windowsChanged);


        }

        @Override
        public void onPlayerError(ExoPlaybackException error) {

            switch (error.type) {
                case ExoPlaybackException.TYPE_SOURCE:
                    String myError=error.getSourceException().getMessage();;
                    if (myError.contains("Response code: 404") ||
                            myError.contains("pdf") ||
                            myError.contains(ERROR_NO_PLAYABLE)
                            || getModels().get(getPosition(getCurrentWindow(),getModels())).getTypeMedia_lectureList().equals(TYPE_ARTICLE)
                            ) {
                        isDocument = true;
//                        Log.e(TAG, "in the document gecurrenwoindow: "+player.getCurrentWindowIndex()
//                        +"reason: "+myReason);
                        if( !getModels().get(getPosition(getCurrentWindow(),getModels())).getTypeMedia_lectureList().equals(TYPE_ARTICLE) && myReason!=1000){
                          //  Log.e(TAG,"in +1 currenwiendo");
                            player.seekTo(getCurrentWindow() + 1,0);
                        }
//                        Intent intent=new Intent(BaseExoWithServiceActivity.this,PlayerService.class);
//                        intent.putExtra(ACTION_SHOW_NOTIFICATION,2);
//                        startService(intent);
                        if(isDestroyed){
                            return;
                        }

                        updateButtonVisibilities(true, 10);
                        return;
                    }
                    else if (myError.contains("Unable to connect to")
                            && !myError.contains("pdf")
                            && !isOnline()) {
                       // updateNetworkVisibility(false);
                        //isConnected=false;
                        isDocument = false;
                       // Log.e(TAG, "TYPE_SOURCE: else if " + myError);
                        return;
                    } else {
                        isDocument = false;
                        //isConnected=true;
                       // Log.e(TAG, "TYPE_SOURCE: else " + myError);
                    }
                    break;
                case ExoPlaybackException.TYPE_RENDERER:
                    isDocument=false;
                   // Log.e(TAG, "TYPE_RENDERER: " + error.getRendererException().getMessage());
                    break;

                case ExoPlaybackException.TYPE_UNEXPECTED:
                    isDocument=false;
                   // Log.e(TAG, "TYPE_UNEXPECTED: " + error.getUnexpectedException().getMessage());
                    break;
            }
        }


    }

    public abstract String getImageCourse();
    public abstract Course getCurrentCourse();
    public abstract void setVisibilityOpenDoc(boolean visible);
    public abstract void setVisibilityNetworkState(boolean isConnect);
    public int getCurrentWindow(){
        if(player!=null) {

                return player.getCurrentWindowIndex();
        }
        else {
            return -1;
        }
    }

    private void updateButtonVisibilities(boolean isDocument,int reason) {
        if (isDocument) {
            updateNetworkVisibility(true);
            mBottomLayout.setVisibility(View.GONE);
            mPlayPauseLayout.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.GONE);
            mProgressBar.hide();
            txtSpeed.setVisibility(View.GONE);
            setVisibilityOpenDoc(true);
            mPlayerView.hideController();
            mPlayerView.setPlayer(null);
           // Log.e(TAG,"isdocument=true");
        } else {
            mBottomLayout.setVisibility(View.VISIBLE);
            mPlayPauseLayout.setVisibility(View.VISIBLE);
            txtSpeed.setVisibility(View.VISIBLE);
            setVisibilityOpenDoc(false);
            mPlayerView.setPlayer(player);
           // Log.e(TAG,"isdocument=false");
        }

        if (player == null) {
            return;
        }
    }

    private void updateNetworkVisibility(boolean isConnection){
        if(!isConnection && !isDocument && !isConnected){
            mBottomLayout.setVisibility(View.GONE);
            mPlayPauseLayout.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.GONE);
            mProgressBar.hide();
            txtSpeed.setVisibility(View.GONE);
            setVisibilityNetworkState(false);
            mPlayerView.hideController();
            mPlayerView.setPlayer(null);
            isInUpdateNetwork=true;

//            Intent intent=new Intent(BaseExoWithServiceActivity.this,PlayerService.class);
//            intent.putExtra(ACTION_SHOW_NOTIFICATION,2);
//            //startService(intent);
//            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
//                startForegroundService(intent);
//            else
//                startService(intent);


            //Log.e(TAG,"isConnection=false");
        }else {
            isInUpdateNetwork=false;
            if(!isDocument)
                player.retry();
            else {
                setVisibilityNetworkState(true);
                return;
            }
            mBottomLayout.setVisibility(View.VISIBLE);
            mPlayPauseLayout.setVisibility(View.VISIBLE);
            txtSpeed.setVisibility(View.VISIBLE);
            setVisibilityNetworkState(true);
            mPlayerView.setPlayer(player);

           // Log.e(TAG,"isConnection=true");
        }

        if (player == null) {
            return;
        }

    }

    public boolean isOnline() {
        boolean isOnline = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) this.getSystemService(this.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            isOnline = (netInfo != null && netInfo.isConnected());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return isOnline;
    }

    protected class PlayerErrorMessageProvider implements ErrorMessageProvider<ExoPlaybackException> {

        @Override
        public Pair<Integer, String> getErrorMessage(ExoPlaybackException throwable) {
            String errorString = "Playback Error DEBUGGING THIS ERROR";
            //isErrorProvider=true;
            //return Pair.create(0, errorString);
            String error=throwable.getMessage();
            int index=error.indexOf(" ")+22;
            String url=error.substring(index);
            Log.e(TAG,"url = "+url);
            Log.e(TAG,"throwable: "+error);
            Log.e(TAG,"type expestion: "+throwable.type);
            if(!isDestroyed && throwable.getMessage().contains(ERROR_NO_CONNECTION) &&
                    !throwable.getMessage().contains("pdf") && !isDocument &&
            getModels().get(getPosition(getCurrentWindow(),getModels())).getUrlPath_lectureList().equals(url)
            && throwable.type==ExoPlaybackException.TYPE_SOURCE){
                updateNetworkVisibility(false);
                Log.e(TAG,"in if netwodk");
            }

            return Pair.create(0, "");
        }


    }

    class serviceBroadCastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String location=intent.getStringExtra(KEY_LOCATION_BROADCAST);
            if(location.equals(KEY_LOCATION_RELEASE_PLAYER)){
                updateStartPosition();
                player.removeListener(new BaseExoWithServiceActivity.PlayerEventListener());
                player.addVideoListener(null);
            }

        }
    }

    protected void registerPlayerBroadcast(){
        serviceBroadCastReceiver=new serviceBroadCastReceiver();
        IntentFilter filter=new IntentFilter(KEY_SEND_BROADCAST);
        registerReceiver(serviceBroadCastReceiver,filter);
    }


    protected void showSnackBar() {
        snackbar = Snackbar
                .make(rootLayoutMain, getString(R.string.no_connection_internet), Snackbar.LENGTH_LONG)
                .setDuration(BaseTransientBottomBar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor(Color.RED);
        View sbView = snackbar.getView();
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams)
                sbView.getLayoutParams();
        sbView.setLayoutParams(params);
        TextView textView = sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        } else {
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
        }
        snackbar.show();

    }
}
