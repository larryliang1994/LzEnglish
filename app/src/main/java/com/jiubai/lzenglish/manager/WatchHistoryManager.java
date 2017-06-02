package com.jiubai.lzenglish.manager;

import android.content.SharedPreferences;
import android.util.Log;

import com.jiubai.lzenglish.App;
import com.jiubai.lzenglish.bean.WatchHistory;
import com.jiubai.lzenglish.common.UtilBox;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Leunghowell on 2017/5/29.
 */

public class WatchHistoryManager {
    public ArrayList<WatchHistory> watchHistoryList;

    private static WatchHistoryManager instance = null;

    public static WatchHistoryManager getInstance() {
        if (instance == null) {
            instance = new WatchHistoryManager();
            instance.watchHistoryList = new ArrayList<>();
        }

        return instance;
    }

    private WatchHistoryManager() {
    }

    public void saveHistory(WatchHistory watchHistory) {
        for (int i = 0; i < watchHistoryList.size(); i++) {
            WatchHistory history = watchHistoryList.get(i);
            if (history.getVideoId() == watchHistory.getVideoId()) {
                watchHistoryList.remove(i);
                watchHistoryList.add(0, watchHistory);
                writeHistory();
                return;
            }
        }

        watchHistoryList.add(0, watchHistory);

        writeHistory();
    }

    public void writeHistory() {
        SharedPreferences.Editor editor = App.sharedPreferences.edit();

        JSONArray jsonArray = new JSONArray();

        try {
            for (WatchHistory history : watchHistoryList) {
                if (history.getVideoId() == -100
                        || history.getVideoId() == -101
                        || history.getVideoId() == -102) {
                    break;
                }

                JSONObject jsonObject = new JSONObject();

                jsonObject.put("videoId", history.getVideoId());
                jsonObject.put("name", history.getName());
                jsonObject.put("totalTime", history.getTotalTime() + "");
                jsonObject.put("watchedTime", history.getWatchedTime() + "");
                jsonObject.put("nextVideoId", history.getNextVideoId());
                jsonObject.put("time", history.getTime() + "");
                jsonObject.put("image", history.getImage());

                jsonArray.put(jsonObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        editor.putString("watchHistory", jsonArray.toString());
        editor.apply();
    }

    public void readHistory() {
        watchHistoryList = new ArrayList<>();

        SharedPreferences sp = App.sharedPreferences;

        try {
            JSONArray jsonArray = new JSONArray(sp.getString("watchHistory", "[]"));

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                WatchHistory watchHistory = new WatchHistory(
                        jsonObject.getInt("videoId"),
                        jsonObject.getString("name"),
                        Long.valueOf(jsonObject.getString("totalTime")),
                        Long.valueOf(jsonObject.getString("watchedTime")),
                        jsonObject.getInt("nextVideoId"),
                        Long.valueOf(jsonObject.getString("time")),
                        jsonObject.getString("image")
                );

                watchHistoryList.add(watchHistory);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void clearHistory() {
        watchHistoryList = new ArrayList<>();

        SharedPreferences.Editor editor = App.sharedPreferences.edit();
        editor.putString("watchHistory", "");
        editor.apply();
    }

    public void initTimeHeader() {
        WatchHistory today = new WatchHistory();
        WatchHistory yesterday = new WatchHistory();
        WatchHistory before = new WatchHistory();

        today.setVideoId(-100);
        yesterday.setVideoId(-101);
        before.setVideoId(-102);

        // 先把原来的去掉
        int index = 0;
        while (index < watchHistoryList.size()) {
            if (watchHistoryList.get(index).getVideoId() == -100
                    || watchHistoryList.get(index).getVideoId() == -101
                    || watchHistoryList.get(index).getVideoId() == -102) {
                watchHistoryList.remove(index);
            } else {
                index ++;
            }
        }

        for (int i = 0; i < watchHistoryList.size(); i++) {
            if (watchHistoryList.get(i).getVideoId() == -100
                    || watchHistoryList.get(i).getVideoId() == -101
                    || watchHistoryList.get(i).getVideoId() == -102) {
                continue;
            }

            if (UtilBox.getDataTodayYesterday(watchHistoryList.get(i).getTime()) == 1) {
                watchHistoryList.add(i, today);
                Log.i("text", "add today");
                break;
            }
        }

        for (int i = 0; i < watchHistoryList.size(); i++) {
            if (watchHistoryList.get(i).getVideoId() == -100
                    || watchHistoryList.get(i).getVideoId() == -101
                    || watchHistoryList.get(i).getVideoId() == -102) {
                continue;
            }

            if (UtilBox.getDataTodayYesterday(watchHistoryList.get(i).getTime()) == 2) {
                watchHistoryList.add(i, yesterday);
                Log.i("text", "add yesterday");
                break;
            }
        }

        for (int i = 0; i < watchHistoryList.size(); i++) {
            if (watchHistoryList.get(i).getVideoId() == -100
                    || watchHistoryList.get(i).getVideoId() == -101
                    || watchHistoryList.get(i).getVideoId() == -102) {
                continue;
            }

            if (UtilBox.getDataTodayYesterday(watchHistoryList.get(i).getTime()) == -1) {
                watchHistoryList.add(i, before);
                Log.i("text", "add before");
                break;
            }
        }
    }
}
