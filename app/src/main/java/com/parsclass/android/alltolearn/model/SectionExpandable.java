package com.struct.android.alltolearn.model;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.ArrayList;
import java.util.Date;

public class SectionExpandable extends ExpandableGroup<Lecture> {
    private int id;
    private String title_section;
    private ArrayList<Lecture> lectures;
    private int courseId;
    private Date lastRefresh;

    public SectionExpandable(int id, String title_section, int courseId , ArrayList<Lecture> lectures) {
        super(title_section,lectures);
        this.id = id;
        this.title_section = title_section;
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
}
