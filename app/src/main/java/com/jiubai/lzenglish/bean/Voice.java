package com.jiubai.lzenglish.bean;

/**
 * Created by Larry Liang on 23/05/2017.
 */

public class Voice {
    private int id;
    private long createTime;
    private int idMember;
    private int idCartoonItem;
    private int idCartoonReviewTraining;
    private int score;
    private int temScore;
    private int trueScore;
    private int type;
    private String baiduTranslate;
    private String baiduTranslateAll;
    private int seconds;
    private String videoPath;
    private String weixinServerId;
    private String weixinLocalId;
    private String headImgAudio;
    private String wav;

    public Voice() {
    }

    public Voice(int id, long createTime, int idMember, int idCartoonItem, int idCartoonReviewTraining, int score, int temScore, int trueScore, int type, String baiduTranslate, String baiduTranslateAll, int seconds, String videoPath, String weixinServerId, String weixinLocalId, String headImgAudio, String wav) {
        this.id = id;
        this.createTime = createTime;
        this.idMember = idMember;
        this.idCartoonItem = idCartoonItem;
        this.idCartoonReviewTraining = idCartoonReviewTraining;
        this.score = score;
        this.temScore = temScore;
        this.trueScore = trueScore;
        this.type = type;
        this.baiduTranslate = baiduTranslate;
        this.baiduTranslateAll = baiduTranslateAll;
        this.seconds = seconds;
        this.videoPath = videoPath;
        this.weixinServerId = weixinServerId;
        this.weixinLocalId = weixinLocalId;
        this.headImgAudio = headImgAudio;
        this.wav = wav;
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

    public int getIdMember() {
        return idMember;
    }

    public void setIdMember(int idMember) {
        this.idMember = idMember;
    }

    public int getIdCartoonItem() {
        return idCartoonItem;
    }

    public void setIdCartoonItem(int idCartoonItem) {
        this.idCartoonItem = idCartoonItem;
    }

    public int getIdCartoonReviewTraining() {
        return idCartoonReviewTraining;
    }

    public void setIdCartoonReviewTraining(int idCartoonReviewTraining) {
        this.idCartoonReviewTraining = idCartoonReviewTraining;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getTemScore() {
        return temScore;
    }

    public void setTemScore(int temScore) {
        this.temScore = temScore;
    }

    public int getTrueScore() {
        return trueScore;
    }

    public void setTrueScore(int trueScore) {
        this.trueScore = trueScore;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getBaiduTranslate() {
        return baiduTranslate;
    }

    public void setBaiduTranslate(String baiduTranslate) {
        this.baiduTranslate = baiduTranslate;
    }

    public String getBaiduTranslateAll() {
        return baiduTranslateAll;
    }

    public void setBaiduTranslateAll(String baiduTranslateAll) {
        this.baiduTranslateAll = baiduTranslateAll;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public String getWeixinServerId() {
        return weixinServerId;
    }

    public void setWeixinServerId(String weixinServerId) {
        this.weixinServerId = weixinServerId;
    }

    public String getWeixinLocalId() {
        return weixinLocalId;
    }

    public void setWeixinLocalId(String weixinLocalId) {
        this.weixinLocalId = weixinLocalId;
    }

    public String getHeadImgAudio() {
        return headImgAudio;
    }

    public void setHeadImgAudio(String headImgAudio) {
        this.headImgAudio = headImgAudio;
    }

    public String getWav() {
        return wav;
    }

    public void setWav(String wav) {
        this.wav = wav;
    }
}
