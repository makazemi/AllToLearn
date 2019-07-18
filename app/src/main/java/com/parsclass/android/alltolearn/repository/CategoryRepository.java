package com.parsclass.android.alltolearn.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.parsclass.android.alltolearn.Utils.CourseGenerator;
import com.parsclass.android.alltolearn.local.CategoryDao;
import com.parsclass.android.alltolearn.local.LearnDatabase;
import com.parsclass.android.alltolearn.model.CategoryItem;
import com.parsclass.android.alltolearn.remote.APIClient;
import com.parsclass.android.alltolearn.remote.APIInterface;
import com.parsclass.android.alltolearn.remote.Status;
import com.parsclass.android.alltolearn.viewmodel.CategoryViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryRepository {

    private static final String TAG="CategoryRepository";
    private static int FRESH_TIMEOUT_IN_MINUTES = 1;

    private final Executor executor;

    private APIInterface apiInterface;
    public MutableLiveData<ArrayList<CategoryViewModel>> arrayListMutableLiveData=new MutableLiveData<>();


    private CategoryDao categoryDao;
    private Application application;

   private MutableLiveData<Status> statusMutableLiveData=new MutableLiveData<>();

    public CategoryRepository(Application application, Executor executor) {
        this.executor = executor;
        this.application = application;
        apiInterface= APIClient.getClient().create(APIInterface.class);
        LearnDatabase database= LearnDatabase.getInstance(application);
        categoryDao=database.categoryDao();
    }

    public LiveData<List<CategoryItem>>  getCategory(){

        refreshCategory();

        return categoryDao.loadCategoryItem();
//        List<CategoryItem> items;
//        categoryDao.loadCategoryItem();
//
//        items=getCategories();
//
//       // items=categoryDao.loadCategoryItem().getValue(); // return null
//        CategoryItem category;
//        ArrayList<CategoryViewModel> arrayList=new ArrayList<>();
//
//        for(int i=0;i<items.size();i++){
//
//            category=items.get(i);
//            CategoryViewModel categoryViewModel=new CategoryViewModel(application);
//            categoryViewModel.init(category);
//            arrayList.add(categoryViewModel);
//        }
//
//
//        arrayListMutableLiveData.setValue(arrayList);
//
//        return arrayListMutableLiveData;
    }

    private void refreshCategory(){

        executor.execute(() -> {
            String lastRefresh=getMaxRefreshTime(new Date()).toString();
            boolean sliderExists =(!(categoryDao.hasCategory(lastRefresh)).isEmpty());

            // If user have to be updated
            if (!sliderExists) {
               // statusMutableLiveData.postValue(Status.LOADING);
                setStatus(Status.LOADING);
               // Log.e(TAG,"in if");
                apiInterface.getCategory().enqueue(new Callback<List<CategoryItem>>() {
                    @Override
                    public void onResponse(Call<List<CategoryItem>> call, Response<List<CategoryItem>> response) {

                        executor.execute(() -> {
                            List<CategoryItem> categories=response.body();
                            if(response.body()!=null) {
                                for (int i = 0; i < categories.size(); i++) {
                                    categories.get(i).setLastRefresh(new Date());
                                    categoryDao.saveCategory(categories.get(i));
                                }
                            }

                        });
                        setStatus(Status.SUCCESS);
                        //statusMutableLiveData.postValue(Status.SUCCESS);
                    }

                    @Override
                    public void onFailure(Call<List<CategoryItem>> call, Throwable t) {

                      //  Log.e(TAG,"onFailure "+t.toString());
                        setStatus(Status.ERROR);
                        //statusMutableLiveData.postValue(Status.ERROR);
                    }
                });
            }

        });
    }

    private Date getMaxRefreshTime(Date currentDate){
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        cal.add(Calendar.MINUTE, -FRESH_TIMEOUT_IN_MINUTES);
        return cal.getTime();
    }

    public void setStatus(Status status){

        statusMutableLiveData.postValue(status);
    }

    public MutableLiveData<Status> getStatus(){
        return statusMutableLiveData;
    }

    public MutableLiveData<List<CategoryItem>> getFakeCategory(){
        MutableLiveData<List<CategoryItem>> mutableLiveData=new MutableLiveData<>();
        CourseGenerator courseGenerator=new CourseGenerator("jd",application);
        List<CategoryItem> list=courseGenerator.getCategory();
        mutableLiveData.setValue(list);

        return mutableLiveData;
    }

}


