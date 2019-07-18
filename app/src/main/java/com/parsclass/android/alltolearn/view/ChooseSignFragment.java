package com.parsclass.android.alltolearn.view;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.parsclass.android.alltolearn.R;
import com.parsclass.android.alltolearn.Utils.ConstantUtil;
import com.parsclass.android.alltolearn.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ChooseSignFragment extends BaseFragment {


    private OnFragmentInteractionListener mListener;
    private View rootView;

    public ChooseSignFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(rootView==null) {
            rootView = inflater.inflate(R.layout.fragment_choose_sign, container, false);
        }
        ButterKnife.bind(this,rootView);
        //initView(rootView);
        return rootView;
    }

    @OnClick(R.id.btnSignEmail)
    public void signInWithEmail(){
        loadFragment(R.id.flContent,new SignEmailFragment(),ConstantUtil.TAG_SIGNING_ACTIVITY);
    }

    @OnClick(R.id.btnSignGoogle)
    public void signInWithGoogle(){

    }

    @OnClick(R.id.txtRegister)
    public void createAccount(){
        loadFragment(R.id.flContent,new RegisterFragment(), ConstantUtil.TAG_SIGNING_ACTIVITY);
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
