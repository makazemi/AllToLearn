package com.parsclass.android.alltolearn.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.annotation.NonNull;

import com.parsclass.android.alltolearn.model.Question;
import com.parsclass.android.alltolearn.remote.Status;
import com.parsclass.android.alltolearn.repository.QuestionRepository;

import java.util.List;
import java.util.concurrent.Executors;

public class QuestionViewModel extends AndroidViewModel {
    private QuestionRepository repository;

    public QuestionViewModel(@NonNull Application application) {
        super(application);
        repository=new QuestionRepository(application, Executors.newSingleThreadExecutor());
    }
    public LiveData<List<Question>> getAllQuestions(int courseId){

        return repository.getQuestions(courseId);
    }


    public MutableLiveData<Status> getStatus(){
        return repository.getStatus();
    }
}
