package com.parsclass.android.alltolearn.view;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.parsclass.android.alltolearn.R;
import com.parsclass.android.alltolearn.Utils.ConnectivityReceiver;
import com.parsclass.android.alltolearn.Utils.ConstantUtil;
import com.parsclass.android.alltolearn.base.BaseActivity;
import com.parsclass.android.alltolearn.config.MyApplication;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SigningActivity extends BaseActivity implements
ChooseSignFragment.OnFragmentInteractionListener,
SignEmailFragment.OnFragmentInteractionListener,
RegisterFragment.OnFragmentInteractionListener,
SuccessLoginFragment.OnFragmentInteractionListener,
        ConnectivityReceiver.ConnectivityReceiverListener{

    private static final String TAG ="SigningActivity" ;
    @BindView(R.id.root_layout)
    CoordinatorLayout rootLayout;
    private Snackbar snackbar;

    private ConnectivityReceiver connectivityReceiver = new ConnectivityReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_signing);

        ButterKnife.bind(this);

        loadFragmentWithoutStack(R.id.flContent,new ChooseSignFragment(),ConstantUtil.TAG_SIGNING_ACTIVITY);
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onBackPressed() {
        int count=getSupportFragmentManager().getBackStackEntryCount();
        Fragment f = getSupportFragmentManager().findFragmentByTag(ConstantUtil.TAG_SIGNING_ACTIVITY);

      if(f instanceof ChooseSignFragment) {
            Log.e(TAG,"f instanceof ChooseSignFragment count= "+count+" f= "+f.toString());
            finish();
            startActivity(new Intent(this,StartActivity.class));
        }
        else if(f instanceof SuccessLoginFragment){
            return;
      }
        else {
            Log.e(TAG,"count>0 and count= "+count+" f= "+f.toString());
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (!isConnected) {
            showSnackBar();
            // Log.e(TAG, "in interface: " + isConnected);
        } else {
            if (snackbar != null)
                snackbar.dismiss();
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

    public void loadFragmentWithoutStack(int layoutId, Fragment fragment, String tag) {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(layoutId, fragment, tag).commit();
    }

    protected void showSnackBar() {
        snackbar = Snackbar
                .make(rootLayout, getString(R.string.no_connection_internet), Snackbar.LENGTH_LONG)
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
