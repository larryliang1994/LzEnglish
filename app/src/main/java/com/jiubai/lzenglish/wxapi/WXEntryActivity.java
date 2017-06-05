package com.jiubai.lzenglish.wxapi;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
//import com.tencent.mm.opensdk.modelbase.BaseReq;
//import com.tencent.mm.opensdk.modelbase.BaseResp;
//import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

/**
 * Created by Larry Liang on 15/05/2017.
 */

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

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
        //((App)getApplication()).api.handleIntent(getIntent(), this);
    }
}
