package com.parsclass.android.alltolearn.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.parsclass.android.alltolearn.model.LectureList;

import java.util.List;

@Dao
public interface LectureListDao {

    @Query("SELECT * FROM lecture_list_table")
    LiveData<List<LectureList>> loadLectureItem();


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveLecture(LectureList lecture);

    @Query("SELECT * FROM lecture_list_table WHERE lastRefresh > Date(:lastRefreshMax) AND courseId=:courseId")
    List<LectureList> hasLecture(String lastRefreshMax,int courseId);


    @Query("SELECT * FROM lecture_list_table WHERE  courseId=:courseId")
    LiveData<List<LectureList>> getLecture(int courseId);

    @Update
    void update(LectureList lectureList);

}
