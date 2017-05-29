package com.jiubai.lzenglish.bean;

/**
 * Created by Leunghowell on 2017/5/28.
 */

public class SearchHistory {
    private int cartoonId;
    private String name;

    public SearchHistory() {
    }

    public SearchHistory(int cartoonId, String name) {
        this.cartoonId = cartoonId;
        this.name = name;
    }

    public int getCartoonId() {
        return cartoonId;
    }

    public void setCartoonId(int cartoonId) {
        this.cartoonId = cartoonId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
