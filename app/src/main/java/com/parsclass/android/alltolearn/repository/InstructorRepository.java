package com.parsclass.android.alltolearn.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.util.Log;

import com.parsclass.android.alltolearn.R;
import com.parsclass.android.alltolearn.local.CourseInstructorJoinDao;
import com.parsclass.android.alltolearn.local.LearnDatabase;
import com.parsclass.android.alltolearn.model.CourseInstructorJoin;
import com.parsclass.android.alltolearn.model.Instructor;
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

import static com.parsclass.android.alltolearn.Utils.ConstantUtil.TYPE_BRIEF_ITEM_INSTRUCTOR;
import static com.parsclass.android.alltolearn.Utils.ConstantUtil.TYPE_DETAIL_ITEM_INSTRUCTOR;

public class InstructorRepository {
    private static final String TAG="CommentRepository";
    private static int FRESH_TIMEOUT_IN_MINUTES = 1;

    private final Executor executor;

    private APIInterface apiInterface;

    private CourseInstructorJoinDao joinDao;
    private Application application;

    private MutableLiveData<Status> statusMutableLiveData=new MutableLiveData<>();
    public InstructorRepository(Application application, Executor executor) {
        this.executor = executor;
        this.application = application;
        apiInterface= APIClient.getClient().create(APIInterface.class);
        LearnDatabase database= LearnDatabase.getInstance(application);
        joinDao=database.courseInstructorJoinDao();
    }
    public LiveData<List<Instructor>> getInstructors(int courseId){

        refreshInstructor(courseId);
        return joinDao.getInstructorForCourse(courseId);
    }

    private void refreshCommentFake(int courseId){
        String lastRefresh=getMaxRefreshTime(new Date()).toString();
        boolean sliderExists =(!(joinDao.hasInstructor(lastRefresh,courseId)).isEmpty());

        if (!sliderExists) {
            setStatus(Status.LOADING);
            List<Instructor> instructors=new ArrayList<>();
            Date date=new Date();
            instructors.add(new Instructor(1,"علیرضا عزیزی"," متون را ندارند و در همان حال کار آنها به نوعی وابسته به متن مباشد آنها با استفاده از محتویات ساختگی، صفحه گرافیکی خود را صفحهآرایی  تا مرحله طراحی و صندی را به پایان برند","برنامه نویس اندروید","","2367","5",5));
            instructors.add(new Instructor(2,"علیرضا عزیزی"," متون را ندارند و در همان حال کار آنها به نوعی وابسته به متن مباشد آنها با استفاده از محتویات ساختگی، صفحه گرافیکی خود را صفحهآرایی  تا مرحله طراحی و صندی را به پایان برند","برنامه نویس اندروید","","2367","5",5));
            instructors.add(new Instructor(3,"علیرضا عزیزی"," متون را ندارند و در همان حال کار آنها به نوعی وابسته به متن مباشد آنها با استفاده از محتویات ساختگی، صفحه گرافیکی خود را صفحهآرایی  تا مرحله طراحی و صندی را به پایان برند","برنامه نویس اندروید","","2367","5",5));
            instructors.add(new Instructor(4,"علیرضا عزیزی"," متون را ندارند و در همان حال کار آنها به نوعی وابسته به متن مباشد آنها با استفاده از محتویات ساختگی، صفحه گرافیکی خود را صفحهآرایی  تا مرحله طراحی و صندی را به پایان برند","برنامه نویس اندروید","","2367","5",5));

            for (int i=0;i<instructors.size();i++){
                instructors.get(i).setLastRefresh(new Date());
                joinDao.saveInstructor(instructors.get(i));
                joinDao.insertJoin(new CourseInstructorJoin(courseId,instructors.get(i).getId()));
            }
            setStatus(Status.SUCCESS);
        }

    }

    private void refreshInstructor(int courseId){

        executor.execute(() -> {
            String lastRefresh=getMaxRefreshTime(new Date()).toString();
            boolean isExists =(!(joinDao.hasInstructor(lastRefresh,courseId)).isEmpty());

            // If user have to be updated
            if (!isExists) {
                // statusMutableLiveData.postValue(Status.LOADING);
                setStatus(Status.LOADING);
                Log.e(TAG,"in if");
                apiInterface.getInstructor(courseId).enqueue(new Callback<List<Instructor>>() {
                    @Override
                    public void onResponse(Call<List<Instructor>> call, Response<List<Instructor>> response) {

                        executor.execute(() -> {
                            List<Instructor> instructors=response.body();
                            for (int i=0;i<instructors.size();i++){
                                instructors.get(i).setLastRefresh(new Date());
                                joinDao.saveInstructor(instructors.get(i));
                                joinDao.insertJoin(new CourseInstructorJoin(courseId,instructors.get(i).getId()));
                            }

                        });
                        setStatus(Status.SUCCESS);
                        //statusMutableLiveData.postValue(Status.SUCCESS);
                    }

                    @Override
                    public void onFailure(Call<List<Instructor>> call, Throwable t) {

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

    public MutableLiveData<List<Instructor>> getFakInstructor(int courseId){
        List<Instructor> instructors=new ArrayList<>();
        String img="2wCEAAkGBxIQEA8QEBIPEBAVDw8QEA8QDxAPDw8VFREWFhUVFRUYHSggGBolGxUVITEhJSkrLi4uFx8zODMtNygtLisBCgoKDg0OGBAQGi0lHR4tLS0tLS0tLS0rLS0tLS0tLS0tLS0tLS0tLS0rLS0tLS0tLS0tLS0tLSstLS0tLS0tLf";
        String about=application.getString(R.string.txt_about_instructor);
        Instructor instructor1=new Instructor(1,"علیرضا عزیزی",about,"برنامه نویس اندروید",img,"423","10",4);
        Instructor instructor2=new Instructor(2,"علیرضا عزیزی",about,"برنامه نویس اندروید",img,"423","10",4);
        Instructor instructor3=new Instructor(3,"علیرضا عزیزی",about,"برنامه نویس اندروید",img,"423","10",4);
        instructor1.setType_instructor(TYPE_DETAIL_ITEM_INSTRUCTOR);
        instructor2.setType_instructor(TYPE_DETAIL_ITEM_INSTRUCTOR);
        instructor3.setType_instructor(TYPE_DETAIL_ITEM_INSTRUCTOR);

        instructors.add(instructor1);
        instructors.add(instructor2);
        instructors.add(instructor3);
        MutableLiveData<List<Instructor>> mutableLiveData=new MutableLiveData<>();
        mutableLiveData.setValue(instructors);
        return mutableLiveData;
    }

    public MutableLiveData<List<Instructor>> getFakInstructorBrief(int courseId){
        List<Instructor> instructors=new ArrayList<>();
        String img="https://www.google.com/imgres?imgurl=https%3A%2F%2Fwww.profiletalent.com.au%2Fwp-content%2Fuploads%2F2017%2F05%2Fprofile-talent-ant-simpson-feature.jpg&imgrefurl=https%3A%2F%2Fwww.profiletalent.com.au%2Ftalent%2F&docid=5c-yC3q9IukOkM&tbnid=oY2fFzWvtyG9FM%3A&vet=10ahUKEwiWrsX_pPLhAhXC8OAKHR8ECNgQMwhtKAkwCQ..i&w=420&h=380&bih=576&biw=1366&q=profile%20image&ved=0ahUKEwiWrsX_pPLhAhXC8OAKHR8ECNgQMwhtKAkwCQ&iact=mrc&uact=8";
        String about=application.getString(R.string.txt_about_instructor);
        Instructor instructor1=new Instructor(1,"علیرضا عزیزی",about,"برنامه نویس اندروید",img,"423","10",4);
        Instructor instructor2=new Instructor(2,"علیرضا عزیزی",about,"برنامه نویس اندروید",img,"423","10",4);
        Instructor instructor3=new Instructor(3,"علیرضا عزیزی",about,"برنامه نویس اندروید",img,"423","10",4);
        instructor1.setType_instructor(TYPE_BRIEF_ITEM_INSTRUCTOR);
        instructor2.setType_instructor(TYPE_BRIEF_ITEM_INSTRUCTOR);
        instructor3.setType_instructor(TYPE_BRIEF_ITEM_INSTRUCTOR);

        instructors.add(instructor1);
        instructors.add(instructor2);
        instructors.add(instructor3);

        MutableLiveData<List<Instructor>> mutableLiveData=new MutableLiveData<>();
        mutableLiveData.setValue(instructors);
        return mutableLiveData;
    }
}
