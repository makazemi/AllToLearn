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


public class RegisterFragment extends BaseFragment {

    private OnFragmentInteractionListener mListener;
    private static final String TAG ="RegisterFragment";
    public static final String KEY_USER ="KEY_USER";
    private View rootView;
    @BindView(R.id.edtName)
    EditText edtName;
    @BindView(R.id.edtUsername)
    EditText edtEmail;
    @BindView(R.id.edtPassword)
    EditText edtPassword;
    @BindView(R.id.btnRegister)
    Button btnRegister;
    @BindView(R.id.btnSignUpGoogle)
    Button btnGoogle;
    @BindView(R.id.progressBar)
    AVLoadingIndicatorView progressBar;
    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;
    private Snackbar snackbar;
    private LoginViewModel loginViewModel;
    private User user;

    public RegisterFragment() {
        // Required empty public constructor
    }


    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(rootView==null) {
            rootView = inflater.inflate(R.layout.fragment_register, container, false);
        }
        ButterKnife.bind(this,rootView);
        loginViewModel= ViewModelProviders.of(this).get(LoginViewModel.class);
        return rootView;
    }

    private void initView(View rootView){
        edtName=rootView.findViewById(R.id.edtName);
        edtEmail=rootView.findViewById(R.id.edtUsername);
        edtPassword=rootView.findViewById(R.id.edtPassword);
        btnRegister=rootView.findViewById(R.id.btnRegister);
        btnGoogle=rootView.findViewById(R.id.btnSignUpGoogle);
        progressBar=rootView.findViewById(R.id.progressBar);
        coordinatorLayout=rootView.findViewById(R.id.coordinatorLayout);

    }
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
            btnRegister.setEnabled(false);
        }else{
            progressBar.setVisibility(View.GONE);
            btnRegister.setEnabled(true);
        }
    }
    private void updateUI(User user){
        SuccessLoginFragment fragment = new SuccessLoginFragment();
        Bundle args = new Bundle();
        args.putSerializable(KEY_USER,user);
        fragment.setArguments(args);
        loadFragment(R.id.flContent,fragment, ConstantUtil.TAG_SIGNING_ACTIVITY);
    }


    private void showSnackBar() {
        snackbar = Snackbar
                .make(coordinatorLayout, getString(R.string.no_connection_internet), Snackbar.LENGTH_LONG)
                .setDuration(BaseTransientBottomBar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor(Color.RED);
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        snackbar.show();

    }

    @OnClick(R.id.btnRegister)
    public void doRegister(){
        if(!ConnectivityReceiver.isConnected()){
           // showSnackBar();
            return;
        }
        else {
            if(validation())
                register();

        }
    }

    public void register(){
        inProgress(true);
        String token=loginViewModel.getToken(edtName.getText().toString(),
                edtEmail.getText().toString(),edtPassword.getText().toString());
        user=new User();
        user.setName(edtName.getText().toString());
        user.setEmail(edtEmail.getText().toString());
        user.setToken(token);
        loginViewModel.getStatus().observe(this, new Observer<Status>() {
            @Override
            public void onChanged(@Nullable Status status) {
                if(status!=null){
                    Log.e(TAG,status.toString());
                    if(status==Status.SUCCESS){
                        Log.e(TAG,"success");
                        inProgress(false);
                        updateUI(user);

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

    @OnClick(R.id.btnSignUpGoogle)
    public void registerGoogle(){

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
