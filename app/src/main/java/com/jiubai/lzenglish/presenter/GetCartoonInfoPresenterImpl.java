package com.jiubai.lzenglish.presenter;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jiubai.lzenglish.bean.Cartoon;
import com.jiubai.lzenglish.bean.DetailedSeason;
import com.jiubai.lzenglish.bean.Season;
import com.jiubai.lzenglish.bean.Video;
import com.jiubai.lzenglish.common.UtilBox;
import com.jiubai.lzenglish.config.Constants;
import com.jiubai.lzenglish.net.RequestUtil;
import com.jiubai.lzenglish.ui.iview.IGetCartoonInfoView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Larry Liang on 20/05/2017.
 */

public class GetCartoonInfoPresenterImpl implements IGetCartoonInfoPresenter {
    private IGetCartoonInfoView iGetCartoonInfoView;

    public GetCartoonInfoPresenterImpl(IGetCartoonInfoView iGetCartoonInfoView) {
        this.iGetCartoonInfoView = iGetCartoonInfoView;
    }

    @Override
    public void getCartoonList(int ageGroupsIndex) {
        Map<String, String> params = new HashMap<>();
        params.put("_url", "cartoon/index");
        params.put("_ajax", "1");
        params.put("age_idx", ageGroupsIndex + 1 + "");

        RequestUtil.request(params,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String result = jsonObject.getString("code");

                            if (Constants.SUCCESS.equals(result)) {
                                JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("result");

                                ArrayList<Cartoon> cartoons = new ArrayList<>();

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject cartoonObject = jsonArray.getJSONObject(i);

                                    Cartoon cartoon = new Cartoon(
                                            cartoonObject.getInt("id"),
                                            cartoonObject.getInt("pid"),
                                            cartoonObject.getInt("sort"),
                                            UtilBox.getStringToTimestamp(cartoonObject.getString("create_time")),
                                            cartoonObject.getInt("id_cartoon_cat"),
                                            cartoonObject.getString("name2"),
                                            cartoonObject.getString("name"),
                                            cartoonObject.getString("age"),
                                            cartoonObject.getString("image"),
                                            cartoonObject.getString("video_pic"),
                                            cartoonObject.getString("brief_info"),
                                            cartoonObject.getString("info"),
                                            cartoonObject.getDouble("price"),
                                            cartoonObject.getInt("ifshow"),
                                            cartoonObject.getString("head_img"),
                                            cartoonObject.getInt("max_free_num"),
                                            cartoonObject.getInt("max_train_num"),
                                            cartoonObject.getString("buy_pic"),
                                            cartoonObject.getString("share_pic"),
                                            cartoonObject.getString("share_title"),
                                            cartoonObject.getString("share_content"),
                                            cartoonObject.getInt("star_rule"),
                                            cartoonObject.getString("promote_image_template"),
                                            cartoonObject.getJSONObject("___id_cartoon_cat"),
                                            cartoonObject.getString("___url"),
                                            cartoonObject.getString("___image")
                                    );

                                    cartoons.add(cartoon);
                                }

                                iGetCartoonInfoView.onGetCartoonListResult(true, "", cartoons);
                            } else {
                                iGetCartoonInfoView.onGetCartoonListResult(false, jsonObject.getString("msg"), response);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        iGetCartoonInfoView.onGetCartoonListResult(false, "获取动画片列表失败", volleyError);
                    }
                });
    }

    @Override
    public void getCartoonSeason(int cartoonId) {
        Map<String, String> params = new HashMap<>();
        params.put("_url", "cartoon/get");
        params.put("_ajax", "1");
        params.put("id", cartoonId + "");

        RequestUtil.request(params,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String result = jsonObject.getString("code");

                            if (Constants.SUCCESS.equals(result)) {
                                JSONArray seasonArray = jsonObject.getJSONObject("data").getJSONArray("results");

                                ArrayList<Season> seasons = new ArrayList<>();

                                for (int i = 0; i < seasonArray.length(); i++) {
                                    JSONObject seasonObject = seasonArray.getJSONObject(i);

                                    Season season = new Season(
                                            seasonObject.getInt("id"),
                                            seasonObject.getInt("pid"),
                                            seasonObject.getInt("sort"),
                                            UtilBox.getStringToTimestamp(seasonObject.getString("create_time")),
                                            seasonObject.getInt("id_cartoon_cat"),
                                            seasonObject.getString("name2"),
                                            seasonObject.getString("name"),
                                            seasonObject.getString("age"),
                                            seasonObject.getString("image"),
                                            seasonObject.getString("video_pic"),
                                            seasonObject.getString("brief_info"),
                                            seasonObject.getString("info"),
                                            seasonObject.getDouble("price"),
                                            seasonObject.getInt("ifshow"),
                                            seasonObject.getString("head_img"),
                                            seasonObject.getInt("max_free_num"),
                                            seasonObject.getInt("max_train_num"),
                                            seasonObject.getString("buy_pic"),
                                            seasonObject.getString("share_pic"),
                                            seasonObject.getString("share_title"),
                                            seasonObject.getString("share_content"),
                                            seasonObject.getInt("star_rule"),
                                            seasonObject.getString("promote_image_template"),
                                            seasonObject.getString("___url"),
                                            seasonObject.getInt("___item_id")
                                    );

                                    seasons.add(season);
                                }

                                JSONObject cartoonObject = jsonObject.getJSONObject("data").getJSONObject("data");

                                Cartoon cartoon = new Cartoon(
                                        cartoonObject.getInt("id"),
                                        cartoonObject.getInt("pid"),
                                        cartoonObject.getInt("sort"),
                                        UtilBox.getStringToTimestamp(cartoonObject.getString("create_time")),
                                        cartoonObject.getInt("id_cartoon_cat"),
                                        cartoonObject.getString("name2"),
                                        cartoonObject.getString("name"),
                                        cartoonObject.getString("age"),
                                        cartoonObject.getString("image"),
                                        cartoonObject.getString("video_pic"),
                                        cartoonObject.getString("brief_info"),
                                        cartoonObject.getString("info"),
                                        cartoonObject.getDouble("price"),
                                        cartoonObject.getInt("ifshow"),
                                        cartoonObject.getString("head_img"),
                                        cartoonObject.getInt("max_free_num"),
                                        cartoonObject.getInt("max_train_num"),
                                        cartoonObject.getString("buy_pic"),
                                        cartoonObject.getString("share_pic"),
                                        cartoonObject.getString("share_title"),
                                        cartoonObject.getString("share_content"),
                                        cartoonObject.getInt("star_rule"),
                                        cartoonObject.getString("promote_image_template"),
                                        new JSONObject(),
                                        "",
                                        ""
                                );

                                cartoon.setSeasonList(seasons);

                                iGetCartoonInfoView.onGetCartoonSeasonListResult(true, "", cartoon);
                            } else {
                                iGetCartoonInfoView.onGetCartoonSeasonListResult(false, jsonObject.getString("msg"), response);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        iGetCartoonInfoView.onGetCartoonSeasonListResult(false, "获取季列表失败", volleyError);
                    }
                });
    }

    @Override
    public void getVideoList(int seasonId) {
        Map<String, String> params = new HashMap<>();
        params.put("_url", "cartoonItem/getV2");
        params.put("_ajax", "1");
        params.put("id", seasonId + "");

        RequestUtil.request(params,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String result = jsonObject.getString("code");

                            if (Constants.SUCCESS.equals(result)) {
                                JSONObject dataObject = jsonObject.getJSONObject("data");

                                List<Video> videos = new ArrayList<>();

                                JSONArray videoArray = dataObject.getJSONArray("items");

                                for (int i = 0; i < videoArray.length(); i++) {
                                    JSONObject videoObject = videoArray.getJSONObject(i);

                                    if (i == 0) {
                                        Log.i("text", videoObject.toString());
                                    }

                                    Video video = new Video(
                                            videoObject.getInt("id"),
                                            UtilBox.getStringToTimestamp(videoObject.getString("create_time")),
                                            videoObject.getInt("id_cartoon"),
                                            videoObject.getString("name"),
                                            videoObject.getString("video"),
                                            videoObject.getInt("sort"),
                                            videoObject.getInt("count_time"),
                                            videoObject.getInt("ifshow"),
                                            dataObject.getJSONObject("cartoon_info").getString("video_image"),
                                            videoObject.getString("share_pic"),
                                            videoObject.getString("share_title"),
                                            videoObject.getString("share_content"),
                                            videoObject.getInt("star_rule"),
                                            videoObject.getString("note"),
                                            videoObject.getInt("___log_cartoon_item_score"),
                                            videoObject.getBoolean("___allow_watch"),
                                            videoObject.getBoolean("___has_finish_watch"),
                                            videoObject.getBoolean("___has_review"),
                                            videoObject.getBoolean("___allow_review"),
                                            videoObject.toString().contains("\"___has_watch\":false") ? null : new JSONObject()
                                    );

                                    videos.add(video);
                                }

                                DetailedSeason detailedSeason = new DetailedSeason(
                                        dataObject.getJSONObject("set_cartoon_info").getString("name"),
                                        dataObject.getJSONObject("set_cartoon_info").getString("brief_info"),
                                        dataObject.getJSONObject("data").getString("note"),
                                        !dataObject.toString().contains("not_allow_train"),
                                        videos
                                );

                                iGetCartoonInfoView.onGetVideoListResult(true, "", detailedSeason);
                            } else {
                                iGetCartoonInfoView.onGetVideoListResult(false, jsonObject.getString("msg"), response);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        iGetCartoonInfoView.onGetVideoListResult(false, "获取视频列表失败", volleyError);
                    }
                });
    }
}
