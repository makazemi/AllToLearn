package com.parsclass.android.alltolearn.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.parsclass.android.alltolearn.model.QResponse;

import java.util.List;

@Dao
public interface ResponseDao {
    @Query("SELECT * FROM response_table WHERE questionId=:questionId")
    LiveData<List<QResponse>> loadResponseItem(int questionId);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveResponse(QResponse QResponse);

    @Query("SELECT * FROM response_table WHERE lastRefresh > Date(:lastRefreshMax) " +
            "AND questionId=:questionId")
    List<QResponse> hasResponse(String lastRefreshMax, int questionId);
}
