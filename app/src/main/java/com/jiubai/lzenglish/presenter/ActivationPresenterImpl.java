package com.jiubai.lzenglish.presenter;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jiubai.lzenglish.config.Constants;
import com.jiubai.lzenglish.net.RequestUtil;
import com.jiubai.lzenglish.ui.iview.IActivationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Larry Liang on 02/06/2017.
 */

public class ActivationPresenterImpl implements IActivationPresenter {
    private IActivationView iActivationView;

    public ActivationPresenterImpl(IActivationView iActivationView) {
        this.iActivationView = iActivationView;
    }

    @Override
    public void activate(String code) {
        final Map<String, String> params = new HashMap<>();
        params.put("_url", "activationCode/activation");
        params.put("_ajax", "1");

        final Map<String, String> postParams = new HashMap<>();
        postParams.put("acode", code);

        RequestUtil.request(params, postParams,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String result = jsonObject.getString("code");

                            if (Constants.SUCCESS.equals(result)) {
                                iActivationView.onActivateResult(true, "", response);
                            } else {
                                iActivationView.onActivateResult(false, jsonObject.getString("msg"), response);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            iActivationView.onActivateResult(false, "激活数据源出错", e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        iActivationView.onActivateResult(false, "激活失败", volleyError);
                    }
                });
    }
}
