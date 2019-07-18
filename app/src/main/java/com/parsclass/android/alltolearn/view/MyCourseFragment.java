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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parsclass.android.alltolearn.R;
import com.parsclass.android.alltolearn.Utils.ConstantUtil;
import com.parsclass.android.alltolearn.Utils.GridSpacingItemDecoration;
import com.parsclass.android.alltolearn.adapter.MyListCourseAdapter;
import com.parsclass.android.alltolearn.adapter.SimpleCategoryAdapter;
import com.parsclass.android.alltolearn.adapter.SimpleCourseAdapter;
import com.parsclass.android.alltolearn.base.BaseFragment;
import com.parsclass.android.alltolearn.config.MyApplication;
import com.parsclass.android.alltolearn.model.CategoryItem;
import com.parsclass.android.alltolearn.model.Course;
import com.parsclass.android.alltolearn.viewmodel.CategoryViewModel;
import com.parsclass.android.alltolearn.viewmodel.CourseViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.parsclass.android.alltolearn.Utils.ConstantUtil.FAVORITE_COURSE_ITEM;
import static com.parsclass.android.alltolearn.Utils.ConstantUtil.MY_COURSE_ITEM;
import static com.parsclass.android.alltolearn.Utils.ConstantUtil.TAG_MAIN_ACTIVITY;
import static com.parsclass.android.alltolearn.adapter.SimpleCourseAdapter.FAVORITE_COURSE;


public class MyCourseFragment extends BaseFragment
        implements SwipeRefreshLayout.OnRefreshListener {


    private static final String TAG = "MyCourseFragment";
    private OnFragmentInteractionListener mListener;
    private View rootView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView txtTitleToolbar;
    @BindView(R.id.categoryRcy)
    RecyclerView categoryRcy;
    // GridView categoryRcy;
    @BindView(R.id.courseRcy)
    RecyclerView courseRcy;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefreshLayout;
    private CourseViewModel courseViewModel;
    private CategoryViewModel categoryViewModel;
   // private CategoryAdapter categoryAdapter;
    private SimpleCategoryAdapter categoryAdapter;
    private MyListCourseAdapter courseAdapter;
    @BindView(R.id.txtTitle1)
    TextView txtTitle1;
    @BindView(R.id.txtTitle2)
    TextView txtTitle2;
    @BindView(R.id.txtTitle3)
    TextView txtTitle3;
    @BindView(R.id.favoriteCourseRcy)
    RecyclerView favoriteCourseRcy;
    @BindView(R.id.progress)
    ProgressBar progressBar;
    private SimpleCourseAdapter favoriteCourseAdapter;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e(TAG,"onCreateView");
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_my_course, container, false);
            ButterKnife.bind(this, rootView);
            setHasOptionsMenu(true);


        }

        setInitCategoryRcy();
        initView();





        return rootView;
    }


    private void setInitCourseRcy() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        courseRcy.setLayoutManager(linearLayoutManager);

//        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(),
//                DividerItemDecoration.VERTICAL);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { //>= API 21
//            itemDecoration.setDrawable(getActivity().getDrawable(R.drawable.divider));
//        } else {
//            itemDecoration.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.divider));
//        }


        courseRcy.addItemDecoration(getDivider());

        courseAdapter = new MyListCourseAdapter(getActivity());
        courseRcy.setAdapter(courseAdapter);
    }

    private void initFavoriteCourseRcy(){
        favoriteCourseRcy.setLayoutManager(new LinearLayoutManager(getActivity()));
        favoriteCourseRcy.addItemDecoration(getDivider());
        favoriteCourseAdapter=new SimpleCourseAdapter(getActivity(),FAVORITE_COURSE);
        favoriteCourseRcy.setAdapter(favoriteCourseAdapter);
    }

    private void initView() {

        courseViewModel = ViewModelProviders.of(this).get(CourseViewModel.class);

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

        collapsingToolbarLayout.setTitleEnabled(false);
        ViewCompat.setTransitionName(appBarLayout, "Name");
        ViewCompat.setNestedScrollingEnabled(categoryRcy, false);
        ViewCompat.setNestedScrollingEnabled(courseRcy, false);
        ViewCompat.setNestedScrollingEnabled(favoriteCourseRcy, false);


        if(MyApplication.prefHelper.getWhichMenuActive()==MY_COURSE_ITEM){

            txtTitleToolbar.setText(getString(R.string.my_course_title_toolbar));
        }else {
            txtTitleToolbar.setText(getString(R.string.favorite_course_title_toolbar));
        }
        if (MyApplication.prefHelper.isUserLogin()) {
            txtTitle1.setVisibility(View.GONE);
            txtTitle2.setVisibility(View.GONE);
            txtTitle3.setVisibility(View.GONE);
            categoryRcy.setVisibility(View.GONE);
            swipeRefreshLayout.setVisibility(View.VISIBLE);

            if(MyApplication.prefHelper.getWhichMenuActive()==MY_COURSE_ITEM) {
                courseRcy.setVisibility(View.VISIBLE);
                favoriteCourseRcy.setVisibility(View.GONE);
                setInitCourseRcy();
                setCourseRcy();
                Log.e(TAG,"user login and my course");
            }else {
                courseRcy.setVisibility(View.GONE);
                favoriteCourseRcy.setVisibility(View.VISIBLE);
                initFavoriteCourseRcy();
                setFavoriteCourseRcy();
                Log.e(TAG,"user login and favo course");
            }

        } else {
            Log.e(TAG, "user not login");
            txtTitle1.setVisibility(View.VISIBLE);
            txtTitle2.setVisibility(View.VISIBLE);
            txtTitle3.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setRefreshing(false);
            swipeRefreshLayout.setEnabled(false);
            courseRcy.setVisibility(View.GONE);
            favoriteCourseRcy.setVisibility(View.GONE);
            categoryRcy.setVisibility(View.VISIBLE);
           // setInitCategoryRcy();
            setCategoryRcy();
             Log.e(TAG,"isUserLogin: ");
        }
    }

    private void setFavoriteCourseRcy(){
        List<Course> list=new ArrayList<>();
        courseViewModel.getFavoriteCourses(MyApplication.prefHelper.getToken(),"favorite").observe(this, new Observer<List<Course>>() {
            @Override
            public void onChanged(@Nullable List<Course> courses) {
                list.clear();
                list.addAll(courses);
                favoriteCourseAdapter.setValues(list);
                setFavoriteCourseClickListener();
            }
        });
    }

    private void setFavoriteCourseClickListener(){
        favoriteCourseAdapter.setOnItemClickListener(new SimpleCourseAdapter.OnItemClickListener() {
            @Override
            public void onItemCourseClick(Course item) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(HomeFragment.KEY_DETAIL_COURSE_ACTIVITY, item);
                DetailCourseFragment fragment = new DetailCourseFragment();
                fragment.setArguments(bundle);
                loadFragment(R.id.flContent,fragment, ConstantUtil.TAG_MAIN_ACTIVITY);
            }
        });
    }

    private void setInitCategoryRcy(){
        categoryViewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);
        int spanCount = 2; // 3 columns
        int spacing = dpToPx(2); // 50px
        boolean includeEdge = true;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        categoryRcy.setLayoutManager(gridLayoutManager);
        categoryRcy.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        //categoryRcy.setItemAnimator(new DefaultItemAnimator());
       // categoryAdapter=new CategoryAdapter(getActivity());
        categoryAdapter=new SimpleCategoryAdapter(getActivity());
        categoryRcy.setAdapter(categoryAdapter);
    }
    private void setCategoryRcy() {

        List<CategoryItem> myList = new ArrayList<>();
        categoryViewModel.getFakeCategory().observe(this, new Observer<List<CategoryItem>>() {
            @Override
            public void onChanged(@Nullable List<CategoryItem> categoryItems) {
                myList.clear();
                myList.addAll(categoryItems);
                Log.e(TAG, "myLsit size: " + myList.size());
                //categoryAdapter = new CategoryAdapter(getActivity(), myList);
                //categoryRcy.setAdapter(categoryAdapter);
                categoryAdapter.setValues(myList);

                setOnCategoryClick();
            }
        });

    }

    private void setOnCategoryClick() {
        categoryAdapter.setOnItemClickListener(new SimpleCategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemInstructorClick(CategoryItem categoryItem) {
                Bundle bundle=new Bundle();
                bundle.putSerializable(ConstantUtil.KEY_SUB_CATEGORY,categoryItem);
                SubCategoryFragment fragment=new SubCategoryFragment();
                fragment.setArguments(bundle);
                loadFragment(R.id.flContent,fragment, TAG_MAIN_ACTIVITY);
            }
        });

//        categoryRcy.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView parent, View view, int position, long id) {
//                Bundle bundle = new Bundle();
//                CategoryItem item = (CategoryItem) parent.getAdapter().getItem(position);
//                bundle.putSerializable(ConstantUtil.KEY_SUB_CATEGORY, item);
//                SubCategoryFragment fragment = new SubCategoryFragment();
//                fragment.setArguments(bundle);
//                loadFragment(R.id.flContent, fragment, TAG_MAIN_ACTIVITY);
//            }
//        });
    }

    private void setCourseRcy() {
        List<Course> myList = new ArrayList<>();

        courseViewModel.getFakCourses("myCourse").observe(this, new Observer<List<Course>>() {
            @Override
            public void onChanged(@Nullable List<Course> courses) {
                myList.clear();
                myList.addAll(courses);
                courseAdapter.setCourse(myList);
            }
        });
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        //toolbar.inflateMenu(R.menu.select_type_list_course_menu);
        inflater.inflate(R.menu.select_type_list_course_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.my_course_item:
                Log.e(TAG, "my_course_item");
                if(!MyApplication.prefHelper.isUserLogin()) {
                    txtTitleToolbar.setText(getString(R.string.my_course_title_toolbar));
                    return true;
                }
                if(MyApplication.prefHelper.getWhichMenuActive()!=MY_COURSE_ITEM) {
                    MyApplication.prefHelper.putWhichMenuActive(MY_COURSE_ITEM);
                    refreshFragment();
                }
                return true;
            case R.id.favorite_course_item:
                Log.e(TAG, "favorite_course_item");
                if(!MyApplication.prefHelper.isUserLogin()) {
                    txtTitleToolbar.setText(getString(R.string.favorite_course_title_toolbar));
                    return true;
                }
                if(MyApplication.prefHelper.getWhichMenuActive()!=FAVORITE_COURSE_ITEM) {
                    MyApplication.prefHelper.putWhichMenuActive(FAVORITE_COURSE_ITEM);
                    refreshFragment();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttach(Context context) {
        Log.e(TAG,"onAtach");
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

    @Override
    public void onRefresh() {
        if (!MyApplication.prefHelper.isUserLogin()) {
            swipeRefreshLayout.setRefreshing(false);
            return;
        }else {
            if(MyApplication.prefHelper.getWhichMenuActive()==MY_COURSE_ITEM)
                setCourseRcy();
            else
                setFavoriteCourseRcy();
            swipeRefreshLayout.setRefreshing(false);
        }

    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
