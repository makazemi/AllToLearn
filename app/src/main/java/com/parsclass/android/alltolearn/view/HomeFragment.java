package com.parsclass.android.alltolearn.view;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;

import com.crashlytics.android.Crashlytics;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import androidx.core.view.ViewCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parsclass.android.alltolearn.Interfaces.LoadingListener;
import com.parsclass.android.alltolearn.R;
import com.parsclass.android.alltolearn.Utils.ConstantUtil;
import com.parsclass.android.alltolearn.adapter.CategoryAdapter;
import com.parsclass.android.alltolearn.adapter.SimpleCourseAdapter;
import com.parsclass.android.alltolearn.adapter.MainSliderAdapter;
import com.parsclass.android.alltolearn.base.BaseFragment;
import com.parsclass.android.alltolearn.config.MyApplication;
import com.parsclass.android.alltolearn.model.CategoryItem;
import com.parsclass.android.alltolearn.model.Course;
import com.parsclass.android.alltolearn.remote.Status;
import com.parsclass.android.alltolearn.viewmodel.CategoryViewModel;
import com.parsclass.android.alltolearn.viewmodel.CourseViewModel;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.fabric.sdk.android.Fabric;
import me.relex.circleindicator.CircleIndicator;

import static com.parsclass.android.alltolearn.Utils.ConstantUtil.TAG_MAIN_ACTIVITY;


public class HomeFragment extends BaseFragment {


    private OnFragmentInteractionListener mListener;


    private static final String TAG="HomeFragment";
    public static final String KEY_DETAIL_COURSE_ACTIVITY="COURSE";
    private View view;
    @BindView(R.id.slider)
    ViewPager viewPager;
    @BindView(R.id.Indicator)
    CircleIndicator circleIndicator;

    private MainSliderAdapter adapterSlider;
    private CourseViewModel courseViewModel;

    @BindView(R.id.categoryRecy)
    RecyclerView CategoryRecy;
    private CategoryViewModel categoryViewModel;
   // private SimpleCategoryAdapter categoryAdapter;
    private CategoryAdapter categoryAdapter;
    private SimpleCourseAdapter popularCourseAdapter;

    @BindView(R.id.topCourseCat1Recy)
    RecyclerView topCourseOneRecy;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
   @BindView(R.id.progressBar)
   AVLoadingIndicatorView progressBar;

    private boolean flagMainSlider=true, flagCategoryList =false, flagCourseList =false;
    private LoadingListener loadingListener;

    @BindView(R.id.txtShowMoreFavorite)
    TextView txtShowMoreFavorite;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(view==null) {
            view = inflater.inflate(R.layout.fragment_home, container, false);

            ButterKnife.bind(this, view);

//            setupView();
//            setProgressBar();
        }

        setupView();

        setupSlider();
        setupCategoryRecycler();
        //  setupTopCourseRecycler();
        setupFakCourseRecycler();
        handleStatus();
        setProgressBar();

        return view;
    }

    private void setupView(){
        courseViewModel = ViewModelProviders.of(this).get(CourseViewModel.class);
        categoryViewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);

        //progressBar=getActivity().findViewById(R.id.progressBar);
        setupToolbar();

        popularCourseAdapter =new SimpleCourseAdapter(getContext(),SimpleCourseAdapter.LIMIT_LIST);
        LinearLayoutManager linearLayoutManagerPopularCourse = new LinearLayoutManager(getContext());
        linearLayoutManagerPopularCourse.setOrientation(LinearLayoutManager.HORIZONTAL);
        topCourseOneRecy.setLayoutManager(linearLayoutManagerPopularCourse);
        topCourseOneRecy.setAdapter(popularCourseAdapter);

        LinearLayoutManager linearLayoutManagerCategory = new LinearLayoutManager(getContext());
        linearLayoutManagerCategory.setOrientation(LinearLayoutManager.HORIZONTAL);
        CategoryRecy.setLayoutManager(linearLayoutManagerCategory);



    }

    private void setupToolbar(){
        ((MainActivity)getActivity()).setSupportActionBar(toolbar);
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

        collapsingToolbarLayout.setTitleEnabled(false);
        ViewCompat.setTransitionName(appBarLayout, "Name");

        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        collapsingToolbarLayout.setContentScrimColor(getResources().getColor(R.color.colorPrimary));
        //collapsingToolbarLayout.setContentScrimColor(getResources().getColor(R.color.colorTransparent));
        collapsingToolbarLayout.setStatusBarScrimColor(getResources().getColor(R.color.colorTransparent));
        //collapsingToolbarLayout.setStatusBarScrimColor(getResources().getColor(R.color.colorPrimaryDark));

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {

                if(Math.abs(i) >= appBarLayout.getTotalScrollRange()) {
                    toolbarTitle.setVisibility(View.VISIBLE);
                }
                else {
                    toolbarTitle.setVisibility(View.GONE);
                }
            }
        });

        ViewCompat.setNestedScrollingEnabled(topCourseOneRecy, false);
        ViewCompat.setNestedScrollingEnabled(CategoryRecy, false);
    }

    // a reversed arraylist
    public List<Course> reverseArrayList(List<Course> alist)
    {
        // Arraylist for storing reversed elements
       List<Course> revArrayList = new ArrayList<>();
        for (int i = alist.size() - 1; i >= 0; i--) {

            // Append the elements in reverse order
            revArrayList.add(alist.get(i));
        }

        // Return the reversed arraylist
        return revArrayList;
    }
    private void setupSlider(){
        List<Course> courseList=new ArrayList<>();
        courseViewModel.getFakAlsoView().observe(this, new Observer<List<Course>>() {
            @Override
            public void onChanged(@Nullable List<Course> courses) {
              //  Log.e(TAG,"listresourse: "+courses.toString());
                courseList.clear();
                courseList.addAll(courses);
//               if(MyApplication.isRtl(getResources())){
//                   courseList.addAll(reverseArrayList(courses));
//               }else {
//
//               }
                adapterSlider = new MainSliderAdapter(courseList);
                viewPager.setAdapter(adapterSlider);
                circleIndicator.setViewPager(viewPager);
                adapterSlider.registerDataSetObserver(circleIndicator.getDataSetObserver());

                flagMainSlider=true;
               // setOnClickSlider();
            }
        });

    }

    private void setOnClickSlider(){
        adapterSlider.setOnItemClickListener(new MainSliderAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Course course) {
                Bundle bundle=new Bundle();
                bundle.putSerializable(HomeFragment.KEY_DETAIL_COURSE_ACTIVITY,course);
                DetailCourseFragment fragment=new DetailCourseFragment();
                fragment.setArguments(bundle);
                loadFragment(R.id.flContent,fragment, ConstantUtil.TAG_MAIN_ACTIVITY);
            }
        });
    }

    private void setupCategoryRecycler() {
        List<CategoryItem> categoryItemList=new ArrayList<>();

        categoryViewModel.getFakeCategory().observe(this, new Observer<List<CategoryItem>>() {
            @Override
            public void onChanged(@Nullable List<CategoryItem> categoryItems) {
                Log.e(TAG, "categoryitem: " + categoryItems.toString());
//                categoryItemList.clear();
//                categoryItemList.addAll(categoryItems);
                categoryAdapter =new CategoryAdapter(getContext(),categoryItems);
                CategoryRecy.setAdapter(categoryAdapter);
                setCategoryClickListener();

            }
        });

    }
    private void setCategoryClickListener(){
        categoryAdapter.setOnItemClickListener(new CategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemInstructorClick(CategoryItem categoryItem) {
                Bundle bundle=new Bundle();
                bundle.putSerializable(ConstantUtil.KEY_SUB_CATEGORY,categoryItem);
                SubCategoryFragment fragment=new SubCategoryFragment();
                fragment.setArguments(bundle);
                loadFragment(R.id.flContent,fragment, TAG_MAIN_ACTIVITY);
            }
        });
    }

    private void handleStatus(){
       categoryViewModel.getStatus().observe(this, new Observer<Status>() {
           @Override
           public void onChanged(@Nullable Status status) {
               if(status!=null){
                 //  Log.e(TAG,status.toString());
                   if(status==Status.SUCCESS){
                       flagCategoryList =true;
                     //  Log.e(TAG,"success");
                       if(loadingListener!=null)
                           loadingListener.setStatus(flagCategoryList, flagCourseList);
                   }
                   else if(status==Status.ERROR){
                       flagCategoryList =true;
                //       Log.e(TAG,"Error");
                       if(loadingListener!=null)
                           loadingListener.setStatus(flagCategoryList, flagCourseList);
                   }
               }
           }
       });


       courseViewModel.getStatus().observe(this, new Observer<Status>() {
           @Override
           public void onChanged(@Nullable Status status) {
               if(status!=null){
                 //  Log.e(TAG,status.toString());
                   if(status==Status.SUCCESS){
                       flagCourseList =true;
                    //   Log.e(TAG,"success");
                       if(loadingListener!=null)
                           loadingListener.setStatus(flagCategoryList, flagCourseList);
                   }
                   else if(status==Status.ERROR){
                       flagCourseList =true;
                    //   Log.e(TAG,"Error");
                       if(loadingListener!=null)
                           loadingListener.setStatus(flagCategoryList, flagCourseList);
                   }
               }
           }
       });


    }
    private void setupTopCourseRecycler(){
        List<Course> courseList=new ArrayList<>();
        courseViewModel.getCourse("topCourse").observe(this, new Observer<List<Course>>() {
            @Override
            public void onChanged(@Nullable List<Course> courses) {
//                popularCourseAdapter =new SimpleCourseAdapter(getContext(),courses, SimpleCourseAdapter.LIMIT_LIST);
//                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true);
//                linearLayoutManager.setReverseLayout(true);
//                topCourseOneRecy.setLayoutManager(linearLayoutManager);
//                topCourseOneRecy.setAdapter(popularCourseAdapter);

                courseList.clear();
                courseList.addAll(courses);
                popularCourseAdapter.setValues(courseList);

            }
        });
    }

    private void setupFakCourseRecycler(){
        List<Course> courseList=new ArrayList<>();
        courseViewModel.getFakCourses("toCourse").observe(this, new Observer<List<Course>>() {
            @Override
            public void onChanged(@Nullable List<Course> courses) {
//                popularCourseAdapter =new SimpleCourseAdapter(getContext(),courses, SimpleCourseAdapter.LIMIT_LIST);
//                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true);
//                linearLayoutManager.setReverseLayout(true);
//                topCourseOneRecy.setLayoutManager(linearLayoutManager);
//                topCourseOneRecy.setAdapter(popularCourseAdapter);
//                flagCourseList =true;
                courseList.clear();
                courseList.addAll(courses);
                popularCourseAdapter.setValues(courseList);
                setOnclickCourseItem();
            }
        });



    }

    private void setOnclickCourseItem(){
        popularCourseAdapter.setOnItemClickListener(new SimpleCourseAdapter.OnItemClickListener() {
            @Override
            public void onItemCourseClick(Course item) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(HomeFragment.KEY_DETAIL_COURSE_ACTIVITY, item);
                DetailCourseFragment fragment = new DetailCourseFragment();
                fragment.setArguments(bundle);
                loadFragment(R.id.flContent,fragment, ConstantUtil.TAG_MAIN_ACTIVITY);
            }

        });

//        popularCourseAdapter.setTestListener(new SimpleCourseAdapter.test() {
//            @Override
//            public void onItemCourseClickWithHolder(Course item, SimpleCourseAdapter.CustomView holder) {
//                Log.e(TAG,"onItemCourseClickWithHolder");
//                Bundle bundle = new Bundle();
//                bundle.putSerializable(HomeFragment.KEY_DETAIL_COURSE_ACTIVITY, item);
//
//                String name=ViewCompat.getTransitionName(holder.imgCourse);
//                bundle.putString(KEY_TRANSITION_NAME,name);
//                DetailCourseFragment fragment = new DetailCourseFragment();
//                fragment.setArguments(bundle);
//
////                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
////                    fragment.setSharedElementEnterTransition(new DetailsTransition());
////                    fragment.setEnterTransition(new Fade());
////                    setExitTransition(new Fade());
////                    fragment.setSharedElementReturnTransition(new DetailsTransition());
////                }
//
//
//                loadFragmentWithTransitionDetail(R.id.flContent,fragment,TAG_MAIN_ACTIVITY,
//                        name,holder.imgCourse);
//            }
//        });
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


    private void setProgressBar(){
        setLoadingListener(new LoadingListener() {
            @Override
            public void setStatus(boolean flag1,boolean flag2) {
                if(flag1 && flag2)
                    progressBar.setVisibility(View.GONE);
            }
        });
    }

    public void setLoadingListener(LoadingListener listener){
        this.loadingListener=listener;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }



    @OnClick(R.id.txtShowMoreFavorite)
    void showFavoriteAll(){
        loadFragment(R.id.flContent,new ListCourseFragment(),ConstantUtil.TAG_MAIN_ACTIVITY);
    }



}
