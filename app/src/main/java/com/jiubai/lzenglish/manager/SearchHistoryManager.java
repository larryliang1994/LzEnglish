package com.jiubai.lzenglish.manager;

import android.content.SharedPreferences;

import com.jiubai.lzenglish.App;
import com.jiubai.lzenglish.bean.SearchHistory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Leunghowell on 2017/5/28.
 */

public class SearchHistoryManager {
    public ArrayList<SearchHistory> searchHistoryList;

    private static SearchHistoryManager instance = null;

    public static SearchHistoryManager getInstance() {
        if (instance == null) {
            instance = new SearchHistoryManager();
            instance.searchHistoryList = new ArrayList<>();
        }

        return instance;
    }

    private SearchHistoryManager() {
    }

    public void saveHistory(SearchHistory searchHistory) {
        for (SearchHistory history : searchHistoryList) {
            if (history.getCartoonId() == searchHistory.getCartoonId()) {
                searchHistoryList.add(0, searchHistoryList.get(searchHistoryList.size() - 1));
                searchHistoryList.remove(searchHistoryList.size() - 1);
                writeHistory();
                return;
            }
        }

        searchHistoryList.add(0, searchHistory);

        if (searchHistoryList.size() > 8) {
            searchHistoryList.remove(8);
        }

        writeHistory();
    }

    public void writeHistory() {
        SharedPreferences.Editor editor = App.sharedPreferences.edit();
        JSONArray jsonArray = new JSONArray();

        try {
            for (SearchHistory history : searchHistoryList) {
                JSONObject jsonObject = new JSONObject();

                jsonObject.put("id", history.getCartoonId());
                jsonObject.put("name", history.getName());

                jsonArray.put(jsonObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        editor.putString("searchHistory", jsonArray.toString());
        editor.apply();
    }

    public void readHistory() {
        searchHistoryList = new ArrayList<>();

        SharedPreferences sp = App.sharedPreferences;

        try {
            JSONArray jsonArray = new JSONArray(sp.getString("searchHistory", "[]"));

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                SearchHistory searchHistory = new SearchHistory(
                        jsonObject.getInt("id"),
                        jsonObject.getString("name")
                );

                searchHistoryList.add(searchHistory);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void clearHistory() {
        searchHistoryList = new ArrayList<>();

        SharedPreferences.Editor editor = App.sharedPreferences.edit();
        editor.putString("searchHistory", "");
        editor.apply();
    }
}
