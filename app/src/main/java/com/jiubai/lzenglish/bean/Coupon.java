package com.jiubai.lzenglish.bean;

/**
 * Created by Larry Liang on 15/06/2017.
 */

public class Coupon {
    private int id;
    private float price;
    private int idMember;
    private String desc;
    private int status;

    public Coupon() {
    }

    public Coupon(int id, float price, int idMember, String desc, int status) {
        this.id = id;
        this.price = price;
        this.idMember = idMember;
        this.desc = desc;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public float getPrice() {
        return price;
    }

    public int getIdMember() {
        return idMember;
    }

    public String getDesc() {
        return desc;
    }

    public int getStatus() {
        return status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setIdMember(int idMember) {
        this.idMember = idMember;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
