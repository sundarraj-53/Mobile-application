package com.example.moodle.models;

public class ModelPdf
{
    String id,title,Description,url;
    long timestamp,viewsCount,downloadsCount;

    public ModelPdf() {
    }

    public ModelPdf(String id, String title, String description, String url, long timestamp, long viewsCount, long downloadsCount) {
        this.id = id;
        this.title = title;
        Description = description;
        this.url = url;
        this.timestamp = timestamp;
        this.viewsCount = viewsCount;
        this.downloadsCount = downloadsCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
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
}
