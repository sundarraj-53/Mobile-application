package com.example.moodle.models;

public class ModelClass {
    private String Courses, url;
    private long viewsCount,downloadsCount;
    private String timestamp;

    public ModelClass() {
    }

    public ModelClass(String courses, String url, long viewsCount, long downloadsCount, String timestamp) {
        Courses = courses;
        this.url = url;
        this.viewsCount = viewsCount;
        this.downloadsCount = downloadsCount;
        this.timestamp = timestamp;
    }

    public String getCourses() {
        return Courses;
    }

    public void setCourses(String courses) {
        Courses = courses;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getViewsCount() {
        return viewsCount;
    }

    public void setViewsCount(long viewsCount) {
        this.viewsCount = viewsCount;
    }

    public long getDownloadsCount() {
        return downloadsCount;
    }

    public void setDownloadsCount(long downloadsCount) {
        this.downloadsCount = downloadsCount;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
