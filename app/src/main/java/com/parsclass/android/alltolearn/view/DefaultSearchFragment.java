package com.parsclass.android.alltolearn.view;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parsclass.android.alltolearn.R;
import com.parsclass.android.alltolearn.Utils.ConstantUtil;
import com.parsclass.android.alltolearn.adapter.ListCategoryAdapter;
import com.parsclass.android.alltolearn.base.BaseFragment;
import com.parsclass.android.alltolearn.model.CategoryItem;
import com.parsclass.android.alltolearn.viewmodel.CategoryViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DefaultSearchFragment extends BaseFragment {


    public static final String TAG="DefaultSearchFragment";
    private View rootView;
    private ListCategoryAdapter adapter;
    private CategoryViewModel categoryViewModel;

    @BindView(R.id.CategoryRcy)
    RecyclerView categoryRcy;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(rootView==null) {
            rootView = inflater.inflate(R.layout.fragment_default_search, container, false);
        }
        ButterKnife.bind(this,rootView);

        initView();
        initRecyclerView();
        return rootView;
    }

    private void initView(){
        categoryViewModel= ViewModelProviders.of(this).get(CategoryViewModel.class);
        categoryRcy.setLayoutManager(new LinearLayoutManager(getActivity()));
        ViewCompat.setNestedScrollingEnabled(categoryRcy, false);
    }

    private void initRecyclerView(){

//        String[] listTitle=getResources().getStringArray(R.array.listTitleCategory);
//        for (int i=0;i<listTitle.length;i++){
//            categoryItems.add(new CategoryItem(i,listTitle[i],R.drawable.ic_course));
//        }
        List<CategoryItem> myList=new ArrayList<>();
        categoryViewModel.getFakeCategory().observe(this, new Observer<List<CategoryItem>>() {
            @Override
            public void onChanged(@Nullable List<CategoryItem> categoryItems) {
                myList.clear();
                myList.addAll(categoryItems);
                adapter=new ListCategoryAdapter(getActivity(),myList);
                categoryRcy.setAdapter(adapter);
                setCategoryOnClick();
            }
        });


    }

    private void setCategoryOnClick(){
        if(adapter!=null){
            adapter.setOnItemClickListener(new ListCategoryAdapter.OnItemClickListener() {
                @Override
                public void onItemCategoryClick(CategoryItem categoryItem) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(ConstantUtil.KEY_SUB_CATEGORY, categoryItem);
                    SubCategoryFragment fragment = new SubCategoryFragment();
                    fragment.setArguments(bundle);
                    loadFragment(R.id.flContent, fragment,ConstantUtil.TAG_MAIN_ACTIVITY);
                }
            });
        }
    }





}
