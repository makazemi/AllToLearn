package com.parsclass.android.alltolearn.base;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.parsclass.android.alltolearn.R;
import com.parsclass.android.alltolearn.view.DetailMyCourseActivity;
import com.parsclass.android.alltolearn.view.MainActivity;

public class BaseHideBottomNavFragment extends BaseFragment {

    public static final String TAG="BaseHideBottomNav";
    @Override
    public void onStop() {
        super.onStop();
        showBottomUIMenu();
        setMarginSnackBar(2);
    }

    @Override
    public void onStart() {
        super.onStart();
        hideBottomUIMenu();
        setMarginSnackBar(1);
    }



    protected void hideBottomUIMenu(){
        BottomNavigationView view=getActivity().findViewById(R.id.bottom_nav);
        if(view!=null) {
            view.setVisibility(View.GONE);
            FrameLayout frameLayout = getActivity().findViewById(R.id.flContent);
            frameLayout.setPadding(0, 0, 0, 0);
        }
    }

    protected void showBottomUIMenu(){
        BottomNavigationView view=getActivity().findViewById(R.id.bottom_nav);
        if(view!=null) {
            view.setVisibility(View.VISIBLE);
            FrameLayout frameLayout = getActivity().findViewById(R.id.flContent);
        }
        // frameLayout.setPadding(0,0,0,56);
    }

//    public void loadFragment(int layoutId,Fragment fragment,String tag){
//        FragmentManager manager = getActivity().getSupportFragmentManager();
//        manager.beginTransaction().addToBackStack("f").replace(layoutId,fragment,tag).commit();
//    }

    protected void setMarginSnackBar(int type) {
         Snackbar snackbar=null;
        if (getActivity() instanceof MainActivity)
             snackbar = ((MainActivity) getActivity()).getSnackbar();
        else {
            Log.e(TAG," else not instancs of mainactii");
        }

            if (snackbar != null) {
                View sbView = snackbar.getView();
                CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams)
                        sbView.getLayoutParams();
                if (type == 1) {
                    Log.e(TAG,"type set margin 0");
                    params.setMargins(0, 0, 0, 0);
                }
                else {
                    Log.e(TAG,"type set margin 100");
                    //params.setMargins(0, 0, 0, 100);
                    if(((MainActivity)getActivity()).getBottomNavigationView()!=null)
                    params.setMargins(0, 0, 0, ((MainActivity)getActivity()).getBottomNavigationView().getHeight());
                }
                sbView.setLayoutParams(params);
            } else
                return;


    }

}
