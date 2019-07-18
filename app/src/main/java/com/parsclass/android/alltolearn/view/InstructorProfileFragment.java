package com.parsclass.android.alltolearn.view;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import androidx.core.view.ViewCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;
import com.parsclass.android.alltolearn.R;
import com.parsclass.android.alltolearn.Utils.ConstantUtil;
import com.parsclass.android.alltolearn.Utils.GlideApp;
import com.parsclass.android.alltolearn.adapter.SimpleCourseAdapter;
import com.parsclass.android.alltolearn.base.BaseHideBottomNavFragment;
import com.parsclass.android.alltolearn.model.Course;
import com.parsclass.android.alltolearn.model.Instructor;
import com.parsclass.android.alltolearn.viewmodel.CourseViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class InstructorProfileFragment extends BaseHideBottomNavFragment {

    private OnFragmentInteractionListener mListener;
    private View rootView;
    @BindView(R.id.presentCourseRcy)
    RecyclerView presentCourseRcy;
    private Instructor instructor;
    @BindView(R.id.toolbar_title)
    TextView txtTitleToolbar;
    @BindView(R.id.imgInstructor)
    ImageView imgInstructor;
    @BindView(R.id.txtNameInstructor)
    TextView txtName;
    @BindView(R.id.txtOccupation)
    TextView txtOccupation;
    @BindView(R.id.txtNumberStudent)
    TextView txtNumberStudent;
    @BindView(R.id.txtNumberCourse)
    TextView txtNumberCourse;
    @BindView(R.id.txtAvgRate)
    TextView txtAvgRate;
    @BindView(R.id.txtBiography)
    TextView txtBiography;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private CourseViewModel courseViewModel;
    private SimpleCourseAdapter adapter;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;


    public InstructorProfileFragment() {
        // Required empty public constructor
    }

    public static InstructorProfileFragment newInstance(String param1, String param2) {
        InstructorProfileFragment fragment = new InstructorProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
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
            rootView = inflater.inflate(R.layout.fragment_instructor_profile, container, false);
        }
        ButterKnife.bind(this,rootView);
        initView();
        initRecyclerView();
        return rootView;
    }

    private void adjustAppbar(){
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                if(Math.abs(i) >= appBarLayout.getTotalScrollRange()){
                    imgInstructor.setVisibility(View.GONE);
                }else {
                    imgInstructor.setVisibility(View.VISIBLE);
                }
            }
        });

        collapsingToolbarLayout.setTitleEnabled(false);
        ViewCompat.setTransitionName(appBarLayout, "Name");
        ViewCompat.setNestedScrollingEnabled(presentCourseRcy, false);

        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        collapsingToolbarLayout.setContentScrimColor(getResources().getColor(R.color.colorPrimary));
        collapsingToolbarLayout.setStatusBarScrimColor(getResources().getColor(R.color.colorTransparent));

    }

    private void initView(){
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        instructor=(Instructor) getArguments().getSerializable(ConstantUtil.KEY_DETAIL_PROFILE_INSTRUCTOR);
        txtTitleToolbar.setText(instructor.getName());
        txtName.setText(instructor.getName());
        txtOccupation.setText(instructor.getOccupation());
        txtNumberStudent.setText(instructor.getNumberStudents());
        txtNumberCourse.setText(instructor.getNumberCourses());
        txtAvgRate.setText(String.valueOf(instructor.getRateAvg()));
        if(!TextUtils.isEmpty(instructor.getAbout()))
            txtBiography.setText(instructor.getAbout());
        GlideApp.with(getContext())
                .load(instructor.getImagePath())
                .placeholder(R.drawable.img_instructor)
                .error(R.drawable.img_instructor)
                .apply(RequestOptions.circleCropTransform())
                .into(imgInstructor);

        courseViewModel= ViewModelProviders.of(this).get(CourseViewModel.class);
        adjustAppbar();

    }

    private void initRecyclerView(){
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        presentCourseRcy.setLayoutManager(layoutManager);
        courseViewModel.getFakCourses("courseOfInstructor").observe(this, new Observer<List<Course>>() {
            @Override
            public void onChanged(@Nullable List<Course> courses) {
                adapter=new SimpleCourseAdapter(getContext(),courses,SimpleCourseAdapter.INSTRUCTOR_COURSE);
                presentCourseRcy.setAdapter(adapter);
                setonCourseClickListener();
            }
        });
    }

    private void setonCourseClickListener(){
        if(adapter!=null){
            adapter.setOnItemClickListener(new SimpleCourseAdapter.OnItemClickListener() {
                @Override
                public void onItemCourseClick(Course item) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(HomeFragment.KEY_DETAIL_COURSE_ACTIVITY, item);
                    DetailCourseFragment fragment = new DetailCourseFragment();
                    fragment.setArguments(bundle);
                    loadFragment(R.id.flContent,fragment,ConstantUtil.TAG_MAIN_ACTIVITY);
                }
            });
        }
    }

    @OnClick(R.id.imgBack)
    public void onImgBackClick(){
        getFragmentManager().popBackStack();
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