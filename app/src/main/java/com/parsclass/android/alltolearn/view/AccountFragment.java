package com.parsclass.android.alltolearn.view;

import android.Manifest;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.parsclass.android.alltolearn.R;
import com.parsclass.android.alltolearn.Utils.GlideApp;
import com.parsclass.android.alltolearn.base.BaseFragment;
import com.parsclass.android.alltolearn.config.MyApplication;
import com.parsclass.android.alltolearn.model.User;
import com.parsclass.android.alltolearn.viewmodel.LoginViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

import static com.parsclass.android.alltolearn.Utils.ConstantUtil.EXTERNAL_LOCATION_DOWNLOAD;
import static com.parsclass.android.alltolearn.Utils.ConstantUtil.LOCAL_LOCATION_DOWNLOAD;


public class AccountFragment extends BaseFragment implements EasyPermissions.PermissionCallbacks {

    public static final String TAG="AccountFragment";
    private OnFragmentInteractionListener mListener;
    private View rootView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView txtTitleToolbar;
    @BindView(R.id.ViewInstructor)
    RelativeLayout cardViewInstructor;
    @BindView(R.id.ViewSetting)
    RelativeLayout cardViewSetting;
    @BindView(R.id.ViewPolicy)
    RelativeLayout cardViewPolicy;
    @BindView(R.id.txtName)
    TextView txtName;
    @BindView(R.id.txtSign)
    TextView txtSign;
    @BindView(R.id.switchDSD)
    Switch switchDSD;
    private LoginViewModel loginViewModel;
    private final int STORAGE_PERMISSION_CODE=1;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;
    @BindView(R.id.imgAvatar)
    ImageView imgAvatar;
    private User currentUser=new User();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG,"oncreate fra");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e(TAG,"onCreateView fra");
        if(rootView==null) {
            rootView = inflater.inflate(R.layout.fragment_account, container, false);
        }
        ButterKnife.bind(this,rootView);


       // initView();
        setLocationDownload();
        return rootView;
    }

    private void setupToolbar(){

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                if(Math.abs(i) >= appBarLayout.getTotalScrollRange()){
                    imgAvatar.setVisibility(View.GONE);
                    txtTitleToolbar.setVisibility(View.VISIBLE);
                }else {
                    imgAvatar.setVisibility(View.VISIBLE);
                    txtTitleToolbar.setVisibility(View.GONE);

                }
            }
        });
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

        collapsingToolbarLayout.setTitleEnabled(false);

        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        collapsingToolbarLayout.setContentScrimColor(getResources().getColor(R.color.colorPrimary));
        collapsingToolbarLayout.setStatusBarScrimColor(getResources().getColor(R.color.colorTransparent));

    }

    private void initView(){

        setupToolbar();
        loginViewModel= ViewModelProviders.of(this).get(LoginViewModel.class);

        if(loginViewModel==null)
            Log.e(TAG,"loginViewModel==null");

        if(MyApplication.prefHelper.isUserLogin()){
            loginViewModel.getUserFake("user@gmail.com","").observe(this, new Observer<User>() {
                @Override
                public void onChanged(@Nullable User user) {
                    currentUser=user;
                }
            });
            txtName.setVisibility(View.VISIBLE);
            cardViewInstructor.setVisibility(View.VISIBLE);
            cardViewSetting.setVisibility(View.VISIBLE);
            cardViewPolicy.setVisibility(View.VISIBLE);
            txtSign.setText(getString(R.string.txt_sign_out));
            if(MyApplication.prefHelper.getUserLocationDownload().equals(EXTERNAL_LOCATION_DOWNLOAD))
                switchDSD.setChecked(true);
            else
                switchDSD.setChecked(false);
            GlideApp.with(getActivity())
                    .load(currentUser.getImagePath())
                    .placeholder(R.drawable.avator_placeholder)
                    .error(R.drawable.avator_placeholder)
                    .into(imgAvatar);


            Log.e(TAG,"isUserLogin: "+MyApplication.prefHelper.isUserLogin());
        }else {
            txtName.setVisibility(View.GONE);
            cardViewInstructor.setVisibility(View.GONE);
            cardViewSetting.setVisibility(View.GONE);
            cardViewPolicy.setVisibility(View.VISIBLE);
            txtSign.setText(getString(R.string.txt_sign_in));

            GlideApp.with(getActivity())
                    .load(R.drawable.avator_placeholder)
                    .placeholder(R.drawable.avator_placeholder)
                    .error(R.drawable.avator_placeholder)
                    .into(imgAvatar);

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(dpToPx(10), dpToPx(60), dpToPx(10), dpToPx(70));
            cardViewPolicy.setLayoutParams(params);

            Log.e(TAG,"isUserLogin: "+MyApplication.prefHelper.isUserLogin());
        }
    }

    private void setLocationDownload(){
        switchDSD.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    if(!isSDCardPresent()){
                        switchDSD.setChecked(false);
                        Toast.makeText(getContext(),getString(R.string.toast_have_not_sd_card),Toast.LENGTH_LONG);
                        return;
                    }
                    checkPermission();
                    MyApplication.prefHelper.putLocationDownload(EXTERNAL_LOCATION_DOWNLOAD);
                    Log.e(TAG,"ischecked=true");
                }else {
                    MyApplication.prefHelper.putLocationDownload(LOCAL_LOCATION_DOWNLOAD);
                }
            }
        });
    }

    @AfterPermissionGranted(STORAGE_PERMISSION_CODE)
    private void checkPermission(){
        String[] prems={Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};
        if(EasyPermissions.hasPermissions(getContext(),prems)){
            Log.e(TAG,"EasyPermissions.hasPermissions(getContext()");

        }else {
            Log.e(TAG,"EasyPermissions.hasNotPermissions(getContext()");
            EasyPermissions.requestPermissions(getActivity(),getString(R.string.dialog_we_need_permistion_to_write_storage),STORAGE_PERMISSION_CODE,prems);
        }
    }

    private boolean isSDCardPresent(){
        Boolean isSDPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        Boolean isSDSupportedDevice = Environment.isExternalStorageRemovable();

        if(isSDPresent)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    @OnClick(R.id.txtBeInstructor)
    public void becomeInstructor(){

    }

    @OnClick(R.id.txtShareApp)
    public void shareApp(){
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String content=getString(R.string.content_share_text_app)+"\n"+getString(R.string.link_site);
        shareIntent.putExtra(Intent.EXTRA_TEXT,content);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.title_share_text_course));
        startActivity(Intent.createChooser(shareIntent, "Share..."));
    }
    @OnClick(R.id.txtSign)
    public void signInOut(){
//        if(MyApplication.prefHelper.isUserLogin()){
//            loginViewModel.signOutUser();
//            // show progress bar and reload app
//        }
//        else {
//            startActivity(new Intent(getActivity(),SigningActivity.class));
//        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        builder.setMessage(getString(R.string.txt_content_dialog_sign_out));
        builder.setTitle(getString(R.string.txt_sign_out));
        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(MyApplication.prefHelper.isUserLogin()) {
                    loginViewModel.signOutUser();
                    getActivity().finish();
                    Intent intent=new Intent(getActivity(),StartActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                return;
            }
        });
        final AlertDialog dialog = builder.create();
        if(MyApplication.prefHelper.isUserLogin()) {
            dialog.show();
        }else {
            startActivity(new Intent(getActivity(),StartActivity.class));
        }
//        if(MyApplication.prefHelper.isUserLogin()) {
//            loginViewModel.signOutUser();
//        }
      //  startActivity(new Intent(getActivity(),StartActivity.class));
    }

    @OnClick(R.id.txtChooseLang)
    public void chooseLangOnclick(){
        MyApplication.getInstance().chooseDialog(getActivity());
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        Log.e(TAG,"onPermissionsGranted");

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Log.e(TAG,"onPermissionsDenied");
        if(EasyPermissions.somePermissionPermanentlyDenied(getActivity(),perms)){
            new AppSettingsDialog.Builder(this).build().show();

            Log.e(TAG,"somePermissionPermanentlyDenied");

        }
        switchDSD.setChecked(false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE){
            Log.e(TAG,"requestCode==AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e(TAG,"onAttach fra");
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
        Log.e(TAG,"ondetAttach fra");
        mListener = null;
    }



    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this);
    }



    @Override
    public void onStart() {
        super.onStart();
        Log.e(TAG,"onstart fra");
        initView();
    }




}
