package com.parsclass.android.alltolearn.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.annotation.NonNull;

import com.parsclass.android.alltolearn.model.Instructor;
import com.parsclass.android.alltolearn.remote.Status;
import com.parsclass.android.alltolearn.repository.InstructorRepository;

import java.util.List;
import java.util.concurrent.Executors;

public class InstructorViewModel extends AndroidViewModel {
    private InstructorRepository repository;

    public InstructorViewModel(@NonNull Application application) {
        super(application);
        repository=new InstructorRepository(application, Executors.newSingleThreadExecutor());
    }
    public LiveData<List<Instructor>> getAllInstructors(int courseId){

        return repository.getInstructors(courseId);
    }

    public MutableLiveData<List<Instructor>> getFakeInstructor(int courseId){
        return repository.getFakInstructor(courseId);
    }

    public MutableLiveData<List<Instructor>> getBriefInstructor(int courseId){
        return repository.getFakInstructorBrief(courseId);
    }


    public MutableLiveData<Status> getStatus(){
        return repository.getStatus();
    }
}
