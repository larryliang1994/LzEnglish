package com.jiubai.lzenglish.bean;

/**
 * Created by Leunghowell on 2017/6/6.
 */

public class InterestRecommend {
    private int id;
    private String image;
    private String title;
    private String text;

    public InterestRecommend() {
    }

    public InterestRecommend(int id, String image, String title, String text) {
        this.id = id;
        this.image = image;
        this.title = title;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
