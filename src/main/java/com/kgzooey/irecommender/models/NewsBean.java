package com.kgzooey.irecommender.models;

public class NewsBean {
    private int newsId;
    private String newsTitle;
    private String newsContent;

    public void setNewsId(int newsId) {
        this.newsId = newsId;
    }

    public int getNewsId() {
        return newsId;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsContent(String newsContent) {
        this.newsContent = newsContent;
    }

    public String getNewsContent() {
        return newsContent;
    }
}
