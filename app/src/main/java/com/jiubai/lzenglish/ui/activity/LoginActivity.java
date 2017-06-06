package com.jiubai.lzenglish.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
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
import com.jiubai.lzenglish.presenter.IInitDataPresenter;
import com.jiubai.lzenglish.presenter.InitDataPresenterImpl;
import com.jiubai.lzenglish.ui.iview.IInitDataView;
import com.nostra13.universalimageloader.utils.L;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity implements IInitDataView {

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

    private ProgressDialog progressDialog;

    private boolean logining = false;

    private int requestNum = 0;
    private final int totalRequestNum = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        StatusBarUtil.StatusBarLightMode(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("加载中...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        initAnimation();
    }

    @OnClick({R.id.imageView_login, R.id.textView_login})
    public void login(View view) {
        logining = true;

        if (progressDialog != null) {
            progressDialog.show();
        }

        SharedPreferences.Editor editor = App.sharedPreferences.edit();
        editor.putString("ageIndex", Config.AgeIndex);
        editor.putString("preferenceVideoIndex", Config.PreferenceVideoIndex);
        editor.apply();

        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "lzenglish";
        App.iwxapi.sendReq(req);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!TextUtils.isEmpty(Config.ThirdSession)) {
            IInitDataPresenter initDataPresenter = new InitDataPresenterImpl(this);
            initDataPresenter.getResourceUrl();
            initDataPresenter.getAgeGroups();
            initDataPresenter.getAllCartoon();
            initDataPresenter.getUserInfo();
        } else {
            if (progressDialog != null) {
                progressDialog.dismiss();
            }

            logining = false;
        }
    }

    @Override
    public void onGetResourceUrlResult(boolean result, String info, Object extras) {
        handleResult(result, info);
    }

    @Override
    public void onGetAgeGroupsResult(boolean result, String info, Object extras) {
        handleResult(result, info);
    }

    @Override
    public void onGetAllCartoonResult(boolean result, String info, Object extras) {
        handleResult(result, info);
    }

    @Override
    public void onGetUserInfoResult(boolean result, String info, Object extras) {
        handleResult(result, info);
    }

    @Override
    public void onGetAgeInterestConfigResult(boolean result, String info, Object extras) {
        handleResult(result, info);
    }

    private void handleResult(boolean result, String info) {
        if (progressDialog != null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressDialog.dismiss();
                }
            }, 0);
        }

        if (result) {
            requestNum ++;
            if (requestNum == totalRequestNum) {
                SharedPreferences sp = App.sharedPreferences;
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("third_session", Config.ThirdSession);
                editor.apply();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        UtilBox.startActivity(LoginActivity.this, intent, true);
                    }
                }, 0);
            }
        } else {
            logining = false;
            Toast.makeText(this, info, Toast.LENGTH_SHORT).show();
        }
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
                    if (!logining) {
                        final Animation translateAnimation = new TranslateAnimation(
                                location[0], location[0] + (float) Math.random() * 100 - (float) Math.random() * 100,
                                location[1], location[1] + (float) Math.random() * 100 - (float) Math.random() * 100);
                        translateAnimation.setDuration(3000);
                        translateAnimation.setRepeatCount(1);
                        translateAnimation.setRepeatMode(Animation.REVERSE);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                view.startAnimation(translateAnimation);
                            }
                        });
                    }

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
