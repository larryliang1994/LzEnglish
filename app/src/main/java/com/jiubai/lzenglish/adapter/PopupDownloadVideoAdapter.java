package com.jiubai.lzenglish.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jiubai.lzenglish.R;
import com.jiubai.lzenglish.bean.Video;
import com.jiubai.lzenglish.config.Config;
import com.jiubai.lzenglish.net.DownloadManager;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.techery.properratingbar.ProperRatingBar;

/**
 * Created by Larry Liang on 16/05/2017.
 */

public class PopupDownloadVideoAdapter extends RecyclerView.Adapter {
    private ArrayList<Video> videoList;
    private Context context;
    private OnStateChangeListener listener;

    public PopupDownloadVideoAdapter(Context context, ArrayList<Video> list) {
        this.context = context;
        this.videoList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_popup_download_video, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final Video video = videoList.get(position);

        final ItemViewHolder viewHolder = (ItemViewHolder) holder;

        viewHolder.playImageView.setVisibility(View.GONE);
        viewHolder.ePTextView.setText(position + 1 + "");
        viewHolder.ratingBar.setRating(video.getLogCartoonItemScore());

        if (DownloadManager.getInstance().getPrefetchVideoByVideoId(video.getId()) == -1) {
            viewHolder.progressImageView.setVisibility(View.GONE);
        } else {
            viewHolder.progressImageView.setVisibility(View.VISIBLE);
        }

        if (video.isAllowWatch()) {
            viewHolder.lockImageView.setVisibility(View.GONE);
        } else {
            viewHolder.lockImageView.setVisibility(View.VISIBLE);
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (video.isAllowWatch()) {
                    DownloadManager.getInstance().downloadVideo(video.getId(), video.getName(),
                            Config.ResourceUrl + video.getVideo(),
                            Config.ResourceUrl + video.getHeadImg());

                    notifyDataSetChanged();

                    if (listener != null) {
                        listener.onItemClick(position);
                    }
                } else {
                    Toast.makeText(context, "您还未购买该视频", Toast.LENGTH_SHORT).show();
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

        @Bind(R.id.imageView_lock)
        ImageView lockImageView;

        public ItemViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnStateChangeListener {
        void onItemClick(int position);
    }
}
