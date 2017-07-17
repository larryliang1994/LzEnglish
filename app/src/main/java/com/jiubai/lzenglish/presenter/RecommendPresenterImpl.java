package com.jiubai.lzenglish.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jiubai.lzenglish.bean.AgeRecommend;
import com.jiubai.lzenglish.bean.InterestRecommend;
import com.jiubai.lzenglish.config.Config;
import com.jiubai.lzenglish.config.Constants;
import com.jiubai.lzenglish.config.Urls;
import com.jiubai.lzenglish.manager.RequestCacheManager;
import com.jiubai.lzenglish.net.RequestUtil;
import com.jiubai.lzenglish.ui.iview.IRecommendView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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
            final Map<String, String> params = new HashMap<>();
            params.put("_url", "appSuggest/suggestData");
            params.put("_ajax", "1");

            RequestUtil.request(params,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("text", response);

                            try {
                                RequestCacheManager.getInstance().insertRequestCache(Urls.SERVER_URL, params, response);

                                JSONObject jsonObject = new JSONObject(response);

                                String result = jsonObject.getString("code");

                                if (Constants.SUCCESS.equals(result)) {
                                    decodeRecommendInfo(jsonObject.getJSONObject("data"));
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
            final Map<String, String> params = new HashMap<>();
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
                                RequestCacheManager.getInstance().insertRequestCache(Urls.SERVER_URL, params, response);

                                JSONObject jsonObject = new JSONObject(response);

                                String result = jsonObject.getString("code");

                                if (Constants.SUCCESS.equals(result)) {
                                    decodeRecommendInfo(jsonObject.getJSONObject("data"));
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

    private void decodeRecommendInfo(JSONObject dataObject) throws JSONException {
        JSONArray ageArray = dataObject.getJSONArray("age");
        ArrayList<AgeRecommend> ageRecommends = new ArrayList<>();

        for (int i = 0; i < ageArray.length(); i++) {
            JSONObject ageObject = ageArray.getJSONObject(i);

            AgeRecommend ageRecommend = new AgeRecommend(
                    ageObject.getInt("id_cartoon"),
                    ageObject.getString("image"),
                    ageObject.getString("main_title"),
                    ageObject.getString("sub_title"),
                    ageObject.getString("footer_text")
            );

            ageRecommends.add(ageRecommend);
        }

        JSONArray interestArray = dataObject.getJSONArray("interest");
        ArrayList<InterestRecommend> interestRecommends = new ArrayList<>();

        for (int i = 0; i < interestArray.length(); i++) {
            JSONObject interestObject = interestArray.getJSONObject(i);

            InterestRecommend interestRecommend = new InterestRecommend(
                    interestObject.getInt("id_cartoon"),
                    interestObject.getString("image"),
                    interestObject.getString("title"),
                    interestObject.getString("text")
            );

            interestRecommends.add(interestRecommend);
        }

        Object[] objects = {ageRecommends, interestRecommends};

        iRecommendView.OnGetHomeInfoResult(true, "", objects);
    }
}
