package com.kgzooey.irecommender.models;

public class VitalityBean {
    private String date;
    private int newsAmount;

    public int getNewsAmount() {
        return newsAmount;
    }

    public void setNewsAmount(int newsAmount) {
        this.newsAmount = newsAmount;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }


}
