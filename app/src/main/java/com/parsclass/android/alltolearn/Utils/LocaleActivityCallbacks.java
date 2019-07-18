package com.parsclass.android.alltolearn.Utils;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.parsclass.android.alltolearn.config.MyApplication;

public class LocaleActivityCallbacks implements Application.ActivityLifecycleCallbacks {

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        MyApplication.localeManager.setLocal(activity);
       // Utility.resetActivityTitle(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {
    }

    @Override
    public void onActivityResumed(Activity activity) {
    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivityStopped(Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
    }
}