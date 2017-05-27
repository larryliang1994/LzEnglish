package com.jiubai.lzenglish.bean;

import android.os.Parcelable;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Larry Liang on 20/05/2017.
 */

public class Cartoon implements Serializable {
    private int id;
    private int pid;
    private int sort;
    private long createTime;
    private int idCartoonCat;
    private String name2;
    private String name;
    private String age;
    private String image;
    private String videoPic;
    private String briefInfo;
    private String info;
    private double price;
    private int ifShow;
    private String headImg;
    private int maxFreeNum;
    private int maxTrainNum;
    private String buyPic;
    private String sharePrc;
    private String shareTitle;
    private String shareContent;
    private int starRule;
    private String promoteImageTemplate;
    private JSONObject _idCartoonCat;
    private String _url;
    private String _image;
    private List<Season> seasonList;

    public Cartoon() {
    }

    public Cartoon(int id, int pid, int sort, long createTime, int idCartoonCat, String name2, String name, String age, String image, String videoPic, String briefInfo, String info, double price, int ifShow, String headImg, int maxFreeNum, int maxTrainNum, String buyPic, String sharePrc, String shareTitle, String shareContent, int starRule, String promoteImageTemplate, JSONObject _idCartoonCat, String _url, String _image) {
        this.id = id;
        this.pid = pid;
        this.sort = sort;
        this.createTime = createTime;
        this.idCartoonCat = idCartoonCat;
        this.name2 = name2;
        this.name = name;
        this.age = age;
        this.image = image;
        this.videoPic = videoPic;
        this.briefInfo = briefInfo;
        this.info = info;
        this.price = price;
        this.ifShow = ifShow;
        this.headImg = headImg;
        this.maxFreeNum = maxFreeNum;
        this.maxTrainNum = maxTrainNum;
        this.buyPic = buyPic;
        this.sharePrc = sharePrc;
        this.shareTitle = shareTitle;
        this.shareContent = shareContent;
        this.starRule = starRule;
        this.promoteImageTemplate = promoteImageTemplate;
        this._idCartoonCat = _idCartoonCat;
        this._url = _url;
        this._image = _image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getIdCartoonCat() {
        return idCartoonCat;
    }

    public void setIdCartoonCat(int idCartoonCat) {
        this.idCartoonCat = idCartoonCat;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getVideoPic() {
        return videoPic;
    }

    public void setVideoPic(String videoPic) {
        this.videoPic = videoPic;
    }

    public String getBriefInfo() {
        return briefInfo;
    }

    public void setBriefInfo(String briefInfo) {
        this.briefInfo = briefInfo;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getIfShow() {
        return ifShow;
    }

    public void setIfShow(int ifShow) {
        this.ifShow = ifShow;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public int getMaxFreeNum() {
        return maxFreeNum;
    }

    public void setMaxFreeNum(int maxFreeNum) {
        this.maxFreeNum = maxFreeNum;
    }

    public int getMaxTrainNum() {
        return maxTrainNum;
    }

    public void setMaxTrainNum(int maxTrainNum) {
        this.maxTrainNum = maxTrainNum;
    }

    public String getBuyPic() {
        return buyPic;
    }

    public void setBuyPic(String buyPic) {
        this.buyPic = buyPic;
    }

    public String getSharePrc() {
        return sharePrc;
    }

    public void setSharePrc(String sharePrc) {
        this.sharePrc = sharePrc;
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

    public String getPromoteImageTemplate() {
        return promoteImageTemplate;
    }

    public void setPromoteImageTemplate(String promoteImageTemplate) {
        this.promoteImageTemplate = promoteImageTemplate;
    }

    public JSONObject get_idCartoonCat() {
        return _idCartoonCat;
    }

    public void set_idCartoonCat(JSONObject _idCartoonCat) {
        this._idCartoonCat = _idCartoonCat;
    }

    public String get_url() {
        return _url;
    }

    public void set_url(String _url) {
        this._url = _url;
    }

    public String get_image() {
        return _image;
    }

    public void set_image(String _image) {
        this._image = _image;
    }

    public List<Season> getSeasonList() {
        return seasonList;
    }

    public void setSeasonList(List<Season> seasonList) {
        this.seasonList = seasonList;
    }
}
