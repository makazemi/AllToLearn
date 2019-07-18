package com.parsclass.android.alltolearn.view;
import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import android.os.Bundle;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.parsclass.android.alltolearn.R;
import com.parsclass.android.alltolearn.Utils.ConnectivityReceiver;
import com.parsclass.android.alltolearn.Utils.ConstantUtil;
import com.parsclass.android.alltolearn.Utils.SelectFilterDialog;
import com.parsclass.android.alltolearn.Utils.SelectSortDialog;
import com.parsclass.android.alltolearn.base.BaseActivity;
import com.parsclass.android.alltolearn.config.MyApplication;


import static com.parsclass.android.alltolearn.Utils.ConstantUtil.MY_COURSE_ITEM;
import static com.parsclass.android.alltolearn.base.BaseExoWithServiceActivity.KEY_OPEN_FROM_NOTIFICATION;


public class MainActivity extends BaseActivity implements
        HomeFragment.OnFragmentInteractionListener,
        AccountFragment.OnFragmentInteractionListener,
        SearchFragment.OnFragmentInteractionListener,
        ConnectivityReceiver.ConnectivityReceiverListener,
        MyCourseFragment.OnFragmentInteractionListener,
        DetailCourseFragment.OnFragmentInteractionListener,
        InstructorProfileFragment.OnFragmentInteractionListener,
        SubCategoryFragment.OnFragmentInteractionListener,
        ListCourseFragment.OnFragmentInteractionListener,
        ShowResultSearchFragment.OnFragmentInteractionListener,
        SelectFilterDialog.FilterDialogListener,
        SelectSortDialog.SortDialogListener{

    private ConnectivityReceiver connectivityReceiver = new ConnectivityReceiver();

    @BindView(R.id.bottom_nav)
    BottomNavigationView bottomNavigationView;
    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout rootLayout;


    Snackbar snackbar;
    public static final String TAG = "MainActivity";
    private boolean isDestroy = false;
    private final Fragment homeFragment = new HomeFragment();
    private final Fragment myCourseFragment = new MyCourseFragment();
    private final Fragment searchFragment = new SearchFragment();
    private final Fragment accountFragment = new AccountFragment();


//    @Override
//    protected void attachBaseContext(Context newBase) {
//        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        isDestroy = false;

        Menu menu = bottomNavigationView.getMenu();
        selectBottomNavFragment(menu.getItem(3));
        menu.getItem(3).setChecked(true);

        Log.e(TAG, "onCreate");

        setupView();
        handleIntent();


    }

    private void handleIntent() {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.flContent);
        if (f != null)
            Log.e(TAG, f.toString());
        if (getIntent().getStringExtra(KEY_OPEN_FROM_NOTIFICATION) != null) {

            if (!(f instanceof MyCourseFragment))
                {
                if (MyApplication.prefHelper.getWhichMenuActive() == MY_COURSE_ITEM) {
                    loadFragmentWithoutStack(R.id.flContent, new MyCourseFragment(), ConstantUtil.TAG_MAIN_ACTIVITY);
                    Menu menu = bottomNavigationView.getMenu();
                    MenuItem item = menu.getItem(2);
                    item.setChecked(true);
                }
            }
        }


    }

    private void setupView() {


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectBottomNavFragment(item);
                return false;
            }
        });

    }

    private void selectBottomNavFragment(MenuItem item) {
        item.setChecked(true);
        int id = item.getItemId();
        switch (id) {
            case R.id.accountItem:
                loadFragmentWithoutStack(R.id.flContent, accountFragment, ConstantUtil.TAG_MAIN_ACTIVITY);
                break;
            case R.id.homeItem:
                loadFragmentWithoutStack(R.id.flContent, homeFragment, ConstantUtil.TAG_MAIN_ACTIVITY);
                break;
            case R.id.searchItem:
                loadFragmentWithoutStack(R.id.flContent, searchFragment, ConstantUtil.TAG_MAIN_ACTIVITY);
                break;
            case R.id.myCourseItem:
                    loadFragmentWithoutStack(R.id.flContent, myCourseFragment, ConstantUtil.TAG_MAIN_ACTIVITY);
                break;

        }

    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (!isConnected) {
            showSnackBar();
        } else {
            if (snackbar != null) {
                snackbar.dismiss();
                Log.e(TAG,"dismiss sandkbar");
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(connectivityReceiver, intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(connectivityReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.getInstance().setConnectivityListener(this);
    }

    private void showSnackBar() {
        snackbar = Snackbar
                .make(rootLayout, getString(R.string.no_connection_internet), Snackbar.LENGTH_LONG)
                .setDuration(BaseTransientBottomBar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor(Color.RED);
        View sbView = snackbar.getView();
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams)
                sbView.getLayoutParams();
       // params.setMargins(0, 0, 0, 100);
        params.setMargins(0, 0, 0, bottomNavigationView.getHeight());
        sbView.setLayoutParams(params);
        TextView textView = sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setGravity(Gravity.CENTER_HORIZONTAL & Gravity.CENTER_VERTICAL);

        snackbar.show();

    }

    public Snackbar getSnackbar() {
        return snackbar;
    }

    public BottomNavigationView getBottomNavigationView(){
        return bottomNavigationView;
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.flContent);
        if (count == 0 && !(f instanceof HomeFragment)) {
            bottomNavigationView.setSelectedItemId(R.id.homeItem);
            loadFragmentWithoutStack(R.id.flContent, new HomeFragment(), ConstantUtil.TAG_MAIN_ACTIVITY);

        } else if (f instanceof HomeFragment) {
            super.onBackPressed();

        }
        else {
            getSupportFragmentManager().popBackStack();
        }

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        isDestroy = true;
    }

    @Override
    public void onFilterChanged(boolean price, String category, String duration) {

    }

    @Override
    public void onTypeSortChanged(String selectedItem) {

    }

}


