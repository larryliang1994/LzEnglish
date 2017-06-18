package com.jiubai.lzenglish.ui.iview;

/**
 * Created by Larry Liang on 15/06/2017.
 */

public interface IPurchaseView {
    void onGetPurchaseInfoResult(boolean result, String info, Object extras);
    void onDoPurchaseResult(boolean result, String info, Object extras);
}
