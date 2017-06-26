package com.jiubai.lzenglish.presenter;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jiubai.lzenglish.common.UtilBox;
import com.jiubai.lzenglish.config.Urls;
import com.jiubai.lzenglish.net.RequestUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Leunghowell on 2017/6/19.
 */

public class LoggerPresenterImpl implements ILoggerPresenter {
    @Override
    public void writeLog(Context context, String activityName, String content) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("date", UtilBox.getTimestampToString(UtilBox.getCurrentTime(), UtilBox.DATE_TIME_SECOND_MILL));
            jsonObject.put("device", Build.VERSION.SDK_INT + "_"
                    + UtilBox.getPackageInfo(context).versionName + "_"
                    + android.os.Build.MODEL);
            jsonObject.put("activityName", activityName);
            jsonObject.put("content", content);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Map<String, String> postParams = new HashMap<>();
        postParams.put("data", jsonObject.toString());

        RequestUtil.request(Urls.SERVER_URL + "/app_log.php", new HashMap<String, String>(), postParams,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("text", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
    }
}
