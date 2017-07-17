package com.jiubai.lzenglish.presenter;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jiubai.lzenglish.config.Config;
import com.jiubai.lzenglish.config.Constants;
import com.jiubai.lzenglish.net.RequestUtil;
import com.jiubai.lzenglish.ui.iview.ILoginView;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Leunghowell on 2017/6/5.
 */

public class LoginPresenterImpl implements ILoginPresenter {
    private ILoginView iLoginView;

    public LoginPresenterImpl(ILoginView iLoginView) {
        this.iLoginView = iLoginView;
    }

    @Override
    public void login(BaseResp resp) {
        final Map<String, String> params = new HashMap<>();
        params.put("appid", Constants.WX_APP_ID);
        params.put("secret", Constants.WX_APP_SECRET);
        params.put("code", ((SendAuth.Resp) resp).code);
        params.put("grant_type", "authorization_code");

        RequestUtil.request("https://api.weixin.qq.com/sns/oauth2/access_token",
                params, new HashMap<String, String>(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (response.contains("access_token")) {
                                getUserInfo(jsonObject.getString("access_token"), jsonObject.getString("openid"));
                            } else {
                                iLoginView.onLoginResult(false, jsonObject.getString("errmsg"), response);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            iLoginView.onLoginResult(false, "登录数据源出错", e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        iLoginView.onLoginResult(false, "登录失败", error);
                    }
                }
        );
    }

    private void getUserInfo(String accessToken, String openId) {
        Map<String, String> params = new HashMap<>();
        params.put("access_token", accessToken);
        params.put("openid", openId);

        RequestUtil.request("https://api.weixin.qq.com/sns/userinfo",
                params, new HashMap<String, String>(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (response.contains("unionid")) {
                                loginToServer(response);
                            } else {
                                iLoginView.onLoginResult(false, jsonObject.getString("errmsg"), response);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            iLoginView.onLoginResult(false, "登录数据源出错", e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        iLoginView.onLoginResult(false, "登录失败", error);
                    }
                }
        );
    }

    private void loginToServer(String userInfo) {
        Map<String, String> postParams = new HashMap<>();
        postParams.put("login", "weixinapp");
        postParams.put("user_info", userInfo);

        RequestUtil.request(new HashMap<String, String>(), postParams,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String result = jsonObject.getString("code");

                            if (Constants.SUCCESS.equals(result)) {
                                Config.ThirdSession = jsonObject.getJSONObject("data").getString("third_session");

                                iLoginView.onLoginResult(true, "", "");
                            } else {
                                iLoginView.onLoginResult(false, jsonObject.getString("msg"), response);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            iLoginView.onLoginResult(false, "登录数据源出错", e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        iLoginView.onLoginResult(false, "登录失败", error);
                    }
                });
    }
}
