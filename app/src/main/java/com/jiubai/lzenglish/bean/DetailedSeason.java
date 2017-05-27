package com.jiubai.lzenglish.bean;

import java.util.List;

/**
 * Created by Larry Liang on 22/05/2017.
 */

public class DetailedSeason {
    private String title;
    private String seoKeywords;
    private String seoDesc;
    private boolean allowShadowing;
    private List<Video> videoList;

    public DetailedSeason() {
    }

    public DetailedSeason(String title, String seoKeywords, String seoDesc, boolean allowShadowing, List<Video> videoList) {
        this.title = title;
        this.seoKeywords = seoKeywords;
        this.seoDesc = seoDesc;
        this.allowShadowing = allowShadowing;
        this.videoList = videoList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSeoKeywords() {
        return seoKeywords;
    }

    public void setSeoKeywords(String seoKeywords) {
        this.seoKeywords = seoKeywords;
    }

    public String getSeoDesc() {
        return seoDesc;
    }

    public void setSeoDesc(String seoDesc) {
        this.seoDesc = seoDesc;
    }

    public boolean isAllowShadowing() {
        return allowShadowing;
    }

    public void setAllowShadowing(boolean allowShadowing) {
        this.allowShadowing = allowShadowing;
    }

    public List<Video> getVideoList() {
        return videoList;
    }

    public void setVideoList(List<Video> videoList) {
        this.videoList = videoList;
    }
}
