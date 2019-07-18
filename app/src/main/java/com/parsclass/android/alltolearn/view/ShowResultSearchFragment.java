package com.parsclass.android.alltolearn.view;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterViewFlipper;
import android.widget.ProgressBar;

import com.parsclass.android.alltolearn.R;
import com.parsclass.android.alltolearn.Utils.ConstantUtil;
import com.parsclass.android.alltolearn.Utils.SelectFilterDialog;
import com.parsclass.android.alltolearn.Utils.SelectSortDialog;
import com.parsclass.android.alltolearn.adapter.AdapterSearch;
import com.parsclass.android.alltolearn.adapter.CoursePageListAdapter;
import com.parsclass.android.alltolearn.base.BaseHideBottomNavFragment;
import com.parsclass.android.alltolearn.model.Article;
import com.parsclass.android.alltolearn.viewmodel.CourseViewModel;
import com.parsclass.android.alltolearn.viewmodel.SearchViewModel;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShowResultSearchFragment extends BaseHideBottomNavFragment implements
        SelectFilterDialog.FilterDialogListener,
        SelectSortDialog.SortDialogListener {

    private OnFragmentInteractionListener mListener;
    private View view;
    @BindView(R.id.rcy)
    RecyclerView courseRcy;
    private String currentQuery;
    @BindView(R.id.progress_circular)
    AVLoadingIndicatorView progressBar;
    private String currentCategory,currentDuration,currentSortBy;
    private boolean currentPrice;
    private SearchViewModel viewModel;

    private CoursePageListAdapter adapter;
    private CourseViewModel courseViewModel;

    private AdapterSearch adapter2;

    private String TAG="ShowResultSearchFragment";

    public ShowResultSearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_show_result_search, container, false);
        ButterKnife.bind(this,view);
        initView();

        return view;
    }

    private void initView(){
        courseViewModel= ViewModelProviders.of(this).get(CourseViewModel.class);
       // adapter=new CoursePageListAdapter();

       // courseRcy.setAdapter(adapter);

        DividerItemDecoration itemDecoration=new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { //>= API 21
            itemDecoration.setDrawable(getActivity().getDrawable(R.drawable.divider));
        } else {
            itemDecoration.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.divider));
        }
        courseRcy.addItemDecoration(itemDecoration);
        ViewCompat.setNestedScrollingEnabled(courseRcy, false);

        currentQuery=getArguments().getString("my_key");

        /* for api test */
        viewModel=ViewModelProviders.of(this).get(SearchViewModel.class);
        //adapter2=new AdapterSearch(getContext());

        processQuery(currentQuery,currentCategory,currentDuration,currentPrice,currentSortBy);
    }

    private void processQuery(String query,String category,String duration,boolean price,String sortBy){
        courseRcy.setVisibility(View.VISIBLE);
        Log.e(TAG,"in process query");
        Log.e(TAG,"current query "+currentQuery);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        courseRcy.setLayoutManager(layoutManager);
        //courseRcy.setAdapter(adapter2);
        viewModel.getLiveData(query, ConstantUtil.API_KEY)
                .observe(this, new Observer<ArrayList<Article>>() {
                    @Override
                    public void onChanged(@Nullable ArrayList<Article> articles) {

                        ArrayList<String> myList=new ArrayList<>();
                        for (int i=0;i<articles.size();i++){
                            myList.add(articles.get(i).getTitle());
                        }
                        Log.e(TAG,"list: "+articles.toString());
                        adapter2=new AdapterSearch(getActivity(),articles);
                        courseRcy.setAdapter(adapter2);
                       // adapter2.setValues(articles);

                    }
                });

        viewModel.getProgressLoadStatus().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                if (s.equals(ConstantUtil.LOADING)) {
                    progressBar.setVisibility(View.VISIBLE);
                }
                else {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    @OnClick(R.id.btnSort)
    public void onSortClick(){
        SelectSortDialog selectSortDialog=new SelectSortDialog();
        selectSortDialog.show(getActivity().getSupportFragmentManager(),"SelectSortDialog");
    }

    @OnClick(R.id.btnFilter)
    public void onFilterClick(){
        SelectFilterDialog selectFilterDialog=new SelectFilterDialog();
        selectFilterDialog.show(getActivity().getSupportFragmentManager(),"SelectFilterDialog");
    }


    @Override
    public void onFilterChanged(boolean price, String category, String duration) {
        currentCategory=category;
        currentDuration=duration;
        currentPrice=price;
        processQuery(currentQuery,category,duration,price,currentSortBy);
    }

    @Override
    public void onTypeSortChanged(String selectedItem) {
        currentSortBy=selectedItem;
        processQuery(currentQuery,currentCategory,currentDuration,currentPrice,currentSortBy);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
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

}
