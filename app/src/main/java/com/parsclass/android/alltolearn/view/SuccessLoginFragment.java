package com.parsclass.android.alltolearn.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.airbnb.lottie.LottieAnimationView;
import com.parsclass.android.alltolearn.R;
import com.parsclass.android.alltolearn.model.User;


public class SuccessLoginFragment extends Fragment {


    private OnFragmentInteractionListener mListener;
    private View rootView;
    final int SPLASH_TIME_OUT = 4000;
    private User user;

    public SuccessLoginFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(rootView==null) {
            rootView = inflater.inflate(R.layout.fragment_success_login, container, false);
        }
        user=(User)getArguments().getSerializable(SignEmailFragment.KEY_USER);

        LottieAnimationView animationView=rootView.findViewById(R.id.animation_view);
        animationView.setScale(0.2f);


        waitMillisecond();
        return rootView;
    }

    private void waitMillisecond(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getActivity().finish();
                Intent intent=new Intent(getActivity(),MainActivity.class);
                intent.putExtra(SignEmailFragment.KEY_USER,user);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        }, SPLASH_TIME_OUT);
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
