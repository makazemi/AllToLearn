package com.parsclass.android.alltolearn.model;

public class MainVPItem {
    private String image;
    private String title;
    private String count;
    private float rate;

    public MainVPItem(String image, String title,String count,float rate) {
        this.image = image;
        this.title = title;
        this.count = count;
        this.rate = rate;
    }

    public MainVPItem() {
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }
}
