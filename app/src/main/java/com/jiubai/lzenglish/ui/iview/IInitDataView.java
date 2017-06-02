package com.jiubai.lzenglish.ui.iview;

/**
 * Created by Larry Liang on 20/05/2017.
 */

public interface IInitDataView {
    void onGetResourceUrlResult(boolean result, String info, Object extras);
    void onGetAgeGroupsResult(boolean result, String info, Object extras);
    void onGetAllCartoonResult(boolean result, String info, Object extras);
    void onGetUserInfoResult(boolean result, String info, Object extras);
    void onGetAgeInterestConfigResult(boolean result, String info, Object extras);
}
