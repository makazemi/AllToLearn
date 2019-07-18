package com.parsclass.android.alltolearn.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.parsclass.android.alltolearn.Utils.DateConverter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "comment_table",
        foreignKeys = @ForeignKey(entity = Course.class,
                parentColumns = "id",
                childColumns = "courseId",
                onDelete = CASCADE)
        ,indices = {@Index("courseId")})
@TypeConverters({DateConverter.class})
public class Comment {
    @PrimaryKey
    private int id;
    private String author;
    private String content;
    private float rate;
    private int courseId;
    private Date created_at;
    private Date lastRefresh;

    public Comment(int id, String author, String content, float rate, Date created_at,int courseId) {
        this.id = id;
        this.author = author;
        this.content = content;
        this.rate = rate;
        this.created_at = created_at;
        this.courseId=courseId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getLastRefresh() {
        return lastRefresh;
    }

    public void setLastRefresh(Date lastRefresh) {
        this.lastRefresh = lastRefresh;
    }

    public String getStringDate(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String strDate = dateFormat.format(created_at);
        return strDate;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", author='" + author + '\'' +
                ", content='" + content + '\'' +
                ", rate=" + rate +
                ", created_at=" + created_at +
                ", lastRefresh=" + lastRefresh +
                '}';
    }
}
