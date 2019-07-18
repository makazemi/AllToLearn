package com.parsclass.android.alltolearn.model;


import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.parsclass.android.alltolearn.Utils.DateConverter;
import com.parsclass.android.alltolearn.Utils.LectureConverter;

import java.util.ArrayList;
import java.util.Date;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "section_table",
        foreignKeys = @ForeignKey(entity = Course.class,
                parentColumns = "id",
                childColumns = "courseId",
                onDelete = CASCADE)
        ,indices = {@Index("courseId")})
@TypeConverters({DateConverter.class})
public class Section {
    @PrimaryKey
    private int id;
    private String title_section;
    private String number_section;
    @TypeConverters({LectureConverter.class})
    private ArrayList<Lecture> lectures;
    private int courseId;
    private Date lastRefresh;

    public Section(int id,String number_section ,String title_section, int courseId , ArrayList<Lecture> lectures) {
        this.id = id;
        this.title_section = title_section;
        this.number_section=number_section;
        this.lectures = lectures;
        this.courseId=courseId;

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle_section() {
        return title_section;
    }

    public void setTitle_section(String title_section) {
        this.title_section = title_section;
    }

    public ArrayList<Lecture> getLectures() {
        return lectures;
    }

    public void setLectures(ArrayList<Lecture> lectures) {
        this.lectures = lectures;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public Date getLastRefresh() {
        return lastRefresh;
    }

    public void setLastRefresh(Date lastRefresh) {
        this.lastRefresh = lastRefresh;
    }

    public String getNumber_section() {
        return number_section;
    }

    public void setNumber_section(String number_section) {
        this.number_section = number_section;
    }

    @Override
    public String toString() {
        return "Section{" +
                "id=" + id +
                ", title_section='" + title_section + '\'' +
                ", number_section='" + number_section + '\'' +
                ", lectures=" + lectures +
                ", courseId=" + courseId +
                ", lastRefresh=" + lastRefresh +
                '}';
    }
}
