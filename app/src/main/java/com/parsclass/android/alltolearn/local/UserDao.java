package com.parsclass.android.alltolearn.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.parsclass.android.alltolearn.model.User;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface UserDao {
    @Insert(onConflict = REPLACE)
    void save(User user);

    @Delete
    void deleteUser(User user);

    @Query("DELETE FROM user_table")
    void deleteAllUsers();


    @Query("SELECT * FROM user_table WHERE email = :email")
    LiveData<User> load(String email);

    @Query("SELECT * FROM user_table WHERE email = :email AND lastRefresh > Date(:lastRefreshMax)")
    User hasUser(String email, String lastRefreshMax);
}
