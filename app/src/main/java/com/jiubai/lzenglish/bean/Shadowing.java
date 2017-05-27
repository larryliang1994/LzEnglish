package com.jiubai.lzenglish.bean;

import java.util.List;

/**
 * Created by Larry Liang on 23/05/2017.
 */

public class Shadowing {
    private int id;
    private long createTime;
    private int idCartoon;
    private int idCartoonItem;
    private String sentenceEng;
    private String sentenceCh;
    private String content;
    private double startSecond;
    private double endSecond;
    private int sort;
    private int ifShow;
    private int hasLook;
    private List<Voice> voiceList;
    private String headerImg;

    public Shadowing() {
    }

    public Shadowing(int id, long createTime, int idCartoon, int idCartoonItem, String sentenceEng, String sentenceCh, String content, double startSecond, double endSecond, int sort, int ifShow, int hasLook, List<Voice> voiceList, String headerImg) {
        this.id = id;
        this.createTime = createTime;
        this.idCartoon = idCartoon;
        this.idCartoonItem = idCartoonItem;
        this.sentenceEng = sentenceEng;
        this.sentenceCh = sentenceCh;
        this.content = content;
        this.startSecond = startSecond;
        this.endSecond = endSecond;
        this.sort = sort;
        this.ifShow = ifShow;
        this.hasLook = hasLook;
        this.voiceList = voiceList;
        this.headerImg = headerImg;
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

    public int getIdCartoonItem() {
        return idCartoonItem;
    }

    public void setIdCartoonItem(int idCartoonItem) {
        this.idCartoonItem = idCartoonItem;
    }

    public String getSentenceEng() {
        return sentenceEng;
    }

    public void setSentenceEng(String sentenceEng) {
        this.sentenceEng = sentenceEng;
    }

    public String getSentenceCh() {
        return sentenceCh;
    }

    public void setSentenceCh(String sentenceCh) {
        this.sentenceCh = sentenceCh;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public double getStartSecond() {
        return startSecond;
    }

    public void setStartSecond(double startSecond) {
        this.startSecond = startSecond;
    }

    public double getEndSecond() {
        return endSecond;
    }

    public void setEndSecond(double endSecond) {
        this.endSecond = endSecond;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getIfShow() {
        return ifShow;
    }

    public void setIfShow(int ifShow) {
        this.ifShow = ifShow;
    }

    public int getHasLook() {
        return hasLook;
    }

    public void setHasLook(int hasLook) {
        this.hasLook = hasLook;
    }

    public List<Voice> getVoiceList() {
        return voiceList;
    }

    public void setVoiceList(List<Voice> voiceList) {
        this.voiceList = voiceList;
    }

    public String getHeaderImg() {
        return headerImg;
    }

    public void setHeaderImg(String headerImg) {
        this.headerImg = headerImg;
    }
}
