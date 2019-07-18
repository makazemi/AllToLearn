package com.parsclass.android.alltolearn.remote;

import com.google.gson.JsonElement;
import com.parsclass.android.alltolearn.model.Article;
import com.parsclass.android.alltolearn.model.CategoryItem;
import com.parsclass.android.alltolearn.model.Comment;
import com.parsclass.android.alltolearn.model.Course;
import com.parsclass.android.alltolearn.model.Instructor;
import com.parsclass.android.alltolearn.model.Lecture;
import com.parsclass.android.alltolearn.model.Question;
import com.parsclass.android.alltolearn.model.QResponse;
import com.parsclass.android.alltolearn.model.ResponseModel;
import com.parsclass.android.alltolearn.model.Section;
import com.parsclass.android.alltolearn.model.User;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import retrofit2.http.Streaming;

public interface APIInterface {

    @GET("Default.aspx?metod=kazemi")
    Call<List<Course>> getSliderCourses();

    @GET("llkjd")
    Call<List<Course>> getCourse(@Field("typeListCourse") String type);


    @GET("Default.aspx?metod=kazemi")
    Call<List<CategoryItem>> getCategory();

    @GET("Default.aspx?metod=kazemi")
    Call<List<Course>> getTopCourse(@Field("typeListCourse") String typeListCourse);

    @GET("kdj")
    Call<List<Comment>> getComment(@Field("courseId") int courseId);

    @GET("ld")
    Call<List<Section>> getSection(@Field("courseId") int courseId);

    @GET("kld")
    Call<List<Instructor>> getInstructor(@Field("courseId") int courseId);

    @GET("jj")
    Call<List<Question>> getQuestion(@Field("courseId") int courseId);

    @GET("k")
    Call<List<QResponse>> getQResponse(@Field("questionId") int questionId);

    @POST("dds")
    Call<User> loginUser(@Field("email") String email, @Field("password") String password);

    @POST("ljdj")
    Call<String> getToken(@Field("name") String name,@Field("email") String email, @Field("password") String password);

    @PUT("fffn")
    Call<Section> updateSection(@Header("Authorization") String token,
                                @Field("courseId") int courseId,
                                @Field("sectionId") int sectionId,
                                @Field("lectureList")ArrayList<Lecture> lectureList);

    @GET("download/arshad97q/333-E.pdf")
    @Streaming
    Call<ResponseBody> downloadFile();

    @GET("everything")
    Observable<ArrayList<Course>> fetchListCourse(
            @Query("q") String source,
            @Query("apiKey") String apiKey);

    @GET("everything")
    Observable<JsonElement> fetchListNews(
            @Query("q") String source,
            @Query("apiKey") String apiKey);

    @GET("everything")
    Call<ArrayList<Article>> fetchListNewsMy(
            @Query("q") String source,
            @Query("apiKey") String apiKey);



    @GET("everything")
    Call<ResponseModel> fetchListNewsSearch(
            @Query("q") String source,
            @Query("apiKey") String apiKey);


    @GET("everything")
    Call<ResponseModel> fetchFilterListNews(
            @Query("q") String source,
            @Query("apiKey") String apiKey,
            @Query("sortBy") String sortBy,
            @Query("category") String category,
            @Query("country") String country);


    @GET("ks")
    Call<String> isEnrolledCourse(@Field("courseId") String server_id);

}
