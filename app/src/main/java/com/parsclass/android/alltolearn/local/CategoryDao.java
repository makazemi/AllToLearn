package com.parsclass.android.alltolearn.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.parsclass.android.alltolearn.model.CategoryItem;

import java.util.List;

@Dao
public interface CategoryDao {

    @Query("SELECT * FROM category_table")
    LiveData<List<CategoryItem>> loadCategoryItem();


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveCategory(CategoryItem category);

    @Query("SELECT * FROM category_table WHERE lastRefresh > Date(:lastRefreshMax)")
    List<CategoryItem> hasCategory(String lastRefreshMax);
}
