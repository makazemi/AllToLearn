package com.parsclass.android.alltolearn.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.annotation.NonNull;

import com.parsclass.android.alltolearn.model.QResponse;
import com.parsclass.android.alltolearn.remote.Status;
import com.parsclass.android.alltolearn.repository.ResponseRepository;

import java.util.List;
import java.util.concurrent.Executors;

public class ResponseViewModel extends AndroidViewModel {
    private ResponseRepository repository;

    public ResponseViewModel(@NonNull Application application) {
        super(application);
        repository=new ResponseRepository(application, Executors.newSingleThreadExecutor());
    }
    public LiveData<List<QResponse>> getAllResponses(int questionId){

        return repository.getResponses(questionId);
    }


    public MutableLiveData<Status> getStatus(){
        return repository.getStatus();
    }
}
