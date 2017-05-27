package com.jiubai.lzenglish.presenter;

import android.content.Context;

import com.jiubai.lzenglish.bean.Voice;

/**
 * Created by Larry Liang on 23/05/2017.
 */

public interface IShadowingPresenter {
    void getShadowingList(int videoId);
    void saveVoice(Context context, Voice voice);
    void scoringVoice(int voiceId);
    void deleteVoice(int voiceId);
}
