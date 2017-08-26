package com.jiubai.lzenglish;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jiubai.lzenglish.common.StatusBarUtil;
import com.jiubai.lzenglish.common.UtilBox;
import com.jiubai.lzenglish.config.Config;
import com.jiubai.lzenglish.presenter.IInitDataPresenter;
import com.jiubai.lzenglish.presenter.InitDataPresenterImpl;
import com.jiubai.lzenglish.ui.activity.BaseActivity;
import com.jiubai.lzenglish.ui.activity.ChooseAgeActivity;
import com.jiubai.lzenglish.ui.activity.HomeActivity;
import com.jiubai.lzenglish.ui.iview.IInitDataView;
import com.tencent.stat.StatService;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EntryActivity extends BaseActivity implements IInitDataView {

    @Bind(R.id.ll_no_network)
    LinearLayout mNoNetworkLinearLayout;

    private ProgressDialog progressDialog;

    private int requestNum = 0;
    private int totalRequestNum = 5;

    private Class entryActivity = HomeActivity.class;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_entry);

        Config.DeviceType = StatusBarUtil.StatusBarLightMode(this);
        Config.AppbarHeight = (int) (getResources().getDimension(R.dimen.appbar_height));
        Config.StatusbarHeight = UtilBox.getStatusBarHeight(this);

        ButterKnife.bind(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("加载中...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        SharedPreferences sp = App.sharedPreferences;
        Config.ThirdSession = sp.getString("third_session", "");

        Config.ThirdSession = "d01fZMGeq1z5ZIJc441rjadHaq75Ajjp8VNg3ikDWNTiUcZjZFdLk+GPQjV+dhBVaUi/9y/xi14Rh46Iti5iqvYZk+xgRsx0g8DHzraoOjZFPA";

        if (!TextUtils.isEmpty(Config.ThirdSession)) {
            totalRequestNum = 4;
            entryActivity = HomeActivity.class;
            IInitDataPresenter initDataPresenter = new InitDataPresenterImpl(this);
            initDataPresenter.getResourceUrl();
            initDataPresenter.getAgeGroups();
            initDataPresenter.getAllCartoon();
            initDataPresenter.getUserInfo();
        } else {
            totalRequestNum = 1;
            entryActivity = ChooseAgeActivity.class;

            IInitDataPresenter initDataPresenter = new InitDataPresenterImpl(this);
            initDataPresenter.getAgeInterestConfig();
        }

        StatService.trackCustomEvent(this, "onCreate", "");
    }

    @OnClick(R.id.btn_reconnect)
    public void onClick(View v) {
        progressDialog.show();

        if (!TextUtils.isEmpty(Config.ThirdSession)) {
            totalRequestNum = 4;
            entryActivity = HomeActivity.class;
            IInitDataPresenter initDataPresenter = new InitDataPresenterImpl(this);
            initDataPresenter.getResourceUrl();
            initDataPresenter.getAgeGroups();
            initDataPresenter.getAllCartoon();
            initDataPresenter.getUserInfo();
        } else {
            totalRequestNum = 1;
            entryActivity = ChooseAgeActivity.class;

            IInitDataPresenter initDataPresenter = new InitDataPresenterImpl(this);
            initDataPresenter.getAgeInterestConfig();
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
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        if (result) {
            requestNum ++;
            if (requestNum == totalRequestNum) {
                Intent intent = new Intent(this, entryActivity);
                UtilBox.startActivity(this, intent, true);
                overridePendingTransition(R.anim.zoom_in_scale, R.anim.zoom_out_scale);
            }
        } else {
            mNoNetworkLinearLayout.setVisibility(View.VISIBLE);

            Toast.makeText(this, info, Toast.LENGTH_SHORT).show();
        }
    }
}