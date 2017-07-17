package com.jiubai.lzenglish.manager;

import android.content.SharedPreferences;
import android.text.TextUtils;

import com.jiubai.lzenglish.App;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by larry on 11/07/2017.
 */

public class RequestCacheManager {
    public static HashMap<String, String> requestCache;

    private static RequestCacheManager instance = null;

    private RequestCacheManager() {
        requestCache = new HashMap<>();
    }

    public static RequestCacheManager getInstance() {
        if (instance == null) {
            instance = new RequestCacheManager();
        }

        return instance;
    }

    public void insertRequestCache(String url, String response) {
        requestCache.put(url, response);

        try {
            writeRequestCache();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void insertRequestCache(String serverUrl, Map<String, String> params, String response) {
        String url = serverUrl + "?";
        for (String key : params.keySet()) {
            url += key + "=" + params.get(key) + "&";
        }

        url += "app=android";

        requestCache.put(url, response);

        try {
            writeRequestCache();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void readRequestCache() {
        requestCache = new HashMap<>();

        SharedPreferences sharedPreferences = App.sharedPreferences;

        try {
            String cacheString = sharedPreferences.getString("requestCache", "");

            if (TextUtils.isEmpty(cacheString)) {
                return;
            }

            JSONArray jsonArray = new JSONArray(cacheString);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                requestCache.put(jsonObject.getString("key"), jsonObject.getString("value"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void writeRequestCache() throws JSONException {
        SharedPreferences.Editor editor = App.sharedPreferences.edit();

        JSONArray jsonArray = new JSONArray();

        for (String key : requestCache.keySet()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("key", key);
            jsonObject.put("value", requestCache.get(key));

            jsonArray.put(jsonObject);
        }

        editor.putString("requestCache", jsonArray.toString());

        editor.apply();
    }
}
