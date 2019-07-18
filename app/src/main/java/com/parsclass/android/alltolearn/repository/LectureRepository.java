package com.parsclass.android.alltolearn.repository;

import android.app.Application;
import androidx.lifecycle.MutableLiveData;
import android.os.AsyncTask;

import com.parsclass.android.alltolearn.local.LearnDatabase;
import com.parsclass.android.alltolearn.local.LectureListDao;
import com.parsclass.android.alltolearn.model.LectureList;
import com.parsclass.android.alltolearn.remote.APIClient;
import com.parsclass.android.alltolearn.remote.APIInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import static com.parsclass.android.alltolearn.Utils.ConstantUtil.TYPE_ARTICLE;
import static com.parsclass.android.alltolearn.Utils.ConstantUtil.TYPE_AUDIO;
import static com.parsclass.android.alltolearn.Utils.ConstantUtil.TYPE_LECTURE;
import static com.parsclass.android.alltolearn.Utils.ConstantUtil.TYPE_SECTION;
import static com.parsclass.android.alltolearn.Utils.ConstantUtil.TYPE_VIDEO;

public class LectureRepository {

    private final Executor executor;

    private APIInterface apiInterface;

    private LectureListDao lectureDao;
    private Application application;

    MutableLiveData<List<LectureList>> liveData=new MutableLiveData<>();
    //LiveData<List<LectureList>> liveData=new MutableLiveData<>();

    public LectureRepository(Application application, Executor executor){
        this.executor = executor;
        this.application = application;
        apiInterface= APIClient.getClient().create(APIInterface.class);
        LearnDatabase database= LearnDatabase.getInstance(application);
        lectureDao=database.lectureDao();


    }

    private void setData(int courseId){
        ArrayList<LectureList> lectures=new ArrayList<>();
        int index=0;
        for (int i=0;i<8;i++){
            if(i==0){
                LectureList lectureList=new LectureList(i+1,"1","مقدمه و معرفی",
                        TYPE_SECTION,courseId);
                lectureList.setNumberSection_lectureList(4);
                lectures.add(lectureList);
            }
            else if(i==2){
                LectureList lectureList=new LectureList(i+1,"2","عنوان بخش دوم",TYPE_SECTION,courseId);
                lectureList.setNumberSection_lectureList(4);
                lectures.add(lectureList);
            }
//            else if(i==10){
//                LectureList lectureList=new LectureList(i,"3","عنوان بخش سوم",TYPE_SECTION,courseId);
//                lectureList.setNumberSection(4);
//                lectures.add(lectureList);
//            }
//            else if(i==16){
//                LectureList lectureList=new LectureList(i,"4","عنوان بخش چهارم",TYPE_SECTION,courseId);
//                lectureList.setNumberSection(4);
//                lectures.add(lectureList);
//            }
            else if(i==1) {
                LectureList lecture = new LectureList(i+1,String.valueOf(i + 1), "مدیا اول",
                        TYPE_VIDEO, "5:24",
                        "https://tci1.asset.aparat.com/aparat-video/5c7a530d2ed509f21e8f3f98771312e914639427-144p__20342.mp4",
                        TYPE_LECTURE, courseId);
                lecture.setIndex_dataSource_lectureList(index);
                lecture.setNumberSection_lectureList(4);
                lectures.add(lecture);
                index++;
            }
            else if(i==3){
                LectureList lecture = new LectureList(i+1,String.valueOf(i + 1), "مدیا دوم",
                        TYPE_VIDEO, "5:24",
                        "https://hw16.cdn.asset.aparat.com/aparat-video/b335d4d30ee4a0316fbf3df94ccdf59114638163-144p__87103.mp4",
                        TYPE_LECTURE, courseId);
                lecture.setIndex_dataSource_lectureList(index);
                lecture.setNumberSection_lectureList(4);
                lectures.add(lecture);
                index++;
            }
            else if(i==4){
                LectureList lecture = new LectureList(i+1,String.valueOf(i + 1), "مدیا سوم",
                        TYPE_ARTICLE, "5:24",
                        "http://dl.konkur.in/Arshad/97/pasokh/248-F-key-Arshad97-%5bwww.konkur.in%5d.pdf",
                        TYPE_LECTURE, courseId);
                lecture.setIndex_dataSource_lectureList(index);
                lecture.setNumberSection_lectureList(4);
                lectures.add(lecture);
                index++;
            }
            else if(i==5) {
                LectureList lecture = new LectureList(i+1,String.valueOf(i + 1), "مدیا چهارم",
                        TYPE_AUDIO, "5:24",
                        "http://www.seminarema.com/dl/free-audio/500/1-moghimi/mogimi_Part_1.mp3",
                        TYPE_LECTURE, courseId);
                lecture.setIndex_dataSource_lectureList(index);
                lecture.setNumberSection_lectureList(4);
                lectures.add(lecture);
                index++;

            }
            else if(i==6) {
                LectureList lecture = new LectureList(i+1,String.valueOf(i + 1), "مدیا پنجم",
                        TYPE_ARTICLE, "5:24",
                        "http://dl.konkur.in/Arshad/97/pasokh/248-F-key-Arshad97-%5bwww.konkur.in%5d.pdf",
                        TYPE_LECTURE, courseId);
                lecture.setIndex_dataSource_lectureList(index);
                lecture.setNumberSection_lectureList(4);
                lectures.add(lecture);
                index++;

            }
            else {
                LectureList lecture = new LectureList(i+1,String.valueOf(i + 1), "مدیا ششم",
                        TYPE_VIDEO, "5:24",
                        "https://hw4.cdn.asset.aparat.com/aparat-video/e3127fe6b118a47cfdb4375c5a14538414560798-240p__61652.mp4",
                        TYPE_LECTURE, courseId);
                lecture.setIndex_dataSource_lectureList(index);
                lecture.setNumberSection_lectureList(4);
                lectures.add(lecture);
                index++;
            }
        }
        //MutableLiveData<List<LectureList>> liveData=new MutableLiveData<>();

//        for (LectureList lectureList:lectures){
//            insert(lectureList);
//        }
       // liveData.setValue(lectures);
    }

    public void saveLecture(int courseId){
        setData(courseId);
    }

//    public LiveData<List<LectureList>> getFakLecture(int courseId){
//        //setData(courseId);
//        liveData=lectureDao.getLecture(courseId);
//        return liveData;
//    }

    public MutableLiveData<List<LectureList>> getFakLecture(int courseId){

        ArrayList<LectureList> lectures=new ArrayList<>();
        int index=0;
        for (int i=0;i<8;i++){
            if(i==0){
                LectureList lectureList=new LectureList(i+1,"1","مقدمه و معرفی",
                        TYPE_SECTION,courseId);
                lectureList.setNumberSection_lectureList(4);
                lectures.add(lectureList);
            }
            else if(i==2){
                LectureList lectureList=new LectureList(i+1,"2","عنوان بخش دوم",TYPE_SECTION,courseId);
                lectureList.setNumberSection_lectureList(4);
                lectures.add(lectureList);
            }
//            else if(i==10){
//                LectureList lectureList=new LectureList(i,"3","عنوان بخش سوم",TYPE_SECTION,courseId);
//                lectureList.setNumberSection(4);
//                lectures.add(lectureList);
//            }
//            else if(i==16){
//                LectureList lectureList=new LectureList(i,"4","عنوان بخش چهارم",TYPE_SECTION,courseId);
//                lectureList.setNumberSection(4);
//                lectures.add(lectureList);
//            }
            else if(i==1) {
                LectureList lecture = new LectureList(i+1,String.valueOf(i + 1), "مدیا اول",
                        TYPE_VIDEO, "5:24",
                        "https://tci1.asset.aparat.com/aparat-video/5c7a530d2ed509f21e8f3f98771312e914639427-144p__20342.mp4",
                        TYPE_LECTURE, courseId);
                lecture.setIndex_dataSource_lectureList(index);
                lecture.setNumberSection_lectureList(4);
                lectures.add(lecture);
                index++;
            }
            else if(i==3){
                LectureList lecture = new LectureList(i+1,String.valueOf(i + 1), "مدیا دوم",
                        TYPE_VIDEO, "5:24",
                        "https://hw16.cdn.asset.aparat.com/aparat-video/b335d4d30ee4a0316fbf3df94ccdf59114638163-144p__87103.mp4",
                        TYPE_LECTURE, courseId);
                lecture.setIndex_dataSource_lectureList(index);
                lecture.setNumberSection_lectureList(4);
                lectures.add(lecture);
                index++;
            }
            else if(i==4){
                LectureList lecture = new LectureList(i+1,String.valueOf(i + 1), "مدیا سوم",
                        TYPE_ARTICLE, "5:24",
                        "http://dl.konkur.in/Arshad/97/pasokh/248-F-key-Arshad97-%5bwww.konkur.in%5d.pdf",
                        TYPE_LECTURE, courseId);
                lecture.setIndex_dataSource_lectureList(index);
                lecture.setNumberSection_lectureList(4);
                lectures.add(lecture);
                index++;
            }
            else if(i==5) {
                LectureList lecture = new LectureList(i+1,String.valueOf(i + 1), "مدیا چهارم",
                        TYPE_AUDIO, "5:24",
                        "http://www.seminarema.com/dl/free-audio/500/1-moghimi/mogimi_Part_1.mp3",
                        TYPE_LECTURE, courseId);
                lecture.setIndex_dataSource_lectureList(index);
                lecture.setNumberSection_lectureList(4);
                lectures.add(lecture);
                index++;

            }
            else if(i==6) {
                LectureList lecture = new LectureList(i+1,String.valueOf(i + 1), "مدیا پنجم",
                        TYPE_ARTICLE, "5:24",
                        "http://dl.konkur.in/Arshad/97/pasokh/248-F-key-Arshad97-%5bwww.konkur.in%5d.pdf",
                        TYPE_LECTURE, courseId);
                lecture.setIndex_dataSource_lectureList(index);
                lecture.setNumberSection_lectureList(4);
                lectures.add(lecture);
                index++;

            }
            else {
                LectureList lecture = new LectureList(i+1,String.valueOf(i + 1), "مدیا ششم",
                        TYPE_VIDEO, "5:24",
                        "https://tci1.asset.aparat.com/aparat-video/3af6190b446cc4ea6415b00b8a845d5b14807428-240p__47480.mp4",
                        TYPE_LECTURE, courseId);
                lecture.setIndex_dataSource_lectureList(index);
                lecture.setNumberSection_lectureList(4);
                lectures.add(lecture);
                index++;
            }
        }
        //MutableLiveData<List<LectureList>> liveData=new MutableLiveData<>();

         liveData.setValue(lectures);
        return liveData;
    }

//    public void insert(LectureList lectureList){
//        new InsertNoteAsyncTask(lectureDao).execute(lectureList);
//    }

    public void update(LectureList lectureList){
        new UpdateNoteAsyncTask(lectureDao).execute(lectureList);
    }

    private static class UpdateNoteAsyncTask extends AsyncTask<LectureList,Void,Void> {
        private LectureListDao lectureListDao;

        private UpdateNoteAsyncTask(LectureListDao lectureListDao){
            this.lectureListDao=lectureListDao;
        }
        @Override
        protected Void doInBackground(LectureList... lectureLists) {
            lectureListDao.update(lectureLists[0]);
            return null;
        }
    }

//    private static class InsertNoteAsyncTask extends AsyncTask<LectureList,Void,Void> {
//        private LectureListDao lectureListDao;
//
//        private InsertNoteAsyncTask(LectureListDao lectureListDao){
//            this.lectureListDao=lectureListDao;
//        }
//        @Override
//        protected Void doInBackground(LectureList... lectureLists) {
//            lectureListDao.saveLecture(lectureLists[0]);
//            return null;
//        }
//    }

}
