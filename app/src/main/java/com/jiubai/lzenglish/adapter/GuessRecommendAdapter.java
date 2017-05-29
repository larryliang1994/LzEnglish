package com.jiubai.lzenglish.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.jiubai.lzenglish.common.UtilBox;

import java.util.ArrayList;

/**
 * Created by Leunghowell on 2017/5/29.
 */

public class GuessRecommendAdapter extends FragmentPagerAdapter {
    ArrayList<Fragment> list;
    Context context;

    public GuessRecommendAdapter(Context context, FragmentManager fm, ArrayList<Fragment> list) {
        super(fm);
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public float getPageWidth(int position) {
        return (float) (UtilBox.dip2px(context, 230) * 1.0 / UtilBox.getWidthPixels(context));
    }
}
