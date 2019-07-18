package com.parsclass.android.alltolearn.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.parsclass.android.alltolearn.Utils.DateConverter;

import java.io.Serializable;
import java.util.Date;

import static com.parsclass.android.alltolearn.Utils.ConstantUtil.TYPE_DETAIL_ITEM_INSTRUCTOR;

@Entity(tableName = "instructor_table")
@TypeConverters({DateConverter.class})
public class Instructor implements Serializable {

    @PrimaryKey
    private int id;
    private String name;
    private String about;
    private String occupation;
    private String imagePath;
    private String numberStudents;
    private String numberCourses;
    private float rateAvg;
    private Date lastRefresh;
    private int type_instructor=TYPE_DETAIL_ITEM_INSTRUCTOR;

    public Instructor(int id, String name, String about, String occupation, String imagePath, String numberStudents, String numberCourses, float rateAvg) {
        this.id = id;
        this.name = name;
        this.about = about;
        this.occupation = occupation;
        this.imagePath = imagePath;
        this.numberStudents = numberStudents;
        this.numberCourses = numberCourses;
        this.rateAvg = rateAvg;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getNumberStudents() {
        return numberStudents;
    }

    public void setNumberStudents(String numberStudents) {
        this.numberStudents = numberStudents;
    }

    public String getNumberCourses() {
        return numberCourses;
    }

    public void setNumberCourses(String numberCourses) {
        this.numberCourses = numberCourses;
    }

    public float getRateAvg() {
        return rateAvg;
    }

    public void setRateAvg(float rateAvg) {
        this.rateAvg = rateAvg;
    }

    public Date getLastRefresh() {
        return lastRefresh;
    }

    public void setLastRefresh(Date lastRefresh) {
        this.lastRefresh = lastRefresh;
    }

    public int getType_instructor() {
        return type_instructor;
    }

    public void setType_instructor(int type_instructor) {
        this.type_instructor = type_instructor;
    }
}
