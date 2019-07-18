package com.parsclass.android.alltolearn.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.util.Log;
import android.widget.Toast;


import com.google.gson.JsonElement;
import com.parsclass.android.alltolearn.Utils.ConstantUtil;
import com.parsclass.android.alltolearn.Utils.CourseGenerator;
import com.parsclass.android.alltolearn.local.CourseDao;
import com.parsclass.android.alltolearn.local.LearnDatabase;
import com.parsclass.android.alltolearn.model.Course;
import com.parsclass.android.alltolearn.remote.APIClient;
import com.parsclass.android.alltolearn.remote.APIInterface;
import com.parsclass.android.alltolearn.remote.Status;
import com.parsclass.android.alltolearn.viewmodel.CourseViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.concurrent.Executor;

import static com.parsclass.android.alltolearn.Utils.ConstantUtil.ENROLLED;


public class CourseRepository {

    private static final String TAG="CourseRepository";


    public static final String  SLIDER="SLIDER";
    public static final String  TOP_COURSE_ONE="TO_COURSE_ONE";
    public static final String  STUDENT_ALSO_VIEWED="STUDENT_ALSO_VIEWED";
    private static int FRESH_TIMEOUT_IN_MINUTES = 1;

    private final Executor executor;

    private APIInterface apiInterface;
    private APIInterface apiInterface2;
    public MutableLiveData<ArrayList<CourseViewModel>> arrayListMutableLiveData=new MutableLiveData<>();
    private ArrayList<CourseViewModel> arrayList=new ArrayList<>();
    private ArrayList<Course> items;

    private MutableLiveData<ArrayList<Course>> sliderCourses=new MutableLiveData<>();

    private CourseDao courseDao;
    private Application application;
    private MutableLiveData<Status> statusMutableLiveData=new MutableLiveData<>();
    private MutableLiveData<Boolean> isEnrolledLiveData=new MutableLiveData<>();

    public CourseRepository(Application application,Executor executor) {
        this.apiInterface =APIClient.getClient().create(APIInterface.class);
        this.apiInterface2 =APIClient.getClient2().create(APIInterface.class);
        LearnDatabase database= LearnDatabase.getInstance(application);
        courseDao=database.courseDao();
        this.application=application;
        this.executor=executor;
    }

    public LiveData<List<Course>> getSliderCourse(){

        refreshSlider(SLIDER); // try to refresh data if possible from Github Api

        return courseDao.loadSliderCourses();
    }

    private void refreshSlider(String typeListCourse){

        executor.execute(() -> {
            String lastRefresh=getMaxRefreshTime(new Date()).toString();
            boolean sliderExists =(!(courseDao.hasCourse(lastRefresh,typeListCourse)).isEmpty());
//            Log.e(TAG,"sliderExist: "+sliderExists);
//            Log.e(TAG,"lastrefresh: "+lastRefresh);
//            Log.e(TAG,"hasSlider: "+courseDao.hasCourse(lastRefresh,typeListCourse).toString());
            // If user have to be updated
            if (!sliderExists) {
             //   Log.e(TAG,"in if");
                setStatus(Status.LOADING);
                apiInterface.getSliderCourses().enqueue(new Callback<List<Course>>() {
                    @Override
                    public void onResponse(Call<List<Course>> call, Response<List<Course>> response) {
                        setStatus(Status.SUCCESS);
                        Toast.makeText(application, "Data refreshed from network !", Toast.LENGTH_LONG).show();
                        executor.execute(() -> {
                            List<Course> courses=response.body();
                            if(response.body()!=null) {
                                for (int i = 0; i < courses.size(); i++) {
                                    courses.get(i).setLastRefresh(new Date());
                                    courseDao.saveCourses(courses.get(i));
                                }
                            }
                            //  courseDao.saveCourses(courses);
                        });
                    }

                    @Override
                    public void onFailure(Call<List<Course>> call, Throwable t) {
                        setStatus(Status.ERROR);
                        Log.e(TAG,"onFailure "+t.toString());
                    }
                });
            }

        });

    }

    public LiveData<List<Course>> getTopCourse(){

        refreshCourse(TOP_COURSE_ONE); // try to refresh data if possible from Github Api

        return courseDao.loadListCourse(TOP_COURSE_ONE);


    }

    public LiveData<List<Course>> getCourse(String typeListCourse){
        //  refreshCourse(typeListCourse);
        refreshCourseFake(typeListCourse);

        return courseDao.loadListCourse(typeListCourse);
    }

    public MutableLiveData<List<Course>> getFakCourse(String typeListCourse){
        CourseGenerator courseGenerator=new CourseGenerator(typeListCourse);
        List<Course> courses=courseGenerator.getFakeData();
        //courseList.add(new Course("عنوان دوره","http://isha.sadhguru.org/blog/wp-content/uploads/2016/05/natures-temples.jpg","18733",3,"1500"));
        MutableLiveData<List<Course>> mutableLiveData=new MutableLiveData<>();
        mutableLiveData.setValue(courses);
        return mutableLiveData;
    }

    public MutableLiveData<List<Course>> getAlsoViewFak(){
        List<Course> courseList=new ArrayList<>();
        courseList.add(new Course(1000,"عنوان دوره","http://isha.sadhguru.org/blog/wp-content/uploads/2016/05/natures-temples.jpg","25,486",4,"25000","علیرضا عزیزی"));
        courseList.add(new Course(1001,"عنوان دوره","http://isha.sadhguru.org/blog/wp-content/uploads/2016/05/natures-temples.jpg","25,486",4,"25000","علیرضا عزیزی"));
        courseList.add(new Course(1002,"عنوان دوره","http://isha.sadhguru.org/blog/wp-content/uploads/2016/05/natures-temples.jpg","25,486",4,"25000","علیرضا عزیزی"));
        courseList.add(new Course(1003,"عنوان دوره","http://isha.sadhguru.org/blog/wp-content/uploads/2016/05/natures-temples.jpg","25,486",4,"25000","علیرضا عزیزی"));
        MutableLiveData<List<Course>> mutableLiveData=new MutableLiveData<>();
        mutableLiveData.setValue(courseList);
        return mutableLiveData;
    }

    private void refreshCourseFake(String typeListCourse){
        String lastRefresh=getMaxRefreshTime(new Date()).toString();
        boolean courseExists =(!(courseDao.hasCourse(lastRefresh,typeListCourse)).isEmpty());

        if (!courseExists) {
            setStatus(Status.LOADING);
            CourseGenerator courseGenerator=new CourseGenerator(typeListCourse);
            List<Course> courses=courseGenerator.getFakeData();
            for (int i=0;i<courses.size();i++){
                courses.get(i).setLastRefresh(new Date());
                courseDao.saveCourses(courses.get(i));
            }
            setStatus(Status.SUCCESS);
        }
    }


    private void refreshCourse(String typeListCourse){

        executor.execute(() -> {
            String lastRefresh=getMaxRefreshTime(new Date()).toString();
            boolean courseExists =(!(courseDao.hasCourse(lastRefresh,typeListCourse)).isEmpty());

            // If user have to be updated
            if (!courseExists) {
               // Log.e(TAG,"in if");
                setStatus(Status.LOADING);
                apiInterface.getCourse(TOP_COURSE_ONE).enqueue(new Callback<List<Course>>() {
                    @Override
                    public void onResponse(Call<List<Course>> call, Response<List<Course>> response) {
                        setStatus(Status.SUCCESS);
                        Toast.makeText(application, "Data refreshed from network !", Toast.LENGTH_LONG).show();
                        executor.execute(() -> {
                            List<Course> courses=response.body();
                            for (int i=0;i<courses.size();i++){
                                courses.get(i).setLastRefresh(new Date());
                                courseDao.saveCourses(courses.get(i));
                            }

                        });
                    }

                    @Override
                    public void onFailure(Call<List<Course>> call, Throwable t) {

                      //  Log.e(TAG,"onFailure "+t.toString());
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

    public MutableLiveData<Status> getStatus() {
        return statusMutableLiveData;
    }

    public void setStatus(Status status){

        statusMutableLiveData.postValue(status);
    }

    public APIInterface getApiCallInterface(){
        return apiInterface2;
    }
    public Observable<JsonElement> executeCourseApi(int index) {
        return apiInterface2.fetchListNews(ConstantUtil.sources[index], String.valueOf(ConstantUtil.API_KEY));
    }

    private void geEnrolled(String id){
        setStatus(Status.LOADING);
        apiInterface.isEnrolledCourse(id).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()) {
                    if (response.body().equals(ENROLLED)) {
                        isEnrolledLiveData.setValue(true);
                    } else {
                        isEnrolledLiveData.setValue(false);
                    }

                    setStatus(Status.SUCCESS);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                isEnrolledLiveData.setValue(null);
                setStatus(Status.ERROR);
            }
        });
    }

    public MutableLiveData<Boolean> isEnrolled(String id){
        geEnrolled(id);
        return isEnrolledLiveData;
    }

}
