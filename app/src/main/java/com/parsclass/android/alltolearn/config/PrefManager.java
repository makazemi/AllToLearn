package com.parsclass.android.alltolearn.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Locale;

import retrofit2.http.PUT;

import static com.parsclass.android.alltolearn.Utils.ConstantUtil.LOCAL_LOCATION_DOWNLOAD;
import static com.parsclass.android.alltolearn.Utils.ConstantUtil.MY_COURSE_ITEM;
import static com.parsclass.android.alltolearn.Utils.ConstantUtil.PERSIAN;

/**
 * Created by win10 on 16/01/2019.
 */

public class PrefManager {

    private static final String USER_TOKEN="USER_TOKEN";
    private static final String IS_USER_LOGIN="IS_USER_LOGIN";
    private static final String USER_OBJECT="USER_OBJECT";
    private static final String USER_LOCATION_DOWNLOAD="USER_LOCATION_DOWNLOAD";
    private static final String KEY_PLAYER_ACTIVITY_IS_DESTROYED="KEY_PLAYER_ACTIVITY_IS_DESTROYED";
    private static final String KEY_WHICH_MENU_ACTIVE="KEY_WHICH_MENU_ACTIVE";
    private static final String KEY_LANGUAGE="KEY_LANGUAGE";

    SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    public PrefManager(Context context){
        preferences= PreferenceManager.getDefaultSharedPreferences(context);
        editor=preferences.edit();
    }


    public void putToken(String token){
        editor.putString(USER_TOKEN,token);
        editor.apply();
    }
    public String getToken(){
        return preferences.getString(USER_TOKEN,"");
    }

    public void setUserLogin(boolean isUserLogin) {
        editor.putBoolean(IS_USER_LOGIN, isUserLogin);
        editor.commit();
    }

    public boolean isUserLogin() {
        return preferences.getBoolean(IS_USER_LOGIN, false);
    }

    public void putLocationDownload(String location){
        editor.putString(USER_LOCATION_DOWNLOAD,location);
        editor.apply();
    }

    public String getUserLocationDownload(){
        return preferences.getString(USER_LOCATION_DOWNLOAD,LOCAL_LOCATION_DOWNLOAD);
    }

    public void putIsDestroyedPlayerActivity(boolean isDestroy){
        editor.putBoolean(KEY_PLAYER_ACTIVITY_IS_DESTROYED,isDestroy);
        editor.apply();
    }
    public boolean isDestroyPlayerActivity(){
        return preferences.getBoolean(KEY_PLAYER_ACTIVITY_IS_DESTROYED,false);

    }

    public int getWhichMenuActive(){
        return preferences.getInt(KEY_WHICH_MENU_ACTIVE,MY_COURSE_ITEM);
    }

    public void putWhichMenuActive(int active){
        editor.putInt(KEY_WHICH_MENU_ACTIVE,active);
        editor.apply();
    }
    public String getMyLanguage(){
        String defaultLang=Locale.getDefault().getLanguage();
        return preferences.getString(KEY_LANGUAGE,defaultLang);
    }

    public void putLanguage(String language){
        editor.putString(KEY_LANGUAGE,language);
        editor.commit();
    }



}
