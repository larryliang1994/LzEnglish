package com.jiubai.lzenglish.bean;

/**
 * Created by Leunghowell on 2017/6/6.
 */

public class AgeRecommend {
    private int id;
    private String image;
    private String mainTitle;
    private String subTitle;
    private String footerText;

    public AgeRecommend() {
    }

    public AgeRecommend(int id, String image, String mainTitle, String subTitle, String footerText) {
        this.id = id;
        this.image = image;
        this.mainTitle = mainTitle;
        this.subTitle = subTitle;
        this.footerText = footerText;
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

    public String getMainTitle() {
        return mainTitle;
    }

    public void setMainTitle(String mainTitle) {
        this.mainTitle = mainTitle;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getFooterText() {
        return footerText;
    }

    public void setFooterText(String footerText) {
        this.footerText = footerText;
    }
}
