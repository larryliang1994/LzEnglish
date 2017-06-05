package com.jiubai.lzenglish;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.jiubai.lzenglish.common.StatusBarUtil;
import com.jiubai.lzenglish.common.UtilBox;
import com.jiubai.lzenglish.config.Config;
import com.jiubai.lzenglish.config.Constants;
import com.jiubai.lzenglish.presenter.IInitDataPresenter;
import com.jiubai.lzenglish.presenter.InitDataPresenterImpl;
import com.jiubai.lzenglish.ui.activity.BaseActivity;
import com.jiubai.lzenglish.ui.activity.ChooseAgeActivity;
import com.jiubai.lzenglish.ui.activity.HomeActivity;
import com.jiubai.lzenglish.ui.iview.IInitDataView;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import butterknife.ButterKnife;

public class EntryActivity extends BaseActivity implements IInitDataView {

    private int requestNum = 0;
    private final int totalRequestNum = 5;

    private Class entryActivity = ChooseAgeActivity.class;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_entry);

        Config.DeviceType = StatusBarUtil.StatusBarLightMode(this);
        Config.AppbarHeight = (int) (getResources().getDimension(R.dimen.appbar_height));
        Config.StatusbarHeight = UtilBox.getStatusBarHeight(this);

        ButterKnife.bind(this);

        IInitDataPresenter initDataPresenter = new InitDataPresenterImpl(this);
        initDataPresenter.getResourceUrl();
        initDataPresenter.getAgeGroups();
        initDataPresenter.getAllCartoon();
        initDataPresenter.getUserInfo();
        initDataPresenter.getAgeInterestConfig();
    }

    @Override
    public void onGetResourceUrlResult(boolean result, String info, Object extras) {
        if (result) {
            requestNum ++;
            if (requestNum == totalRequestNum) {
                Intent intent = new Intent(this, entryActivity);
                UtilBox.startActivity(this, intent, true);
                overridePendingTransition(R.anim.zoom_in_scale, R.anim.zoom_out_scale);
            }
        } else {
            Toast.makeText(this, info, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onGetAgeGroupsResult(boolean result, String info, Object extras) {
        if (result) {
            requestNum ++;
            if (requestNum == totalRequestNum) {
                Intent intent = new Intent(this, entryActivity);
                UtilBox.startActivity(this, intent, true);
                overridePendingTransition(R.anim.zoom_in_scale, R.anim.zoom_out_scale);
            }
        } else {
            Toast.makeText(this, info, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onGetAllCartoonResult(boolean result, String info, Object extras) {
        if (result) {
            requestNum ++;
            if (requestNum == totalRequestNum) {
                Intent intent = new Intent(this, entryActivity);
                UtilBox.startActivity(this, intent, true);
                overridePendingTransition(R.anim.zoom_in_scale, R.anim.zoom_out_scale);
            }
        } else {
            Toast.makeText(this, info, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onGetUserInfoResult(boolean result, String info, Object extras) {
        if (result) {
            requestNum ++;
            if (requestNum == totalRequestNum) {
                Intent intent = new Intent(this, entryActivity);
                UtilBox.startActivity(this, intent, true);
                overridePendingTransition(R.anim.zoom_in_scale, R.anim.zoom_out_scale);
            }
        } else {
            Toast.makeText(this, info, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onGetAgeInterestConfigResult(boolean result, String info, Object extras) {
        if (result) {
            requestNum ++;
            if (requestNum == totalRequestNum) {
                Intent intent = new Intent(this, entryActivity);
                UtilBox.startActivity(this, intent, true);
                overridePendingTransition(R.anim.zoom_in_scale, R.anim.zoom_out_scale);
            }
        } else {
            Toast.makeText(this, info, Toast.LENGTH_SHORT).show();
        }
    }
}