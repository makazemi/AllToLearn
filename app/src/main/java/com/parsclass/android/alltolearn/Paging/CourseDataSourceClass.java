package com.parsclass.android.alltolearn.Paging;

import android.annotation.SuppressLint;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;
import androidx.annotation.NonNull;
import android.util.Log;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.parsclass.android.alltolearn.Utils.ConstantUtil;
import com.parsclass.android.alltolearn.model.Course;
import com.parsclass.android.alltolearn.repository.CourseRepository;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import io.reactivex.disposables.CompositeDisposable;

public class CourseDataSourceClass extends PageKeyedDataSource<Integer, Course> {

    public static final String TAG = "NewsDataSourceClass";

    private CourseRepository repository;
    private Gson gson;
    private int sourceIndex;
    private MutableLiveData<String> progressLiveStatus;
    private CompositeDisposable compositeDisposable;

    CourseDataSourceClass(CourseRepository repository, CompositeDisposable compositeDisposable) {
        this.repository = repository;
        this.compositeDisposable = compositeDisposable;
        progressLiveStatus = new MutableLiveData<>();
        GsonBuilder builder =
                new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        gson = builder.setLenient().create();

        Log.e(TAG, "in constructor newsdatasourcefactory ");
    }


    public MutableLiveData<String> getProgressLiveStatus() {
        return progressLiveStatus;
    }

    @SuppressLint("CheckResult")
    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, Course> callback) {

        repository.executeCourseApi(sourceIndex)
                .doOnSubscribe(disposable ->
                {
                    compositeDisposable.add(disposable);
                    progressLiveStatus.postValue(ConstantUtil.LOADING);
                })
                .subscribe(
                        (JsonElement result) ->
                        {
                            progressLiveStatus.postValue(ConstantUtil.LOADED);

                            JSONObject object = new JSONObject(gson.toJson(result));
                            JSONArray array = object.getJSONArray("articles");

                            ArrayList<Course> arrayList = new ArrayList<>();

                            for (int i = 0; i < array.length(); i++) {
                                arrayList.add(new Course(array.getJSONObject(i).optString("title"),
                                        array.getJSONObject(i).optString("urlToImage")));
                            }

                            sourceIndex++;
                            callback.onResult(arrayList, null, sourceIndex);

                            Log.e(TAG, "arraylist: " + arrayList.toString());
                        },
                        throwable -> {
                            progressLiveStatus.postValue(ConstantUtil.LOADED);
                            Log.e(TAG, "in throwable");
                        }


                );

    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Course> callback) {

    }

    @SuppressLint("CheckResult")
    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Course> callback) {

        repository.executeCourseApi(params.key)
                .doOnSubscribe(disposable ->
                {
                    compositeDisposable.add(disposable);
                    progressLiveStatus.postValue(ConstantUtil.LOADING);
                })
                .subscribe(
                        (JsonElement result) ->
                        {
                            progressLiveStatus.postValue(ConstantUtil.LOADED);

                            JSONObject object = new JSONObject(gson.toJson(result));
                            JSONArray array = object.getJSONArray("articles");

                            ArrayList<Course> arrayList = new ArrayList<>();

                            for (int i = 0; i < array.length(); i++) {
                                arrayList.add(new Course(array.getJSONObject(i).optString("title"),
                                        array.getJSONObject(i).optString("urlToImage")));
                            }

                            callback.onResult(arrayList, params.key == 3 ? null : params.key + 1);

                        },
                        throwable ->
                                progressLiveStatus.postValue(ConstantUtil.LOADED)
                );
    }
}
