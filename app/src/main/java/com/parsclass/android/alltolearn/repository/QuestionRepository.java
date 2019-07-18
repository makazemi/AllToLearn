package com.parsclass.android.alltolearn.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.util.Log;

import com.parsclass.android.alltolearn.local.LearnDatabase;
import com.parsclass.android.alltolearn.local.QuestionDao;
import com.parsclass.android.alltolearn.model.Question;
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

public class QuestionRepository {
    private static final String TAG="CommentRepository";
    private static int FRESH_TIMEOUT_IN_MINUTES = 1;

    private final Executor executor;

    private APIInterface apiInterface;

    private QuestionDao questionDao;
    private Application application;

    private MutableLiveData<Status> statusMutableLiveData=new MutableLiveData<>();
    public QuestionRepository(Application application, Executor executor) {
        this.executor = executor;
        this.application = application;
        apiInterface= APIClient.getClient().create(APIInterface.class);
        LearnDatabase database= LearnDatabase.getInstance(application);
        questionDao=database.questionDao();
    }
    public LiveData<List<Question>> getQuestions(int courseId){

        refreshComment(courseId);

        return questionDao.loadQuestionItem(courseId);
    }

    private void refreshComment(int courseId){

        executor.execute(() -> {
            String lastRefresh=getMaxRefreshTime(new Date()).toString();
            boolean isExists =(!(questionDao.hasQuestion(lastRefresh,courseId)).isEmpty());

            // If user have to be updated
            if (!isExists) {
                // statusMutableLiveData.postValue(Status.LOADING);
                setStatus(Status.LOADING);
                Log.e(TAG,"in if");
                apiInterface.getQuestion(courseId).enqueue(new Callback<List<Question>>() {
                    @Override
                    public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {

                        executor.execute(() -> {
                            List<Question> questions=response.body();
                            for (int i=0;i<questions.size();i++){
                                questions.get(i).setLastRefresh(new Date());
                                questionDao.saveQuestion(questions.get(i));
                            }

                        });
                        setStatus(Status.SUCCESS);
                        //statusMutableLiveData.postValue(Status.SUCCESS);
                    }

                    @Override
                    public void onFailure(Call<List<Question>> call, Throwable t) {

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
