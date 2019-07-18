package com.parsclass.android.alltolearn.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.annotation.NonNull;

import com.parsclass.android.alltolearn.model.User;
import com.parsclass.android.alltolearn.remote.Status;
import com.parsclass.android.alltolearn.repository.UserRepository;

import java.util.concurrent.Executors;

public class LoginViewModel extends AndroidViewModel {

    private LiveData<User> user;
    private UserRepository repository;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        repository=new UserRepository(application, Executors.newSingleThreadExecutor());
    }

    public void initLogin(String email,String password) {
        if (this.user != null) {
            return;
        }
        user = repository.getUser(email,password);
    }

    public String getToken(String name,String email,String password){
        //return repository.getToken(name,email,password);
        return repository.getTokenFake(name,email,password);
    }

    public LiveData<User> getUser() {
        return this.user;
    }
    public MutableLiveData<User> getUserFake(String email,String password){
        return repository.getUserFake(email,password);
    }

    public boolean signOutUser(){
        return repository.signOutUser();
    }

    public String getTokenFake(String name,String email,String password){
        return repository.getTokenFake(name,email,password);
    }

    public MutableLiveData<Status> getStatus(){
        return repository.getStatus();
    }



}
