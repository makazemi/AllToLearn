package com.parsclass.android.alltolearn.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.annotation.NonNull;

import com.parsclass.android.alltolearn.model.Comment;
import com.parsclass.android.alltolearn.remote.Status;
import com.parsclass.android.alltolearn.repository.CommentRepository;

import java.util.List;
import java.util.concurrent.Executors;

public class CommentViewModel extends AndroidViewModel {

    private CommentRepository repository;

    public CommentViewModel(@NonNull Application application) {
        super(application);
        repository=new CommentRepository(application, Executors.newSingleThreadExecutor());
    }
    public LiveData<List<Comment>> getAllComments(int courseId){

      //  return repository.getComments(courseId);
        return repository.getFakComment(courseId);
    }


    public MutableLiveData<Status> getStatus(){
        return repository.getStatus();
    }
}
