package com.example.newsfeedapp;

public class NewsArticle {

    public String title;
    public String publishTime;
    public String webUrl;
    public String articleSection;
    public String[] contributor;

    public NewsArticle (String title, String publishTime, String webUrl, String articleSection, String[] contributor){
        this.title = title;
        this.publishTime = publishTime;
        this.webUrl = webUrl;
        this.articleSection = articleSection;
        this.contributor = contributor;
    }
}
