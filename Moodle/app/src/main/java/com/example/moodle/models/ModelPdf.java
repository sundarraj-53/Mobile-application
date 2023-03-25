package com.example.moodle.models;

public class ModelPdf
{
    String id,title,Description,url;
    long timestamp;

    public ModelPdf() {
    }

    public ModelPdf(String id, String title, String description, String url, long timestamp) {
        this.id = id;
        this.title = title;
        Description = description;
        this.url = url;
        this.timestamp = timestamp;
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
}
