package com.parsclass.android.alltolearn.base;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.parsclass.android.alltolearn.config.MyApplication;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public abstract class BaseActivity extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));

       // super.attachBaseContext(MyApplication.localeManager.setLocale(newBase));
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }

    public void loadFragmentWithoutStack(int layoutId, Fragment fragment, String tag) {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(layoutId, fragment, tag).commit();
    }

    public void loadFragment(int layoutId, Fragment fragment, String tag) {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().addToBackStack("f").replace(layoutId, fragment, tag).commit();
    }
}
