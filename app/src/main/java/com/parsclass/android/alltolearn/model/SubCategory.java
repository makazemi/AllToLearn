package com.parsclass.android.alltolearn.model;

import java.io.Serializable;

public class SubCategory implements Serializable {
    private int id;
    private String title;
    private String imagePath;

    public SubCategory(String title, String imagePath) {
        this.title = title;
        this.imagePath = imagePath;
    }

    public SubCategory(int id, String title, String imagePath) {
        this.id = id;
        this.title = title;
        this.imagePath = imagePath;
    }

    public SubCategory(String title) {
        this.title = title;
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
}
