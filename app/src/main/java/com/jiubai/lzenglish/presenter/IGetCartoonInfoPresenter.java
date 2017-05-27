package com.jiubai.lzenglish.presenter;

/**
 * Created by Larry Liang on 20/05/2017.
 */

public interface IGetCartoonInfoPresenter {
    void getCartoonList(int ageGroupsIndex);
    void getCartoonSeason(int cartoonId);
    void getVideoList(int seasonId);
}
