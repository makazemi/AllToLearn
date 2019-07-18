package com.parsclass.android.alltolearn.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import androidx.annotation.NonNull;

import com.parsclass.android.alltolearn.Paging.CourseDataSourceClass;
import com.parsclass.android.alltolearn.Paging.CourseDataSourceFactory;
import com.parsclass.android.alltolearn.model.Course;
import com.parsclass.android.alltolearn.remote.Status;
import com.parsclass.android.alltolearn.repository.CourseRepository;

import java.util.List;
import java.util.concurrent.Executors;

import io.reactivex.disposables.CompositeDisposable;

public class CourseViewModel extends AndroidViewModel {

    private LiveData<List<Course>>  sliderCourses;
    private MutableLiveData<List<Course>> mSliderCourse;
    private CourseRepository repository;


    private CourseDataSourceFactory courseDataSourceFactory;
    private LiveData<PagedList<Course>> listLiveData;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private LiveData<String> progressLoadStatus = new MutableLiveData<>();


    public CourseViewModel(@NonNull Application application) {
        super(application);
        repository=new CourseRepository(application,Executors.newSingleThreadExecutor());
        courseDataSourceFactory = new CourseDataSourceFactory(repository, compositeDisposable);
        initializePaging();
    }

    private void initializePaging() {

        PagedList.Config pagedListConfig =
                new PagedList.Config.Builder()
                        .setEnablePlaceholders(true)
                        .setInitialLoadSizeHint(10)
                        .setPageSize(10).build();

        listLiveData = new LivePagedListBuilder<>(courseDataSourceFactory, pagedListConfig)
                .build();

        progressLoadStatus = Transformations.switchMap(courseDataSourceFactory.getMutableLiveData(), CourseDataSourceClass::getProgressLiveStatus);

    }

    public LiveData<String> getProgressLoadStatus() {
        return progressLoadStatus;
    }

    public LiveData<PagedList<Course>> getListLiveData() {
        return listLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }



    public void init(Course course){

    }



    public LiveData<List<Course>> getSlider(){
        sliderCourses=repository.getSliderCourse();
        return sliderCourses;
//        mSliderCourse=repository.refreshSlider();
//        return mSliderCourse;
    }

    public LiveData<List<Course>> getCourse(String typeListCourse){
        //   return repository.getTopCourse();
        return repository.getCourse(typeListCourse);
    }

    public MutableLiveData<List<Course>> getFakCourses(String typeListCourse) {

        return repository.getFakCourse(typeListCourse);
    }

    public MutableLiveData<List<Course>> getFavoriteCourses(String userToken,String typeListCourse){
        return repository.getFakCourse(typeListCourse);
    }

    public MutableLiveData<List<Course>> getFakAlsoView() {

        return repository.getAlsoViewFak();
    }
    public LiveData<List<Course>> getAlsoViewCourse(String typeListCourse){

        return repository.getCourse(typeListCourse);

    }

    public MutableLiveData<Status> getStatus(){
        return repository.getStatus();
    }

    public MutableLiveData<Boolean> isEnrolled(String courseId){
        return repository.isEnrolled(courseId);
    }


}
