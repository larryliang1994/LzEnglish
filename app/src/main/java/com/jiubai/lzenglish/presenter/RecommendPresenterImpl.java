package com.jiubai.lzenglish.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jiubai.lzenglish.config.Config;
import com.jiubai.lzenglish.config.Constants;
import com.jiubai.lzenglish.net.RequestUtil;
import com.jiubai.lzenglish.ui.iview.IRecommendView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Larry Liang on 02/06/2017.
 */

public class RecommendPresenterImpl implements IRecommendPresenter {
    private IRecommendView iRecommendView;

    public RecommendPresenterImpl(IRecommendView iRecommendView) {
        this.iRecommendView = iRecommendView;
    }

    @Override
    public void getHomeInfo() {
        if (TextUtils.isEmpty(Config.AgeIndex) || TextUtils.isEmpty(Config.PreferenceVideoIndex)) {
            Map<String, String> params = new HashMap<>();
            params.put("_url", "appSuggest/suggestData");
            params.put("_ajax", "1");

            RequestUtil.request(params,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("text", response);

                            try {
                                JSONObject jsonObject = new JSONObject(response);

                                String result = jsonObject.getString("code");

                                if (Constants.SUCCESS.equals(result)) {
                                    iRecommendView.OnGetHomeInfoResult(true, "", response);
                                } else {
                                    iRecommendView.OnGetHomeInfoResult(false, jsonObject.getString("msg"), response);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                iRecommendView.OnGetHomeInfoResult(false, "推荐页数据源出错", e);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            iRecommendView.OnGetHomeInfoResult(false, "获取推荐页数据失败", error);
                        }
                    });
        } else {
            Map<String, String> params = new HashMap<>();
            params.put("_url", "appSuggest/saveMemberChose");
            params.put("_ajax", "1");

            Map<String, String> postParams = new HashMap<>();
            postParams.put("age", Config.AgeIndex);
            postParams.put("interest", Config.PreferenceVideoIndex);

            RequestUtil.request(params, postParams,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("text", response);

                            try {
                                JSONObject jsonObject = new JSONObject(response);

                                String result = jsonObject.getString("code");

                                if (Constants.SUCCESS.equals(result)) {
                                    iRecommendView.OnGetHomeInfoResult(true, "", response);
                                } else {
                                    iRecommendView.OnGetHomeInfoResult(false, jsonObject.getString("msg"), response);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                iRecommendView.OnGetHomeInfoResult(false, "推荐页数据源出错", e);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            iRecommendView.OnGetHomeInfoResult(false, "获取推荐页数据失败", error);
                        }
                    });
        }
    }
}
