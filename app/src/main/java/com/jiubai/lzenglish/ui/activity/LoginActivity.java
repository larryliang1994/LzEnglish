package com.jiubai.lzenglish.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.jiubai.lzenglish.App;
import com.jiubai.lzenglish.R;
import com.jiubai.lzenglish.common.StatusBarUtil;
import com.jiubai.lzenglish.common.UtilBox;
import com.jiubai.lzenglish.config.Config;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    @Bind(R.id.imageView1)
    ImageView mImageView1;

    @Bind(R.id.imageView2)
    ImageView mImageView2;

    @Bind(R.id.imageView3)
    ImageView mImageView3;

    @Bind(R.id.imageView4)
    ImageView mImageView4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        StatusBarUtil.StatusBarLightMode(this);

        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        initAnimation();
    }

    @OnClick({R.id.imageView_login, R.id.textView_login})
    public void login(View view) {
        SharedPreferences.Editor editor = App.sharedPreferences.edit();
        editor.putString("ageIndex", Config.AgeIndex);
        editor.putString("preferenceVideoIndex", Config.PreferenceVideoIndex);
        editor.apply();

        Intent intent = new Intent(this, HomeActivity.class);
        UtilBox.startActivity(this, intent, true);
        // send oauth request
//        final com.tencent.mm.opensdk.modelmsg.SendAuth.Req req = new SendAuth.Req();
//        req.scope = "snsapi_userinfo";
//        req.state = "wechat_sdk_demo_test";
//        //((App)getApplication()).api.sendReq(req);
    }

    private void initAnimation() {
        getThread(mImageView1).start();
        getThread(mImageView2).start();
        getThread(mImageView3).start();
        getThread(mImageView4).start();
    }

    private Thread getThread(final View view) {
        return new Thread(new Runnable() {
            @Override
            public void run() {
                int[] location = {0, 0};
                view.getLocationOnScreen(location);

                while (true) {
                    final Animation translateAnimation = new TranslateAnimation(
                            location[0], location[0] + (float)Math.random() * 100 - (float)Math.random() * 100,
                            location[1], location[1]+ (float)Math.random() * 100 - (float)Math.random() * 100);
                    translateAnimation.setDuration(3000);
                    translateAnimation.setRepeatCount(1);
                    translateAnimation.setRepeatMode(Animation.REVERSE);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            view.startAnimation(translateAnimation);
                        }
                    });

                    try {
                        Thread.sleep(6000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
