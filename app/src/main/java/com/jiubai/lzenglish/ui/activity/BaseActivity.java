package com.jiubai.lzenglish.ui.activity;

import android.support.v7.app.AppCompatActivity;

import com.jiubai.lzenglish.R;
import com.umeng.analytics.MobclickAgent;

public class BaseActivity extends AppCompatActivity {

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public void onBackPressed() {
        overridePendingTransition(R.anim.in_left_right, R.anim.out_left_right);
        finish();
    }
}