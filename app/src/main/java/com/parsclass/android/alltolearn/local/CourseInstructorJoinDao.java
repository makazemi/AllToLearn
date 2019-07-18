package com.parsclass.android.alltolearn.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.parsclass.android.alltolearn.model.Course;
import com.parsclass.android.alltolearn.model.CourseInstructorJoin;
import com.parsclass.android.alltolearn.model.Instructor;

import java.util.List;

@Dao
public interface CourseInstructorJoinDao {
    @Insert
    void insertJoin(CourseInstructorJoin courseInstructorJoin);

    @Insert
    void saveInstructor(Instructor instructor);

    @Query("SELECT * "+
            "FROM course_table INNER JOIN course_instructor_join_table ON "+
            "course_table.id=course_instructor_join_table.courseId WHERE "+
            "course_instructor_join_table.instructorId=:instructorId")
    LiveData<List<Course>> getCoursesForInstructor(final int instructorId);

    @Query("SELECT * FROM instructor_table INNER JOIN course_instructor_join_table ON "+
            "instructor_table.id=course_instructor_join_table.instructorId WHERE "+
            "course_instructor_join_table.courseId=:courseId")
    LiveData<List<Instructor>> getInstructorForCourse(final int courseId);


    @Query("SELECT * FROM instructor_table INNER JOIN course_instructor_join_table ON "+
            "instructor_table.id=course_instructor_join_table.instructorId WHERE "+
            "course_instructor_join_table.courseId=:courseId AND " +
            "lastRefresh > Date(:lastRefreshMax)")
    List<Instructor> hasInstructor(String lastRefreshMax,int courseId);
}
