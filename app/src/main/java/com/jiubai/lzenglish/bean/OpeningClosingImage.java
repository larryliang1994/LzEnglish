package com.jiubai.lzenglish.bean;

/**
 * Created by larry on 11/07/2017.
 */

public class OpeningClosingImage {
    private String url;
    private int interval;

    public OpeningClosingImage() {
    }

    public OpeningClosingImage(String url, int interval) {
        this.url = url;
        this.interval = interval;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }
}
