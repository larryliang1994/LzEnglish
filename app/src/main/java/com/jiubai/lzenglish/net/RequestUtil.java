package com.jiubai.lzenglish.net;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jiubai.lzenglish.config.Config;
import com.jiubai.lzenglish.config.Constants;
import com.jiubai.lzenglish.config.Urls;
import com.jiubai.lzenglish.manager.RequestCacheManager;

import java.util.HashMap;
import java.util.Map;


/**
 * Volley框架
 */
public class RequestUtil {
    public static RequestQueue requestQueue = null;

    public static void initRequestQueue(Context context) {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
        }
    }

    public static void request(final Map<String, String> params,
                               Response.Listener<String> successCallback,
                               Response.ErrorListener errorCallback) {

        String url = Urls.SERVER_URL + "?";
        for (String key : params.keySet()) {
            url += key + "=" + params.get(key) + "&";
        }

        url += "app=android";

        Log.i("url", url);

        // 无网络时，显示缓存数据
        if (!Config.IS_CONNECTED) {
            if (RequestCacheManager.requestCache.containsKey(url)) {
                successCallback.onResponse(RequestCacheManager.requestCache.get(url));
                Log.i(Constants.TAG, "no network");
                return;
            }
        }

        // 构建Post请求对象
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                successCallback, errorCallback) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                if (!TextUtils.isEmpty(Config.ThirdSession)) {
                    params.put("third_session", Config.ThirdSession);
                }

                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.REQUEST_TIMEOUT, 1, 1.0f));

        // 加入请求队列
        requestQueue.add(stringRequest);
    }

    public static void request(final Map<String, String> params, final Map<String, String> postParams,
                               Response.Listener<String> successCallback,
                               Response.ErrorListener errorCallback) {

        String url = Urls.SERVER_URL + "?";
        for (String key : params.keySet()) {
            url += key + "=" + params.get(key) + "&";
        }

        url += "app=android";

        Log.i("url", url);

        // 无网络时，显示缓存数据
        if (!Config.IS_CONNECTED) {
            if (RequestCacheManager.requestCache.containsKey(url)) {
                successCallback.onResponse(RequestCacheManager.requestCache.get(url));
                Log.i(Constants.TAG, "no network");
                return;
            }
        }

        // 构建Post请求对象
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                successCallback, errorCallback) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                if (!TextUtils.isEmpty(Config.ThirdSession)) {
                    params.put("third_session", Config.ThirdSession);
                }

                for (String key : postParams.keySet()) {
                    params.put(key, postParams.get(key));
                }

                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.REQUEST_TIMEOUT, 1, 1.0f));

        // 加入请求队列
        requestQueue.add(stringRequest);
    }

    public static void request(final String serverUrl, final Map<String, String> params, final Map<String, String> postParams,
                               Response.Listener<String> successCallback,
                               Response.ErrorListener errorCallback) {
        String url = serverUrl + "?";
        for (String key : params.keySet()) {
            url += key + "=" + params.get(key) + "&";
        }

        url += "app=android";

        Log.i("url", url);

        // 无网络时，显示缓存数据
        if (!Config.IS_CONNECTED) {
            if (RequestCacheManager.requestCache.containsKey(url)) {
                successCallback.onResponse(RequestCacheManager.requestCache.get(url));
                Log.i(Constants.TAG, "no network");
                return;
            }
        }

        // 构建Post请求对象
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                successCallback, errorCallback) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                if (!TextUtils.isEmpty(Config.ThirdSession)) {
                    params.put("third_session", Config.ThirdSession);
                }

                for (String key : postParams.keySet()) {
                    params.put(key, postParams.get(key));
                }

                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.REQUEST_TIMEOUT, 1, 1.0f));

        // 加入请求队列
        requestQueue.add(stringRequest);
    }

    public static void getRequest(final Map<String, String> params,
                               Response.Listener<String> successCallback,
                               Response.ErrorListener errorCallback) {

        String url = Urls.SERVER_URL + "?";
        for (String key : params.keySet()) {
            url += key + "=" + params.get(key) + "&";
        }

        url += "&app=android";

        Log.i("url", url);

        // 无网络时，显示缓存数据
        if (!Config.IS_CONNECTED) {
            if (RequestCacheManager.requestCache.containsKey(url)) {
                successCallback.onResponse(RequestCacheManager.requestCache.get(url));
                Log.i(Constants.TAG, "no network");
                return;
            }
        }

        // 构建Post请求对象
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                successCallback, errorCallback);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.REQUEST_TIMEOUT, 1, 1.0f));

        // 加入请求队列
        requestQueue.add(stringRequest);
    }
}