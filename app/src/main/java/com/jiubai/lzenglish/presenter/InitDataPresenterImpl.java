package com.jiubai.lzenglish.presenter;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jiubai.lzenglish.bean.Cartoon;
import com.jiubai.lzenglish.config.Config;
import com.jiubai.lzenglish.config.Constants;
import com.jiubai.lzenglish.config.Urls;
import com.jiubai.lzenglish.manager.RequestCacheManager;
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
        final Map<String, String> params = new HashMap<>();
        params.put("_url", "api/getSrcPath");
        params.put("_ajax", "1");

        RequestUtil.request(params,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            RequestCacheManager.getInstance().insertRequestCache(Urls.SERVER_URL, params, response);

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
                            iInitDataView.onGetResourceUrlResult(false, "图像资源地址数据源出错", e);
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
        final Map<String, String> params = new HashMap<>();
        params.put("_url", "cartoon/getAgeList");
        params.put("_ajax", "1");

        RequestUtil.request(params,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            RequestCacheManager.getInstance().insertRequestCache(Urls.SERVER_URL, params, response);

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
                            iInitDataView.onGetAgeGroupsResult(false, "年龄段数据源出错", e);
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
        final Map<String, String> params = new HashMap<>();
        params.put("_url", "cartoon/getAll");
        params.put("_ajax", "1");

        RequestUtil.request(params,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            RequestCacheManager.getInstance().insertRequestCache(Urls.SERVER_URL, params, response);

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
                            iInitDataView.onGetAllCartoonResult(false, "动画片列表数据源出错", e);
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

    @Override
    public void getUserInfo() {
        final Map<String, String> params = new HashMap<>();
        params.put("_url", "member/getUserInfo");
        params.put("_ajax", "1");

        RequestUtil.request(params,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            RequestCacheManager.getInstance().insertRequestCache(Urls.SERVER_URL, params, response);

                            JSONObject jsonObject = new JSONObject(response);

                            String result = jsonObject.getString("code");

                            if (Constants.SUCCESS.equals(result)) {
                                JSONObject infoObject = jsonObject.getJSONObject("data").getJSONObject("_weixin_info_");
                                Config.UserName = infoObject.getString("nickname");
                                Config.UserImage = infoObject.getString("headimgurl");

                                iInitDataView.onGetUserInfoResult(true, "", response);
                            } else {
                                iInitDataView.onGetUserInfoResult(false, jsonObject.getString("msg"), response);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            iInitDataView.onGetUserInfoResult(false, "用户信息数据源出错", e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        iInitDataView.onGetUserInfoResult(false, "获取用户信息失败", error);
                    }
                });
    }

    @Override
    public void getAgeInterestConfig() {
        final Map<String, String> params = new HashMap<>();
        params.put("_url", "appSuggest/getAgeInterestConfig");
        params.put("_ajax", "1");

        Map<String, String> postParams = new HashMap<>();
        postParams.put("no_check_user", "1");

        if (!Config.IS_CONNECTED) {
            String res = "{" +
                    "\"code\": 200,\n" +
                    "\"data\": {\n" +
                    "\"age\": {\n" +
                    "\"1\": \"4岁以下\",\n" +
                    "\"2\": \"5-9岁\",\n" +
                    "\"3\": \"10岁以上\",\n" +
                    "\"4\": \"其它\"\n" +
                    "},\n" +
                    "\"interest\": {\n" +
                    "\"1\": \"字母积木\",\n" +
                    "\"2\": \"数字虫\",\n" +
                    "\"3\": \"英文儿歌\",\n" +
                    "\"4\": \"单词世界\",\n" +
                    "\"5\": \"疯狂动物城\",\n" +
                    "\"6\": \"小猪佩奇\",\n" +
                    "\"7\": \"功夫熊猫\",\n" +
                    "\"8\": \"托福\",\n" +
                    "\"9\": \"美国之音\"\n" +
                    "}\n" +
                    "}\n" +
                    "}";

            RequestCacheManager.getInstance().insertRequestCache(Urls.SERVER_URL, params, res);
        }

        RequestUtil.request(params, postParams,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            RequestCacheManager.getInstance().insertRequestCache(Urls.SERVER_URL, params, response);

                            JSONObject jsonObject = new JSONObject(response);

                            String result = jsonObject.getString("code");

                            if (Constants.SUCCESS.equals(result)) {
                                JSONObject dataObject = jsonObject.getJSONObject("data");

                                JSONObject ageObject = dataObject.getJSONObject("age");
                                JSONObject preferenceObject = dataObject.getJSONObject("interest");

                                Config.Ages = new String[ageObject.length()];
                                for (int i = 0; i < ageObject.length(); i++) {
                                    Config.Ages[i] = ageObject.getString(i + 1 + "");
                                }

                                Config.PreferenceVideos = new String[preferenceObject.length()];
                                for (int i = 0; i < preferenceObject.length(); i++) {
                                    StringBuilder name = new StringBuilder(preferenceObject.getString(i + 1 + ""));

                                    if (name.length() == 4 || name.length() == 5) {
                                        name.insert(2, "\n");
                                    } else if (name.length() == 6) {
                                        name.insert(3, "\n");
                                    }

                                    Config.PreferenceVideos[i] = name.toString();
                                }

                                iInitDataView.onGetAgeInterestConfigResult(true, "", response);
                            } else {
                                iInitDataView.onGetAgeInterestConfigResult(false, jsonObject.getString("msg"), response);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            iInitDataView.onGetAgeInterestConfigResult(false, "年龄兴趣信息数据源出错", e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        iInitDataView.onGetAgeInterestConfigResult(false, "获取年龄兴趣信息失败", error);
                    }
                });
    }
}
