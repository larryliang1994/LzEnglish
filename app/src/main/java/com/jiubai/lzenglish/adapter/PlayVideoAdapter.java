package com.jiubai.lzenglish.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiubai.lzenglish.R;
import com.jiubai.lzenglish.bean.Video;
import com.jiubai.lzenglish.common.UtilBox;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.techery.properratingbar.ProperRatingBar;

/**
 * Created by Larry Liang on 16/05/2017.
 */

public class PlayVideoAdapter extends RecyclerView.Adapter {
    private ArrayList<Video> videoList;
    private Context context;
    private OnStateChangeListener listener;

    private int currentVideo = 0;

    public PlayVideoAdapter(Context context, ArrayList<Video> list) {
        this.context = context;
        this.videoList = list;
    }

    public void setCurrentVideo(int currentVideo) {
        this.currentVideo = currentVideo;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_play_video, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final Video video = videoList.get(position);

        final ItemViewHolder viewHolder = (ItemViewHolder) holder;

        if (currentVideo == position) {
            viewHolder.playImageView.setVisibility(View.VISIBLE);
            viewHolder.ePTextView.setText("");
        } else {
            viewHolder.playImageView.setVisibility(View.GONE);
            viewHolder.ePTextView.setText(position + 1 + "");
        }

        if (!video.isAllowWatch()) {
            viewHolder.progressImageView.setVisibility(View.VISIBLE);
            viewHolder.progressImageView.setImageResource(R.drawable.lock);
            viewHolder.progressImageView.setImageTintList(
                    ColorStateList.valueOf(Color.parseColor("#D7DADD")));
        } else {
            if (video.isHasFinishWatch()) { // 看完了
                viewHolder.progressImageView.setVisibility(View.VISIBLE);
                viewHolder.progressImageView.setImageResource(R.drawable.drop_full);
                viewHolder.progressImageView.setImageTintList(
                        ColorStateList.valueOf(Color.parseColor("#FA8919")));
            } else {
                if (video.getHasWatch() == null) { // 没看过
                    viewHolder.progressImageView.setVisibility(View.GONE);
                } else { // 看了一半
                    viewHolder.progressImageView.setVisibility(View.VISIBLE);
                    viewHolder.progressImageView.setImageResource(R.drawable.drop_half);
                    viewHolder.progressImageView.setImageTintList(
                            ColorStateList.valueOf(Color.parseColor("#FA8919")));
                }
            }
        }

        viewHolder.ratingBar.setRating(video.getLogCartoonItemScore());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (video.isAllowWatch()) {
                    currentVideo = position;

                    notifyDataSetChanged();

                    if (listener != null) {
                        listener.onItemClick(position);
                    }
                } else {
                    UtilBox.purchaseAlert(context, "您还未购买此视频", video.getIdCartoon());
                }
            }
        });
    }

    public OnStateChangeListener getListener() {
        return listener;
    }

    public void setListener(OnStateChangeListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.textView_ep)
        TextView ePTextView;

        @Bind(R.id.imageView_play)
        ImageView playImageView;

        @Bind(R.id.rating)
        ProperRatingBar ratingBar;

        @Bind(R.id.imageView_progress)
        ImageView progressImageView;

        public ItemViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnStateChangeListener {
        void onItemClick(int position);
    }
}
