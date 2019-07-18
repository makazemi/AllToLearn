package com.parsclass.android.alltolearn.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.util.Log;

import com.parsclass.android.alltolearn.local.CommentDao;
import com.parsclass.android.alltolearn.local.LearnDatabase;
import com.parsclass.android.alltolearn.model.Comment;
import com.parsclass.android.alltolearn.remote.APIClient;
import com.parsclass.android.alltolearn.remote.APIInterface;
import com.parsclass.android.alltolearn.remote.Status;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentRepository {

    private static final String TAG="CommentRepository";
    private static int FRESH_TIMEOUT_IN_MINUTES = 1;

    private final Executor executor;

    private APIInterface apiInterface;

    private CommentDao commentDao;
    private Application application;

    private MutableLiveData<Status> statusMutableLiveData=new MutableLiveData<>();
    public CommentRepository(Application application, Executor executor) {
        this.executor = executor;
        this.application = application;
        apiInterface= APIClient.getClient().create(APIInterface.class);
        LearnDatabase database= LearnDatabase.getInstance(application);
        commentDao=database.commentDao();
    }
    public LiveData<List<Comment>> getComments(int courseId){

        //refreshComment(courseId);
        refreshCommentFake(courseId);

        return commentDao.loadCommentItem(courseId);
    }

    private void refreshCommentFake(int courseId){
        String lastRefresh=getMaxRefreshTime(new Date()).toString();
        boolean sliderExists =(!(commentDao.hasComment(lastRefresh,courseId)).isEmpty());

        if (!sliderExists) {
            setStatus(Status.LOADING);
            List<Comment> comments=new ArrayList<>();
            Date date=new Date();
            comments.add(new Comment(1,"زهرا بیات","دوره ی بسیار خوبی بود. از استاد عزیز کمال تشکر را دارم.دوره ی بسیار خوبی بود. از استاد عزیز کمال تشکر را دارم.دوره ی بسیار خوبی بود. از استاد عزیز کمال تشکر را دارم.",3,date,1));
            comments.add(new Comment(2,"زهرا بیات","دوره ی بسیار خوبی بود. از استاد عزیز کمال تشکر را دارم.دوره ی بسیار خوبی بود. از استاد عزیز کمال تشکر را دارم.دوره ی بسیار خوبی بود. از استاد عزیز کمال تشکر را دارم.",3,date,1));
            comments.add(new Comment(3,"زهرا بیات","دوره ی بسیار خوبی بود. از استاد عزیز کمال تشکر را دارم.دوره ی بسیار خوبی بود. از استاد عزیز کمال تشکر را دارم.دوره ی بسیار خوبی بود. از استاد عزیز کمال تشکر را دارم.",3,date,1));
            comments.add(new Comment(4,"زهرا بیات","دوره ی بسیار خوبی بود. از استاد عزیز کمال تشکر را دارم.دوره ی بسیار خوبی بود. از استاد عزیز کمال تشکر را دارم.دوره ی بسیار خوبی بود. از استاد عزیز کمال تشکر را دارم.",3,date,1));
            comments.add(new Comment(5,"زهرا بیات","دوره ی بسیار خوبی بود. از استاد عزیز کمال تشکر را دارم.دوره ی بسیار خوبی بود. از استاد عزیز کمال تشکر را دارم.دوره ی بسیار خوبی بود. از استاد عزیز کمال تشکر را دارم.",3,date,1));

            for (int i=0;i<comments.size();i++){
                comments.get(i).setLastRefresh(new Date());
                commentDao.saveComment(comments.get(i));
            }
            setStatus(Status.SUCCESS);
        }

    }

    private void refreshComment(int courseId){

        executor.execute(() -> {
            String lastRefresh=getMaxRefreshTime(new Date()).toString();
            boolean sliderExists =(!(commentDao.hasComment(lastRefresh,courseId)).isEmpty());

            // If user have to be updated
            if (!sliderExists) {
                // statusMutableLiveData.postValue(Status.LOADING);
                setStatus(Status.LOADING);
                Log.e(TAG,"in if");
                apiInterface.getComment(courseId).enqueue(new Callback<List<Comment>>() {
                    @Override
                    public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {

                        executor.execute(() -> {
                            List<Comment> comments=response.body();
                            for (int i=0;i<comments.size();i++){
                                comments.get(i).setLastRefresh(new Date());
                                commentDao.saveComment(comments.get(i));
                            }

                        });
                        setStatus(Status.SUCCESS);
                        //statusMutableLiveData.postValue(Status.SUCCESS);
                    }

                    @Override
                    public void onFailure(Call<List<Comment>> call, Throwable t) {

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

    public MutableLiveData<List<Comment>> getFakComment(int courseId){
        List<Comment> comments=new ArrayList<>();
        Date date=new Date();
        comments.add(new Comment(1,"زهرا بیات","دوره ی بسیار خوبی بود. از استاد عزیز کمال تشکر را دارم.دوره ی بسیار خوبی بود. از استاد عزیز کمال تشکر را دارم.دوره ی بسیار خوبی بود. از استاد عزیز کمال تشکر را دارم.",3,date,courseId));
        comments.add(new Comment(2,"زهرا بیات","دوره ی بسیار خوبی بود. از استاد عزیز کمال تشکر را دارم.دوره ی بسیار خوبی بود. از استاد عزیز کمال تشکر را دارم.دوره ی بسیار خوبی بود. از استاد عزیز کمال تشکر را دارم.",3,date,courseId));
        comments.add(new Comment(3,"زهرا بیات","دوره ی بسیار خوبی بود. از استاد عزیز کمال تشکر را دارم.دوره ی بسیار خوبی بود. از استاد عزیز کمال تشکر را دارم.دوره ی بسیار خوبی بود. از استاد عزیز کمال تشکر را دارم.",3,date,courseId));
        comments.add(new Comment(4,"زهرا بیات","دوره ی بسیار خوبی بود. از استاد عزیز کمال تشکر را دارم.دوره ی بسیار خوبی بود. از استاد عزیز کمال تشکر را دارم.دوره ی بسیار خوبی بود. از استاد عزیز کمال تشکر را دارم.",3,date,courseId));
        comments.add(new Comment(5,"زهرا بیات","دوره ی بسیار خوبی بود. از استاد عزیز کمال تشکر را دارم.دوره ی بسیار خوبی بود. از استاد عزیز کمال تشکر را دارم.دوره ی بسیار خوبی بود. از استاد عزیز کمال تشکر را دارم.",3,date,courseId));
        comments.add(new Comment(5,"زهرا بیات","دوره ی بسیار خوبی بود. از استاد عزیز کمال تشکر را دارم.دوره ی بسیار خوبی بود. از استاد عزیز کمال تشکر را دارم.دوره ی بسیار خوبی بود. از استاد عزیز کمال تشکر را دارم.",3,date,courseId));
        comments.add(new Comment(5,"زهرا بیات","دوره ی بسیار خوبی بود. از استاد عزیز کمال تشکر را دارم.دوره ی بسیار خوبی بود. از استاد عزیز کمال تشکر را دارم.دوره ی بسیار خوبی بود. از استاد عزیز کمال تشکر را دارم.",3,date,courseId));
        comments.add(new Comment(5,"زهرا بیات","دوره ی بسیار خوبی بود. از استاد عزیز کمال تشکر را دارم.دوره ی بسیار خوبی بود. از استاد عزیز کمال تشکر را دارم.دوره ی بسیار خوبی بود. از استاد عزیز کمال تشکر را دارم.",3,date,courseId));
        comments.add(new Comment(5,"زهرا بیات","دوره ی بسیار خوبی بود. از استاد عزیز کمال تشکر را دارم.دوره ی بسیار خوبی بود. از استاد عزیز کمال تشکر را دارم.دوره ی بسیار خوبی بود. از استاد عزیز کمال تشکر را دارم.",3,date,courseId));
        comments.add(new Comment(5,"زهرا بیات","دوره ی بسیار خوبی بود. از استاد عزیز کمال تشکر را دارم.دوره ی بسیار خوبی بود. از استاد عزیز کمال تشکر را دارم.دوره ی بسیار خوبی بود. از استاد عزیز کمال تشکر را دارم.",3,date,courseId));
        comments.add(new Comment(5,"زهرا بیات","دوره ی بسیار خوبی بود. از استاد عزیز کمال تشکر را دارم.دوره ی بسیار خوبی بود. از استاد عزیز کمال تشکر را دارم.دوره ی بسیار خوبی بود. از استاد عزیز کمال تشکر را دارم.",3,date,courseId));
        comments.add(new Comment(5,"زهرا بیات","دوره ی بسیار خوبی بود. از استاد عزیز کمال تشکر را دارم.دوره ی بسیار خوبی بود. از استاد عزیز کمال تشکر را دارم.دوره ی بسیار خوبی بود. از استاد عزیز کمال تشکر را دارم.",3,date,courseId));
        comments.add(new Comment(5,"زهرا بیات","دوره ی بسیار خوبی بود. از استاد عزیز کمال تشکر را دارم.دوره ی بسیار خوبی بود. از استاد عزیز کمال تشکر را دارم.دوره ی بسیار خوبی بود. از استاد عزیز کمال تشکر را دارم.",3,date,courseId));
        comments.add(new Comment(5,"زهرا بیات","دوره ی بسیار خوبی بود. از استاد عزیز کمال تشکر را دارم.دوره ی بسیار خوبی بود. از استاد عزیز کمال تشکر را دارم.دوره ی بسیار خوبی بود. از استاد عزیز کمال تشکر را دارم.",3,date,courseId));
        comments.add(new Comment(5,"زهرا بیات","دوره ی بسیار خوبی بود. از استاد عزیز کمال تشکر را دارم.دوره ی بسیار خوبی بود. از استاد عزیز کمال تشکر را دارم.دوره ی بسیار خوبی بود. از استاد عزیز کمال تشکر را دارم.",3,date,courseId));
        comments.add(new Comment(5,"زهرا بیات","دوره ی بسیار خوبی بود. از استاد عزیز کمال تشکر را دارم.دوره ی بسیار خوبی بود. از استاد عزیز کمال تشکر را دارم.دوره ی بسیار خوبی بود. از استاد عزیز کمال تشکر را دارم.",3,date,courseId));
        comments.add(new Comment(5,"زهرا بیات","دوره ی بسیار خوبی بود. از استاد عزیز کمال تشکر را دارم.دوره ی بسیار خوبی بود. از استاد عزیز کمال تشکر را دارم.دوره ی بسیار خوبی بود. از استاد عزیز کمال تشکر را دارم.",3,date,courseId));
        comments.add(new Comment(5,"زهرا بیات","دوره ی بسیار خوبی بود. از استاد عزیز کمال تشکر را دارم.دوره ی بسیار خوبی بود. از استاد عزیز کمال تشکر را دارم.دوره ی بسیار خوبی بود. از استاد عزیز کمال تشکر را دارم.",3,date,courseId));
        comments.add(new Comment(5,"زهرا بیات","دوره ی بسیار خوبی بود. از استاد عزیز کمال تشکر را دارم.دوره ی بسیار خوبی بود. از استاد عزیز کمال تشکر را دارم.دوره ی بسیار خوبی بود. از استاد عزیز کمال تشکر را دارم.",3,date,courseId));
        comments.add(new Comment(5,"زهرا بیات","دوره ی بسیار خوبی بود. از استاد عزیز کمال تشکر را دارم.دوره ی بسیار خوبی بود. از استاد عزیز کمال تشکر را دارم.دوره ی بسیار خوبی بود. از استاد عزیز کمال تشکر را دارم.",3,date,courseId));
        comments.add(new Comment(5,"زهرا بیات","دوره ی بسیار خوبی بود. از استاد عزیز کمال تشکر را دارم.دوره ی بسیار خوبی بود. از استاد عزیز کمال تشکر را دارم.دوره ی بسیار خوبی بود. از استاد عزیز کمال تشکر را دارم.",3,date,courseId));

        MutableLiveData<List<Comment>> mutableLiveData=new MutableLiveData<>();
        mutableLiveData.setValue(comments);
        return mutableLiveData;
    }
}
