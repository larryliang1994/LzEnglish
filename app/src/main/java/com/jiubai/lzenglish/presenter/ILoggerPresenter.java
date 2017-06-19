package com.jiubai.lzenglish.presenter;

import android.content.Context;

/**
 * Created by Leunghowell on 2017/6/19.
 */

public interface ILoggerPresenter {
    void writeLog(Context context, String activityName, String content);
}
