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
import com.jiubai.lzenglish.widget.recorder.AudioPlayer;
import com.labo.kaji.relativepopupwindow.RelativePopupWindow;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zl.reik.dilatingdotsprogressbar.DilatingDotsProgressBar;

import java.util.ArrayList;
import java.util.Timer;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.techery.properratingbar.ProperRatingBar;

/**
 * Created by Larry Liang on 16/05/2017.
 */

public class ShadowingAdapter extends RecyclerView.Adapter {
    private ArrayList<Shadowing> shadowingList;
    private Context context;
    private OnItemClickListener listener;

    private OnConstructHandlerListener constructHandlerListener;

    public ArrayList<Integer> voiceIndex;   // {   1, 2, 3,    5, 6, 7}
    public ArrayList<Integer> shadowIndex;  // {0,          4         }
    public ArrayList<Integer> arrangeIndex; // {0, 0, 0, 0, 1, 1, 1, 1}

    public WeakHandler handler;

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
            Shadowing shadowing = shadowingList.get(arrangeIndex.get(position));

            final LeftItemViewHolder viewHolder = (LeftItemViewHolder) holder;

            ImageLoader.getInstance().displayImage(shadowing.getHeaderImg(), viewHolder.portraitImageView);
            viewHolder.engTextView.setText(position + 1 + "." + shadowing.getSentenceEng());
            viewHolder.chTextView.setText(shadowing.getSentenceCh());

            if (currentIndex == position) {
                viewHolder.engTextView.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
                viewHolder.playImageView.setImageTintList(
                        ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorPrimary)));
            } else {
                viewHolder.engTextView.setTextColor(Color.parseColor("#333333"));
                viewHolder.playImageView.setImageTintList(
                        ColorStateList.valueOf(Color.parseColor("#A2A2A2")));
            }

            viewHolder.chTextView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    viewHolder.chTextView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                    ViewGroup.LayoutParams params = viewHolder.chatImageView.getLayoutParams();
                    params.height = viewHolder.engTextView.getHeight()
                            + viewHolder.chTextView.getHeight()
                            + UtilBox.dip2px(context, 12 + 10 + 12);
                    viewHolder.chatImageView.setLayoutParams(params);
                }
            });

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentIndex = position;
                    notifyDataSetChanged();

                    if (listener != null) {
                        listener.onLeftItemClick(position);
                    }
                }
            });
        } else {
            final Voice voice = shadowingList.get(arrangeIndex.get(position))
                    .getVoiceList().get(position - arrangeIndex.get(position) - 1);

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

                viewHolder.lengthTextView.setVisibility(View.GONE);
                viewHolder.button.setVisibility(View.GONE);
                viewHolder.ratingBar.setVisibility(View.GONE);
                viewHolder.readImageView.setVisibility(View.GONE);
                viewHolder.chatImageView.setVisibility(View.GONE);
                viewHolder.layout.setVisibility(View.GONE);
            } else {
                viewHolder.lengthTextView.setVisibility(View.VISIBLE);
                viewHolder.lengthTextView.setText(voice.getSeconds() + "''");

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
                                                .getVoiceList().get(position - arrangeIndex.get(position) - 1)
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
                        } catch (IllegalStateException e) {
                            e.printStackTrace();
                        }

                        if (handler != null) {
                            handler.removeMessages(0);
                            handler.sendEmptyMessage(1);
                        }

                        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                        vibrator.vibrate(50);

                        final CardPopup popup = new CardPopup(context);
                        popup.setListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                popup.dismiss();

                                if (v.getId() == R.id.textView_delete) {
                                    new ShadowingPresenterImpl(null).deleteVoice(voice.getId());
                                    shadowingList.get(arrangeIndex.get(position))
                                            .getVoiceList().remove(position - arrangeIndex.get(position) - 1);
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
                        readTimer = new Timer();

                        //readTimer.schedule();

                        final int[] count = {0};

                        if (handler != null) {
                            handler.removeMessages(0);
                            handler.sendEmptyMessage(1);
                        }

                        handler = new WeakHandler(new Handler.Callback() {
                            @Override
                            public boolean handleMessage(Message message) {
                                if (message.what == 0) {
                                    ((Activity) context).runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            int resId = context.getResources().getIdentifier("read_right_" + (count[0] % 3 + 1),
                                                    "drawable", context.getPackageName());
                                            viewHolder.readImageView.setImageTintList(ColorStateList.valueOf(Color.WHITE));
                                            viewHolder.readImageView.setImageResource(resId);

                                            count[0]++;

                                            handler.sendEmptyMessageDelayed(0, 300);
                                        }
                                    });
                                } else if (message.what == 1) {
                                    viewHolder.readImageView.setImageTintList(ColorStateList.valueOf(Color.parseColor("#439E1B")));
                                    viewHolder.readImageView.setImageResource(R.drawable.read_right_3);
                                }

                                return false;
                            }
                        });

                        if (constructHandlerListener != null) {
                            constructHandlerListener.onConstructed();
                        }

                        if (AudioPlayer.getInstance().currentId != voice.getId()) {
                            handler.sendEmptyMessage(0);
                        }

                        if (AudioPlayer.getInstance().mediaPlayer != null
                                && AudioPlayer.getInstance().mediaPlayer.isPlaying()) {
                            AudioPlayer.getInstance().pause();
                            AudioPlayer.getInstance().stop();
                        }

                        AudioPlayer.getInstance().setListener(new AudioPlayer.onAudioStateChangedListener() {
                            @Override
                            public void onCompleted() {
                                if (handler != null) {
                                    handler.removeMessages(0);
                                    handler.sendEmptyMessage(1);
                                }
                            }
                        });

                        Log.i("text", voice.getWav());

                        if (voice.getWav().contains("http")) {

                            HttpProxyCacheServer proxy = App.getProxy(context);
                            String proxyUrl = proxy.getProxyUrl("http://music.baidutt.com/up/kwcswaks/ksdkk.mp3");

                            AudioPlayer.getInstance().playUrl(proxyUrl, voice.getId());
                        } else {
                            AudioPlayer.getInstance().playUrl(voice.getWav(), voice.getId());
                        }
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

    public OnConstructHandlerListener getConstructHandlerListener() {
        return constructHandlerListener;
    }

    public void setConstructHandlerListener(OnConstructHandlerListener constructHandlerListener) {
        this.constructHandlerListener = constructHandlerListener;
    }

    @Override
    public int getItemCount() {
        return shadowIndex.size() + voiceIndex.size();
    }

    class RightItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.textView_length)
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

        public RightItemViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

    class LeftItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.imageView_portrait)
        ImageView portraitImageView;

        @Bind(R.id.chatImageView)
        ChatImageView chatImageView;

        @Bind(R.id.textView_eng)
        TextView engTextView;

        @Bind(R.id.textView_ch)
        TextView chTextView;

        @Bind(R.id.imageView_play)
        ImageView playImageView;

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
