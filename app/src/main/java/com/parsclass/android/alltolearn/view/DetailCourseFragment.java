package com.parsclass.android.alltolearn.view;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import androidx.core.view.ViewCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parsclass.android.alltolearn.R;
import com.parsclass.android.alltolearn.Utils.ConstantUtil;
import com.parsclass.android.alltolearn.adapter.CommentAdapter;
import com.parsclass.android.alltolearn.adapter.InstructorAdapter;
import com.parsclass.android.alltolearn.adapter.LectureViewHolder;
import com.parsclass.android.alltolearn.adapter.SectionAdapter;
import com.parsclass.android.alltolearn.adapter.SimpleCourseAdapter;
import com.parsclass.android.alltolearn.adapter.mySimpleAdapter;
import com.parsclass.android.alltolearn.base.BaseHideBottomNavFragment;
import com.parsclass.android.alltolearn.config.MyApplication;
import com.parsclass.android.alltolearn.databinding.FragmentDetialCourseBinding;
import com.parsclass.android.alltolearn.model.Comment;
import com.parsclass.android.alltolearn.model.Course;
import com.parsclass.android.alltolearn.model.Instructor;
import com.parsclass.android.alltolearn.model.Lecture;
import com.parsclass.android.alltolearn.model.Section;
import com.parsclass.android.alltolearn.model.SectionExpandable;
import com.parsclass.android.alltolearn.viewmodel.CommentViewModel;
import com.parsclass.android.alltolearn.viewmodel.CourseViewModel;
import com.parsclass.android.alltolearn.viewmodel.InstructorViewModel;
import com.parsclass.android.alltolearn.viewmodel.SectionViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.parsclass.android.alltolearn.Utils.ConstantUtil.AVG_RATING_COURSE;
import static com.parsclass.android.alltolearn.Utils.ConstantUtil.ID_COURSE;
import static com.parsclass.android.alltolearn.Utils.ConstantUtil.KEY_URI_PREVIEW_PLAYBACK;
import static com.parsclass.android.alltolearn.Utils.ConstantUtil.NUMBER_RATING_COURSE;
import static com.parsclass.android.alltolearn.Utils.ConstantUtil.PERSIAN_LANGUAGE;
import static com.parsclass.android.alltolearn.Utils.ConstantUtil.RATING_COURSE;
import static com.parsclass.android.alltolearn.Utils.ConstantUtil.TAG_MAIN_ACTIVITY;
import static com.parsclass.android.alltolearn.Utils.ConstantUtil.TITLE_COURSE;


public class DetailCourseFragment extends BaseHideBottomNavFragment {

    private OnFragmentInteractionListener mListener;

    private static final String TAG ="DetailCourseFragment";
    private FragmentDetialCourseBinding mBinding;
    private Course currentCourse;
    private int courseId;
    private ArrayList<String> courseInfo=new ArrayList<>();
    private ArrayList<String> whatLearn=new ArrayList<>();
    private ArrayList<String> requirement=new ArrayList<>();
    private mySimpleAdapter adapterCourseInfo,adapterWhatLearn,adapterRequirement;
    private SimpleCourseAdapter adapterAlsoViewed;
    private InstructorViewModel instructorViewModel;
    private CourseViewModel courseViewModel;
    private CommentViewModel commentViewModel;
    private SectionViewModel sectionViewModel;
    private InstructorAdapter adapterInstructor;
    private CommentAdapter adapterComment;
    private SectionAdapter adapterSection;
   // private ExpandableListAdapter adapterSection;
    private View rootView;
    private BottomSheetBehavior<View> bottomSheetBehavior;

    public DetailCourseFragment() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        postponeEnterTransition();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding= DataBindingUtil.inflate(
                inflater, R.layout.fragment_detial_course, container, false);
        if (rootView==null) {
            rootView = mBinding.getRoot();
            Log.e(TAG,"rootView==null");
        }
        //here data must be an instance of the class MarsDataProvider

//        Bundle bundle=getActivity().getIntent().getExtras();
//        currentCourse=(Course) bundle.getSerializable(HomeFragment.KEY_DETAIL_COURSE_ACTIVITY);

        currentCourse=(Course)  getArguments().getSerializable(HomeFragment.KEY_DETAIL_COURSE_ACTIVITY);
        courseId=currentCourse.getId();

        mBinding.setCourseItem(currentCourse);
        setupView();

//        String  transitionName="";
//       if(!TextUtils.isEmpty(getArguments().getString(KEY_TRANSITION_NAME)))
//            transitionName=getArguments().getString(KEY_TRANSITION_NAME);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            mBinding.imgBackAppbar.setTransitionName(transitionName);
//        }

        return rootView;

    }
    private void setupView(){
        setupToolbar();
        setupCourseInfoRecy();
        setupWhatLearn();
        setupDescriptionCourse();
        setupRequirementRecy();
        setupSimilarCourse();
        //setGraph();
        setDataChart();
        setComment();
        setupInstructor();
        setSection();
        setClickShowCommentListener();
        onClickBackButton();
        onClickPreviewButton();
        onClickShareButton();
        //setSectionClickListener();
       // setVisibilityBuyBtn();
    }

    private void setVisibilityBuyBtn(){

        courseViewModel.isEnrolled(currentCourse.getServer_id()).observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if(aBoolean!=null) {
                    if (aBoolean)
                        mBinding.btnBuy.setVisibility(View.GONE);
                    else
                        mBinding.btnBuy.setVisibility(View.VISIBLE);
                }
            }
        });

       // mBinding.btnBuy.setVisibility(View.GONE);
    }

    private void setClickShowCommentListener(){
        mBinding.txtSeeMoreComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putInt(ID_COURSE,currentCourse.getId());
                bundle.putString(TITLE_COURSE,currentCourse.getTitle());
                bundle.putString(AVG_RATING_COURSE,String.valueOf(currentCourse.getRatingAvg()));
                bundle.putFloat(RATING_COURSE,currentCourse.getRatingAvg());
                bundle.putString(NUMBER_RATING_COURSE,currentCourse.getNumberPersonRate());
                CommentFragment fragment=new CommentFragment();
                fragment.setArguments(bundle);
                if(Locale.getDefault().getLanguage().equals(PERSIAN_LANGUAGE))
                    loadFragmentWithTransitionRTL(R.id.flContent,fragment,TAG_MAIN_ACTIVITY);
                else
                    loadFragmentWithTransitionLTR(R.id.flContent,fragment,TAG_MAIN_ACTIVITY);
                //loadFragment(R.id.flContent,fragment,TAG_MAIN_ACTIVITY);
            }
        });
    }


    private void setShowAlsoViewClickListener(){
        mBinding.txtShowMoreAlsoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go to listCourseFragment
            }
        });
    }

    private void setupToolbar(){
        ((AppCompatActivity)getActivity()).setSupportActionBar(mBinding.toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

        mBinding.collapsingToolbar.setTitleEnabled(false);
        ViewCompat.setTransitionName(mBinding.appbar, "Name");

        mBinding.collapsingToolbar.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        mBinding.collapsingToolbar.setContentScrimColor(getResources().getColor(R.color.colorPrimary));
        mBinding.collapsingToolbar.setStatusBarScrimColor(getResources().getColor(R.color.colorPrimaryDark));

        mBinding.appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
//                if(i == 0 ){
//                    mBinding.imgBack.setVisibility(View.GONE);
//                    mBinding.imgShare.setVisibility(View.GONE);
//                    mBinding.imgFavorit.setVisibility(View.GONE);
//
//                }
//                else {
//                    mBinding.imgBack.setVisibility(View.VISIBLE);
//                    mBinding.imgShare.setVisibility(View.VISIBLE);
//                    mBinding.imgFavorit.setVisibility(View.VISIBLE);
//
//                }
//                if(Math.abs(i)>=appBarLayout.getTotalScrollRange()){
//                    mBinding.layoutPreview.setVisibility(View.GONE);
//                }else {
//                    mBinding.layoutPreview.setVisibility(View.VISIBLE);
//                }

            }
        });

        /* sticky but button  */
        bottomSheetBehavior = BottomSheetBehavior.from(mBinding.bottomSheet);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        /* make STRIKE_THRU_TEXT of previous price */
        mBinding.txtPreviousPrice.setPaintFlags(mBinding.txtPreviousPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        //getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window =getActivity().getWindow();
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.
//           setStatusBarColor(getResources().getColor(R.color.colorBlackAlpha90));
//        }

        if(!MyApplication.prefHelper.isUserLogin()) {
            mBinding.imgFavorit.setVisibility(View.GONE);
            mBinding.btnAddFavorit.setVisibility(View.GONE);
        }else {
            mBinding.imgFavorit.setVisibility(View.VISIBLE);
            mBinding.btnAddFavorit.setVisibility(View.VISIBLE);
        }

    }

    private void setupInstructor(){
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        mBinding.instructorRecy.setLayoutManager(linearLayoutManager);
        ViewCompat.setNestedScrollingEnabled(mBinding.instructorRecy,false);
        instructorViewModel= ViewModelProviders.of(this).get(InstructorViewModel.class);
        instructorViewModel.getFakeInstructor(courseId).observe(this, new Observer<List<Instructor>>() {
            @Override
            public void onChanged(@Nullable List<Instructor> instructors) {
                String name=instructors.get(0).getName();
                int count=instructors.size();
               // setupNameInstructor(name,count);
                adapterInstructor=new InstructorAdapter(getContext(),instructors,InstructorAdapter.LIMIT_SIZE_TYPE);
                mBinding.instructorRecy.setAdapter(adapterInstructor);
                setInstructorClick();
            }
        });
    }

    private void setInstructorClick(){
        if(adapterInstructor!=null) {
            adapterInstructor.setOnItemClickListener(new InstructorAdapter.OnItemClickListener() {
                @Override
                public void onItemInstructorClick(Instructor instructor) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(ConstantUtil.KEY_DETAIL_PROFILE_INSTRUCTOR, instructor);
                    InstructorProfileFragment fragment = new InstructorProfileFragment();
                    fragment.setArguments(bundle);
                    if( Locale.getDefault().getLanguage().equals(PERSIAN_LANGUAGE))
                        loadFragmentWithTransitionRTL(R.id.flContent,fragment,TAG_MAIN_ACTIVITY);
                    else
                        loadFragmentWithTransitionLTR(R.id.flContent,fragment,TAG_MAIN_ACTIVITY);
                }
            });
        }
    }

    private void setupCourseInfoRecy(){
        courseInfo=currentCourse.getInfoCourse();
//        Log.e(TAG,courseInfo.toString());
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        mBinding.InfoCourseRecy.setLayoutManager(linearLayoutManager);
        adapterCourseInfo=new mySimpleAdapter(getContext(),courseInfo,mySimpleAdapter.COURSE_INFO_TYPE);
        mBinding.InfoCourseRecy.setAdapter(adapterCourseInfo);
        ViewCompat.setNestedScrollingEnabled(mBinding.InfoCourseRecy,false);

    }
    private void setupWhatLearn(){
        whatLearn=currentCourse.getWhatLearn();
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        mBinding.WhatLeaernRecy.setLayoutManager(linearLayoutManager);
        adapterWhatLearn=new mySimpleAdapter(getContext(),whatLearn,mySimpleAdapter.CHECK_LIST_TYPE);
        mBinding.WhatLeaernRecy.setAdapter(adapterWhatLearn);
        ViewCompat.setNestedScrollingEnabled(mBinding.WhatLeaernRecy,false);
    }

    private void setupDescriptionCourse(){

        mBinding.expandableText.setText(currentCourse.getLongDescription());
    }
    private void setupRequirementRecy(){
        requirement=currentCourse.getRequirements();
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        mBinding.RequirementsRecy.setLayoutManager(linearLayoutManager);
        adapterRequirement=new mySimpleAdapter(getContext(),requirement,mySimpleAdapter.CHECK_LIST_TYPE);
        mBinding.RequirementsRecy.setAdapter(adapterRequirement);
        ViewCompat.setNestedScrollingEnabled(mBinding.RequirementsRecy,false);
    }
    private void setupSimilarCourse(){
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        mBinding.alsoViewedRecy.setLayoutManager(linearLayoutManager);
        ViewCompat.setNestedScrollingEnabled(mBinding.alsoViewedRecy,false);


        courseViewModel = ViewModelProviders.of(this).get(CourseViewModel.class);
        courseViewModel.getFakCourses("ALSO_VIEWED").observe(this, new Observer<List<Course>>() {
            @Override
            public void onChanged(@Nullable List<Course> courses) {
                mBinding.alsoViewedRecy.setLayoutManager(linearLayoutManager);
                adapterAlsoViewed=new SimpleCourseAdapter(getContext(),courses, SimpleCourseAdapter.ALSO_VIEWED);
                mBinding.alsoViewedRecy.setAdapter(adapterAlsoViewed);
                Log.e(TAG,"similar course: "+courses.size()+" "+courses.toString());
                setOnclickCourseItem();
            }
        });


    }

    private void setOnclickCourseItem(){
        adapterAlsoViewed.setOnItemClickListener(new SimpleCourseAdapter.OnItemClickListener() {
            @Override
            public void onItemCourseClick(Course item) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(HomeFragment.KEY_DETAIL_COURSE_ACTIVITY, item);
                DetailCourseFragment fragment = new DetailCourseFragment();
                fragment.setArguments(bundle);
                loadFragment(R.id.flContent,fragment, TAG_MAIN_ACTIVITY);
            }
        });
    }

    private void setSection(){
        RecyclerView.ItemAnimator animator = mBinding.sectionRecy.getItemAnimator();
        if (animator instanceof DefaultItemAnimator) {
            ((DefaultItemAnimator) animator).setSupportsChangeAnimations(false);
        }
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        mBinding.sectionRecy.setLayoutManager(linearLayoutManager);
        ViewCompat.setNestedScrollingEnabled(mBinding.sectionRecy,false);
        sectionViewModel = ViewModelProviders.of(this).get(SectionViewModel.class);

        List<SectionExpandable> list=new ArrayList<>();
        mBinding.sectionRecy.setLayoutManager(linearLayoutManager);
        sectionViewModel.getFakeSection(courseId).observe(this, new Observer<List<Section>>() {
            @Override
            public void onChanged(@Nullable List<Section> sections) {
                // list.clear();
                //list.addAll(sections);
                ArrayList<SectionExpandable> sectionExpandableList=new ArrayList<>();
                for (int i=0;i<sections.size();i++){
                    Section section=sections.get(i);
                    int id=section.getId();
                    String title=section.getTitle_section();
                    int courseId=section.getCourseId();
                    ArrayList<Lecture> lectures=section.getLectures();
                    SectionExpandable sectionExpandable=new SectionExpandable(id,title,courseId,lectures);
                    sectionExpandableList.add(sectionExpandable);
                }
                adapterSection=new SectionAdapter(sectionExpandableList, LectureViewHolder.TYPE_LIST_PUBLIC_COURSE,getContext());
                mBinding.sectionRecy.setAdapter(adapterSection);
                adapterSection.notifyDataSetChanged();
                setSectionClickListener();
            }
        });

      //  ViewCompat.setNestedScrollingEnabled(mBinding.sectionRecy,false);
//        sectionViewModel = ViewModelProviders.of(this).get(SectionViewModel.class);
//        HashMap<Section, List<Lecture>> listDataChild=new HashMap<>();
//        List<Section> listDataHeader=new ArrayList<>();
//        sectionViewModel.getFakeSection(courseId).observe(this, new Observer<List<Section>>() {
//            @Override
//            public void onChanged(@Nullable List<Section> sections) {
//                listDataChild.clear();
//                listDataHeader.clear();
//                listDataHeader.addAll(sections);
//                for (int i=0;i<sections.size();i++){
//                    listDataChild.put(listDataHeader.get(i),sections.get(i).getLectures());
//                }
//                adapterSection=new ExpandableListAdapter(getActivity(),listDataHeader,listDataChild);
//                mBinding.sectionRecy.setAdapter(adapterSection);
//                //adapterSection.setValues(listDataHeader,listDataChild);
//                setSectionClickListener(sections);
//
//            }
//        });


    }

    private void setSectionClickListener(){
        adapterSection.setOnItemClickListener(new SectionAdapter.OnItemClickListener() {
            @Override
            public void onLectureClick(Lecture lecture) {
                Log.e(TAG,"inOnClick");
                onClickLecture(lecture);
            }

            @Override
            public void onPreviewClick(Lecture lecture) {
                onClickPreview(lecture);
            }

            @Override
            public void onDownloadClick(Lecture lecture,int sectionId,int lectureId,ArrayList<Lecture> lectures) {

            }

        });

//        mBinding.sectionRecy.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
//
//            @Override
//            public boolean onChildClick(ExpandableListView parent, View v,
//                                        int groupPosition, int childPosition, long id) {
//                Lecture item=sections.get(groupPosition).getLectures().get(childPosition);
//                if(item!=null) {
//                    if (item.isHasPreview()) {
//                        onClickPreview(item);
//                    } else {
//                        Toast.makeText(getActivity(), item.getTitle_lecture(), Toast.LENGTH_SHORT).show();
//                    }
//                }
//                return false;
//            }
//        });
    }

//    private void setGraph(){
//        mBinding.barChart.getDescription().setEnabled(false);
//
//        mBinding.barChart.setDrawBarShadow(false);
//        mBinding.barChart.getLegend().setEnabled(false);
//        mBinding.barChart.setPinchZoom(false);
//        mBinding.barChart.setDrawValueAboveBar(false);
//
//        XAxis xAxis=mBinding.barChart.getXAxis();
//        xAxis.setDrawGridLines(false);
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setEnabled(true);
//        xAxis.setDrawAxisLine(false);
//
//        YAxis yAxis=mBinding.barChart.getAxisLeft();
//        yAxis.setAxisMaximum(100f);
//        yAxis.setAxisMinimum(0f);
//        yAxis.setEnabled(false);
//
//        xAxis.setLabelCount(5);
//
//
//        YAxis yRight=mBinding.barChart.getAxisRight();
//        yRight.setDrawAxisLine(true);
//        yRight.setDrawGridLines(false);
//        yRight.setEnabled(false);
//
//        mBinding.barChart.animateY(2000);
//
//        mBinding.barChart.setDrawBarShadow(true);
//
//        mBinding.barChart.getAxisLeft().setDrawLabels(false);
//        mBinding.barChart.getAxisRight().setDrawLabels(false);
//        mBinding.barChart.getXAxis().setDrawLabels(false);
//    }
    private void setDataChart(){
        ArrayList<String> percentageEachRating=currentCourse.getPercentageEachRating();
//        ArrayList<BarEntry> entries=new ArrayList<>();
//        float barWidth=1f;
//        float spaceForBar=7f;
//        for (int i=0;i<percentageEachRating.size();i++){
//            float val=Float.valueOf(percentageEachRating.get(i));
//            entries.add(new BarEntry(i*spaceForBar,val));
//        }
//
//        BarDataSet set=new BarDataSet(entries,"Data set1");
//        set.setDrawValues(false);
//        set.setColors(ContextCompat.getColor(getContext(),R.color.colorRateOne),
//                ContextCompat.getColor(getContext(),R.color.colorRateTwo),
//                ContextCompat.getColor(getContext(),R.color.colorRateThree),
//                ContextCompat.getColor(getContext(),R.color.colorRateFour),
//                ContextCompat.getColor(getContext(),R.color.colorRateFive));
//        mBinding.barChart.setDrawBarShadow(true);
//        set.setBarShadowColor(Color.argb(40,150,150,150));
//        BarData data=new BarData(set);
//        data.setBarWidth(barWidth);
//        mBinding.barChart.setData(data);
//        mBinding.barChart.invalidate();


    }
    private void setComment(){
        List<Comment> commentList=new ArrayList<>();
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        mBinding.commentRecy.setLayoutManager(linearLayoutManager);
        ViewCompat.setNestedScrollingEnabled(mBinding.commentRecy,false);
        commentViewModel = ViewModelProviders.of(this).get(CommentViewModel.class);
        commentViewModel.getAllComments(currentCourse.getId()).observe(this, new Observer<List<Comment>>() {
            @Override
            public void onChanged(@Nullable List<Comment> comments) {
                mBinding.commentRecy.setLayoutManager(linearLayoutManager);
                commentList.addAll(comments);
                Log.e(TAG,"commentlist "+comments.toString());
                adapterComment=new CommentAdapter(getContext(),commentList,CommentAdapter.LIMIT_SIZE_TYPE);
                mBinding.commentRecy.setAdapter(adapterComment);
            }
        });
    }

    public void onClickPreviewButton(){
        Log.e(TAG,"onClickPreviewButton");
        mBinding.imgPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!MyApplication.getInstance().isOnline()){
                    MyApplication.showAlertDialog(getString(R.string.title_alert_dialog)
                            ,getString(R.string.no_connection_internet),getActivity());
                    return;
                }
                Log.e(TAG,"onClickPreview");
                Intent intent=new Intent(getActivity(),PlaybackPreviewActivity.class);
                intent.putExtra(KEY_URI_PREVIEW_PLAYBACK,currentCourse.getUrlIntroLecture());
                startActivity(intent);
            }
        });
    }
    public void onClickBackButton(){
        Log.e(TAG,"onClickBackButton");
        mBinding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getFragmentManager().popBackStack();
            }
        });


    }

    public void onClickShareButton(){
        mBinding.imgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                String content=getString(R.string.content_share_text_course)+"\n"+currentCourse.getLinkWebSite();
                shareIntent.putExtra(Intent.EXTRA_TEXT,content);
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.title_share_text_course));
                startActivity(Intent.createChooser(shareIntent, "Share..."));
            }
        });
    }
    public void onClickFavoriteButton(){

    }
    public void onClickBuyButton(){

    }

    private void onClickLecture(Lecture lecture){
        Log.e(TAG,"onlectureClick");
    }
    private void onClickPreview(Lecture lecture){

        Log.e(TAG,"onClickPreview");
        Intent intent=new Intent(getActivity(),PlaybackPreviewActivity.class);
        intent.putExtra(KEY_URI_PREVIEW_PLAYBACK,lecture.getUrlPath());
        startActivity(intent);
    }

    // TODO: Rename method, update argument and hook method into UI event
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
