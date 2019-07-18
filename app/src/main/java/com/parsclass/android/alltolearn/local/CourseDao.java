package com.parsclass.android.alltolearn.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.parsclass.android.alltolearn.model.Course;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface CourseDao {

//    @Insert
//    void insert(Course course);
//
//    @Insert
//    void insertAll(Course... courses);
//
//    @Update
//    void update(Course course);
//
//    @Delete
//    void delete(Course course);
//
//    @Query("DELETE FROM course_table")
//    void deleteAllNotes();

    @Query("SELECT * FROM course_table")
    LiveData<List<Course>> loadSliderCourses();

    @Query("SELECT * FROM course_table WHERE typeListCourse = :typeListCourse")
    LiveData<List<Course>> loadListCourse(String typeListCourse);


    @Insert(onConflict = REPLACE)
    void saveCourses(Course course);

    //typeListCourse = :typeListCourse
    @Query("SELECT * FROM course_table WHERE typeListCourse = :typeListCourse AND lastRefresh > Date(:lastRefreshMax)")
    List<Course> hasCourse(String lastRefreshMax,String typeListCourse);
}
