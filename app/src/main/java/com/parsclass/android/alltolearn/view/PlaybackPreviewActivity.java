package com.parsclass.android.alltolearn.view;

import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.PersistableBundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.analytics.AnalyticsListener;
import com.google.android.exoplayer2.offline.Download;
import com.google.android.exoplayer2.offline.DownloadManager;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory;
import com.google.android.exoplayer2.util.ErrorMessageProvider;
import com.google.android.exoplayer2.util.Util;
import com.parsclass.android.alltolearn.R;
import com.parsclass.android.alltolearn.Utils.ConnectivityReceiver;
import com.parsclass.android.alltolearn.Utils.DownloadTracker;
import com.parsclass.android.alltolearn.Utils.speedPlaybackDialog;
import com.parsclass.android.alltolearn.base.BaseActivity;
import com.parsclass.android.alltolearn.config.MyApplication;
import com.wang.avi.AVLoadingIndicatorView;

import static com.parsclass.android.alltolearn.Utils.ConstantUtil.KEY_URI_PREVIEW_PLAYBACK;

public class PlaybackPreviewActivity extends BaseActivity
        implements View.OnClickListener,
        speedPlaybackDialog.NoticeDialogListener,
        DownloadTracker.Listener,
        ConnectivityReceiver.ConnectivityReceiverListener{

    private static final String TAG = "PlaybackPreviewActivity";
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
    protected AVLoadingIndicatorView mProgressBar;
    protected TextView txtSpeed;
    protected ImageView imgFullscreen;
    protected ConstraintLayout rootLayout;


    private long startPosition;
    protected int startWindow;
    protected boolean startAutoPlay;
    protected boolean mExoPlayerFullscreen = false;

    protected CacheDataSourceFactory cacheDataSourceFactory;


    private Dialog mFullScreenDialog;
    protected int currentWindow = 0;
    protected long currentPosition = 0;


    private ConnectivityReceiver connectivityReceiver = new ConnectivityReceiver();
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playback_preview);

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


    private void initializePlayer() {
        if (player == null) {
            //TrackSelection.Factory trackSelectionFactory = new AdaptiveTrackSelection.Factory(BANDWIDTH_METER);
            trackSelector = new DefaultTrackSelector();
            trackSelector.setParameters(trackSelectorParameters);
            lastSeenTrackGroupArray = null;
            player = ExoPlayerFactory.newSimpleInstance(this, trackSelector);
            player.addListener(new PlaybackPreviewActivity.PlayerEventListener());
            mPlayerView.setPlayer(player);
            player.seekTo(currentWindow, currentPosition);
            player.setPlayWhenReady(startAutoPlay);

            //initFullscreenDialog();
        }
        initFullscreenDialog();
        MediaSource videoSource = buildMediaSource(uri);
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
            player.removeListener(new PlaybackPreviewActivity.PlayerEventListener());
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

    public MediaSource buildMediaSource(Uri uri) {
        cacheDataSourceFactory = MyApplication.getInstance().buildDataSourceFactory();
        return new ProgressiveMediaSource.Factory(cacheDataSourceFactory).createMediaSource(uri);
    }


    public void setupWidget() {
        mProgressBar = findViewById(R.id.progress_bar);
        mPlayerView = findViewById(R.id.player_view);
        txtSpeed = findViewById(R.id.exo_speed);
        imgFullscreen = findViewById(R.id.exo_fullscreen_icon);
        imgFullscreen.setVisibility(View.GONE);
        mPlayPauseLayout = findViewById(R.id.linear_layout_play_pause);
//        mBottomLayout = findViewById(R.id.linear_layout_bottom);
//        //btnOpenDoc = findViewById(R.id.btn_open_doc);
//        imgBackground=findViewById(R.id.img_background_player);
//        rootLayout=findViewById(R.id.root_layout);
        mPlayerView.setErrorMessageProvider(new PlaybackPreviewActivity.PlayerErrorMessageProvider());
        txtSpeed.setText(currentSpeedLabel);
        uri=Uri.parse(getIntent().getStringExtra(KEY_URI_PREVIEW_PLAYBACK));
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

    @Override
    public void onDownloadsChanged(DownloadManager downloadManager, Download download) {

    }

    private class PlayerEventListener implements Player.EventListener, AnalyticsListener {
        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            String stateString;
            switch (playbackState) {
                case Player.STATE_IDLE: // The player does not have any media to play.
                    stateString = "Player.STATE_IDLE";
                    mProgressBar.setVisibility(View.VISIBLE);
                    mPlayerView.hideController();
                    Log.e(TAG, "STATE_IDLE");
                    break;
                case Player.STATE_BUFFERING: // The player needs to load media before playing.
                    stateString = "Player.STATE_BUFFERING";
                    mProgressBar.setVisibility(View.VISIBLE);
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
    }

    protected class PlayerErrorMessageProvider implements ErrorMessageProvider<ExoPlaybackException> {

        @Override
        public Pair<Integer, String> getErrorMessage(ExoPlaybackException throwable) {
            String errorString = "Playback Error DEBUGGING THIS ERROR";
            return Pair.create(0, errorString);
        }
    }


    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

        if(player!=null) {
            if (!isConnected && player.getPlaybackState() == Player.STATE_IDLE) {
                player.retry();

            }
        }
    }


}
