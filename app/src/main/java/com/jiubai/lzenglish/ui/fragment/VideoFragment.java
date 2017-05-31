package com.jiubai.lzenglish.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jiubai.lzenglish.R;
import com.jiubai.lzenglish.common.UtilBox;
import com.jiubai.lzenglish.config.Config;
import com.jiubai.lzenglish.ui.activity.SearchVideoActivity;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class VideoFragment extends Fragment {

    @Bind(R.id.tabs)
    TabLayout mTabLayout;

    @Bind(R.id.container)
    ViewPager mViewPager;

    @Bind(R.id.appbar)
    AppBarLayout mAppBarLayout;

    @Bind(R.id.imageView_search)
    ImageView mSearchImageView;

    private MyFragmentPagerAdapter mFragmentPagerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);

        ButterKnife.bind(this, view);

        initView();

        return view;
    }

    private void initView() {
        ArrayList<Fragment> list = new ArrayList<>();

        for (int i = 0; i < Config.AgeGroups.length; i++) {
            list.add(new VideoListFragment(i));
        }

        //Todo 这里要考虑一下性能
        mViewPager.setOffscreenPageLimit(Config.AgeGroups.length);

        mFragmentPagerAdapter = new MyFragmentPagerAdapter(getChildFragmentManager(), list);
        mViewPager.setAdapter(mFragmentPagerAdapter);

        mTabLayout.setSelectedTabIndicatorHeight(UtilBox.dip2px(getActivity(), 1));
        mTabLayout.setupWithViewPager(mViewPager);

        mAppBarLayout.setPadding(0, Config.StatusbarHeight, 0, 0);
    }

    @OnClick({R.id.imageView_search})
    public void onClick(View view) {
        Intent intent;

        switch (view.getId()) {
            case R.id.imageView_search:
                intent = new Intent(getActivity(), SearchVideoActivity.class);
                //UtilBox.startActivityWithTransition(getActivity(), intent, false, mSearchImageView, "searchIcon");
                UtilBox.startActivity(getActivity(), intent, false);
                break;
        }
    }

    private class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        ArrayList<Fragment> list;

        MyFragmentPagerAdapter(FragmentManager fm, ArrayList<Fragment> list) {
            super(fm);
            this.list = list;
        }

        @Override
        public int getCount() {
            return Config.AgeGroups.length;
        }

        @Override
        public Fragment getItem(int arg0) {
            return list.get(arg0);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return Config.AgeGroups[position];
        }
    }
}
