package com.parsclass.android.alltolearn.view;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.exoplayer2.offline.Download;
import com.google.android.exoplayer2.offline.DownloadManager;
import com.google.android.exoplayer2.util.Util;
import com.parsclass.android.alltolearn.Interfaces.CurrentWindowListener;
import com.parsclass.android.alltolearn.Interfaces.DownloadMediaListener;
import com.parsclass.android.alltolearn.Interfaces.ProgressDownloadDocListener;
import com.parsclass.android.alltolearn.R;
import com.parsclass.android.alltolearn.Utils.ConstantUtil;
import com.parsclass.android.alltolearn.Utils.MapPosition;
import com.parsclass.android.alltolearn.adapter.LectureListAdapter;
import com.parsclass.android.alltolearn.base.BaseFragment;
import com.parsclass.android.alltolearn.model.Course;
import com.parsclass.android.alltolearn.model.Lecture;
import com.parsclass.android.alltolearn.model.LectureList;
import com.parsclass.android.alltolearn.viewmodel.LectureViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.parsclass.android.alltolearn.Utils.ConstantUtil.DOWNLOAD_COMPLETED_STATUS;
import static com.parsclass.android.alltolearn.Utils.ConstantUtil.DOWNLOAD_RUNNING_STATUS;
import static com.parsclass.android.alltolearn.Utils.ConstantUtil.TYPE_ARTICLE;
import static com.parsclass.android.alltolearn.Utils.ConstantUtil.TYPE_SECTION;


public class ListLectureFragment extends BaseFragment {

    private OnFragmentInteractionListener mListener;
    public static final String TAG = "ListLectureFragment";
    private View rootView;
    @BindView(R.id.sectionRcy)
    RecyclerView lectureRcy;
    private LectureViewModel lectureViewModel;
    private LectureListAdapter adapter;
    private Course currentCourse;
    private ArrayList<LectureList> lectureArrayList = new ArrayList<>();
    private RecyclerView.SmoothScroller smoothScroller;
    LinearLayoutManager linearLayoutManager;
    ArrayList<Integer> start = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_list_lecture, container, false);
        }
        ButterKnife.bind(this, rootView);
        initView();
        setLectureRcy();
        updateStatusDownloadDoc();
        return rootView;
    }

    private void initView() {
        lectureViewModel = ViewModelProviders.of(this).get(LectureViewModel.class);
        RecyclerView.ItemAnimator animator = lectureRcy.getItemAnimator();
        if (animator instanceof DefaultItemAnimator) {
            ((DefaultItemAnimator) animator).setSupportsChangeAnimations(false);
        }
        //   ViewCompat.setNestedScrollingEnabled(sectionRcy,false);
        linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        lectureRcy.setLayoutManager(linearLayoutManager);


        smoothScroller = new LinearSmoothScroller(getActivity()) {
            @Override
            protected int getVerticalSnapPreference() {
                return LinearSmoothScroller.SNAP_TO_START;
            }
        };

        currentCourse = (Course) getArguments().getSerializable(HomeFragment.KEY_DETAIL_COURSE_ACTIVITY);
        adapter = new LectureListAdapter(getActivity().getApplicationContext());
        lectureRcy.setAdapter(adapter);

    }

    private void setLectureRcy() {

        lectureViewModel.getFakeLecture(currentCourse.getId()).observe(this, new Observer<List<LectureList>>() {
            @Override
            public void onChanged(@Nullable List<LectureList> lectureLists) {
                lectureArrayList.addAll(lectureLists);
                adapter.setValues(lectureArrayList);
                setSectionClickListener();
                getSelectedItem();
            }
        });
    }

    private void setSectionClickListener() {
        adapter.setOnItemClickListener(new LectureListAdapter.OnItemClickListener() {
            @Override
            public void onLectureClick(LectureList lecture, int position) {
                onClickLecture(lecture, position);
            }

            @Override
            public void onDownloadClick(LectureList lecture) {
                ((DetailMyCourseActivity) getActivity()).downloadMedia(lecture.getTitle_lectureList(),
                        Uri.parse(lecture.getUrlPath_lectureList()), lecture.getExtension());
            }
        });
    }

    private void getSelectedItem() {


        for (int i = 0; i < lectureArrayList.size(); i++) {
            if (lectureArrayList.get(i).getTypeLecture_lectureList() == TYPE_SECTION) {
                start.add(i);
            }
        }

        MapPosition mapPosition = new MapPosition(start, lectureArrayList.size());
       // mapPosition.setStart(start);
       // mapPosition.setTotalSize(lectureArrayList.size());

        int startWindow = ((DetailMyCourseActivity) getActivity()).getCurrentWindow();
       // Log.e(TAG, "startWindow in listlecture: " + startWindow);
        if (startWindow != -1) {
            int position = mapPosition.getPosition(startWindow);
            //adapter.updatePlayingLecture(position);
            adapter.updateIsPlaying(position);
            smoothScroller.setTargetPosition(position);
            linearLayoutManager.startSmoothScroll(smoothScroller);
           // adapter.updateIsPlaying(position);
        } else {
            //adapter.updatePlayingLecture(1);
            adapter.updateIsPlaying(1);
            smoothScroller.setTargetPosition(1);
            linearLayoutManager.startSmoothScroll(smoothScroller);
            //adapter.updateIsPlaying(1);
        }


        ((DetailMyCourseActivity) getActivity()).setCurrentWindowListener(new CurrentWindowListener() {
            @Override
            public void getCurrentWindow(int currentWindow, long currentPosition,int reason,boolean windowChanged) {

                int position = mapPosition.getPosition(currentWindow);
                int previousPosition=mapPosition.getPosition(currentWindow-1);
               // adapter.updatePlayingLecture(position);
                if(windowChanged) {
                    adapter.updateIsPlaying(position);
                    smoothScroller.setTargetPosition(position);
                    linearLayoutManager.startSmoothScroll(smoothScroller);
//                    if(reason==0 || (lectureArrayList.get(position).getTypeMedia_lectureList().equals(TYPE_ARTICLE) && reason==1)) {
//                        setCompletedLecture(previousPosition);
//                        Log.e(TAG,"in comle preposi: "+previousPosition);
//                    }
                }

                //Log.e(TAG,"currenwind: "+currentWindow+" prevwindo: "+(currentWindow-1)+" reason: "+reason);
               // updateIsPlaying(position);
                //lectureRcy.getLayoutManager().scrollToPosition(position);
                // Log.e(TAG,"position: "+position+" current window: "+currentWindow);
            }
        });

        setDownloadMediaListener();

    }

    private void setDownloadMediaListener(){
        MapPosition mapPosition = new MapPosition(start, lectureArrayList.size());
        ((DetailMyCourseActivity) getActivity()).setDownloadMediaListener(new DownloadMediaListener() {
            @Override
            public void onDownloadMediaChanged(DownloadManager downloadManager, Download download,int index) {
               // int position=mapPosition.getPosition(index);
                int position=0;
                for (int i=0;i<lectureArrayList.size();i++){
                    if(lectureArrayList.get(i).getTypeLecture_lectureList()!=TYPE_SECTION) {
                        if (lectureArrayList.get(i).getUrlPath_lectureList().equals(download.request.uri.toString())
                                && lectureArrayList.get(i).getTitle_lectureList().equals(Util.fromUtf8Bytes(download.request.data))) {
                            position=i;
                        }
                    }
                }
                int progress=(int)(download.getPercentDownloaded());
               // Log.e(TAG,"progress= "+download.getPercentDownloaded());
                adapter.updateProgressDownloadMedia(position,progress,download.state);
                //Log.e(TAG,"download changed postion= "+position+" state: "+download.state);
//                LectureList item=lectureArrayList.get(index);
//                if(download.state==Download.STATE_DOWNLOADING){
//                    item.setDownloadStatus_lectureList(DOWNLOAD_RUNNING_STATUS);
//                    //item.setProgressDownload_lectureList();
//                }
//                else if(progress==100 || download.state==Download.STATE_COMPLETED){
//                    item.setDownloadStatus_lectureList(DOWNLOAD_COMPLETED_STATUS);
//                }
//                lectureViewModel.update(item);

            }
        });
    }

    private void setCompletedLecture(int position){
        //int index=position-1;
        if(position<0){
            return;
        }
        adapter.updateCompleteLecture(position);
        //LectureList item=lectureArrayList.get(position);
        //item.setHasCompleted_lectureList(true);
        //lectureViewModel.update(item);
        //Log.e(TAG,"in fragmet item: "+item.toString());
    }

    private void updateStatusDownloadDoc() {
        ((DetailMyCourseActivity) getActivity()).setProgressDownloadDocListener(new ProgressDownloadDocListener() {
            @Override
            public void onProgressDownloadDocComplete(int position, int progress) {
               // Log.e(TAG, "Lisprogress: " + progress);
                if (progress == -1000) {
                    Toast.makeText(getActivity(), getString(R.string.failed_doc_download), Toast.LENGTH_SHORT).show();
                    return;
                }
                adapter.updateProgressDownload(position, progress);
            }

        });
    }

//    private void updateIsPlaying(int position){
//        adapter.updateIsPlaying(position);
//    }


    private void onClickLecture(LectureList lecture, int position) {
        ((DetailMyCourseActivity) getActivity()).playSingleVideo(lecture.getIndex_dataSource_lectureList(), position);
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
