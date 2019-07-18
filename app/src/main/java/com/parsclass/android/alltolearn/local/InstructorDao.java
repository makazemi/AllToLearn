package com.parsclass.android.alltolearn.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.parsclass.android.alltolearn.model.Instructor;

import java.util.List;

@Dao
public interface InstructorDao {
    @Query("SELECT * FROM instructor_table")
    LiveData<List<Instructor>> loadInstructorItem();


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveInstructor(Instructor instructor);

    @Query("SELECT * FROM instructor_table WHERE lastRefresh > Date(:lastRefreshMax)")
    List<Instructor> hasInstructor(String lastRefreshMax);
}
