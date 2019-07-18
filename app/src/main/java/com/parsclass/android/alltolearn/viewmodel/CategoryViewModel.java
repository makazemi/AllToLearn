package com.parsclass.android.alltolearn.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.annotation.NonNull;

import com.parsclass.android.alltolearn.model.CategoryItem;
import com.parsclass.android.alltolearn.model.SubCategory;
import com.parsclass.android.alltolearn.remote.Status;
import com.parsclass.android.alltolearn.repository.CategoryRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;

public class CategoryViewModel extends AndroidViewModel {

    private int id;
    private String title;
    private String imagePath;
    private ArrayList<SubCategory> subCategory;
    private Date lastRefresh;
    private LiveData<List<CategoryItem>> allCategories;
    private CategoryRepository repository;




    public CategoryViewModel(@NonNull Application application) {
        super(application);
        repository=new CategoryRepository(application, Executors.newSingleThreadExecutor());
    }

    public void init(CategoryItem categoryItem){
        this.id=categoryItem.getId();
        this.title=categoryItem.getTitle();
        this.imagePath=categoryItem.getImagePath();
        this.subCategory=categoryItem.getSubCategory();
        this.lastRefresh=categoryItem.getLastRefresh();
    }

    public LiveData<List<CategoryItem>> getAllCategories(){

        allCategories=repository.getCategory();
        return allCategories;
    }


    public MutableLiveData<Status> getStatus(){
        return repository.getStatus();
    }

    public MutableLiveData<List<CategoryItem>> getFakeCategory(){
        return repository.getFakeCategory();
    }
}
