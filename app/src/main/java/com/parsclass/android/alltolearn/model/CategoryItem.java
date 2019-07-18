package com.parsclass.android.alltolearn.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.parsclass.android.alltolearn.Utils.DateConverter;
import com.parsclass.android.alltolearn.Utils.SubCatConverter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

@Entity(tableName = "category_table")
public class CategoryItem implements Serializable {

    @PrimaryKey
    private int id;
    private String title;
    private String imagePath;
    private int image;
    @TypeConverters({SubCatConverter.class})
    private ArrayList<SubCategory> subCategory;
    @TypeConverters({DateConverter.class})
    private Date lastRefresh;
    @Ignore
    private int imageDrawable;

    public CategoryItem(int id, String title, String imagePath, ArrayList<SubCategory> subCategory, Date lastRefresh) {
        this.id = id;
        this.title = title;
        this.imagePath = imagePath;
        this.subCategory = subCategory;
        this.lastRefresh=lastRefresh;
    }


    public CategoryItem(int id, String title, int image, ArrayList<SubCategory> subCategory, Date lastRefresh) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.subCategory = subCategory;
        this.lastRefresh=lastRefresh;
    }

    @Ignore
    public CategoryItem(int id, String title, String imagePath) {
        this.id = id;
        this.title = title;
        this.imagePath = imagePath;
    }

    @Ignore
    public CategoryItem(int id, String title, int imageDrawable) {
        this.id = id;
        this.title = title;
        this.imageDrawable = imageDrawable;
    }

    @Ignore
    public CategoryItem(String title, ArrayList<SubCategory> subCategory) {
        this.title = title;
        this.subCategory = subCategory;
    }

    public CategoryItem() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public ArrayList<SubCategory> getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(ArrayList<SubCategory> subCategory) {
        this.subCategory = subCategory;
    }

    public int getImageDrawable() {
        return imageDrawable;
    }

    public void setImageDrawable(int imageDrawable) {
        this.imageDrawable = imageDrawable;
    }

    public Date getLastRefresh() {
        return lastRefresh;
    }

    public void setLastRefresh(Date lastRefresh) {
        this.lastRefresh = lastRefresh;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "CategoryItem{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", image=" + image +
                '}';
    }
}
