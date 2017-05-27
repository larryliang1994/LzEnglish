package com.jiubai.lzenglish.presenter;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jiubai.lzenglish.bean.Cartoon;
import com.jiubai.lzenglish.config.Config;
import com.jiubai.lzenglish.config.Constants;
import com.jiubai.lzenglish.net.RequestUtil;
import com.jiubai.lzenglish.ui.iview.IInitDataView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Larry Liang on 20/05/2017.
 */

public class InitDataPresenterImpl implements IInitDataPresenter {
    private IInitDataView iInitDataView;

    public InitDataPresenterImpl(IInitDataView iInitDataView) {
        this.iInitDataView = iInitDataView;
    }

    @Override
    public void getResourceUrl() {
        Map<String, String> params = new HashMap<>();
        params.put("_url", "api/getSrcPath");
        params.put("_ajax", "1");

        RequestUtil.request(params,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String result = jsonObject.getString("code");

                            if (Constants.SUCCESS.equals(result)) {
                                Config.ResourceUrl = jsonObject.getString("data") + "/";

                                iInitDataView.onGetResourceUrlResult(true, "", response);
                            } else {
                                iInitDataView.onGetResourceUrlResult(false, jsonObject.getString("msg"), response);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        iInitDataView.onGetResourceUrlResult(false, "获取图像资源地址失败", volleyError);
                    }
                });
    }

    @Override
    public void getAgeGroups() {
        Map<String, String> params = new HashMap<>();
        params.put("_url", "cartoon/getAgeList");
        params.put("_ajax", "1");

        RequestUtil.request(params,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String result = jsonObject.getString("code");

                            if (Constants.SUCCESS.equals(result)) {
                                JSONObject ageGroupsObject = jsonObject.getJSONObject("data");

                                Config.AgeGroups = new String[ageGroupsObject.length()];
                                for (int i = 0; i < ageGroupsObject.length(); i++) {
                                    Config.AgeGroups[i] = ageGroupsObject.getString(i + 1 + "");
                                }

                                iInitDataView.onGetAgeGroupsResult(true, "", response);
                            } else {
                                iInitDataView.onGetAgeGroupsResult(false, jsonObject.getString("msg"), response);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        iInitDataView.onGetAgeGroupsResult(false, "获取年龄段失败", volleyError);
                    }
                });
    }

    @Override
    public void getAllCartoon() {
        Map<String, String> params = new HashMap<>();
        params.put("_url", "cartoon/getAll");
        params.put("_ajax", "1");

        RequestUtil.request(params,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String result = jsonObject.getString("code");

                            if (Constants.SUCCESS.equals(result)) {
                                JSONArray cartoonArray = jsonObject.getJSONArray("data");

                                Config.CartoonList = new ArrayList<>();

                                for (int i = 0; i < cartoonArray.length(); i++) {
                                    JSONObject cartoonObject = cartoonArray.getJSONObject(i);

                                    Cartoon cartoon = new Cartoon();
                                    cartoon.setId(cartoonObject.getInt("id"));
                                    cartoon.setName(cartoonObject.getString("name"));

                                    Config.CartoonList.add(cartoon);
                                }

                                iInitDataView.onGetAllCartoonResult(true, "", "");
                            } else {
                                iInitDataView.onGetAllCartoonResult(false, jsonObject.getString("msg"), response);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        iInitDataView.onGetAllCartoonResult(false, "获取动画片列表失败", error);
                    }
                });
    }
}
