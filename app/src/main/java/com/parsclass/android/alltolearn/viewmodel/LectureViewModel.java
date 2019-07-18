package com.parsclass.android.alltolearn.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;

import com.parsclass.android.alltolearn.model.LectureList;
import com.parsclass.android.alltolearn.repository.LectureRepository;

import java.util.List;
import java.util.concurrent.Executors;

public class LectureViewModel extends AndroidViewModel {

    private LectureRepository repository;

    public LectureViewModel(@NonNull Application application) {
        super(application);
        repository=new LectureRepository(application, Executors.newSingleThreadExecutor());
    }

//    public MutableLiveData<List<LectureList>> getFakeLecture(int courseId){
//        return repository.getFakLecture(courseId);
//    }

    public LiveData<List<LectureList>> getFakeLecture(int courseId){
        return repository.getFakLecture(courseId);
    }

    public void saveLecture(int courseId){
        repository.saveLecture(courseId);
    }

    public void update(LectureList lectureList){
        repository.update(lectureList);
    }
}
