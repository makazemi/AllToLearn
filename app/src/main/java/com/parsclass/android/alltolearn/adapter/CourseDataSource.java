package com.parsclass.android.alltolearn.adapter;

import androidx.paging.PageKeyedDataSource;
import androidx.annotation.NonNull;

import com.parsclass.android.alltolearn.model.Course;

public class CourseDataSource extends PageKeyedDataSource<Integer, Course> {
    public static final int PAGE_SIZE=50;
    public static final int FIRST_PAGE=1;
    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, Course> callback) {

       
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Course> callback) {

    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Course> callback) {

    }
}
