package com.parsclass.android.alltolearn.model;

import java.io.Serializable;
import java.util.ArrayList;

public class ResponseModel implements Serializable {

    private String status;
    private int totalResults;
    private ArrayList<Article> articles;

    public ResponseModel(ArrayList<Article> articles) {
        this.articles = articles;
    }

    public ResponseModel(String status, int totalResults, ArrayList<Article> articles) {
        this.status = status;
        this.totalResults = totalResults;
        this.articles = articles;
    }

    public ArrayList<Article> getArticles() {
        return articles;
    }

    public void setArticles(ArrayList<Article> articles) {
        this.articles = articles;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }
}
