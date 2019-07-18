package com.parsclass.android.alltolearn.view;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.parsclass.android.alltolearn.R;
import com.parsclass.android.alltolearn.Utils.ConstantUtil;
import com.parsclass.android.alltolearn.adapter.CoursePageListAdapter;
import com.parsclass.android.alltolearn.base.BaseHideBottomNavFragment;
import com.parsclass.android.alltolearn.model.Course;
import com.parsclass.android.alltolearn.viewmodel.CourseViewModel;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.parsclass.android.alltolearn.view.HomeFragment.KEY_DETAIL_COURSE_ACTIVITY;


public class ListCourseFragment extends BaseHideBottomNavFragment {


    private OnFragmentInteractionListener mListener;
    private View rootView;
    @BindView(R.id.courseRcy)
    RecyclerView courseRcy;
    @BindView(R.id.progress)
    AVLoadingIndicatorView progressBar;
    @BindView(R.id.toolbar_title)
    TextView txtTitleToolbar;
    private CoursePageListAdapter adapter;
    private CourseViewModel courseViewModel;
    @BindView(R.id.shimmer_view_container)
    ShimmerFrameLayout mShimmerViewContainer;
    private final static String TAG="ListCourseFragment";

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;

    public ListCourseFragment() {
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
            rootView = inflater.inflate(R.layout.fragment_list_course, container, false);
        }
        ButterKnife.bind(this,rootView);
        initView();
        setRcy();
        setProgressBar();
        return rootView;
    }

    private void initView(){
        courseViewModel= ViewModelProviders.of(this).get(CourseViewModel.class);
        adapter=new CoursePageListAdapter();
        courseRcy.setLayoutManager(new LinearLayoutManager(getContext()));
        courseRcy.setAdapter(adapter);

        DividerItemDecoration itemDecoration=new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { //>= API 21
            itemDecoration.setDrawable(getActivity().getDrawable(R.drawable.divider));
        } else {
            itemDecoration.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.divider));
        }


        courseRcy.addItemDecoration(itemDecoration);

        collapsingToolbarLayout.setTitleEnabled(false);
        ViewCompat.setTransitionName(appBarLayout, "Name");
        ViewCompat.setNestedScrollingEnabled(courseRcy, false);
    }

    private void setRcy() {

        courseViewModel.getListLiveData().observe(this, new Observer<PagedList<Course>>() {
            @Override
            public void onChanged(@Nullable PagedList<Course> courses) {
                adapter.submitList(courses);
                Log.e(TAG,"list: "+courses.toString());
                setClickListener();
            }
        });

    }

    private void setClickListener(){
        adapter.setOnItemClickListener(new CoursePageListAdapter.OnItemClickListener() {
            @Override
            public void onItemCourseClick(Course course) {
                Bundle bundle=new Bundle();
                bundle.putSerializable(KEY_DETAIL_COURSE_ACTIVITY,course);
                DetailCourseFragment fragment=new DetailCourseFragment();
                fragment.setArguments(bundle);
               // loadFragment(R.id.flContent,fragment,TAG_MAIN_ACTIVITY);

            }
        });

    }

    private void setProgressBar(){
        courseViewModel.getProgressLoadStatus().observe(this, status -> {
            if (Objects.requireNonNull(status).equalsIgnoreCase(ConstantUtil.LOADING)) {
                progressBar.setVisibility(View.VISIBLE);
                progressBar.show();
                //binding.animationView.setVisibility(View.VISIBLE);
                mShimmerViewContainer.startShimmerAnimation();
            } else if (status.equalsIgnoreCase(ConstantUtil.LOADED)) {
                progressBar.setVisibility(View.GONE);
                progressBar.hide();
                mShimmerViewContainer.stopShimmerAnimation();
                mShimmerViewContainer.setVisibility(View.GONE);
                //binding.animationView.setVisibility(View.GONE);
            }
        });
    }

    @OnClick(R.id.imgBack)
    public void onBackClick(){
        getFragmentManager().popBackStack();
    }

    @Override
    public void onResume() {
        super.onResume();
        mShimmerViewContainer.startShimmerAnimation();
    }

    @Override
    public void onPause() {
        super.onPause();
        mShimmerViewContainer.stopShimmerAnimation();
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
