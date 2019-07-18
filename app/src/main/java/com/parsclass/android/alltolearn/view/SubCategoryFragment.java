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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parsclass.android.alltolearn.R;
import com.parsclass.android.alltolearn.Utils.ConstantUtil;
import com.parsclass.android.alltolearn.adapter.SimpleCourseAdapter;
import com.parsclass.android.alltolearn.adapter.SubCategoryAdapter;
import com.parsclass.android.alltolearn.base.BaseHideBottomNavFragment;
import com.parsclass.android.alltolearn.model.CategoryItem;
import com.parsclass.android.alltolearn.model.Course;
import com.parsclass.android.alltolearn.model.SubCategory;
import com.parsclass.android.alltolearn.viewmodel.CourseViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SubCategoryFragment extends BaseHideBottomNavFragment {

    private OnFragmentInteractionListener mListener;
    private View rootView;
    @BindView(R.id.txtSeeMore)
    TextView txtSeeMore;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView txtTitleToolbar;
    @BindView(R.id.courseRcy)
    RecyclerView courseRcy;
    @BindView(R.id.subCategoryRcy)
    RecyclerView subCategoryRcy;
    @BindView(R.id.txtLabelTopCourse)
    TextView txtLabelTopCourse;
    private CategoryItem categoryItem;
    private CourseViewModel courseViewModel;
    private SimpleCourseAdapter courseAdapter;
    private SubCategoryAdapter subCategoryAdapter;
    private ArrayList<SubCategory> subCategories=new ArrayList<>();

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;


    public SubCategoryFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(rootView==null) {
            rootView = inflater.inflate(R.layout.fragment_sub_category, container, false);
        }
        ButterKnife.bind(this,rootView);
        initView();
        setCourseRcy();
        setSubCategoryRcy();
        return rootView;
    }

    private void initView(){
        courseViewModel= ViewModelProviders.of(this).get(CourseViewModel.class);
        categoryItem=(CategoryItem) getArguments().getSerializable(ConstantUtil.KEY_SUB_CATEGORY);
        txtTitleToolbar.setText(categoryItem.getTitle());

        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

        collapsingToolbarLayout.setTitleEnabled(false);
        ViewCompat.setTransitionName(appBarLayout, "Name");
        ViewCompat.setNestedScrollingEnabled(subCategoryRcy, false);
        ViewCompat.setNestedScrollingEnabled(courseRcy, false);

        txtLabelTopCourse.setText(getString(R.string.popular_course)+" "+getString(R.string.in)+" "+categoryItem.getTitle());



    }

    private void setSubCategoryRcy(){
        subCategories.addAll(categoryItem.getSubCategory());
       subCategoryRcy.setLayoutManager(new LinearLayoutManager(getContext()));

        subCategoryAdapter=new SubCategoryAdapter(getContext(),subCategories);
        subCategoryRcy.setAdapter(subCategoryAdapter);
    }
    private void setCourseRcy(){
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        courseRcy.setLayoutManager(layoutManager);
        courseViewModel.getFakCourses("toCourseCategory").observe(this, new Observer<List<Course>>() {
            @Override
            public void onChanged(@Nullable List<Course> courses) {
                courseAdapter=new SimpleCourseAdapter(getActivity(),courses,SimpleCourseAdapter.INSTRUCTOR_COURSE);
                courseRcy.setAdapter(courseAdapter);
                setOnClickCourse();

            }
        });
    }

    private void setOnClickCourse(){
        courseAdapter.setOnItemClickListener(new SimpleCourseAdapter.OnItemClickListener() {
            @Override
            public void onItemCourseClick(Course item) {
                Bundle bundle=new Bundle();
                bundle.putSerializable(HomeFragment.KEY_DETAIL_COURSE_ACTIVITY,item);
                DetailCourseFragment fragment=new DetailCourseFragment();
                fragment.setArguments(bundle);
                loadFragment(R.id.flContent,fragment,ConstantUtil.TAG_MAIN_ACTIVITY);
            }
        });
    }

    @OnClick(R.id.imgBack)
    public void onBackClick(View view){
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
