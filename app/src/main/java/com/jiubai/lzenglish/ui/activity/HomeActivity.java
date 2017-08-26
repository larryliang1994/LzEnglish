package com.jiubai.lzenglish.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.Toast;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.jiubai.lzenglish.R;
import com.jiubai.lzenglish.bean.DetailedSeason;
import com.jiubai.lzenglish.bean.Video;
import com.jiubai.lzenglish.common.StatusBarUtil;
import com.jiubai.lzenglish.common.UtilBox;
import com.jiubai.lzenglish.config.Config;
import com.jiubai.lzenglish.manager.DownloadManager;
import com.jiubai.lzenglish.presenter.GetCartoonInfoPresenterImpl;
import com.jiubai.lzenglish.ui.fragment.RecommendFragment;
import com.jiubai.lzenglish.ui.fragment.UserFragment;
import com.jiubai.lzenglish.ui.fragment.VideoFragment;
import com.jiubai.lzenglish.ui.iview.IGetCartoonInfoView;
import com.jiubai.lzenglish.widget.CustomViewPager;
import com.jiubai.lzenglish.zxing.activity.CaptureActivity;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HomeActivity extends BaseActivity {

    @Bind(R.id.viewPager)
    CustomViewPager mViewPager;

    @Bind(R.id.bottom_navigation)
    BottomNavigationViewEx mBottomNavigation;

    private RecommendFragment mRecommendFragment = new RecommendFragment();
    private VideoFragment mVideoFragment = new VideoFragment();
    private UserFragment mUserFragment = new UserFragment();

    public static final int CODE_QR_SCAN = 1;

    private int previousIndex = 0;

    private ArrayList<Fragment> mFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        StatusBarUtil.StatusBarDarkMode(this, Config.DeviceType);

        ButterKnife.bind(this);

        mBottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        mBottomNavigation.enableAnimation(false);
        mBottomNavigation.enableItemShiftingMode(false);
        mBottomNavigation.enableShiftingMode(false);

        initViewPager();

        Log.i("text", "height = " + UtilBox.px2dip(this, UtilBox.getStatusBarHeight(this)));
    }

    private void initViewPager() {
        mFragments = new ArrayList<>();

        mFragments.add(mRecommendFragment);
        mFragments.add(mVideoFragment);
        mFragments.add(mUserFragment);
        mViewPager.setOffscreenPageLimit(3);
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
                        mBottomNavigation.setSelectedItemId(R.id.navigation_recommend);
                        StatusBarUtil.StatusBarDarkMode(HomeActivity.this, Config.DeviceType);
                        break;
                    case 1:
                        mBottomNavigation.setSelectedItemId(R.id.navigation_video);
                        StatusBarUtil.StatusBarLightMode(HomeActivity.this);
                        break;
                    case 2:
                        mBottomNavigation.setSelectedItemId(R.id.navigation_person);
                        StatusBarUtil.StatusBarLightMode(HomeActivity.this);
                        break;
                }
                mBottomNavigation.setSelectedItemId(position);
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
                    previousIndex = 0;
                    mViewPager.setCurrentItem(0, true);
                    StatusBarUtil.StatusBarDarkMode(HomeActivity.this, Config.DeviceType);
                    return true;
                case R.id.navigation_video:
                    previousIndex = 1;
                    mViewPager.setCurrentItem(1, true);
                    StatusBarUtil.StatusBarLightMode(HomeActivity.this);
                    return true;
                case R.id.navigation_qrscan:
                    if (ActivityCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                            || ActivityCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.VIBRATE) != PackageManager.PERMISSION_GRANTED) {
                        String[] mPermissionList = new String[]{Manifest.permission.CAMERA, Manifest.permission.VIBRATE};
                        ActivityCompat.requestPermissions(HomeActivity.this, mPermissionList, 123);
                    } else {
                        Intent intent = new Intent(HomeActivity.this, CaptureActivity.class);
                        startActivityForResult(intent, CODE_QR_SCAN);
                        overridePendingTransition(R.anim.in_right_left, R.anim.out_right_left);
                    }
                    return true;
                case R.id.navigation_person:
                    previousIndex = 2;
                    mViewPager.setCurrentItem(2, true);
                    StatusBarUtil.StatusBarLightMode(HomeActivity.this);
                    return true;
            }
            return false;
        }

    };

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 123) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(this, CaptureActivity.class);
                startActivityForResult(intent, CODE_QR_SCAN);
            } else {
                UtilBox.alert(this, "需要摄像头权限才能进行扫码，请到系统设置中开启", "关闭", null);

                switch (previousIndex) {
                    case 0:
                        mBottomNavigation.setSelectedItemId(R.id.navigation_recommend);
                        StatusBarUtil.StatusBarDarkMode(HomeActivity.this, Config.DeviceType);
                        break;
                    case 1:
                        mBottomNavigation.setSelectedItemId(R.id.navigation_video);
                        StatusBarUtil.StatusBarLightMode(HomeActivity.this);
                        break;
                    case 2:
                        mBottomNavigation.setSelectedItemId(R.id.navigation_person);
                        StatusBarUtil.StatusBarLightMode(HomeActivity.this);
                        break;
                }
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case CODE_QR_SCAN:
                switch (previousIndex) {
                    case 0:
                        mBottomNavigation.setSelectedItemId(R.id.navigation_recommend);
                        StatusBarUtil.StatusBarDarkMode(HomeActivity.this, Config.DeviceType);
                        break;
                    case 1:
                        mBottomNavigation.setSelectedItemId(R.id.navigation_video);
                        StatusBarUtil.StatusBarLightMode(HomeActivity.this);
                        break;
                    case 2:
                        mBottomNavigation.setSelectedItemId(R.id.navigation_person);
                        StatusBarUtil.StatusBarLightMode(HomeActivity.this);
                        break;
                }

                if (resultCode == RESULT_OK) {
                    String result = data.getStringExtra("result");
                    if (result != null && !"".equals(result)) {
                        if (result.startsWith("http://kid.quantest.cn/?_url=cartoonItem/get")
                                || result.startsWith("https://kid.quantest.cn/?_url=cartoonItem/get")) {
                            decodeQRCode(result);
                        } else {
                            UtilBox.alert(this, "只能识别量子卡片上的二维码", "关闭", null);
                        }
                    } else {
                        UtilBox.alert(this, "只能识别量子卡片上的二维码", "关闭", null);
                    }
                }
                break;
        }
    }

    private void decodeQRCode(String result) {
        UtilBox.showLoading(this, true);

        final int videoId = Integer.valueOf(result.substring(result.indexOf("id=") + 3));

        new GetCartoonInfoPresenterImpl(new IGetCartoonInfoView() {
            @Override
            public void onGetCartoonListResult(boolean result, String info, Object extras) {
            }

            @Override
            public void onGetCartoonSeasonListResult(boolean result, String info, Object extras) {
            }

            @Override
            public void onGetVideoListResult(boolean result, String info, Object extras) {
                UtilBox.dismissLoading(true);

                if (result) {
                    ArrayList<Video> videoList = (ArrayList<Video>) ((DetailedSeason) extras).getVideoList();

                    for (int i = 0; i < videoList.size(); i++) {
                        if (videoList.get(i).getId() == videoId) {
                            if (videoList.get(i).isAllowWatch()) {
                                Intent intent = new Intent(HomeActivity.this, PlayVideoActivity.class);
                                intent.putExtra("videoId", videoId);
                                intent.putExtra("fromQRScan", true);
                                UtilBox.startActivity(HomeActivity.this, intent, false);
                            } else {
                                Intent intent = new Intent(HomeActivity.this, ActivationActivity.class);
                                intent.putExtra("videoId", videoId);
                                UtilBox.startActivity(HomeActivity.this, intent, false);
                            }
                        }
                    }
                } else {
                    UtilBox.alert(HomeActivity.this, info, "关闭", null);
                }
            }
        }).getVideoList(videoId);
    }

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
            if (mViewPager.getCurrentItem() != 0) {
                mViewPager.setCurrentItem(0, true);
                mBottomNavigation.setSelectedItemId(R.id.navigation_recommend);
                StatusBarUtil.StatusBarDarkMode(HomeActivity.this, Config.DeviceType);
            } else if ((System.currentTimeMillis() - exitTime) > 2000) {
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

    @Override
    public void onResume() {
        if (TextUtils.isEmpty(Config.ThirdSession)) {
            finish();
        }

        super.onResume();
    }
}
