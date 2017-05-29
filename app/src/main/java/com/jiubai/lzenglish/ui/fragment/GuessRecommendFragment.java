package com.jiubai.lzenglish.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.jiubai.lzenglish.R;
import com.jiubai.lzenglish.common.UtilBox;

import butterknife.ButterKnife;

/**
 * Created by Leunghowell on 2017/5/29.
 */

public class GuessRecommendFragment extends Fragment {
    public GuessRecommendFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recommend_guess, container, false);

        ButterKnife.bind(this, view);

        return view;
    }
}
