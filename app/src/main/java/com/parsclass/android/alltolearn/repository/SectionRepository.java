package com.parsclass.android.alltolearn.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.util.Log;

import com.parsclass.android.alltolearn.config.MyApplication;
import com.parsclass.android.alltolearn.local.LearnDatabase;
import com.parsclass.android.alltolearn.local.SectionDao;
import com.parsclass.android.alltolearn.model.Lecture;
import com.parsclass.android.alltolearn.model.Section;
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

public class SectionRepository {
    private static final String TAG="CommentRepository";
    private static int FRESH_TIMEOUT_IN_MINUTES = 1;

    private final Executor executor;

    private APIInterface apiInterface;

    private SectionDao sectionDao;
    private Application application;

    private MutableLiveData<Status> statusMutableLiveData=new MutableLiveData<>();
    public SectionRepository(Application application, Executor executor) {
        this.executor = executor;
        this.application = application;
        apiInterface= APIClient.getClient().create(APIInterface.class);
        LearnDatabase database= LearnDatabase.getInstance(application);
        sectionDao=database.sectionDao();
    }
    public LiveData<List<Section>> getSections(int courseId){

        refreshSection(courseId);
        return sectionDao.loadSectionItem();
    }

    private void refreshSection(int courseId){

        executor.execute(() -> {
            String lastRefresh=getMaxRefreshTime(new Date()).toString();
           boolean sliderExists =(!(sectionDao.hasSection(lastRefresh)).isEmpty());

            if (!sliderExists) {
                 statusMutableLiveData.postValue(Status.LOADING);
                setStatus(Status.LOADING);
                Log.e(TAG,"in if");
                apiInterface.getSection(courseId).enqueue(new Callback<List<Section>>() {
                    @Override
                    public void onResponse(Call<List<Section>> call, Response<List<Section>> response) {

                        executor.execute(() -> {
                            List<Section> sections=response.body();

                            for (int i=0;i<sections.size();i++){
                                sections.get(i).setLastRefresh(new Date());
                                sectionDao.saveSection(sections.get(i));
                            }

                        });
                        setStatus(Status.SUCCESS);

                    }

                    @Override
                    public void onFailure(Call<List<Section>> call, Throwable t) {

                        Log.e(TAG,"onFailure "+t.toString());
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

    public MutableLiveData<List<Section>> getFakSection(int courseId){
        List<Section> sections=new ArrayList<>();
        ArrayList<Lecture> lectures1=new ArrayList<>();
        ArrayList<Lecture> lectures2=new ArrayList<>();
        ArrayList<Lecture> lectures3=new ArrayList<>();
        ArrayList<Lecture> lectures4=new ArrayList<>();
        boolean flag=false;
        for (int i=0;i<5;i++){
            if(i%2==0)
                flag=true;
            Lecture lecture=new Lecture(String.valueOf(i),"نحوه نصب اندروید استودیو","VIDEO","5:24",flag,
                    false,false,"https://hw7.cdn.asset.aparat.com/aparat-video/626c9e8c56dd1827ca4b047b900e942413877501-1080p__23543.mp4");
            lecture.setIndex_dataSource(i);
            lectures1.add(lecture);
        }
        for (int i=5;i<10;i++){
            if(i%2==0)
                flag=true;
            Lecture lecture=new Lecture(String.valueOf(i),"نحوه نصب اندروید استودیو","VIDEO","5:24",flag,
                    false,false,"https://hw4.cdn.asset.aparat.com/aparat-video/e3127fe6b118a47cfdb4375c5a14538414560798-240p__61652.mp4");
            lecture.setIndex_dataSource(i);
            lectures2.add(lecture);
        }
        for (int i=10;i<15;i++){
            if(i%2==0)
                flag=true;
            Lecture lecture=new Lecture(String.valueOf(i),"نحوه نصب اندروید استودیو","VIDEO","5:24",flag,
                    false,false,"https://hw14.cdn.asset.aparat.com/aparat-video/44371cbff17d8dacee0c727394c063c814535313-144p__81353.mp4");
            lecture.setIndex_dataSource(i);
            lectures3.add(lecture);
        }
        for (int i=15;i<20;i++){
            if(i%2==0)
                flag=true;
            Lecture lecture=new Lecture(String.valueOf(i),"نحوه نصب اندروید استودیو","VIDEO","5:24",flag,
                    false,false,"https://hw7.cdn.asset.aparat.com/aparat-video/626c9e8c56dd1827ca4b047b900e942413877501-1080p__23543.mp4");
            lecture.setIndex_dataSource(i);
            lectures4.add(lecture);
        }
        Section section1=new Section(1,"1","مقدمه",courseId,lectures1);
        Section section2=new Section(2,"2","آشنایی با ابزار",courseId,lectures2);
        Section section3=new Section(2,"3","طراحی متریال دیزاین",courseId,lectures3);
        Section section4=new Section(4,"4","وب سرویس",courseId,lectures4);
        sections.add(section1);
        sections.add(section2);
        sections.add(section3);
        sections.add(section4);
        MutableLiveData<List<Section>> mutableLiveData=new MutableLiveData<>();
        mutableLiveData.setValue(sections);
        return mutableLiveData;
    }
    private static class UpdateSectionAsyncTask extends AsyncTask<Void,Void,Void> {
        private SectionDao sectionDao;
        private int sectionId;
        private int courseId;
        private ArrayList<Lecture> lectures;
        private UpdateSectionAsyncTask(SectionDao sectionDao,int sectionId,int courseId,ArrayList<Lecture> lectures){
            this.sectionDao=sectionDao;
            this.sectionId=sectionId;
            this.courseId=courseId;
            this.lectures=lectures;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            sectionDao.updateLecture(sectionId,lectures,courseId);
            return null;
        }
    }
    public void updateLocal(int sectionId, ArrayList<Lecture> lectureList,int courseId){
       // new UpdateSectionAsyncTask(sectionDao).execute(section);
        new UpdateSectionAsyncTask(sectionDao,sectionId,courseId,lectureList).execute();
        //sectionDao.updateLecture(sectionId,lectureList,courseId);
    }

    public void updateRemote(int courseId,int sectionId,ArrayList<Lecture> lectureList){
        apiInterface.updateSection(MyApplication.prefHelper.getToken(),courseId,sectionId,lectureList)
                .enqueue(new Callback<Section>() {
                    @Override
                    public void onResponse(Call<Section> call, Response<Section> response) {

                    }

                    @Override
                    public void onFailure(Call<Section> call, Throwable t) {

                    }
                });
    }


}
