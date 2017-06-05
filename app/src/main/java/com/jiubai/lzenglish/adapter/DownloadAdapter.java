package com.jiubai.lzenglish.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import com.jiubai.lzenglish.R;
import com.jiubai.lzenglish.bean.PrefetchVideo;
import com.jiubai.lzenglish.common.UtilBox;
import com.jiubai.lzenglish.manager.DownloadManager;
import com.jiubai.lzenglish.ui.activity.PlayVideoActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Larry Liang on 16/05/2017.
 */

public class DownloadAdapter extends RecyclerView.Adapter implements DownloadManager.OnProgressChangedListener {
    private Context context;
    private OnCheckedListener listener;

    public boolean editing = false;

    public DownloadAdapter(Context context, OnCheckedListener listener) {
        this.context = context;
        this.listener = listener;

        DownloadManager.getInstance().setListener(this);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_download, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        final ItemViewHolder viewHolder = (ItemViewHolder) holder;
        final PrefetchVideo prefetchVideo = DownloadManager.getInstance().getPrefetchVideos().get(position);

        if (editing) {
            viewHolder.mRadioButton.setVisibility(View.VISIBLE);
            viewHolder.mRadioButton.setChecked(prefetchVideo.isChecked());

            if (viewHolder.mRadioButton.isChecked()) {
                viewHolder.mRadioButton.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#23AFD4")));
            } else {
                viewHolder.mRadioButton.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#D0D0D0")));
            }

            final boolean[] touch = {prefetchVideo.isChecked()};
            final boolean[] check = {prefetchVideo.isChecked()};

            viewHolder.mRadioButton.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    touch[0] = viewHolder.mRadioButton.isChecked();

                    return false;
                }
            });

            viewHolder.mRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    check[0] = viewHolder.mRadioButton.isChecked();
                }
            });

            viewHolder.mRadioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewHolder.mRadioButton.setChecked(!(touch[0] && check[0]));

                    if (viewHolder.mRadioButton.isChecked()) {
                        viewHolder.mRadioButton.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#23AFD4")));
                    } else {
                        viewHolder.mRadioButton.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#D0D0D0")));
                    }

                    prefetchVideo.setChecked(viewHolder.mRadioButton.isChecked());

                    listener.onCheckChanged();
                }
            });
        } else {
            viewHolder.mRadioButton.setVisibility(View.GONE);
        }

        if (position == getItemCount() - 1) {
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) viewHolder.itemView.getLayoutParams();
            params.setMargins(UtilBox.dip2px(context, 5), UtilBox.dip2px(context, 10),
                    UtilBox.dip2px(context, 5), UtilBox.dip2px(context, 10));
        } else {
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) viewHolder.itemView.getLayoutParams();
            params.setMargins(UtilBox.dip2px(context, 5), UtilBox.dip2px(context, 10),
                    UtilBox.dip2px(context, 5), UtilBox.dip2px(context, 0));
        }

        DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
                .displayer(new RoundedBitmapDisplayer(UtilBox.dip2px(context, 2)))
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();

        ImageLoader.getInstance().displayImage(prefetchVideo.getImage(), viewHolder.imageView, displayImageOptions);

        viewHolder.nameTextView.setText(prefetchVideo.getName());

        switch (prefetchVideo.getVideoStatus()) {
            case Downloading:
                viewHolder.progressTextView.setVisibility(View.VISIBLE);
                viewHolder.progressTextView.setText(
                        String.format("%.2f", (prefetchVideo.getSoFarSize() * 1.0 / 1000000.0)) + "M/"
                                + String.format("%.2f", (prefetchVideo.getTotalSize() * 1.0 / 1000000.0)) + "M"
                );

                viewHolder.progressBar.setVisibility(View.VISIBLE);
                viewHolder.progressBar.setProgressTintList(ColorStateList.valueOf(
                        ContextCompat.getColor(context, R.color.colorPrimary)
                ));
                viewHolder.progressBar.setProgress(
                        (int) (prefetchVideo.getSoFarSize() * 1.0 / prefetchVideo.getTotalSize() * 100));

                viewHolder.speedTextView.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
                viewHolder.speedTextView.setText("正在缓存" +
                        String.format("%.2f", prefetchVideo.getSpeed() * 1.0 / 1024) + "MB/s");

                viewHolder.statusLayout.setVisibility(View.VISIBLE);
                viewHolder.statusImageView.setImageResource(R.drawable.downloaded);
                viewHolder.statusTextView.setText("缓存中");

                viewHolder.itemView.setOnClickListener(null);
                viewHolder.statusLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DownloadManager.getInstance().changeVideoStatus(prefetchVideo, PrefetchVideo.VideoStatus.Paused);
                    }
                });
                break;

            case Paused:
                viewHolder.progressTextView.setVisibility(View.VISIBLE);
                viewHolder.progressTextView.setText(
                        String.format("%.2f", (prefetchVideo.getSoFarSize() * 1.0 / 1000000.0)) + "M/"
                                + String.format("%.2f", (prefetchVideo.getTotalSize() * 1.0 / 1000000.0)) + "M"
                );

                viewHolder.progressBar.setVisibility(View.VISIBLE);
                viewHolder.progressBar.setProgressTintList(ColorStateList.valueOf(
                        Color.parseColor("#B6B6B6")
                ));
                viewHolder.progressBar.setProgress(
                        (int) (prefetchVideo.getSoFarSize() * 1.0 / prefetchVideo.getTotalSize() * 100));

                viewHolder.speedTextView.setTextColor(Color.parseColor("#909090"));
                viewHolder.speedTextView.setText("已暂停");

                viewHolder.statusLayout.setVisibility(View.VISIBLE);
                viewHolder.statusImageView.setImageResource(R.drawable.pause);
                viewHolder.statusTextView.setText("暂停");

                viewHolder.itemView.setOnClickListener(null);
                viewHolder.statusLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DownloadManager.getInstance().changeVideoStatus(prefetchVideo, PrefetchVideo.VideoStatus.Downloading);
                    }
                });
                break;

            case Error:
                viewHolder.progressTextView.setVisibility(View.VISIBLE);
                viewHolder.progressTextView.setText(
                        String.format("%.2f", (prefetchVideo.getSoFarSize() * 1.0 / 1000000.0)) + "M/"
                                + String.format("%.2f", (prefetchVideo.getTotalSize() * 1.0 / 1000000.0)) + "M"
                );

                viewHolder.progressBar.setVisibility(View.VISIBLE);
                viewHolder.progressBar.setProgressTintList(ColorStateList.valueOf(
                        Color.parseColor("#B6B6B6")
                ));
                viewHolder.progressBar.setProgress(
                        (int) (prefetchVideo.getSoFarSize() * 1.0 / prefetchVideo.getTotalSize() * 100));

                viewHolder.speedTextView.setTextColor(Color.parseColor("#909090"));
                viewHolder.speedTextView.setText("任务出错");

                viewHolder.statusLayout.setVisibility(View.VISIBLE);
                viewHolder.statusImageView.setImageResource(R.drawable.retry);
                viewHolder.statusTextView.setText("重试");

                viewHolder.itemView.setOnClickListener(null);
                viewHolder.statusLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DownloadManager.getInstance().changeVideoStatus(prefetchVideo, PrefetchVideo.VideoStatus.Downloading);
                    }
                });
                break;

            case Downloaded:
                viewHolder.progressTextView.setVisibility(View.GONE);

                viewHolder.progressBar.setVisibility(View.GONE);

                viewHolder.speedTextView.setTextColor(Color.parseColor("#909090"));
                viewHolder.speedTextView.setText(
                        String.format("%.2f", (prefetchVideo.getTotalSize() / 1000000.0)) + "M");

                viewHolder.statusLayout.setVisibility(View.GONE);

                if (!editing) {
                    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(context, PlayVideoActivity.class);
                            intent.putExtra("seasonId", prefetchVideo.getVideoId());
                            UtilBox.startActivity((Activity) context, intent, false);
                        }
                    });
                } else {
                    viewHolder.itemView.setOnClickListener(null);
                }
                break;
        }


    }

    @Override
    public int getItemCount() {
        return DownloadManager.getInstance().getPrefetchVideos().size();
    }

    @Override
    public void onChanged(int index) {
        notifyItemRangeChanged(0, getItemCount());
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.radio)
        RadioButton mRadioButton;

        @Bind(R.id.imageView)
        ImageView imageView;

        @Bind(R.id.textView_name)
        TextView nameTextView;

        @Bind(R.id.textView_progress)
        TextView progressTextView;

        @Bind(R.id.progress)
        ProgressBar progressBar;

        @Bind(R.id.textView_speed)
        TextView speedTextView;

        @Bind(R.id.imageView_status)
        ImageView statusImageView;

        @Bind(R.id.textView_status)
        TextView statusTextView;

        @Bind(R.id.layout_status)
        LinearLayout statusLayout;

        public ItemViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnCheckedListener {
        void onCheckChanged();
    }
}
