package com.jiubai.lzenglish.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


/**
 * 用于监听网络状态
 */
public class NetworkStateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)){
            // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
            try {
                ConnectivityManager connectivity = (ConnectivityManager) context
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                if (connectivity != null) {
                    // 获取网络连接管理的对象
                    NetworkInfo info = connectivity.getActiveNetworkInfo();
                    if (info != null&& info.isConnected()) {
                        // 判断当前网络是否已经连接
                        if (info.getState() == NetworkInfo.State.CONNECTED) {
                            //Config.IS_CONNECTED = true;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //Config.IS_CONNECTED = false;
        }
    }
}