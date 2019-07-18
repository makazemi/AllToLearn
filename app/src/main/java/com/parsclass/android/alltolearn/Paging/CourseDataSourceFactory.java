package com.parsclass.android.alltolearn.Paging;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.parsclass.android.alltolearn.model.Course;
import com.parsclass.android.alltolearn.repository.CourseRepository;

import io.reactivex.disposables.CompositeDisposable;

public class CourseDataSourceFactory extends DataSource.Factory<Integer, Course>{
    private MutableLiveData<CourseDataSourceClass> liveData;
    private CourseRepository repository;
    private CompositeDisposable compositeDisposable;

    public CourseDataSourceFactory(CourseRepository repository, CompositeDisposable compositeDisposable) {
        this.repository = repository;
        this.compositeDisposable = compositeDisposable;
        liveData = new MutableLiveData<>();
    }

    public MutableLiveData<CourseDataSourceClass> getMutableLiveData() {
        return liveData;
    }

    @Override
    public DataSource<Integer, Course> create() {
        CourseDataSourceClass dataSourceClass = new CourseDataSourceClass(repository, compositeDisposable);
        liveData.postValue(dataSourceClass);
        return dataSourceClass;
    }
}
