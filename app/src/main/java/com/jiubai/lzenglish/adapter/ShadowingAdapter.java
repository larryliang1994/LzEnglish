package com.jiubai.lzenglish.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.badoo.mobile.util.WeakHandler;
import com.danikula.videocache.HttpProxyCacheServer;
import com.jiubai.lzenglish.App;
import com.jiubai.lzenglish.R;
import com.jiubai.lzenglish.bean.Shadowing;
import com.jiubai.lzenglish.bean.Voice;
import com.jiubai.lzenglish.common.UtilBox;
import com.jiubai.lzenglish.config.Config;
import com.jiubai.lzenglish.presenter.ShadowingPresenterImpl;
import com.jiubai.lzenglish.ui.iview.IShadowingView;
import com.jiubai.lzenglish.widget.CardPopup;
import com.jiubai.lzenglish.widget.ChatImageView;
import com.jiubai.lzenglish.widget.JCVideoPlayerStandard;
import com.jiubai.lzenglish.widget.recorder.AudioPlayer;
import com.labo.kaji.relativepopupwindow.RelativePopupWindow;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zl.reik.dilatingdotsprogressbar.DilatingDotsProgressBar;

import java.util.ArrayList;
import java.util.Timer;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.techery.properratingbar.ProperRatingBar;

import static fm.jiecao.jcvideoplayer_lib.JCVideoPlayer.CURRENT_STATE_PLAYING;

/**
 * Created by Larry Liang on 16/05/2017.
 */

public class ShadowingAdapter extends RecyclerView.Adapter {
    private ArrayList<Shadowing> shadowingList;
    private Context context;
    private OnItemClickListener listener;

    public ArrayList<Integer> voiceIndex;   // {   1, 2, 3,    5, 6, 7}
    public ArrayList<Integer> shadowIndex;  // {0,          4         }
    public ArrayList<Integer> arrangeIndex; // {0, 0, 0, 0, 1, 1, 1, 1}

    public WeakHandler rightHandler;
    public WeakHandler leftHandler;

    public JCVideoPlayerStandard jcVideoPlayerStandard;

    private Timer readTimer;

    private int currentIndex = 0;

    public ShadowingAdapter(Context context, ArrayList<Shadowing> list) {
        this.context = context;
        this.shadowingList = list;

        initIndex();
    }

    public void initIndex() {
        int count = 0;
        voiceIndex = new ArrayList<>();
        shadowIndex = new ArrayList<>();
        arrangeIndex = new ArrayList<>();

        for (int i = 0; i < shadowingList.size(); i++) {
            arrangeIndex.add(i);

            Shadowing shadowing = shadowingList.get(i);

            shadowIndex.add(count);
            count++;

            for (Voice voice : shadowing.getVoiceList()) {
                arrangeIndex.add(i);
                voiceIndex.add(count);
                count++;
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == 0) { //(viewType == 0) {
            view = LayoutInflater.from(context).inflate(R.layout.item_shadowing_left, parent, false);
            return new LeftItemViewHolder(view);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.item_shadowing_right, parent, false);
            return new RightItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (shadowIndex.contains(position)) {
            final Shadowing shadowing = shadowingList.get(arrangeIndex.get(position));

            final LeftItemViewHolder viewHolder = (LeftItemViewHolder) holder;

            ImageLoader.getInstance().displayImage(shadowing.getHeaderImg(), viewHolder.portraitImageView);
            viewHolder.engTextView.setText(arrangeIndex.get(position) + 1 + "." + shadowing.getSentenceEng());
            viewHolder.chTextView.setText(shadowing.getSentenceCh());

            if (currentIndex == position) {
                viewHolder.engTextView.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
                viewHolder.readImageView.setImageTintList(
                        ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorPrimary)));
            } else {
                viewHolder.engTextView.setTextColor(Color.parseColor("#333333"));
                viewHolder.readImageView.setImageTintList(
                        ColorStateList.valueOf(Color.parseColor("#A2A2A2")));
            }

            viewHolder.engTextView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (TextUtils.isEmpty(shadowing.getSentenceCh())) {
                        ViewGroup.LayoutParams params = viewHolder.chatImageView.getLayoutParams();
                        params.height = viewHolder.engTextView.getHeight()
                                + UtilBox.dip2px(context, 12 + 10 + 12);
                        viewHolder.chatImageView.setLayoutParams(params);
                    } else {
                        ViewGroup.LayoutParams params = viewHolder.chatImageView.getLayoutParams();
                        params.height = viewHolder.engTextView.getHeight()
                                + viewHolder.chTextView.getHeight()
                                + UtilBox.dip2px(context, 12 + 10 + 12);
                        viewHolder.chatImageView.setLayoutParams(params);
                    }

                    viewHolder.engTextView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            });

            if (position == 0) {
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) viewHolder.itemView.getLayoutParams();
                params.setMargins(0, UtilBox.dip2px(context, 16), 0, 0);
                viewHolder.itemView.setLayoutParams(params);
            } else {
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) viewHolder.itemView.getLayoutParams();
                params.setMargins(0, UtilBox.dip2px(context, 8), 0, 0);
                viewHolder.itemView.setLayoutParams(params);
            }

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    leftItemClick(viewHolder, position);
                }
            });
        } else {
            final Voice voice = shadowingList
                    .get(arrangeIndex.get(position))
                    .getVoiceList()
                    .get(position - shadowIndex.get(arrangeIndex.get(position)) - 1);

            final RightItemViewHolder viewHolder = (RightItemViewHolder) holder;

            ImageLoader.getInstance().displayImage(Config.UserImage, viewHolder.portraitImageView);

            if (voice.getId() == -99) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 0, UtilBox.dip2px(context, 12), 0);
                params.gravity = Gravity.CENTER_VERTICAL;
                viewHolder.progress.setLayoutParams(params);
                viewHolder.progress.setVisibility(View.VISIBLE);
                viewHolder.progress.showNow();

                viewHolder.dotTextView.setVisibility(View.GONE);
                viewHolder.lengthTextView.setVisibility(View.GONE);
                viewHolder.button.setVisibility(View.GONE);
                viewHolder.ratingBar.setVisibility(View.GONE);
                viewHolder.readImageView.setVisibility(View.GONE);
                viewHolder.chatImageView.setVisibility(View.GONE);
                viewHolder.layout.setVisibility(View.GONE);
            } else {
                viewHolder.dotTextView.setVisibility(View.VISIBLE);
                viewHolder.lengthTextView.setVisibility(View.VISIBLE);
                viewHolder.lengthTextView.setText(voice.getSeconds() + "");

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 0, UtilBox.dip2px(context, 5), 0);
                params.gravity = Gravity.CENTER_VERTICAL;
                viewHolder.progress.setLayoutParams(params);
                viewHolder.progress.setVisibility(View.GONE);

                if (voice.getScore() == -1) {
                    viewHolder.button.setVisibility(View.VISIBLE);
                    viewHolder.ratingBar.setVisibility(View.GONE);

                    viewHolder.button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            viewHolder.button.setVisibility(View.GONE);
                            viewHolder.progress.showNow();

                            new ShadowingPresenterImpl(new IShadowingView() {
                                @Override
                                public void onGetShadowingListResult(boolean result, String info, Object extras) {
                                }

                                @Override
                                public void onSaveVoiceResult(boolean result, String info, Object extras) {
                                }

                                @Override
                                public void onScoringVoiceResult(boolean result, String info, Object extras) {
                                    viewHolder.progress.hideNow();

                                    if (result) {
                                        viewHolder.ratingBar.setVisibility(View.VISIBLE);

                                        viewHolder.ratingBar.setRating((Integer) extras);

                                        shadowingList.get(arrangeIndex.get(position))
                                                .getVoiceList()
                                                .get(position - shadowIndex.get(arrangeIndex.get(position)) - 1)
                                                .setScore((Integer) extras);
                                    } else {
                                        viewHolder.button.setVisibility(View.VISIBLE);
                                        viewHolder.ratingBar.setVisibility(View.GONE);

                                        Toast.makeText(context, info, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).scoringVoice(voice.getId());
                        }
                    });
                } else {
                    viewHolder.button.setVisibility(View.GONE);
                    viewHolder.ratingBar.setVisibility(View.VISIBLE);
                    viewHolder.ratingBar.setRating(voice.getScore());
                }

                viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        try {
                            if (AudioPlayer.getInstance().mediaPlayer != null
                                    && AudioPlayer.getInstance().mediaPlayer.isPlaying()) {
                                AudioPlayer.getInstance().pause();
                                AudioPlayer.getInstance().stop();
                            }

                            if (jcVideoPlayerStandard.currentState == CURRENT_STATE_PLAYING) {
                                jcVideoPlayerStandard.startButton.performClick();
                            }
                        } catch (IllegalStateException e) {
                            e.printStackTrace();
                        }

                        if (rightHandler != null) {
                            rightHandler.removeMessages(0);
                            rightHandler.sendEmptyMessage(1);
                        }
                        AudioPlayer.getInstance().currentId = -99;

                        if (leftHandler != null) {
                            leftHandler.removeMessages(0);
                            leftHandler.sendEmptyMessage(1);
                        }
                        jcVideoPlayerStandard.currentId = -99;

                        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                        vibrator.vibrate(50);

                        final CardPopup popup = new CardPopup(context);
                        popup.setListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                popup.dismiss();

                                if (v.getId() == R.id.textView_delete) {
                                    new ShadowingPresenterImpl(null).deleteVoice(voice.getId());
                                    shadowingList
                                            .get(arrangeIndex.get(position))
                                            .getVoiceList().remove(position - shadowIndex.get(arrangeIndex.get(position)) - 1);
                                    initIndex();
                                    notifyDataSetChanged();
                                } else {
                                    AlertDialog dialog = new AlertDialog.Builder(context
                                            , android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth)
                                            .setMessage(voice.getBaiduTranslate())
                                            .setCancelable(true)
                                            .create();
                                    dialog.setCanceledOnTouchOutside(true);
                                    dialog.show();
                                }
                            }
                        });
                        popup.showOnAnchor(viewHolder.chatImageView,
                                RelativePopupWindow.VerticalPosition.ABOVE,
                                RelativePopupWindow.HorizontalPosition.LEFT,
                                viewHolder.chatImageView.getLayoutParams().width / 2,
                                UtilBox.dip2px(context, -5),
                                true);

                        return false;
                    }
                });

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rightItemClick(viewHolder, position);
                    }
                });

                viewHolder.layout.setVisibility(View.VISIBLE);
                viewHolder.chatImageView.setVisibility(View.VISIBLE);
                viewHolder.readImageView.setVisibility(View.VISIBLE);

                ViewGroup.LayoutParams chatParams = viewHolder.chatImageView.getLayoutParams();
                int length = voice.getSeconds() / 10 >= 1 ? 10 : voice.getSeconds();
                chatParams.width = UtilBox.dip2px(context, 5 * length + 80);
                viewHolder.chatImageView.setLayoutParams(chatParams);

                ViewGroup.LayoutParams layoutParams = viewHolder.layout.getLayoutParams();
                layoutParams.width = UtilBox.dip2px(context, 5 * length + 80);
                viewHolder.layout.setLayoutParams(layoutParams);
            }
        }
    }

    public void leftItemClick(final LeftItemViewHolder viewHolder, final int position) {
        final Shadowing shadowing = shadowingList.get(arrangeIndex.get(position));

        currentIndex = position;
        notifyDataSetChanged();

        // 先停掉右动画
        if (rightHandler != null) {
            rightHandler.removeMessages(0);
            rightHandler.sendEmptyMessage(1);
        }

        // 如果在播放音频，就停掉音频
        if (AudioPlayer.getInstance().mediaPlayer != null
                && AudioPlayer.getInstance().mediaPlayer.isPlaying()) {
            AudioPlayer.getInstance().pause();
            AudioPlayer.getInstance().stop();
        }

        // 先停掉当前的左动画
        if (leftHandler != null) {
            leftHandler.removeMessages(0);
            leftHandler.sendEmptyMessage(1);
        }

        final int[] count = {0};

        // 如果在播放自身，就停下来
        if (jcVideoPlayerStandard.currentId == shadowing.getId()
                && jcVideoPlayerStandard.currentState == CURRENT_STATE_PLAYING) {
            jcVideoPlayerStandard.currentId = -99;
            jcVideoPlayerStandard.startButton.performClick();
        } else { // 否则开始播放新视频
            leftHandler = new WeakHandler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message message) {
                    if (message.what == 0) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                jcVideoPlayerStandard.currentId = shadowing.getId();

                                int resId = context.getResources().getIdentifier("read_left_" + (count[0] % 3 + 1),
                                        "drawable", context.getPackageName());
                                viewHolder.readImageView.setImageTintList(
                                        ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorPrimary)));
                                viewHolder.readImageView.setImageResource(resId);

                                count[0]++;

                                // 用count作计时器，因此不需要监听完成事件
                                if (count[0] * 300 >= (shadowing.getEndSecond() - shadowing.getStartSecond()) * 1000) {
                                    // 已播放完毕，则停止左动画
                                    leftHandler.sendEmptyMessage(1);
                                } else {
                                    // 否则继续播放左动画
                                    leftHandler.sendEmptyMessageDelayed(0, 300);
                                }
                            }
                        });
                    } else if (message.what == 1) {
                        count[0] = 0;
                        jcVideoPlayerStandard.currentId = -99;
                        viewHolder.readImageView.setImageTintList(ColorStateList.valueOf(Color.parseColor("#A2A2A2")));
                        viewHolder.readImageView.setImageResource(R.drawable.read_left_3);
                    }

                    return false;
                }
            });

            // 播放左动画
            count[0] = 0;
            leftHandler.sendEmptyMessage(0);

            // 通知外部
            if (listener != null) {
                listener.onLeftItemClick(arrangeIndex.get(position));
            }
        }
    }

    public void rightItemClick(final RightItemViewHolder viewHolder, final int position) {
        final Voice voice = shadowingList
                .get(arrangeIndex.get(position))
                .getVoiceList()
                .get(position - shadowIndex.get(arrangeIndex.get(position)) - 1);

        // 停掉左动画
        if (leftHandler != null) {
            leftHandler.removeMessages(0);
            leftHandler.sendEmptyMessage(1);
        }

        // 如果在播视频，就停掉视频
        if (jcVideoPlayerStandard.currentState == CURRENT_STATE_PLAYING) {
            jcVideoPlayerStandard.startButton.performClick();
        }

        // 先停掉当前的右动画
        if (rightHandler != null) {
            rightHandler.removeMessages(0);
            rightHandler.sendEmptyMessage(1);
        }

        final int[] count = {0};

        // 如果在播放自身，就停下来
        if (AudioPlayer.getInstance().currentId == voice.getId()
                && AudioPlayer.getInstance().mediaPlayer != null
                && AudioPlayer.getInstance().mediaPlayer.isPlaying()) {
            AudioPlayer.getInstance().currentId = -99;
            AudioPlayer.getInstance().pause();
            AudioPlayer.getInstance().stop();
        } else { // 否则开始播放新音频
            rightHandler = new WeakHandler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message message) {
                    if (message.what == 0) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                AudioPlayer.getInstance().currentId = voice.getId();
                                int resId = context.getResources().getIdentifier("read_right_" + (count[0] % 3 + 1),
                                        "drawable", context.getPackageName());
                                viewHolder.readImageView.setImageTintList(ColorStateList.valueOf(Color.WHITE));
                                viewHolder.readImageView.setImageResource(resId);

                                count[0]++;

                                rightHandler.sendEmptyMessageDelayed(0, 300);
                            }
                        });
                    } else if (message.what == 1) {
                        count[0] = 0;
                        AudioPlayer.getInstance().currentId = -99;
                        viewHolder.readImageView.setImageTintList(ColorStateList.valueOf(Color.parseColor("#439E1B")));
                        viewHolder.readImageView.setImageResource(R.drawable.read_right_3);
                    }

                    return false;
                }
            });

            // 播放右动画
            count[0] = 0;
            rightHandler.sendEmptyMessage(0);

            // 通知外部
            if (listener != null) {
                listener.onRightItemClick(position - shadowIndex.get(arrangeIndex.get(position)) - 1);
            }

            Log.i("text", voice.getWav());

            // 开始播放
            if (voice.getWav().contains("http")) {
                HttpProxyCacheServer proxy = App.getProxy(context);
                String proxyUrl = proxy.getProxyUrl(voice.getWav());

                AudioPlayer.getInstance().playUrl(proxyUrl, voice.getId());
            } else {
                AudioPlayer.getInstance().playUrl(voice.getWav(), voice.getId());
            }

            // 监听完成事件
            AudioPlayer.getInstance().setListener(new AudioPlayer.onAudioStateChangedListener() {
                @Override
                public void onCompleted() {
                    if (rightHandler != null) {
                        rightHandler.removeMessages(0);
                        rightHandler.sendEmptyMessage(1);
                    }
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (shadowIndex.contains(position)) {
            return 0;
        } else {
            return 1;
        }
    }

    public OnItemClickListener getListener() {
        return listener;
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return shadowIndex.size() + voiceIndex.size();
    }

    public class RightItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.textView_seconds)
        TextView lengthTextView;

        @Bind(R.id.button)
        Button button;

        @Bind(R.id.rating)
        ProperRatingBar ratingBar;

        @Bind(R.id.progress)
        DilatingDotsProgressBar progress;

        @Bind(R.id.imageView_read)
        ImageView readImageView;

        @Bind(R.id.chatImageView)
        ChatImageView chatImageView;

        @Bind(R.id.layout_chat)
        RelativeLayout layout;

        @Bind(R.id.imageView_portrait)
        ImageView portraitImageView;

        @Bind(R.id.textView_dot)
        TextView dotTextView;

        public RightItemViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

    public class LeftItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.imageView_portrait)
        ImageView portraitImageView;

        @Bind(R.id.chatImageView)
        ChatImageView chatImageView;

        @Bind(R.id.textView_eng)
        TextView engTextView;

        @Bind(R.id.textView_ch)
        TextView chTextView;

        @Bind(R.id.imageView_play)
        ImageView readImageView;

        public LeftItemViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnItemClickListener {
        void onLeftItemClick(int position);

        void onRightItemClick(int position);
    }

    public interface OnConstructHandlerListener {
        void onConstructed();
    }
}
