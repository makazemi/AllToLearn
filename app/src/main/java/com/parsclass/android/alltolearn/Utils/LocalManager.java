package com.parsclass.android.alltolearn.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.preference.PreferenceManager;

import com.parsclass.android.alltolearn.config.MyApplication;
import com.parsclass.android.alltolearn.config.PrefManager;

import java.util.Locale;

import static android.os.Build.VERSION_CODES.JELLY_BEAN_MR1;
import static android.os.Build.VERSION_CODES.N;
import static com.parsclass.android.alltolearn.Utils.ConstantUtil.ENGLISH;
import static com.parsclass.android.alltolearn.Utils.ConstantUtil.ENGLISH_LANGUAGE;
import static com.parsclass.android.alltolearn.Utils.ConstantUtil.PERSIAN;
import static com.parsclass.android.alltolearn.Utils.ConstantUtil.PERSIAN_LANGUAGE;

public class LocalManager {

    private final PrefManager prefs;

    public LocalManager() {
        prefs=MyApplication.prefHelper;
    }

//    public Context setLocale(Context c) {
//        return updateResources(c, getLanguage());
//    }

    public void setLocal(Context c){
        update(c, getLanguage());
    }

//    public Context setNewLocale(Context c, String language) {
//        persistLanguage(language);
//        return updateResources(c, language);
//    }

    public void setNewLocale(Context c, String language) {
        persistLanguage(language);
        update(c, language);
    }

    public String getLanguage() {
        return prefs.getMyLanguage();
    }

    private void update(Context c, String language) {
        updateResources(c, language);
        updateResources(c.getApplicationContext(), language);
    }

    @SuppressLint("ApplySharedPref")
    private void persistLanguage(String language) {
        prefs.putLanguage(language);
    }

//    private Context updateResources(Context context, String language) {
//        Locale locale = new Locale(language);
//        Locale.setDefault(locale);
//
//        Resources res = context.getResources();
//        Configuration config = new Configuration(res.getConfiguration());
//        if (isAtLeastVersion(JELLY_BEAN_MR1)) {
//            config.setLocale(locale);
//            context = context.createConfigurationContext(config);
//        } else {
//            config.locale = locale;
//            res.updateConfiguration(config, res.getDisplayMetrics());
//        }
//        return context;
//
//    }

    private void updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());
        if (isAtLeastVersion(JELLY_BEAN_MR1)) {
            config.setLocale(locale);
        } else {
            config.locale = locale;
        }
        res.updateConfiguration(config, res.getDisplayMetrics());
    }

    public static Locale getLocale(Resources res) {
        Configuration config = res.getConfiguration();
        return isAtLeastVersion(N) ? config.getLocales().get(0) : config.locale;
    }
    public static boolean isAtLeastVersion(int version) {
        return Build.VERSION.SDK_INT >= version;
    }
}
