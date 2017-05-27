package com.jiubai.lzenglish.presenter;

import android.content.Context;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jiubai.lzenglish.bean.Shadowing;
import com.jiubai.lzenglish.bean.Voice;
import com.jiubai.lzenglish.common.UtilBox;
import com.jiubai.lzenglish.config.Constants;
import com.jiubai.lzenglish.net.RequestUtil;
import com.jiubai.lzenglish.net.UploadUtil;
import com.jiubai.lzenglish.ui.iview.IShadowingView;

import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadStatusDelegate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Larry Liang on 23/05/2017.
 */

public class ShadowingPresenterImpl implements IShadowingPresenter {
    private IShadowingView iShadowingView;

    public ShadowingPresenterImpl(IShadowingView iShadowingView) {
        this.iShadowingView = iShadowingView;
    }

    @Override
    public void getShadowingList(final int videoId) {
        Map<String, String> params = new HashMap<>();
        params.put("_url", "cartoonItemReview/indexV2");
        params.put("_ajax", "1");
        params.put("id", videoId + "");

        RequestUtil.request(params,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String result = jsonObject.getString("code");

                            if (Constants.SUCCESS.equals(result)) {
                                JSONObject dataObject = jsonObject.getJSONObject("data");

                                JSONArray shadowArray = dataObject.getJSONArray("results");

                                List<Shadowing> shadowings = new ArrayList<>();

                                for (int i = 0; i < shadowArray.length(); i++) {
                                    JSONArray voiceArray = shadowArray.getJSONObject(i).getJSONArray("___voice_list");

                                    List<Voice> voices = new ArrayList<>();

                                    for (int j = 0; j < voiceArray.length(); j++) {
                                        JSONObject voiceObject = voiceArray.getJSONObject(j);

                                        Voice voice = new Voice(
                                                voiceObject.getInt("id"),
                                                UtilBox.getStringToTimestamp(voiceObject.getString("create_time")),
                                                voiceObject.getInt("id_member"),
                                                voiceObject.getInt("id_cartoon_item"),
                                                voiceObject.getInt("id_cartoon_review_training"),
                                                voiceObject.getInt("score"),
                                                voiceObject.getInt("tmp_score"),
                                                voiceObject.getInt("true_score"),
                                                voiceObject.getInt("type"),
                                                voiceObject.getString("baidu_translate"),
                                                voiceObject.getString("baidu_translate_all"),
                                                voiceObject.getInt("seconds"),
                                                voiceObject.getString("video_path"),
                                                voiceObject.getString("weixin_server_id"),
                                                voiceObject.getString("weixin_local_id"),
                                                voiceObject.getString("headimg_audio"),
                                                voiceObject.getString("wav")
                                        );

                                        voices.add(voice);
                                    }

                                    JSONObject shadowObject = shadowArray.getJSONObject(i);

                                    Shadowing shadowing = new Shadowing(
                                            shadowObject.getInt("id"),
                                            UtilBox.getStringToTimestamp(shadowObject.getString("create_time")),
                                            shadowObject.getInt("id_cartoon"),
                                            shadowObject.getInt("id_cartoon_item"),
                                            shadowObject.getString("sentence_en"),
                                            shadowObject.getString("sentence_ch"),
                                            shadowObject.getString("content"),
                                            shadowObject.getDouble("start_second"),
                                            shadowObject.getDouble("end_second"),
                                            shadowObject.getInt("sort"),
                                            shadowObject.getInt("ifshow"),
                                            1, //shadowObject.getInt("___has_look"),
                                            voices,
                                            shadowObject.getString("header_img")
                                    );

                                    shadowings.add(shadowing);
                                }

                                iShadowingView.onGetShadowingListResult(true, "", shadowings);
                            } else {
                                iShadowingView.onGetShadowingListResult(false, jsonObject.getString("msg"), response);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        iShadowingView.onGetShadowingListResult(false, "获取跟读列表失败", volleyError);
                    }
                });
    }

    @Override
    public void saveVoice(Context context, final Voice voice) {
        Map<String, String> params = new HashMap<>();
        params.put("_url", "cartoonItemReview/uploadVoiceFile");
        params.put("_ajax", "1");

        UploadUtil.upload(context, params, voice.getVideoPath(), new UploadStatusDelegate() {
            @Override
            public void onProgress(Context context, UploadInfo uploadInfo) {

            }

            @Override
            public void onError(Context context, UploadInfo uploadInfo, Exception exception) {
                iShadowingView.onSaveVoiceResult(false, "上传录音文件失败", uploadInfo);
            }

            @Override
            public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
                try {
                    JSONObject jsonObject = new JSONObject(serverResponse.getBodyAsString());

                    String result = jsonObject.getString("code");

                    if (Constants.SUCCESS.equals(result)) {
                        String serverId = jsonObject.getJSONObject("data").getString("weixin_server_id");

                        voice.setWeixinServerId(serverId);
                        voice.setVideoPath(serverId + ".amr.wav");
                        voice.setWeixinLocalId(serverId);

                        saveVoiceRecord(voice);
                    } else {
                        iShadowingView.onSaveVoiceResult(false, jsonObject.getString("msg"), serverResponse);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(Context context, UploadInfo uploadInfo) {
                iShadowingView.onSaveVoiceResult(false, "上传录音文件失败", uploadInfo);
            }
        });
    }

    @Override
    public void scoringVoice(int voiceId) {
        Map<String, String> params = new HashMap<>();
        params.put("_url", "cartoonItemReview/doScoring");
        params.put("_ajax", "1");

        Map<String, String> postParams = new HashMap<>();
        postParams.put("log_id", voiceId + "");

        RequestUtil.request(params, postParams,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String result = jsonObject.getString("code");

                            if (Constants.SUCCESS.equals(result)) {
                                iShadowingView.onScoringVoiceResult(true, "", jsonObject.getInt("data"));
                            } else {
                                iShadowingView.onScoringVoiceResult(false, jsonObject.getString("msg"), response);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        iShadowingView.onScoringVoiceResult(false, "打分失败", error);
                    }
                });
    }

    @Override
    public void deleteVoice(int voiceId) {
        Map<String, String> params = new HashMap<>();
        params.put("_url", "cartoonItemReview/deleteLog");
        params.put("_ajax", "1");

        Map<String, String> postParams = new HashMap<>();
        postParams.put("log_id", voiceId + "");

        RequestUtil.request(params, postParams,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("text", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("text", error.getMessage());
                    }
                });
    }

    private void saveVoiceRecord(final Voice voice) {
        Map<String, String> params = new HashMap<>();
        params.put("_url", "cartoonItemReview/writeSpeakLog");
        params.put("_ajax", "1");

        Map<String, String> postParams = new HashMap<>();
        postParams.put("id_cartoon_item", voice.getIdCartoonItem() + "");
        postParams.put("id_cartoon_review_train", voice.getIdCartoonReviewTraining() + "");
        postParams.put("weixin_server_id", voice.getWeixinServerId() + "");
        postParams.put("weixin_local_id", voice.getWeixinLocalId() + "");
        postParams.put("video_second", voice.getSeconds() + "");

        RequestUtil.request(params, postParams,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String result = jsonObject.getString("code");

                            if (Constants.SUCCESS.equals(result)) {
                                JSONObject dataObject = jsonObject.getJSONObject("data");

                                int id = dataObject.getInt("id");
                                voice.setId(id);

                                getVoiceText(voice);

                                //iShadowingView.onSaveVoiceResult(true, "", voice);
                            } else {
                                iShadowingView.onSaveVoiceResult(false, "保存录音记录失败", response);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        iShadowingView.onSaveVoiceResult(false, "保存录音记录失败", error);
                    }
                });
    }

    public void getVoiceText(final Voice voice) {
        Map<String, String> params = new HashMap<>();
        params.put("_url", "cartoonItemReview/getTranslateInfo");
        params.put("_ajax", "1");

        Map<String, String> postParams = new HashMap<>();
        postParams.put("log_id", voice.getId() + "");

        RequestUtil.request(params, postParams,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String result = jsonObject.getString("code");

                            if (Constants.SUCCESS.equals(result)) {
                                voice.setBaiduTranslate(jsonObject.getJSONObject("data").getString("baidu_translate"));

                                iShadowingView.onSaveVoiceResult(true, "", voice);
                            } else {
                                iShadowingView.onSaveVoiceResult(false, jsonObject.getString("msg"), response);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        iShadowingView.onSaveVoiceResult(false, "获取文字失败", error);
                    }
                });
    }
}
