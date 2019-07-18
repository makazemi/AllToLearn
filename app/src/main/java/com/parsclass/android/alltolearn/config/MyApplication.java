package com.parsclass.android.alltolearn.config;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Toast;

import com.bumptech.glide.request.target.ViewTarget;
import com.crashlytics.android.Crashlytics;
import com.google.android.exoplayer2.BuildConfig;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.database.DatabaseProvider;
import com.google.android.exoplayer2.database.ExoDatabaseProvider;
import com.google.android.exoplayer2.offline.ActionFileUpgradeUtil;
import com.google.android.exoplayer2.offline.DefaultDownloadIndex;
import com.google.android.exoplayer2.offline.DefaultDownloaderFactory;
import com.google.android.exoplayer2.offline.DownloadManager;
import com.google.android.exoplayer2.offline.DownloaderConstructorHelper;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.FileDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.upstream.cache.Cache;
import com.google.android.exoplayer2.upstream.cache.CacheDataSource;
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.NoOpCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;
import com.google.android.exoplayer2.util.Util;
import com.parsclass.android.alltolearn.R;
import com.parsclass.android.alltolearn.Utils.ConnectivityReceiver;
import com.parsclass.android.alltolearn.Utils.DownloadBroadcastReceiver;
import com.parsclass.android.alltolearn.Utils.DownloadTracker;
import com.parsclass.android.alltolearn.Utils.LocalManager;
import com.parsclass.android.alltolearn.Utils.LocaleActivityCallbacks;
import com.parsclass.android.alltolearn.Utils.TypefaceUtil;
import com.parsclass.android.alltolearn.view.MainActivity;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;

import static com.parsclass.android.alltolearn.Utils.ConstantUtil.ENGLISH;
import static com.parsclass.android.alltolearn.Utils.ConstantUtil.ENGLISH_LANGUAGE;
import static com.parsclass.android.alltolearn.Utils.ConstantUtil.LOCAL_LOCATION_DOWNLOAD;
import static com.parsclass.android.alltolearn.Utils.ConstantUtil.PERSIAN;
import static com.parsclass.android.alltolearn.Utils.ConstantUtil.PERSIAN_LANGUAGE;

public class MyApplication extends MultiDexApplication {
    private static MyApplication mInstance;
    public static PrefManager prefHelper;


    private DownloadManager downloadManager;
    private DownloadTracker downloadTracker;
    private static final String DOWNLOAD_ACTION_FILE = "actions";
    private static final String DOWNLOAD_TRACKER_ACTION_FILE = "tracked_actions";
    private static final String DOWNLOAD_CONTENT_DIRECTORY = "downloads";

    public static final String TAG = "MyApplication";

    private static final int MAX_SIMULTANEOUS_DOWNLOADS = 200;

    protected String userAgent;

    private File downloadDirectory;
    private Cache downloadCache;

    private DatabaseProvider databaseProvider;

    private Locale locale = null;

    public static LocalManager localeManager;

//    @Override
//    protected void attachBaseContext(Context base) {
//
//        prefHelper = new PrefManager(base);
//        localeManager = new LocalManager();
//        super.attachBaseContext(localeManager.setLocale(base));
//
//    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        MultiDex.install(this);
//        Configuration config=new Configuration(newConfig);
//        if (locale != null) {
//            config.locale = locale;
//            Locale.setDefault(locale);
//            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
//        }

        localeManager.setLocal(this);

    }

    @Override
    public void onCreate() {
        super.onCreate();

        userAgent = Util.getUserAgent(this, "ExoPlayerDemo");
        ViewTarget.setTagId(R.id.glide_tag);
        mInstance = this;
        //prefHelper = new PrefManager(getApplicationContext());

        /* initialize calligraphy3 library */
        ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(
                        new CalligraphyConfig.Builder()
                                .setDefaultFontPath("fonts/isans.ttf")
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build());

        prefHelper = new PrefManager(this);
        localeManager = new LocalManager();
        localeManager.setLocal(this);
        registerActivityLifecycleCallbacks(new LocaleActivityCallbacks());

//        TypefaceUtil.overrideFont(
//                getApplicationContext(),
//                "NORMAL",
//                "fonts/isans.ttf");



        /* for localization app */
//        Configuration conf = getBaseContext().getResources().getConfiguration();
//        Configuration config = new Configuration(conf);
//        Log.e(TAG, "pref lag: " + prefHelper.getMyLanguage());
//        String lang = prefHelper.getMyLanguage();
//
//        if (!"".equals(lang) && !config.locale.getLanguage().equals(lang)) {
//            locale = new Locale(lang);
//            Locale.setDefault(locale);
//            config.locale = locale;
//            config.setLayoutDirection(locale);
//            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
//        }
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }

    public void setProgressDownloadDocListener(DownloadBroadcastReceiver.DownloadReceiverListener listener) {
        DownloadBroadcastReceiver.listener = listener;
    }

    public boolean useExtensionRenderers() {
        return "withExtensions".equals(BuildConfig.FLAVOR);
    }


    public RenderersFactory buildRenderersFactory(boolean preferExtensionRenderer) {
        @DefaultRenderersFactory.ExtensionRendererMode
        int extensionRendererMode =
                useExtensionRenderers()
                        ? (preferExtensionRenderer
                        ? DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER
                        : DefaultRenderersFactory.EXTENSION_RENDERER_MODE_ON)
                        : DefaultRenderersFactory.EXTENSION_RENDERER_MODE_OFF;
        return new DefaultRenderersFactory(/* context= */ this)
                .setExtensionRendererMode(extensionRendererMode);
    }


    public DownloadManager getDownloadManager() {
        initDownloadManager();
        return downloadManager;
    }

    public DownloadTracker getDownloadTracker() {
        initDownloadManager();
        return downloadTracker;
    }

    public CacheDataSourceFactory buildDataSourceFactory() {
        DefaultDataSourceFactory upstreamFactory =
                new DefaultDataSourceFactory(this, buildHttpDataSourceFactory());
        return buildReadOnlyCacheDataSource(upstreamFactory, getDownloadCache());
    }


    public HttpDataSource.Factory buildHttpDataSourceFactory() {
        return new DefaultHttpDataSourceFactory(userAgent);
    }

    private synchronized void initDownloadManager() {

        if (downloadManager == null) {
            DefaultDownloadIndex downloadIndex = new DefaultDownloadIndex(getDatabaseProvider());
            upgradeActionFile(
                    DOWNLOAD_ACTION_FILE, downloadIndex, /* addNewDownloadsAsCompleted= */ false);
            upgradeActionFile(
                    DOWNLOAD_TRACKER_ACTION_FILE, downloadIndex, /* addNewDownloadsAsCompleted= */ true);
            DownloaderConstructorHelper downloaderConstructorHelper =
                    new DownloaderConstructorHelper(getDownloadCache(), buildHttpDataSourceFactory());
            downloadManager =
                    new DownloadManager(
                            this, downloadIndex, new DefaultDownloaderFactory(downloaderConstructorHelper));
            downloadManager.setMaxParallelDownloads(MAX_SIMULTANEOUS_DOWNLOADS);
            downloadTracker =
                    new DownloadTracker(/* context= */ this, buildDataSourceFactory(), downloadManager);
        }
    }

        private void upgradeActionFile(String fileName, DefaultDownloadIndex downloadIndex, boolean addNewDownloadsAsCompleted) {
            try {
                ActionFileUpgradeUtil.upgradeAndDelete(
                        new File(getDownloadDirectory(), fileName),
                        /* downloadIdProvider= */ null,
                        downloadIndex,
                        /* deleteOnFailure= */ true,
                        addNewDownloadsAsCompleted);
            } catch (IOException e) {
                Log.e(TAG, "Failed to upgrade action file: " + fileName, e);
            }
        }

        private DatabaseProvider getDatabaseProvider() {
            if (databaseProvider == null) {
                databaseProvider = new ExoDatabaseProvider(this);
            }
            return databaseProvider;
        }
//        if (downloadManager == null) {
//            DownloaderConstructorHelper downloaderConstructorHelper =
//                    new DownloaderConstructorHelper(getDownloadCache(), buildHttpDataSourceFactory());
//            downloadManager =
//                    new DownloadManager(
//                            downloaderConstructorHelper,
//                            MAX_SIMULTANEOUS_DOWNLOADS,
//                            DownloadManager.DEFAULT_MIN_RETRY_COUNT,
//                            new File(getDownloadDirectory(), DOWNLOAD_ACTION_FILE));
//            downloadTracker =
//                    new DownloadTracker(
//                            /* context= */ this,
//                            buildDataSourceFactory(),
//                            new File(getDownloadDirectory(), DOWNLOAD_TRACKER_ACTION_FILE));
//            downloadManager.addListener(downloadTracker);
//        }



    private synchronized Cache getDownloadCache() {
        byte[] secretKey = hexStringToByteArray("e04fd020ea3a6910a2d808002b30309d");
        if (downloadCache == null) {
            File downloadContentDirectory = new File(getDownloadDirectory(), DOWNLOAD_CONTENT_DIRECTORY);
            downloadCache = new SimpleCache(downloadContentDirectory, new NoOpCacheEvictor(),databaseProvider, secretKey, true,false);
        }
        return downloadCache;
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    public File getDownloadDirectory() {
        if (downloadDirectory == null) {
            if (prefHelper.getUserLocationDownload().equals(LOCAL_LOCATION_DOWNLOAD)) {
                downloadDirectory = getExternalFilesDir(null);
                // Log.e(TAG,"local: "+getExternalFilesDir(null));
            } else {

                File[] sdcardPath = getExternalFilesDirs(null);
                //downloadDirectory=getExternalStorageDirectory();
                downloadDirectory = sdcardPath[1];

            }

            if (downloadDirectory == null) {
                downloadDirectory = getFilesDir();
                // Log.e(TAG,"in getFileDir: "+getFilesDir());
            }
        }
        return downloadDirectory;
    }

    private static CacheDataSourceFactory buildReadOnlyCacheDataSource(
            DataSource.Factory upstreamFactory, Cache cache) {
        return new CacheDataSourceFactory(
                cache,
                upstreamFactory,
                new FileDataSourceFactory(),
                /* cacheWriteDataSinkFactory= */ null,
                CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR,
                /* eventListener= */ null);
    }

    public boolean isOnline() {
        boolean isOnline = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            isOnline = (netInfo != null && netInfo.isConnected());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return isOnline;
    }

    public static void showAlertDialog(String title, String message, Context context) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, context.getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    /* method I use for changing language of app */
    public  void resetLang(int item, Context context) {
        String lang = "";
        switch (item) {
            case PERSIAN:
                lang = PERSIAN_LANGUAGE;
                break;
            case ENGLISH:
                lang = ENGLISH_LANGUAGE;
                break;
        }
        Log.e(TAG, "lang: " + lang);
        MyApplication.prefHelper.putLanguage(lang);
        //setNewLocale(lang, true);
        Locale myLocale = new Locale(lang);
       // Resources res = context.getResources();
        Resources res = getBaseContext().getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = new Configuration(res.getConfiguration());
        conf.locale = myLocale;
        conf.setLocale(myLocale);
        Locale.setDefault(myLocale);
        conf.setLayoutDirection(myLocale);
        res.updateConfiguration(conf, dm);

        Intent i = new Intent(context, MainActivity.class);
        context.startActivity(i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
        System.exit(0);
    }

    public static void setNewLocale(int item,Context context, boolean restartProcess) {

        String lang = "";
        switch (item) {
            case PERSIAN:
                lang = PERSIAN_LANGUAGE;
                break;
            case ENGLISH:
                lang = ENGLISH_LANGUAGE;
                break;
        }

        localeManager.setNewLocale(context,lang);

        Intent i = new Intent(context, MainActivity.class);
        context.startActivity(i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));

        if (restartProcess) {
            System.exit(0);
        } else {
            Toast.makeText(context, "Activity restarted", Toast.LENGTH_SHORT).show();
        }

    }

    public void chooseDialog(Context context) {

        String[] grpname = context.getResources().getStringArray(R.array.select_lang);
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(context);
        alt_bld.setTitle(context.getString(R.string.select_lang_title));
        //String language = Locale.getDefault().getLanguage();
        String language  =localeManager.getLanguage();
        int defaultChoice = 0;
        switch (language) {
            case PERSIAN_LANGUAGE:
                defaultChoice = PERSIAN;
                break;
            case ENGLISH_LANGUAGE:
                defaultChoice = ENGLISH;
                break;
            default:
                defaultChoice = PERSIAN;
                break;
        }
        alt_bld.setSingleChoiceItems(grpname, defaultChoice, new DialogInterface
                .OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                //if(item!=language)
                //resetLang(item, context);
                setNewLocale(item,context,true);
                dialog.dismiss();// dismiss the alertbox after chose option

            }
        });
        AlertDialog alert = alt_bld.create();
        alert.show();
    }

    public static void animateSearchToolbar(int numberOfMenuIcon, boolean containsOverflow, boolean show,Toolbar mToolbar,Context context) {

        mToolbar.setBackgroundColor(ContextCompat.getColor(context, android.R.color.white));

        if (show) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                int width = mToolbar.getWidth() -
                        (containsOverflow ? context.getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_overflow_material) : 0) -
                        ((context.getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_material) * numberOfMenuIcon) / 2);
                Animator createCircularReveal = ViewAnimationUtils.createCircularReveal(mToolbar,
                        isRtl(context.getResources()) ? mToolbar.getWidth() - width : width, mToolbar.getHeight() / 2, 0.0f, (float) width);
                createCircularReveal.setDuration(250);
                createCircularReveal.start();
            } else {
                TranslateAnimation translateAnimation = new TranslateAnimation(0.0f, 0.0f, (float) (-mToolbar.getHeight()), 0.0f);
                translateAnimation.setDuration(220);
                mToolbar.clearAnimation();
                mToolbar.startAnimation(translateAnimation);
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                int width = mToolbar.getWidth() -
                        (containsOverflow ? context.getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_overflow_material) : 0) -
                        ((context.getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_material) * numberOfMenuIcon) / 2);
                Animator createCircularReveal = ViewAnimationUtils.createCircularReveal(mToolbar,
                        isRtl(context.getResources()) ? mToolbar.getWidth() - width : width, mToolbar.getHeight() / 2, (float) width, 0.0f);
                createCircularReveal.setDuration(250);
                createCircularReveal.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        mToolbar.setBackgroundColor(getThemeColor(context, R.attr.colorPrimary));
                    }
                });
                createCircularReveal.start();
            } else {
                AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
                Animation translateAnimation = new TranslateAnimation(0.0f, 0.0f, 0.0f, (float) (-mToolbar.getHeight()));
                AnimationSet animationSet = new AnimationSet(true);
                animationSet.addAnimation(alphaAnimation);
                animationSet.addAnimation(translateAnimation);
                animationSet.setDuration(220);
                animationSet.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        mToolbar.setBackgroundColor(getThemeColor(context, R.attr.colorPrimary));
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                mToolbar.startAnimation(animationSet);
            }
        }
    }

    public static boolean isRtl(Resources resources) {
        return resources.getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;
    }

    public static int getThemeColor(Context context, int id) {
        Resources.Theme theme = context.getTheme();
        TypedArray a = theme.obtainStyledAttributes(new int[]{id});
        int result = a.getColor(0, 0);
        a.recycle();
        return result;
    }

}
