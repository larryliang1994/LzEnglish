package com.jiubai.lzenglish.bean;

import org.json.JSONObject;

/**
 * Created by Larry Liang on 22/05/2017.
 */

public class Video {
    private int id;
    private long createTime;
    private int idCartoon;
    private String name;
    private String video;
    private int sort;
    private int countTime;
    private int ifshow;
    private String headImg;
    private String sharePic;
    private String shareTitle;
    private String shareContent;
    private int starRule;
    private String note;
    private int logCartoonItemScore;
    private boolean allowWatch;
    private boolean hasFinishWatch;
    private boolean hasReview;
    private boolean allowReview;
    private JSONObject hasWatch;

    public Video() {
    }

    public Video(int id, long createTime, int idCartoon, String name, String video, int sort, int countTime,
                 int ifshow, String headImg, String sharePic, String shareTitle, String shareContent,
                 int starRule, String note, int logCartoonItemScore, boolean allowWatch,
                 boolean hasFinishWatch, boolean hasReview, boolean allowReview, JSONObject hasWatch) {
        this.id = id;
        this.createTime = createTime;
        this.idCartoon = idCartoon;
        this.name = name;
        this.video = video;
        this.sort = sort;
        this.countTime = countTime;
        this.ifshow = ifshow;
        this.headImg = headImg;
        this.sharePic = sharePic;
        this.shareTitle = shareTitle;
        this.shareContent = shareContent;
        this.starRule = starRule;
        this.note = note;
        this.logCartoonItemScore = logCartoonItemScore;
        this.allowWatch = allowWatch;
        this.hasFinishWatch = hasFinishWatch;
        this.hasReview = hasReview;
        this.allowReview = allowReview;
        this.hasWatch = hasWatch;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getIdCartoon() {
        return idCartoon;
    }

    public void setIdCartoon(int idCartoon) {
        this.idCartoon = idCartoon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getCountTime() {
        return countTime;
    }

    public void setCountTime(int countTime) {
        this.countTime = countTime;
    }

    public int getIfshow() {
        return ifshow;
    }

    public void setIfshow(int ifshow) {
        this.ifshow = ifshow;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getSharePic() {
        return sharePic;
    }

    public void setSharePic(String sharePic) {
        this.sharePic = sharePic;
    }

    public String getShareTitle() {
        return shareTitle;
    }

    public void setShareTitle(String shareTitle) {
        this.shareTitle = shareTitle;
    }

    public String getShareContent() {
        return shareContent;
    }

    public void setShareContent(String shareContent) {
        this.shareContent = shareContent;
    }

    public int getStarRule() {
        return starRule;
    }

    public void setStarRule(int starRule) {
        this.starRule = starRule;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getLogCartoonItemScore() {
        return logCartoonItemScore;
    }

    public void setLogCartoonItemScore(int logCartoonItemScore) {
        this.logCartoonItemScore = logCartoonItemScore;
    }

    public boolean isAllowWatch() {
        return allowWatch;
    }

    public void setAllowWatch(boolean allowWatch) {
        this.allowWatch = allowWatch;
    }

    public boolean isHasFinishWatch() {
        return hasFinishWatch;
    }

    public void setHasFinishWatch(boolean hasFinishWatch) {
        this.hasFinishWatch = hasFinishWatch;
    }

    public boolean isHasReview() {
        return hasReview;
    }

    public void setHasReview(boolean hasReview) {
        this.hasReview = hasReview;
    }

    public boolean isAllowReview() {
        return allowReview;
    }

    public void setAllowReview(boolean allowReview) {
        this.allowReview = allowReview;
    }

    public JSONObject getHasWatch() {
        return hasWatch;
    }

    public void setHasWatch(JSONObject hasWatch) {
        this.hasWatch = hasWatch;
    }
}
