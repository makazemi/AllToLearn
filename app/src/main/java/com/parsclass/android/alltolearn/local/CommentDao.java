package com.parsclass.android.alltolearn.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.parsclass.android.alltolearn.model.Comment;

import java.util.List;

@Dao
public interface CommentDao {
    @Query("SELECT * FROM comment_table WHERE courseId=:courseId")
    LiveData<List<Comment>> loadCommentItem(int courseId);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveComment(Comment comment);

    @Query("SELECT * FROM comment_table WHERE lastRefresh > Date(:lastRefreshMax) " +
            "AND courseId=:courseId")
    List<Comment> hasComment(String lastRefreshMax,int courseId);
}
