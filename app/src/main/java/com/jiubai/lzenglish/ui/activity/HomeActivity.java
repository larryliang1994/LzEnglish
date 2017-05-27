package com.jiubai.lzenglish.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.Toast;

import com.jiubai.lzenglish.R;
import com.jiubai.lzenglish.common.StatusBarUtil;
import com.jiubai.lzenglish.config.Config;
import com.jiubai.lzenglish.net.DownloadManager;
import com.jiubai.lzenglish.ui.fragment.RecommendFragment;
import com.jiubai.lzenglish.ui.fragment.UserFragment;
import com.jiubai.lzenglish.ui.fragment.VideoFragment;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HomeActivity extends BaseActivity {

    @Bind(R.id.viewPager)
    ViewPager mViewPager;

    @Bind(R.id.navigation)
    BottomNavigationView mNavigation;

    private RecommendFragment mRecommendFragment = new RecommendFragment();
    private VideoFragment mVideoFragment = new VideoFragment();
    private UserFragment mUserFragment = new UserFragment();

    private ArrayList<Fragment> mFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        StatusBarUtil.StatusBarDarkMode(this, Config.DeviceType);

        ButterKnife.bind(this);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        initViewPager();
    }

    private void initViewPager() {
        mFragments = new ArrayList<>();

        mFragments.add(mRecommendFragment);
        mFragments.add(mVideoFragment);
        mFragments.add(mUserFragment);

        mViewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), mFragments));

        mViewPager.setCurrentItem(0);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        mNavigation.setSelectedItemId(R.id.navigation_recommend);
                        StatusBarUtil.StatusBarDarkMode(HomeActivity.this, Config.DeviceType);
                        break;
                    case 1:
                        mNavigation.setSelectedItemId(R.id.navigation_video);
                        StatusBarUtil.StatusBarLightMode(HomeActivity.this);
                        break;
                    case 2:
                        mNavigation.setSelectedItemId(R.id.navigation_person);
                        StatusBarUtil.StatusBarLightMode(HomeActivity.this);
                        break;
                }
                mNavigation.setSelectedItemId(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_recommend:
                    mViewPager.setCurrentItem(0, true);
                    StatusBarUtil.StatusBarDarkMode(HomeActivity.this, Config.DeviceType);
                    return true;
                case R.id.navigation_video:
                    mViewPager.setCurrentItem(1, true);
                    StatusBarUtil.StatusBarLightMode(HomeActivity.this);
                    return true;
                case R.id.navigation_person:
                    mViewPager.setCurrentItem(2, true);
                    StatusBarUtil.StatusBarLightMode(HomeActivity.this);
                    return true;
            }
            return false;
        }

    };

    private class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        ArrayList<Fragment> list;

        MyFragmentPagerAdapter(FragmentManager fm, ArrayList<Fragment> list) {
            super(fm);
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Fragment getItem(int arg0) {
            return list.get(arg0);
        }
    }

    private long exitTime = 0;

    @Override
    protected void onDestroy() {
        DownloadManager.getInstance().writeVideoSharedPreferences();

        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                DownloadManager.getInstance().pausedAll();
                DownloadManager.getInstance().writeVideoSharedPreferences();

                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
