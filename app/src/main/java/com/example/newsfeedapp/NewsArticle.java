package com.example.newsfeedapp;

public class NewsArticle {

    private String title;
    private String publishTime;
    private String webUrl;
    private String articleSection;

    public NewsArticle (String title, String publishTime, String webUrl, String articleSection){
        this.title = title;
        this.publishTime = publishTime;
        this.webUrl = webUrl;
        this.articleSection = articleSection;
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

    public String getArticleSection() {
        return articleSection;
    }
}
