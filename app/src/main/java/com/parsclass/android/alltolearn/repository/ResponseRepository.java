package com.parsclass.android.alltolearn.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.util.Log;

import com.parsclass.android.alltolearn.local.LearnDatabase;
import com.parsclass.android.alltolearn.local.ResponseDao;
import com.parsclass.android.alltolearn.model.QResponse;
import com.parsclass.android.alltolearn.remote.APIClient;
import com.parsclass.android.alltolearn.remote.APIInterface;
import com.parsclass.android.alltolearn.remote.Status;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResponseRepository {
    private static final String TAG="CommentRepository";
    private static int FRESH_TIMEOUT_IN_MINUTES = 1;

    private final Executor executor;

    private APIInterface apiInterface;

    private ResponseDao responseDao;
    private Application application;

    private MutableLiveData<Status> statusMutableLiveData=new MutableLiveData<>();
    public ResponseRepository(Application application, Executor executor) {
        this.executor = executor;
        this.application = application;
        apiInterface= APIClient.getClient().create(APIInterface.class);
        LearnDatabase database= LearnDatabase.getInstance(application);
        responseDao=database.responseDao();
    }
    public LiveData<List<QResponse>> getResponses(int questionId){

        refreshResponse(questionId);

        return responseDao.loadResponseItem(questionId);
    }

    private void refreshResponse(int questionId){

        executor.execute(() -> {
            String lastRefresh=getMaxRefreshTime(new Date()).toString();
            boolean isExists =(!(responseDao.hasResponse(lastRefresh,questionId)).isEmpty());

            // If user have to be updated
            if (!isExists) {
                setStatus(Status.LOADING);
                Log.e(TAG,"in if");
                apiInterface.getQResponse(questionId).enqueue(new Callback<List<QResponse>>() {
                    @Override
                    public void onResponse(Call<List<QResponse>> call, Response<List<QResponse>> response) {

                        executor.execute(() -> {
                            List<QResponse> qResponses=response.body();
                            for (int i=0;i<qResponses.size();i++){
                                qResponses.get(i).setLastRefresh(new Date());
                                responseDao.saveResponse(qResponses.get(i));
                            }

                        });
                        setStatus(Status.SUCCESS);
                        //statusMutableLiveData.postValue(Status.SUCCESS);
                    }

                    @Override
                    public void onFailure(Call<List<QResponse>> call, Throwable t) {

                        Log.e(TAG,"onFailure "+t.toString());
                        setStatus(Status.ERROR);
                        //statusMutableLiveData.postValue(Status.ERROR);
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
}
