package com.parsclass.android.alltolearn.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.annotation.NonNull;

import com.parsclass.android.alltolearn.model.Lecture;
import com.parsclass.android.alltolearn.model.Section;
import com.parsclass.android.alltolearn.remote.Status;
import com.parsclass.android.alltolearn.repository.SectionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class SectionViewModel extends AndroidViewModel {
    private SectionRepository repository;

    public SectionViewModel(@NonNull Application application) {
        super(application);
        repository=new SectionRepository(application, Executors.newSingleThreadExecutor());
    }
//    public LiveData<List<Section>> getAllSections(int courseId){
//
//        return repository.getSections(courseId);
//    }

    public MutableLiveData<List<Section>> getFakeSection(int courseId){
        return repository.getFakSection(courseId);
    }


    public MutableLiveData<Status> getStatus(){
        return repository.getStatus();
    }

    public void updateSection(int sectionId, ArrayList<Lecture> lectureList,int courseId){
        repository.updateLocal(sectionId,lectureList,courseId);
    }

    public void updateRemoteSection(int courseId,int sectionId,ArrayList<Lecture> lectureList){
        repository.updateRemote(courseId,sectionId,lectureList);
    }
}
