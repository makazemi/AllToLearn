package com.parsclass.android.alltolearn.base;

import android.content.res.Resources;
import android.os.Build;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import android.util.TypedValue;
import android.view.View;

import com.parsclass.android.alltolearn.R;

public class BaseFragment extends Fragment {

    public void loadFragment(int layoutId,Fragment fragment,String tag){
        FragmentManager manager = getActivity().getSupportFragmentManager();
        manager.beginTransaction()
                .addToBackStack("f")
                .replace(layoutId,fragment,tag)
                .commit();
    }

    public void addFragment(int layoutId,Fragment fragment,String tag){
        FragmentManager manager = getActivity().getSupportFragmentManager();
        manager.beginTransaction()
                .addToBackStack("f")
                .add(layoutId,fragment,tag)
                .commit();
    }

    public void removeFragment(String tag){
        FragmentManager manager = getActivity().getSupportFragmentManager();
        Fragment fragment = manager.findFragmentByTag(tag);
        manager.beginTransaction()
                .remove(fragment)
                .commit();
    }

    public void loadFragmentWithTransitionLTR(int layoutId, Fragment fragment, String tag){

        FragmentManager manager = getActivity().getSupportFragmentManager();
        manager.beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                .addToBackStack("f")
                .replace(layoutId,fragment,tag)
                .commit();
    }

    public void loadFragmentWithTransitionRTL(int layoutId, Fragment fragment, String tag){

        FragmentManager manager = getActivity().getSupportFragmentManager();
        manager.beginTransaction()
                .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left)
                .addToBackStack("f")
                .replace(layoutId,fragment,tag)
                .commit();

    }

    public void loadFragmentWithTransitionDetail(int layoutId, Fragment fragment, String tag, String transitionName, View view){

        FragmentManager manager = getActivity().getSupportFragmentManager();
        manager.beginTransaction()
                .addSharedElement(view,transitionName)
                .addToBackStack("f")
                .replace(layoutId,fragment,tag)
                .commit();
    }

    public void loadFragmentWithoutBackStack(int layoutId,Fragment fragment,String tag){
        FragmentManager manager = getActivity().getSupportFragmentManager();
        manager.beginTransaction().replace(layoutId,fragment,tag).commit();

    }

    public void refreshFragment(){
        Fragment frg = null;
        frg =getActivity().getSupportFragmentManager().findFragmentById(R.id.flContent);
        final FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.detach(frg);
        ft.attach(frg);
        ft.commit();
    }

    public int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public DividerItemDecoration getDivider(){
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { //>= API 21
            itemDecoration.setDrawable(getActivity().getDrawable(R.drawable.divider));
        } else {
            itemDecoration.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.divider));
        }

        return itemDecoration;
    }
}
