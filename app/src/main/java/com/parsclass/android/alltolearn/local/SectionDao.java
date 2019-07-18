package com.parsclass.android.alltolearn.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.TypeConverters;
import androidx.room.Update;

import com.parsclass.android.alltolearn.Utils.LectureConverter;
import com.parsclass.android.alltolearn.model.Lecture;
import com.parsclass.android.alltolearn.model.Section;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface SectionDao {
    @Query("SELECT * FROM section_table")
    LiveData<List<Section>> loadSectionItem();


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveSection(Section section);

    @Query("SELECT * FROM section_table WHERE lastRefresh > Date(:lastRefreshMax)")
    List<Section> hasSection(String lastRefreshMax);

    @Update
    void update(Section section);

    @TypeConverters({LectureConverter.class})
    @Query("UPDATE section_table SET lectures = :lectures WHERE id = :sectionId AND courseId = :courseId")
    void updateLecture(int sectionId, ArrayList<Lecture> lectures,int courseId);

}
