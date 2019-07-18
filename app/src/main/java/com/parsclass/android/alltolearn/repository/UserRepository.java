package com.parsclass.android.alltolearn.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.os.AsyncTask;

import com.parsclass.android.alltolearn.config.MyApplication;
import com.parsclass.android.alltolearn.local.LearnDatabase;
import com.parsclass.android.alltolearn.local.UserDao;
import com.parsclass.android.alltolearn.model.User;
import com.parsclass.android.alltolearn.remote.APIClient;
import com.parsclass.android.alltolearn.remote.APIInterface;
import com.parsclass.android.alltolearn.remote.Status;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {
    private static int FRESH_TIMEOUT_IN_MINUTES = 1;

    private  APIInterface apiInterface;
    private final UserDao userDao;
    private final Executor executor;
    private Application application;


    private MutableLiveData<Status> statusMutableLiveData=new MutableLiveData<>();

    public UserRepository(Application application, Executor executor) {
        apiInterface= APIClient.getClient().create(APIInterface.class);
        LearnDatabase database= LearnDatabase.getInstance(application);
        this.userDao =database.userDao();
        this.executor = executor;
        this.application=application;
    }



    public LiveData<User> getUser(String email, String password) {
        refreshUser(email,password);
        return userDao.load(email);
    }

    public String getToken(String name,String email,String password){
        refreshToken(name,email,password);
        return MyApplication.prefHelper.getToken();

    }

    public void refreshToken(String name,String email,String password){

        if(MyApplication.prefHelper.getToken().equals("")) {
            setStatus(Status.LOADING);
            apiInterface.getToken(name, email, password).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    MyApplication.prefHelper.putToken(response.body());
                    MyApplication.prefHelper.setUserLogin(true);
                    User user=new User(name,email,response.body());
                    userDao.save(user);
                    setStatus(Status.SUCCESS);
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    setStatus(Status.ERROR);
                }
            });
        }
    }



    private void refreshUser(final String email,final String password) {
        executor.execute(() -> {

            String lastRefresh=getMaxRefreshTime(new Date()).toString();
            boolean userExists = (userDao.hasUser(email,lastRefresh)!=null);
            if (!userExists) {
                setStatus(Status.LOADING);
                apiInterface.loginUser(email,password).enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {

                        executor.execute(() -> {
                            User user = response.body();
                            user.setLastRefresh(new Date());
                            MyApplication.prefHelper.putToken(user.getToken());
                            MyApplication.prefHelper.setUserLogin(true);
                            userDao.save(user);
                        });
                        setStatus(Status.SUCCESS);
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        setStatus(Status.ERROR);
                    }
                });
            }
        });
    }



    private Date getMaxRefreshTime(Date currentDate){
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        cal.add(Calendar.MINUTE, -FRESH_TIMEOUT_IN_MINUTES);
        return cal.getTime();
    }
    public void setStatus(Status status){

        statusMutableLiveData.postValue(status);
    }

    public MutableLiveData<Status> getStatus(){
        return statusMutableLiveData;
    }

    public boolean signOutUser(){
        new DeleteAllUserAsyncTask(userDao).execute();
        MyApplication.prefHelper.setUserLogin(false);
        MyApplication.prefHelper.putToken("");
        return true;
    }

    private static class DeleteAllUserAsyncTask extends AsyncTask<Void,Void,Void> {
        private UserDao userDao;

        private DeleteAllUserAsyncTask(UserDao userDao){
            this.userDao=userDao;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            userDao.deleteAllUsers();
            return null;
        }
    }


    public MutableLiveData<User> getUserFake(String email,String password){
        setStatus(Status.LOADING);
        User user=new User(email,password);
        user.setToken("tokentoken");
        user.setName(email);
        MyApplication.prefHelper.putToken(user.getToken());
        MyApplication.prefHelper.setUserLogin(true);
        MutableLiveData<User> mutableLiveData=new MutableLiveData<>();
        mutableLiveData.setValue(user);
        setStatus(Status.SUCCESS);
        return mutableLiveData;
    }

    public String getTokenFake(String name,String email,String password){
        if(MyApplication.prefHelper.getToken().equals("")) {
            setStatus(Status.LOADING);
            String token="tokentoken";
            MyApplication.prefHelper.putToken(token);
            MyApplication.prefHelper.setUserLogin(true);
            setStatus(Status.SUCCESS);
            return token;
        }
       else {
            setStatus(Status.SUCCESS);
            MyApplication.prefHelper.setUserLogin(true);
           return MyApplication.prefHelper.getToken();

        }
    }
}
