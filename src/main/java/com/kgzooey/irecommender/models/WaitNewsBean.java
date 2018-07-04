package com.kgzooey.irecommender.models;

public class WaitNewsBean {
    private int newsId;
    private String newsTitle;
    private int userId;
    private int readStatus;

    public int getNewsId() {
        return newsId;
    }

    public void setNewsId(int newsId) {
        this.newsId = newsId;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public void setReadStatus(int readStatus) {
        this.readStatus = readStatus;
    }

    public int getReadStatus() {
        return readStatus;
    }
}


