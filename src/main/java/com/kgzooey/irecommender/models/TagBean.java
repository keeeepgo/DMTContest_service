package com.kgzooey.irecommender.models;

import java.util.Date;

public class TagBean {
    int tagId;
    String tagContent;
    Date tagDate;

    public Date getTagDate() {
        return tagDate;
    }

    public void setTagDate(Date tagDate) {
        this.tagDate = tagDate;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public String getTagContent() {
        return tagContent;
    }

    public void setTagContent(String tagContent) {
        this.tagContent = tagContent;
    }
}
