package com.jiubai.lzenglish.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import com.jiubai.lzenglish.App;
import com.jiubai.lzenglish.R;
import com.jiubai.lzenglish.common.StatusBarUtil;
import com.jiubai.lzenglish.common.UtilBox;
import com.jiubai.lzenglish.config.Config;
import com.jiubai.lzenglish.config.Constants;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity implements IWXAPIEventHandler {

    @Bind(R.id.imageView1)
    ImageView mImageView1;

    @Bind(R.id.imageView2)
    ImageView mImageView2;

    @Bind(R.id.imageView3)
    ImageView mImageView3;

    @Bind(R.id.imageView4)
    ImageView mImageView4;

    @Bind(R.id.imageView5)
    ImageView mImageView5;

    @Bind(R.id.imageView6)
    ImageView mImageView6;

    @Bind(R.id.imageView7)
    ImageView mImageView7;

    @Bind(R.id.imageView8)
    ImageView mImageView8;

    @Bind(R.id.imageView9)
    ImageView mImageView9;

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

        // send oauth request
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "lzenglish";
        App.iwxapi.sendReq(req);

//        Intent intent = new Intent(this, HomeActivity.class);
//        UtilBox.startActivity(this, intent, true);
        // send oauth request
//        final com.tencent.mm.opensdk.modelmsg.SendAuth.Req req = new SendAuth.Req();
//        req.scope = "snsapi_userinfo";
//        req.state = "wechat_sdk_demo_test";
//        //((App)getApplication()).api.sendReq(req);
    }

    @Override
    public void onResp(BaseResp resp) {
        int result = 0;

        Toast.makeText(this, "baseresp.getType = " + resp.getType(), Toast.LENGTH_SHORT).show();

        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                Log.i("text", "ERR_OK");
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                Log.i("text", "ERR_USER_CANCEL");
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                Log.i("text", "ERR_AUTH_DENIED");
                break;
            case BaseResp.ErrCode.ERR_UNSUPPORT:
                Log.i("text", "ERR_UNSUPPORT");
                break;
            default:
                Log.i("text", "unknown");
                break;
        }

        Toast.makeText(this, resp.errCode, Toast.LENGTH_LONG).show();
    }

    private void initAnimation() {
        getThread(mImageView1).start();
        getThread(mImageView2).start();
        getThread(mImageView3).start();
        getThread(mImageView4).start();
        getThread(mImageView5).start();
        getThread(mImageView6).start();
        getThread(mImageView7).start();
        getThread(mImageView8).start();
        getThread(mImageView9).start();
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

    @Override
    public void onReq(BaseReq baseReq) {

    }
}
