package com.example.newsfeedapp;

public class NewsArticle {

    private String title;
    private String publishTime;
    private String webUrl;

    public NewsArticle (String title, String publishTime, String webUrl){
        this.title = title;
        this.publishTime = publishTime;
        this.webUrl = webUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public String getWebUrl() {
        return webUrl;
    }
}
