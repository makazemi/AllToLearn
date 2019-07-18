package com.parsclass.android.alltolearn.view;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parsclass.android.alltolearn.R;
import com.parsclass.android.alltolearn.Utils.ConnectivityReceiver;
import com.parsclass.android.alltolearn.Utils.ConstantUtil;
import com.parsclass.android.alltolearn.base.BaseFragment;
import com.parsclass.android.alltolearn.model.User;
import com.parsclass.android.alltolearn.remote.Status;
import com.parsclass.android.alltolearn.viewmodel.LoginViewModel;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SignEmailFragment extends BaseFragment {

    private static final String TAG ="SignEmailFragment";
    public static final String KEY_USER ="KEY_USER";
    private OnFragmentInteractionListener mListener;
    private View rootView;
    @BindView(R.id.edtUsername)
    EditText edtEmail;
    @BindView(R.id.edtPassword)
    EditText edtPassword;
    @BindView(R.id.txtForgetPassword)
    TextView txtForget;
    @BindView(R.id.btnLogin)
    Button btnLogin;
    @BindView(R.id.txtRegister)
    TextView txtRegister;
    @BindView(R.id.progressBar)
    AVLoadingIndicatorView progressBar;
    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;
    private Snackbar snackbar;

    private LoginViewModel loginViewModel;
    private LoginListener loginListener;

    public SignEmailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(rootView==null) {
            rootView = inflater.inflate(R.layout.fragment_sing_email, container, false);
        }
        ButterKnife.bind(this,rootView);
        loginViewModel= ViewModelProviders.of(this).get(LoginViewModel.class);
        //initView(rootView);

        return rootView;
    }

//    private void initView(View rootView){
//        edtEmail=rootView.findViewById(R.id.edtUsername);
//        edtPassword=rootView.findViewById(R.id.edtPassword);
//        btnLogin=rootView.findViewById(R.id.btnLogin);
//        txtForget=rootView.findViewById(R.id.txtForgetPassword);
//        progressBar=rootView.findViewById(R.id.progressBar);
//        coordinatorLayout=rootView.findViewById(R.id.coordinatorLayout);
//        loginViewModel= ViewModelProviders.of(this).get(LoginViewModel.class);
//
//    }
    private boolean validation(){
        if(TextUtils.isEmpty(edtEmail.getText().toString())){
            edtEmail.setError(getString(R.string.required));
            return false;
        }
        if(TextUtils.isEmpty(edtPassword.getText().toString())){
            edtPassword.setError(getString(R.string.required));
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(edtEmail.getText().toString()).matches()) {
            edtEmail.setError(getString(R.string.input_error_email_invalid));
            edtEmail.requestFocus();
            return false;
        }
        if (edtPassword.length() < 6) {
            edtPassword.setError(getString(R.string.input_error_password_length));
            edtPassword.requestFocus();
            return false;
        }

        return true;
    }
    private void inProgress(boolean x){
        if(x){
            progressBar.setVisibility(View.VISIBLE);
            btnLogin.setEnabled(false);
        }else{
            progressBar.setVisibility(View.GONE);
            btnLogin.setEnabled(true);
        }
    }

    @OnClick(R.id.btnLogin)
    public void doLogin(){
        if(!ConnectivityReceiver.isConnected()){
            //showSnackBar();
            return;
        }
        else {
            if(validation())
                login();

        }
    }

    @OnClick(R.id.txtRegister)
    public void doRegister(){
        loadFragment(R.id.flContent,new RegisterFragment(), ConstantUtil.TAG_SIGNING_ACTIVITY);
    }
    private void login(){
        inProgress(true);
//        loginViewModel.initLogin(edtEmail.getText().toString(),edtPassword.getText().toString());
//        loginViewModel.getUser().observe(this, new Observer<User>() {
//            @Override
//            public void onChanged(@Nullable User user) {
//                if(user!=null){
//                    updateUI(user);
//                }
//            }
//        });
        loginViewModel.getUserFake(edtEmail.getText().toString(),edtPassword.getText().toString())
                .observe(this, new Observer<User>() {
                    @Override
                    public void onChanged(@Nullable User user) {
                        if(user!=null){
                            updateUI(user);
                        }
                    }
                });

        loginViewModel.getStatus().observe(this, new Observer<Status>() {
            @Override
            public void onChanged(@Nullable Status status) {
                if(status!=null){
                    Log.e(TAG,status.toString());
                    if(status==Status.SUCCESS){
                        Log.e(TAG,"success");
                        inProgress(false);


                    }
                    else if(status==Status.ERROR){
                        Log.e(TAG,"Error");
                        inProgress(false);

                    }
                    else {
                        inProgress(true);
                    }
                }
            }
        });
    }
    private void updateUI(User user){
        SuccessLoginFragment fragment = new SuccessLoginFragment();
        Bundle args = new Bundle();
        args.putSerializable(KEY_USER,user);
        fragment.setArguments(args);
        loadFragment(R.id.flContent,fragment,ConstantUtil.TAG_SIGNING_ACTIVITY);
    }


    private void showSnackBar() {
        snackbar = Snackbar
                .make(coordinatorLayout, getString(R.string.no_connection_internet), Snackbar.LENGTH_LONG)
                .setDuration(BaseTransientBottomBar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor(Color.RED);
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        snackbar.show();

    }
    @OnClick(R.id.txtForgetPassword)
    public void resetPassword(){

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
    private void setLoginListener(LoginListener listener){
        this.loginListener=listener;
    }
    public interface LoginListener{
        //void updateUI(User user);
    }
}
