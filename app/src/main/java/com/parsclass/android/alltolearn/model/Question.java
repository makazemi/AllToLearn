package com.parsclass.android.alltolearn.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.parsclass.android.alltolearn.Utils.DateConverter;

import java.util.Date;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "question_table",
        foreignKeys = @ForeignKey(entity = Course.class,
                parentColumns = "id",
                childColumns = "courseId",
                onDelete = CASCADE)
        ,indices = {@Index("courseId")} )
@TypeConverters({DateConverter.class})
public class Question {
    @PrimaryKey
    private int id;
    private String author;
    private String question;
    private String lecture_number;
    private int courseId;
    private int count_response;
    private Date created_at;
    private Date lastRefresh;

    public Question(int id, String author, String question, String lecture_number, int courseId, int count_response, Date created_at) {
        this.id = id;
        this.author = author;
        this.question = question;
        this.lecture_number = lecture_number;
        this.courseId = courseId;
        this.count_response = count_response;
        this.created_at = created_at;
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

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getLecture_number() {
        return lecture_number;
    }

    public void setLecture_number(String lecture_number) {
        this.lecture_number = lecture_number;
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

    public int getCount_response() {
        return count_response;
    }

    public void setCount_response(int count_response) {
        this.count_response = count_response;
    }
}
