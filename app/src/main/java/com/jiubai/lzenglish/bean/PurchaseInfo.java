package com.jiubai.lzenglish.bean;

import java.util.ArrayList;

/**
 * Created by Larry Liang on 15/06/2017.
 */

public class PurchaseInfo {
    private int id;
    private String name;
    private String image;
    private float price;
    private ArrayList<Coupon> coupons;

    public PurchaseInfo() {
    }

    public PurchaseInfo(int id, String name, String image, float price, ArrayList<Coupon> coupons) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.price = price;
        this.coupons = coupons;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public ArrayList<Coupon> getCoupons() {
        return coupons;
    }

    public void setCoupons(ArrayList<Coupon> coupons) {
        this.coupons = coupons;
    }
}
