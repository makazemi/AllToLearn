package com.parsclass.android.alltolearn.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.parsclass.android.alltolearn.Utils.DateConverter;

import java.util.Date;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "response_table",
        foreignKeys = @ForeignKey(entity = Question.class,
                parentColumns = "id",
                childColumns = "questionId",
                onDelete = CASCADE)
        ,indices = {@Index("questionId")})
@TypeConverters({DateConverter.class})
public class QResponse {

    @PrimaryKey
    private int id;
    private String author;
    private String response;
    private int questionId;
    private Date created_at;
    private Date lastRefresh;

    public QResponse(int id, String author, String response, Date created_at) {
        this.id = id;
        this.author = author;
        this.response = response;
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

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
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
}
