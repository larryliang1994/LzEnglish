package com.jiubai.lzenglish.wxapi;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jiubai.lzenglish.App;
import com.jiubai.lzenglish.R;
import com.jiubai.lzenglish.common.StatusBarUtil;
import com.jiubai.lzenglish.common.UtilBox;
import com.jiubai.lzenglish.config.Constants;
import com.jiubai.lzenglish.net.RequestUtil;
import com.jiubai.lzenglish.presenter.LoginPresenterImpl;
import com.jiubai.lzenglish.ui.activity.HomeActivity;
import com.jiubai.lzenglish.ui.iview.ILoginView;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

import java.util.HashMap;
import java.util.Map;
//import com.tencent.mm.opensdk.modelbase.BaseReq;
//import com.tencent.mm.opensdk.modelbase.BaseResp;
//import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

/**
 * Created by Larry Liang on 15/05/2017.
 */

public class WXEntryActivity extends Activity implements IWXAPIEventHandler, ILoginView {

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);

        StatusBarUtil.StatusBarLightMode(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("加载中...");

        App.iwxapi.handleIntent(getIntent(), this);
    }

    @Override
    public void onReq(BaseReq req) {
        switch (req.getType()) {
            case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
                Log.i("text", "COMMAND_GETMESSAGE_FROM_WX");
                break;
            case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
                Log.i("text", "COMMAND_SHOWMESSAGE_FROM_WX");
                break;
            default:
                Log.i("text", "default");
                break;
        }
    }

    @Override
    public void onResp(BaseResp resp) {
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                Log.i("text", "ERR_OK");
                if (progressDialog != null) {
                    //progressDialog.show();
                }
                new LoginPresenterImpl(this).login(resp);

                break;

            default:
                Toast.makeText(this, "取消登录", Toast.LENGTH_SHORT).show();
                UtilBox.returnActivity(this);
                overridePendingTransition(0, 0);
                break;
        }
    }

    @Override
    public void onLoginResult(boolean result, String info, Object extras) {
        if (progressDialog != null) {
            //progressDialog.dismiss();
        }

        if (result) {

        } else {
            Toast.makeText(this, info, Toast.LENGTH_SHORT).show();
        }

        UtilBox.returnActivity(this);
        overridePendingTransition(0, 0);
    }
}
