package com.parsclass.android.alltolearn.view;

import android.app.DownloadManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import androidx.annotation.Nullable;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.core.content.FileProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.offline.Download;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.Util;
import com.parsclass.android.alltolearn.BuildConfig;
import com.parsclass.android.alltolearn.Interfaces.CurrentWindowListener;
import com.parsclass.android.alltolearn.Interfaces.DownloadMediaListener;
import com.parsclass.android.alltolearn.Interfaces.ProgressDownloadDocListener;
import com.parsclass.android.alltolearn.R;
import com.parsclass.android.alltolearn.Utils.DownloadBroadcastReceiver;
import com.parsclass.android.alltolearn.Utils.GlideApp;
import com.parsclass.android.alltolearn.Utils.MapPosition;
import com.parsclass.android.alltolearn.adapter.TabFragmentAdapter;
import com.parsclass.android.alltolearn.base.BaseExoWithServiceActivity;
import com.parsclass.android.alltolearn.config.MyApplication;
import com.parsclass.android.alltolearn.model.Course;
import com.parsclass.android.alltolearn.model.LectureList;
import com.parsclass.android.alltolearn.viewmodel.LectureViewModel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.parsclass.android.alltolearn.Utils.ConstantUtil.DOWNLOAD_DOCUMENT_CONTENT_DIRECTORY;
import static com.parsclass.android.alltolearn.Utils.ConstantUtil.MESSAGE_PROGRESS;
import static com.parsclass.android.alltolearn.Utils.ConstantUtil.TYPE_SECTION;

public class DetailMyCourseActivity extends BaseExoWithServiceActivity implements
        ListLectureFragment.OnFragmentInteractionListener,
        MyCourseFragment.OnFragmentInteractionListener,
        DetailCourseFragment.OnFragmentInteractionListener,
        ProgressDownloadDocListener,
        DownloadBroadcastReceiver.DownloadReceiverListener {

    private static final String TAG = "DetailMyCourseActivity";
    private CurrentWindowListener currentWindowListener;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TextView txtTitleCourse;
    private TextView txtInstructor;
    private Course currentCourse;
    private TabFragmentAdapter fragmentAdapter;
    private ListLectureFragment listLectureFragment;
    private AboutCourseFragment aboutCourseFragment;
    private LectureViewModel lectureViewModel;
    private ArrayList<Uri> listUri = new ArrayList<>();
    private Button btnOpenDoc;
    private ImageView imgBackgroundPlayer;
    private List<LectureList> listLectures = new ArrayList<>();
    private long downloadID;
    //private String  downloadID;
    DownloadManager downloadManager;
    private ArrayList<Long> keyDownloadsFile = new ArrayList<>();
    //private ArrayList<String> keyDownloadsFile=new ArrayList<>();
    private TextView txtNoInternet;
    private DownloadBroadcastReceiver downloadBroadcastReceiver;
    private ArrayList<Integer> keyProgressDownload = new ArrayList<>();
    private ProgressDownloadDocListener progressDownloadDocListener;

    private DownloadMediaListener downloadMediaListener;


    private MenuItem preferExtensionDecodersMenuItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        initView();

        //lectureViewModel.saveLecture(currentCourse.getId());

        getSection();
        //registerDownloadReceiver();

        super.isDestroyed = false;

        //Log.e(TAG,"onCreate");

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);


        if (getIntent().getIntExtra(KEY_OPEN_FROM_NOTIFICATION, 0) == 88) {
            Log.e(TAG, "KEY_OPEN_FROM_NOTIFICATION");
        }

        setProgressDownloadDocListener(this);

    }

    @Override
    public ArrayList<Uri> getUris() {
        //Log.e(TAG,"getUris in mainactivtity");
        return listUri;
    }

    @Override
    public ArrayList<LectureList> getModels() {
        List<LectureList> a = lectureViewModel.getFakeLecture(currentCourse.getId()).getValue();
        ArrayList<LectureList> lists = new ArrayList<>(a);
       // ArrayList<LectureList> lists = new ArrayList<>(listLectures);

        return lists;
    }

    private void getSection() {
       // lectureViewModel.saveLecture(currentCourse.getId());

        lectureViewModel.getFakeLecture(currentCourse.getId()).observe(this, new Observer<List<LectureList>>() {
            @Override
            public void onChanged(@Nullable List<LectureList> lectureLists) {
                //Log.e(TAG,"onChanged");
                if (lectureLists != null)
                    listLectures.addAll(lectureLists);
                for (int i = 0; i < lectureLists.size(); i++) {
                    if (lectureLists.get(i).getTypeLecture_lectureList() != TYPE_SECTION) {
                        Uri uri = Uri.parse(lectureLists.get(i).getUrlPath_lectureList());
                        //listUri.add(uri);
                    }
                }




                long temp = 0;
                for (int i = 0; i < listLectures.size(); i++) {
                    keyDownloadsFile.add(temp);
                    keyProgressDownload.add(-1);
                }

            }
        });

        List<LectureList> a = lectureViewModel.getFakeLecture(currentCourse.getId()).getValue();
        for (LectureList sample : a) {
            if (sample.getTypeLecture_lectureList() != TYPE_SECTION) {
                Uri uri = Uri.parse(sample.getUrlPath_lectureList());
                listUri.add(uri);
            }
        }

    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_detail_my_course;
    }

    @Override
    public void changeCurrentWindow(int currentWindow, long currentPosition,int reason,boolean windowChanged) {
        Log.e(TAG, "current window " + currentWindow);
        if (currentWindowListener != null) {
            currentWindowListener.getCurrentWindow(currentWindow, currentPosition,reason,windowChanged);
        }


    }

    @Override
    public String getImageCourse() {
        return currentCourse.getImagePath();
    }

    @Override
    public Course getCurrentCourse() {
        return currentCourse;
    }

    @Override
    public void setVisibilityOpenDoc(boolean visible) {
        txtNoInternet.setVisibility(View.GONE);
        if (visible) {
            btnOpenDoc.setVisibility(View.VISIBLE);
            imgBackgroundPlayer.setVisibility(View.VISIBLE);
            GlideApp.with(this)
                    .load(currentCourse.getImagePath())
                    .placeholder(R.drawable.img_itm_1_start_viewpager)
                    .error(R.drawable.img_category_health)
                    .into(imgBackground);
        } else {
            btnOpenDoc.setVisibility(View.GONE);
            imgBackgroundPlayer.setVisibility(View.GONE);
        }
    }

    @Override
    public void setVisibilityNetworkState(boolean isConnect) {
        btnOpenDoc.setVisibility(View.GONE);
        if (!isConnect) {
            txtNoInternet.setVisibility(View.VISIBLE);
            imgBackgroundPlayer.setVisibility(View.VISIBLE);
            GlideApp.with(this)
                    .load(currentCourse.getImagePath())
                    .placeholder(R.drawable.img_itm_1_start_viewpager)
                    .error(R.drawable.img_category_health)
                    .into(imgBackground);
        } else {
            txtNoInternet.setVisibility(View.GONE);
            imgBackgroundPlayer.setVisibility(View.GONE);
        }
    }


    private void initView() {
        registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));


        txtTitleCourse = findViewById(R.id.txtTitle);
        txtInstructor = findViewById(R.id.txtNameInstructor);
        viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tabLayout);
        lectureViewModel = ViewModelProviders.of(this).get(LectureViewModel.class);
        currentCourse = (Course) getIntent().getSerializableExtra(HomeFragment.KEY_DETAIL_COURSE_ACTIVITY);
        if (!TextUtils.isEmpty(currentCourse.getTitle()))
            txtTitleCourse.setText(currentCourse.getTitle());
        if (!TextUtils.isEmpty(currentCourse.getInstructorName()))
            txtInstructor.setText(currentCourse.getInstructorName());
        Log.e(TAG,"insturco: "+currentCourse.getInstructorName());


        fragmentAdapter = new TabFragmentAdapter(getSupportFragmentManager());

        Bundle bundle = new Bundle();
        bundle.putSerializable(HomeFragment.KEY_DETAIL_COURSE_ACTIVITY, currentCourse);
        listLectureFragment = new ListLectureFragment();
        listLectureFragment.setArguments(bundle);
        fragmentAdapter.addFragment(listLectureFragment, getString(R.string.title_tab_list_lecture));
        aboutCourseFragment = new AboutCourseFragment();
        aboutCourseFragment.setArguments(bundle);
        fragmentAdapter.addFragment(aboutCourseFragment, getString(R.string.title_tab_about_course));
        viewPager.setAdapter(fragmentAdapter);
        viewPager.setCurrentItem(0);
        tabLayout.setupWithViewPager(viewPager);

        btnOpenDoc = findViewById(R.id.btn_open_doc);
        imgBackgroundPlayer = findViewById(R.id.img_background_player);
        txtNoInternet = findViewById(R.id.txt_not_internet);

        //downloadBroadcastReceiver=new DownloadBroadcastReceiver();
    }

    public void openDocOnclick(View view) {


        ArrayList<Integer> start = new ArrayList<>();

        for (int i = 0; i < listLectures.size(); i++) {
            if (listLectures.get(i).getTypeLecture_lectureList() == TYPE_SECTION) {
                start.add(i);
            }
        }

        MapPosition mapPosition = new MapPosition(start, listLectures.size());
        int position = mapPosition.getPosition(getCurrentWindow());


        Log.e(TAG, "openDocOnclick position: " + position);

        String titleLecture = listLectures.get(position).getTitle_lectureList();
        String fileName = titleLecture;

        if (isDownloadedDoc(position)) {
            openDoc(fileName);
            Log.e(TAG, "isDownloaded");
        } else {
            if (isRunningDownload(position)) {
                Toast.makeText(this, getString(R.string.toast_download_is_running), Toast.LENGTH_SHORT).show();
                return;
            }
            if (!isOnline()) {
                Toast.makeText(this, getString(R.string.no_connection_internet), Toast.LENGTH_SHORT).show();
                return;
            }
            downloadDoc(position, fileName);
        }

    }

    private BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Fetching the download id received with the broadcast
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

            Toast.makeText(DetailMyCourseActivity.this,getString(R.string.download_completed), Toast.LENGTH_SHORT).show();
            Log.e(TAG, "onDownloadComplete");
            int position = keyDownloadsFile.indexOf(id);
            String titleLecture = listLectures.get(position).getTitle_lectureList();
            String fileName = titleLecture;
            openDoc(fileName);
            if (progressDownloadDocListener != null) {
                Log.e(TAG, "download complete");
                progressDownloadDocListener.onProgressDownloadDocComplete(position, 100);
            }

//            for (int i=0;i<listLectures.size();i++){
//                if(keyDownloadsFile.get(i)==id){
//                    String titleLecture=listLectures.get(i).getTitle_lectureList();
//                    String fileName=titleLecture;
//                    openDoc(fileName);
//                    if(progressDownloadDocListener!=null) {
//                        Log.e(TAG, "download complete");
//                        progressDownloadDocListener.onProgressDownloadDocComplete(i, 100);
//                    }
//                }
//            }

        }
    };

    private void downloadDoc(int position, String fileName) {
        Log.e(TAG, "in downloadDoc");
        String url = listLectures.get(position).getUrlPath_lectureList();
        String titleLecture = listLectures.get(position).getTitle_lectureList();
        String filePath = titleLecture + ".pdf";
        File file = new File(MyApplication.getInstance().getDownloadDirectory()
                , DOWNLOAD_DOCUMENT_CONTENT_DIRECTORY + "/" +
                currentCourse.getTitle() + "/"
                + titleLecture + ".pdf");

        Uri Download_Uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(Download_Uri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setAllowedOverRoaming(false);
        request.setTitle("Downloading");
        request.setDescription(titleLecture);
        request.setVisibleInDownloadsUi(true);
        request.setDestinationUri(Uri.fromFile(file));
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);


        //request.setDestinationInExternalPublicDir(MyApplication.getInstance().getDownloadDirectory(), "/GadgetSaint/"  + "/" + "Sample" + ".png");

        downloadID = downloadManager.enqueue(request);
        keyDownloadsFile.set(position, downloadID);
        if (progressDownloadDocListener != null) {
            Log.e(TAG, "start download");
            progressDownloadDocListener.onProgressDownloadDocComplete(position, 0);
        }

//        Download download=new Download();
//        download.setDownloadId(titleLecture+position);
//        downloadID=download.getDownloadId();
//        keyDownloadsFile.set(position,downloadID);
//
//        Intent intent = new Intent(this, DownloadService.class);
//        intent.putExtra(KEY_FINE_NAME_DOC,filePath);
//        intent.putExtra(KEY_URL_DOC,url);
//        intent.putExtra(KEY_DOWNLOAD_ID_DOC,downloadID);
//        startService(intent);


    }

    private boolean isDownloadedDoc(int position) {
        String titleLecture = listLectures.get(position).getTitle_lectureList();
        String fileName = titleLecture;
        File file = new File(MyApplication.getInstance().getDownloadDirectory(), DOWNLOAD_DOCUMENT_CONTENT_DIRECTORY +
                "/" + currentCourse.getTitle() + "/" +
                fileName + ".pdf");

        if (file.exists()) {
            Log.e(TAG, "file is exist");
            return true;
        } else
            return false;

    }

    private boolean isRunningDownload(int position) {
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(keyDownloadsFile.get(position));
        Cursor cursor = downloadManager.query(query);

        if (cursor.moveToFirst()) {
            int statusIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
            if (DownloadManager.STATUS_RUNNING == cursor.getInt(statusIndex)) {
                return true;
            }

            if (DownloadManager.STATUS_FAILED != cursor.getInt(statusIndex)) {
                Log.e(TAG, "Download Failed");
                if(progressDownloadDocListener!=null){
                    progressDownloadDocListener.onProgressDownloadDocComplete(position,-1000);
                }
            }
        }
        return false;




//        if(keyProgressDownload.get(position)>=0 && keyProgressDownload.get(position)<100)
//            return true;
//
//        return false;

    }

    private void openDoc(String fileName) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        File file = new File(MyApplication.getInstance().getDownloadDirectory(),
                DOWNLOAD_DOCUMENT_CONTENT_DIRECTORY +
                        "/" + currentCourse.getTitle() + "/"
                        + fileName + ".pdf");
        Uri myURI = Uri.parse("");
        if (Util.SDK_INT <= 23) {
            myURI = Uri.fromFile(file);

        } else {
            myURI = FileProvider.getUriForFile(this,
                    BuildConfig.APPLICATION_ID + ".provider", file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.setDataAndType(myURI, "application/pdf");

        startActivity(intent);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void setCurrentWindowListener(CurrentWindowListener listener) {
        this.currentWindowListener = listener;
    }

    public void setProgressDownloadDocListener(ProgressDownloadDocListener listener) {
        this.progressDownloadDocListener = listener;
    }

    public void setDownloadMediaListener(DownloadMediaListener listener){
        this.downloadMediaListener=listener;
    }

    /* this method will be called when user click on download button of each item */
    public void downloadMedia(String name, Uri uri, String extension) {
        if(!MyApplication.getInstance().isOnline()){
            Toast.makeText(this, getString(R.string.no_connection_internet), Toast.LENGTH_SHORT).show();
            return;
        }
            RenderersFactory renderersFactory =
                    MyApplication.getInstance()
                            .buildRenderersFactory(isNonNullAndChecked(preferExtensionDecodersMenuItem));
            downloadTracker.toggleDownload(
                    getSupportFragmentManager(),
                    name,
                    uri,
                    extension,
                    renderersFactory,this);
    }


    private void registerDownloadReceiver() {

        LocalBroadcastManager bManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MESSAGE_PROGRESS);
        bManager.registerReceiver(downloadBroadcastReceiver, intentFilter);

    }

    @Override
    public void onBackPressed() {
        if (getIntent().getIntExtra(KEY_OPEN_FROM_NOTIFICATION, 0) == 88) {
             Log.e(TAG,"KEY_OPEN_FROM_NOTIFICATION");
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(KEY_OPEN_FROM_NOTIFICATION, KEY_OPEN_FROM_NOTIFICATION);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
        super.onBackPressed();

    }

    @Override
    public void onDownloadsChanged(com.google.android.exoplayer2.offline.DownloadManager downloadManager, Download download) {
        int index=listUri.indexOf(download.request.uri);
        Log.e(TAG,"onDownloadsChanged index= "+index);
        if(downloadMediaListener!=null){
            downloadMediaListener.onDownloadMediaChanged(downloadManager,download,index);

        }

        //Log.e(TAG,"in activity taskid: ");
        //Log.e(TAG,"in activty dowoladbyte: "+downloadByte);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(onDownloadComplete);
        super.isDestroyed = true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        //registerDownloadReceiver();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // unregisterReceiver(downloadBroadcastReceiver);
    }

    @Override
    public void onProgressChanged(int progress, String downloadId) {
//        int position=keyDownloadsFile.indexOf(downloadId);
//        keyProgressDownload.set(position,progress);
//        Log.e(TAG,"Detprogress: "+progress);
//        if(progressDownloadDocListener!=null){
//            progressDownloadDocListener.onProgressDownloadDocChanged(position,progress,downloadId);
//        }
    }

    @Override
    public void onDownloadCompleted(String downloadId) {
//        for (int i=0;i<listLectures.size();i++){
//            if(keyDownloadsFile.get(i)==downloadId){
//                String titleLecture=listLectures.get(i).getTitle_lectureList();
//                String fileName=titleLecture;
//                openDoc(fileName);
//            }
//        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        //MyApplication.getInstance().setProgressDownloadDocListener(this);
    }

    @Override
    public void onProgressDownloadDocComplete(int position, int progress) {

    }

    private static boolean isNonNullAndChecked(@Nullable MenuItem menuItem) {
        // Temporary workaround for layouts that do not inflate the options menu.
        return menuItem != null && menuItem.isChecked();
    }

}
