package com.parsclass.android.alltolearn.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.parsclass.android.alltolearn.model.Question;

import java.util.List;

@Dao
public interface QuestionDao {
    @Query("SELECT * FROM question_table WHERE courseId=:courseId")
    LiveData<List<Question>> loadQuestionItem(int courseId);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveQuestion(Question question);

    @Query("SELECT * FROM question_table WHERE lastRefresh > Date(:lastRefreshMax) " +
            "AND courseId=:courseId")
    List<Question> hasQuestion(String lastRefreshMax,int courseId);
}
