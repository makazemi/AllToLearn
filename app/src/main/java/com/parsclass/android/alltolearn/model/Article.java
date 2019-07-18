package com.parsclass.android.alltolearn.model;

import java.io.Serializable;

public class Article implements Serializable {

    private String title;

    public Article(String title) {
        title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        title = title;
    }

    @Override
    public String toString() {
        return "Article{" +
                "Title='" + title + '\'' +
                '}';
    }
}
