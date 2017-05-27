package com.jiubai.lzenglish.ui.iview;

/**
 * Created by Larry Liang on 20/05/2017.
 */

public interface IGetCartoonInfoView {
    void onGetCartoonListResult(boolean result, String info, Object extras);
    void onGetCartoonSeasonListResult(boolean result, String info, Object extras);
    void onGetVideoListResult(boolean result, String info, Object extras);
}
