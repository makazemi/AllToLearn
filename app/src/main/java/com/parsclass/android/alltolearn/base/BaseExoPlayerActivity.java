package com.parsclass.android.alltolearn.base;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Point;
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
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;
import android.view.Display;
import android.view.View;
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
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.analytics.AnalyticsListener;
import com.google.android.exoplayer2.offline.DownloadService;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory;
import com.google.android.exoplayer2.util.ErrorMessageProvider;
import com.google.android.exoplayer2.util.Util;
import com.parsclass.android.alltolearn.R;
import com.parsclass.android.alltolearn.Utils.ConnectivityReceiver;
import com.parsclass.android.alltolearn.Utils.DownloadTracker;
import com.parsclass.android.alltolearn.Utils.speedPlaybackDialog;
import com.parsclass.android.alltolearn.config.MyApplication;
import com.parsclass.android.alltolearn.services.MediaDownloadService;

import java.util.ArrayList;

public abstract class BaseExoPlayerActivity extends AppCompatActivity
        implements View.OnClickListener,
        speedPlaybackDialog.NoticeDialogListener,
        DownloadTracker.Listener,
        ConnectivityReceiver.ConnectivityReceiverListener{

    private static final String TAG = "PlayerActivity";
    private static final String KEY_WINDOW = "window";
    private static final String KEY_POSITION = "position";
    private static final String KEY_AUTO_PLAY = "auto_play";
    private final String STATE_PLAYER_FULLSCREEN = "playerFullscreen";
    private final String STATE_SPEED_VALUE = "stateSpeedValue";
    private final String STATE_SPEED_LABEL = "stateSpeedLabel";
    private final String STATE_CURRENT_WINDOW = "stateCurrentWindow";
    private final String STATE_CURRENT_POSITION = "stateCurrentPosition";
    private float currentSpeedValue = 1.0f;
    private String currentSpeedLabel = "1.0X";


    protected SimpleExoPlayer player;
    protected PlayerView mPlayerView;

    private DefaultTrackSelector trackSelector;
    private DefaultTrackSelector.Parameters trackSelectorParameters;
    private TrackGroupArray lastSeenTrackGroupArray;

    protected LinearLayout mPlayPauseLayout;
    protected ProgressBar mProgressBar;
    //protected LinearLayout debugRootView;
    protected TextView txtSpeed;
    protected ImageView imgFullscreen;
    protected LinearLayout mBottomLayout;
    protected Button btnOpenDoc;
    protected ImageView imgBackground;
    protected ConstraintLayout rootLayout;

    protected ProgressBar UniversalProgressBar;

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
    boolean windowsChanged=false;
    protected int myReason;

    protected DownloadTracker downloadTracker;

    private ConnectivityReceiver connectivityReceiver = new ConnectivityReceiver();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(getLayoutId());

        Log.e(TAG,"oncreate baeexo");
        //mContext=setContext();
        setupWidget();

        setupClickListeners();
        //  setPlayerViewDimensions();

        Log.e(TAG, "onCreate");
       // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);

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


    }

    @Override
    protected void onNewIntent(Intent intent) {
        releasePlayer();
        clearStartPosition();
        setIntent(intent);
        Log.e(TAG, "onNewIntent");
        super.onNewIntent(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, "onStart");
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
        downloadTracker.addListener(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(connectivityReceiver, intentFilter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume");
        if (Util.SDK_INT <= 23 || player == null) {
            Log.e(TAG,"onResume: call initializePlayer");
            initializePlayer();
        }
        MyApplication.getInstance().setConnectivityListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "onPause");
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        downloadTracker.removeListener(this);
        Log.e(TAG, "onStop");
       if (Util.SDK_INT > 23) {
            releasePlayer();
        }
        unregisterReceiver(connectivityReceiver);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        Log.e(TAG, "onSaveInstanceState");
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
        Log.e(TAG, "onConfigurationChanged");
//        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            //  mExoPlayerFullscreen=true;
//            // setPlayerViewDimensionsForLandScapeMode();
//            openFullscreenDialog();
//            Log.e(TAG, "LANDSCAPE");
//
//        } else {
//            // setPlayerViewDimensionsForPortraitMode();
//            // mExoPlayerFullscreen=false;
//            closeFullscreenDialog();
//            Log.e(TAG, "PORTRAITE");
//        }
    }

    private void setPlayerViewDimensionsForLandScapeMode() {
        mExoPlayerFullscreen = true;
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        hideSystemUi();
        // mPlayerView.setDimensions(width, height);


    }

    private void setPlayerViewDimensionsForPortraitMode() {
        mExoPlayerFullscreen = false;
        // 1 (width) : 1/1.5 (height) --> Height is 66% of the width when in Portrait mode.
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        Double heightDouble = width / 1.5;
        Integer height = heightDouble.intValue();
        // mPlayerView.setDimensions(width, height);
    }

    private void setPlayerViewDimensions() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //setPlayerViewDimensionsForLandScapeMode();
            openFullscreenDialog();
        } else {
            //setPlayerViewDimensionsForPortraitMode();
            // closeFullscreenDialog();
        }
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

    @SuppressLint("InlinedApi")
    // View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY min API is 19, current min is 18.
    private void hideSystemUi() {
        mPlayerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    private void initializePlayer() {
        if (player == null) {
            //TrackSelection.Factory trackSelectionFactory = new AdaptiveTrackSelection.Factory(BANDWIDTH_METER);
            trackSelector = new DefaultTrackSelector();
            trackSelector.setParameters(trackSelectorParameters);
            lastSeenTrackGroupArray = null;
            player = ExoPlayerFactory.newSimpleInstance(this, trackSelector);
            player.addListener(new PlayerEventListener());
            mPlayerView.setPlayer(player);
            player.seekTo(currentWindow, currentPosition);
            player.setPlayWhenReady(startAutoPlay);

            //initFullscreenDialog();
        }
        initFullscreenDialog();
        ConcatenatingMediaSource videoSource = buildMediaSource();
        boolean haveStartPosition = startWindow != C.INDEX_UNSET;
        if (haveStartPosition) {
            player.seekTo(startWindow, startPosition);
        }
        player.prepare(videoSource, !haveStartPosition, true);
        //updateButtonVisibilities();
    }

    private void releasePlayer() {
        if (player != null) {
            Log.e(TAG,"releasePlayer");
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
                Log.e(TAG, "txt speed clicked;");
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

    public ConcatenatingMediaSource buildMediaSource() {
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
                Util.getUserAgent(this, getString(R.string.app_name)));

        /* DownloadUtil.getCache(this),*/
//        cacheDataSourceFactory = new CacheDataSourceFactory(
//                DownloadUtil.getCache(this),
//                dataSourceFactory,
//                CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR);
        Log.e(TAG,"buildMediaSource");

        cacheDataSourceFactory = MyApplication.getInstance().buildDataSourceFactory();
        concatenatingMediaSource = new ConcatenatingMediaSource();
        for (int i = 0; i < getUris().size(); i++) {
            MediaSource mediaSource = new ExtractorMediaSource.Factory(cacheDataSourceFactory).
                    createMediaSource(getUris().get(i));
            concatenatingMediaSource.addMediaSource(mediaSource);

            @C.ContentType int type = Util.inferContentType(getUris().get(i), getUris().get(i).toString().substring(getUris().get(i).toString().lastIndexOf(".") + 1));
            Log.e(TAG, "type uri: " + type);
        }


        return concatenatingMediaSource;
    }

    public abstract ArrayList<Uri> getUris();

    public void setupWidget() {
        mProgressBar = findViewById(R.id.progress_bar);
        mPlayerView = findViewById(R.id.player_view);
        txtSpeed = findViewById(R.id.exo_speed);
        imgFullscreen = findViewById(R.id.exo_fullscreen_icon);
        mPlayPauseLayout = findViewById(R.id.linear_layout_play_pause);
        mBottomLayout = findViewById(R.id.linear_layout_bottom);
        UniversalProgressBar=findViewById(R.id.progressBar);
        imgBackground=findViewById(R.id.img_background_player);
        rootLayout=findViewById(R.id.root_layout);
        mPlayerView.setErrorMessageProvider(new PlayerErrorMessageProvider());
        txtSpeed.setText(currentSpeedLabel);
    }

    public abstract int getLayoutId();

    public void playSingleVideo(int index) {
        if (player != null) {
            player.seekTo(index, 0);
            player.setPlayWhenReady(true);
            Log.e(TAG,"playSingleVideo player!=null index= "+index);
        }
        else{
            Log.e(TAG,"playSingleVideo player=null index= "+index);
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

        com.google.android.exoplayer2.util.Log.e(TAG, "which is: " + speed);

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

    public abstract void changeCurrentWindow(int currentWindow, long currentPosition);

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

//        if(!isDocument) {
//            updateNetworkVisibility(isConnected);
//
//        }
//        this.isConnected = isConnected;
        if(!isDocument && isConnected && player.getPlaybackState()==Player.STATE_IDLE){
            player.retry();

        }
            Log.e(TAG,"onNetworkConnectionChanged");
    }


    private class PlayerEventListener implements Player.EventListener, AnalyticsListener {
        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            String stateString;
            switch (playbackState) {
                case Player.STATE_IDLE: // The player does not have any media to play.
                    stateString = "Player.STATE_IDLE";
                    if(!isDocument ) //&& isConnected
                        mProgressBar.setVisibility(View.VISIBLE);
                    else if(isDocument )//|| !isConnected
                        mProgressBar.setVisibility(View.GONE);
                    mPlayerView.hideController();
                    Log.e(TAG, "STATE_IDLE");
                    break;
                case Player.STATE_BUFFERING: // The player needs to load media before playing.
                    stateString = "Player.STATE_BUFFERING";
                    if(!isDocument)
                        mProgressBar.setVisibility(View.VISIBLE);
                    else
                        mProgressBar.setVisibility(View.GONE);
                    mPlayPauseLayout.setVisibility(View.GONE);
                    Log.e(TAG, "STATE_BUFFERING");
                    break;
                case Player.STATE_READY: // The player is able to immediately play from its current position.
                    stateString = "Player.STATE_READY";
                    mProgressBar.setVisibility(View.GONE);
                    mPlayPauseLayout.setVisibility(View.VISIBLE);
                    Log.e(TAG, stateString);
                    break;
                case Player.STATE_ENDED: // The player has e playing the media.
                    stateString = "Player.STATE_ENDED";
                    Log.e(TAG, stateString);
                    break;

                default:
                    stateString = "UNKNOWN_STATE";
                    Log.e(TAG, stateString);
                    break;
            }
            Log.i(TAG, "onPlayerStateChanged: Changed to State: " + stateString + " - startAutoPlay: " + playWhenReady);
            //updateButtonVisibilities();
        }

        @Override
        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
            // updateButtonVisibilities();
            if (trackGroups != lastSeenTrackGroupArray) {
                MappingTrackSelector.MappedTrackInfo mappedTrackInfo = trackSelector.getCurrentMappedTrackInfo();
                if (mappedTrackInfo != null) {
                    if (mappedTrackInfo.getTypeSupport(C.TRACK_TYPE_VIDEO)
                            == MappingTrackSelector.MappedTrackInfo.RENDERER_SUPPORT_UNSUPPORTED_TRACKS) {
                        Toast.makeText(BaseExoPlayerActivity.this, getString(R.string.error_unsupported_video), Toast.LENGTH_SHORT).show();
                    }
                    if (mappedTrackInfo.getTypeSupport(C.TRACK_TYPE_AUDIO)
                            == MappingTrackSelector.MappedTrackInfo.RENDERER_SUPPORT_UNSUPPORTED_TRACKS) {
                        Toast.makeText(BaseExoPlayerActivity.this, getString(R.string.error_unsupported_audio), Toast.LENGTH_SHORT).show();
                    }
                    if (mappedTrackInfo.getTypeSupport(C.TRACK_TYPE_VIDEO) !=
                            MappingTrackSelector.MappedTrackInfo.RENDERER_SUPPORT_PLAYABLE_TRACKS) {
                        Toast.makeText(BaseExoPlayerActivity.this, "RENDERER_SUPPORT_PLAYABLE_TRACKS", Toast.LENGTH_SHORT).show();
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
            changeCurrentWindow(currentWindow,currentPosition);


            Log.e(TAG,"reason: "+reason);
            myReason=reason;

            if(tempCurrentWindow!=currentWindow) {
                Log.e(TAG, "onPositionDiscontinuity window changed");
                updateButtonVisibilities(false,reason);
                //updateNetworkVisibility(true);
                player.retry();
                isDocument=false;
                windowsChanged=true;
            }
            else {
                windowsChanged=false;
                Log.e(TAG, "onPositionDiscontinuity window not changed");

            }


        }

        @Override
        public void onPlayerError(ExoPlaybackException error) {

            switch (error.type) {
                case ExoPlaybackException.TYPE_SOURCE:
                    if (error.getSourceException().getMessage().contains("Response code: 404")||
                            error.getSourceException().getMessage().contains("pdf")||
                    error.getSourceException().getMessage()
                            .contains("None of the available extractors (MatroskaExtractor, FragmentedMp4Extractor, Mp4Extractor, Mp3Extractor, AdtsExtractor, Ac3Extractor, TsExtractor, FlvExtractor, OggExtractor, PsExtractor, WavExtractor, AmrExtractor) could read the stream.")) {
                        updateButtonVisibilities(true,10);
                        isDocument=true;
                        Log.e(TAG, "TYPE_SOURCE: if " + error.getSourceException().getMessage());
                        getCurrentWindow();
                        if(!windowsChanged && myReason!=2) {
                            Log.e(TAG,"windowsChanged=false before change currentwindo="+currentWindow);
                            Log.e(TAG,"windowsChanged=false before change getCurrentWindow="+getCurrentWindow());
                            Log.e(TAG,"windowsChanged=false");
                            changeCurrentWindow(getCurrentWindow() + 1, currentPosition);
                            currentWindow = getCurrentWindow() + 1;

                        }
                        getCurrentWindow();
                       // isConnected=true;
                        //updateNetworkVisibility(true);
                        return;
                    }
                    else if(error.getSourceException().getMessage().contains("Unable to connect to")
                    && !error.getSourceException().getMessage().contains("pdf")
                    && !isOnline()){
//                        updateNetworkVisibility(false);
//                        isConnected=false;
                      isDocument=false;
                        getCurrentWindow();
                        Log.e(TAG, "TYPE_SOURCE: else if " + error.getSourceException().getMessage());
                        return;
                    }
                    else {
                        isDocument=false;
                        //isConnected=true;
                        Log.e(TAG, "TYPE_SOURCE: else " + error.getSourceException().getMessage());
                        getCurrentWindow();

                    }
                    break;
                case ExoPlaybackException.TYPE_RENDERER:
                    isDocument=false;
                    Log.e(TAG, "TYPE_RENDERER: " + error.getRendererException().getMessage());
                    break;

                case ExoPlaybackException.TYPE_UNEXPECTED:
                    isDocument=false;
                    Log.e(TAG, "TYPE_UNEXPECTED: " + error.getUnexpectedException().getMessage());
                    break;
            }
        }


    }

    public abstract String getImageCourse();
    public abstract void setVisibilityOpenDoc(boolean visible);
    public abstract void setVisibilityNetworkState(boolean isConnect);
    public int getCurrentWindow(){
        if(player!=null) {
            //Log.e(TAG,"in if currentwind= "+player.getCurrentWindowIndex());
            return player.getCurrentWindowIndex();
        }
        else {
           // Log.e(TAG,"in else currentwind= "+player.getCurrentWindowIndex());
            return -1;
        }
    }

    private void updateButtonVisibilities(boolean isDocument,int reason) {
        if (isDocument) {
            mBottomLayout.setVisibility(View.GONE);
            mPlayPauseLayout.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.GONE);
            txtSpeed.setVisibility(View.GONE);
            setVisibilityOpenDoc(true);
            mPlayerView.hideController();
           mPlayerView.setPlayer(null);
            Log.e(TAG,"isdocument=true");
        } else {
            mBottomLayout.setVisibility(View.VISIBLE);
            mPlayPauseLayout.setVisibility(View.VISIBLE);
            txtSpeed.setVisibility(View.VISIBLE);
            setVisibilityOpenDoc(false);
           mPlayerView.setPlayer(player);
            Log.e(TAG,"isdocument=false");
        }

        if (player == null) {
            Log.e(TAG,"player=null");
            return;
        }
    }

    private void updateNetworkVisibility(boolean isConnection){
        if(!isConnection && !isDocument){
            mBottomLayout.setVisibility(View.GONE);
            mPlayPauseLayout.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.GONE);
            txtSpeed.setVisibility(View.GONE);
            setVisibilityNetworkState(false);
            mPlayerView.hideController();
            mPlayerView.setPlayer(null);
            //changeCurrentWindow(getCurrentWindow()+1,currentPosition);
            Log.e(TAG,"isConnection=false");
        }else {
            mBottomLayout.setVisibility(View.VISIBLE);
            mPlayPauseLayout.setVisibility(View.VISIBLE);
            txtSpeed.setVisibility(View.VISIBLE);
            setVisibilityNetworkState(true);
            mPlayerView.setPlayer(player);
            if(!isDocument)
                player.retry();
            Log.e(TAG,"isConnection=true");
        }

        if (player == null) {
            Log.e(TAG,"player=null");
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
            return Pair.create(0, errorString);
        }
    }



}
