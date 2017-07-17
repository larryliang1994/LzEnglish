package com.jiubai.lzenglish.presenter;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jiubai.lzenglish.App;
import com.jiubai.lzenglish.bean.Coupon;
import com.jiubai.lzenglish.bean.PurchaseInfo;
import com.jiubai.lzenglish.config.Config;
import com.jiubai.lzenglish.config.Constants;
import com.jiubai.lzenglish.config.Urls;
import com.jiubai.lzenglish.manager.RequestCacheManager;
import com.jiubai.lzenglish.net.RequestUtil;
import com.jiubai.lzenglish.ui.iview.IPurchaseView;
import com.tencent.mm.opensdk.modelpay.PayReq;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Larry Liang on 15/06/2017.
 */

public class PurchasePresenterImpl implements IPurchasePresenter {
    private IPurchaseView iPurchaseView;

    public PurchasePresenterImpl(IPurchaseView iPurchaseView) {
        this.iPurchaseView = iPurchaseView;
    }

    @Override
    public void getPurchaseInfo(int seasonId) {
        final Map<String, String> params = new HashMap<>();
        params.put("_url", "order/buySingleCartoon");
        params.put("id", seasonId + "");
        params.put("_ajax", "1");
        params.put("third_session", Config.ThirdSession);

        RequestUtil.getRequest(params,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            RequestCacheManager.getInstance().insertRequestCache(Urls.SERVER_URL, params, response);

                            JSONObject jsonObject = new JSONObject(response);

                            String result = jsonObject.getString("code");

                            if (Constants.SUCCESS.equals(result)) {
                                decodePurchaseInfo(jsonObject.getJSONObject("data"));
                            } else {
                                iPurchaseView.onGetPurchaseInfoResult(false, jsonObject.getString("msg"), response);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            iPurchaseView.onGetPurchaseInfoResult(false, "动画片信息源出错", response);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        iPurchaseView.onGetPurchaseInfoResult(false, "获取动画片信息失败", error);
                    }
                }
        );
    }

    private void decodePurchaseInfo(JSONObject jsonObject) throws JSONException {

        ArrayList<Coupon> coupons = new ArrayList<>();

        if (jsonObject.toString().contains("coupon_list")) {
            JSONArray couponArray = jsonObject.getJSONArray("coupon_list");
            for (int i = 0; i < couponArray.length(); i++) {
                JSONObject couponObject = couponArray.getJSONObject(i);

                Coupon coupon = new Coupon(
                        couponObject.getInt("id"),
                        (float) couponObject.getDouble("price"),
                        couponObject.getInt("id_member"),
                        couponObject.getString("remarks"),
                        couponObject.getInt("status")
                );

                coupons.add(coupon);
            }
        }

        JSONObject purchaseInfoObject = jsonObject.getJSONObject("data");
        PurchaseInfo purchaseInfo = new PurchaseInfo(
            purchaseInfoObject.getInt("id"),
                purchaseInfoObject.getString("name2"),
                purchaseInfoObject.getString("image"),
                (float) purchaseInfoObject.getDouble("price"),
                coupons
        );

        iPurchaseView.onGetPurchaseInfoResult(true, "", purchaseInfo);
    }

    @Override
    public void didPurchase(int seasonId, int couponId) {
        final Map<String, String> params = new HashMap<>();
        params.put("_url", "order/buySingleCartoon");
        params.put("_ajax", "1");
        params.put("id", seasonId + "");

        Map<String, String> postParams = new HashMap<>();
        if (couponId != -1) {
            postParams.put("id_coupon", couponId + "");
        }
        postParams.put("_url", "order/buySingleCartoon");
        postParams.put("_ajax", "1");
        postParams.put("id", seasonId + "");

        RequestUtil.request(params, postParams,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("text", response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String result = jsonObject.getString("code");

                            if (Constants.SUCCESS.equals(result)) {
                                String orderNum = jsonObject.getJSONObject("data").getString("order_number");

                                initOrder(orderNum);
                            } else {
                                iPurchaseView.onDoPurchaseResult(false, jsonObject.getString("msg"), response);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            iPurchaseView.onDoPurchaseResult(false, "提交订单信息源出错", response);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        iPurchaseView.onDoPurchaseResult(false, "提交订单信息失败", error);
                    }
                }
        );
    }

    public void initOrder(String orderNum) {
        final Map<String, String> params = new HashMap<>();
        params.put("_url", "order/getAppletPayment");
        params.put("_ajax", "1");
        params.put("order_number", orderNum);
        params.put("third_session", Config.ThirdSession);

        RequestUtil.getRequest(params,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String result = jsonObject.getString("code");

                            if (Constants.SUCCESS.equals(result)) {
                                JSONObject dataObject = jsonObject.getJSONObject("data");

                                sendOrder(dataObject);
                            } else {
                                iPurchaseView.onDoPurchaseResult(false, jsonObject.getString("msg"), response);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            iPurchaseView.onDoPurchaseResult(false, "提交订单信息源出错", response);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        iPurchaseView.onDoPurchaseResult(false, "提交订单信息失败", volleyError);
                    }
                });
    }

    public void sendOrder(JSONObject dataObject) throws JSONException {
        String[] prepayIds = dataObject.getString("package").split("=");

        PayReq request = new PayReq();
        request.appId = Constants.WX_APP_ID;
        request.partnerId = Constants.WX_MCH_ID;
        request.prepayId = prepayIds.length == 2 ? prepayIds[1] : prepayIds[0];
        request.packageValue = "Sign=WXPay";
        request.nonceStr = dataObject.getString("nonceStr");
        request.timeStamp = dataObject.getString("timeStamp");
        request.sign = dataObject.getString("paySign");
        App.iwxapi.sendReq(request);
    }
}
