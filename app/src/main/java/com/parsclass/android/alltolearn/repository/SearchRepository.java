package com.parsclass.android.alltolearn.repository;

import androidx.lifecycle.MutableLiveData;
import android.util.Log;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.parsclass.android.alltolearn.Utils.ConstantUtil;
import com.parsclass.android.alltolearn.model.Article;
import com.parsclass.android.alltolearn.model.ResponseModel;
import com.parsclass.android.alltolearn.remote.APIClient;
import com.parsclass.android.alltolearn.remote.APIInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchRepository {

    private final String TAG="Repository";
    private APIInterface apiInterface;
    private Gson gson;

    private MutableLiveData<ArrayList<Article>> liveData=new MutableLiveData<>();
    private MutableLiveData<String> progressLoadStatus = new MutableLiveData<>();

    public SearchRepository() {

        this.apiInterface= APIClient.getClient2().create(APIInterface.class);
        GsonBuilder builder =
                new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        gson = builder.setLenient().create();

    }

    public MutableLiveData<ArrayList<Article>> getLiveData(String query, String apiKey) {
        Log.e(TAG,"in getlivedata");
        executeApi(query,apiKey);
        return liveData;
    }

    public MutableLiveData<String> getProgressLoadStatus() {

        return progressLoadStatus;
    }

    public void executeApi(String query, String apiKey){
        progressLoadStatus.postValue(ConstantUtil.LOADING);
        apiInterface.fetchListNewsSearch(query,apiKey).enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                ArrayList<Article> articles=new ArrayList<>();
                if(response.body()!=null) {
                    articles.addAll(response.body().getArticles());
                    Log.e(TAG, response.body().getStatus());
                    liveData.postValue(articles);
                    progressLoadStatus.setValue(ConstantUtil.LOADED);
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {

                progressLoadStatus.setValue(ConstantUtil.FAILURE);
                Log.e(TAG,"onFauile");
            }
        });
    }
}
