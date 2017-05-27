package com.jiubai.lzenglish.ui.iview;

/**
 * Created by Larry Liang on 23/05/2017.
 */

public interface IShadowingView {
    void onGetShadowingListResult(boolean result, String info, Object extras);
    void onSaveVoiceResult(boolean result, String info, Object extras);
    void onScoringVoiceResult(boolean result, String info, Object extras);
}
