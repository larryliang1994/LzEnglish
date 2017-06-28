package com.jiubai.lzenglish.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jiubai.lzenglish.R;
import com.jiubai.lzenglish.bean.PrefetchVideo;
import com.jiubai.lzenglish.bean.Video;
import com.jiubai.lzenglish.common.UtilBox;
import com.jiubai.lzenglish.config.Config;
import com.jiubai.lzenglish.manager.DownloadManager;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.netopen.hotbitmapgg.library.view.RingProgressBar;
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

        int videoIndex = DownloadManager.getInstance().getPrefetchVideoByVideoId(video.getId());
        if (videoIndex == -1) {
            viewHolder.progressImageView.setVisibility(View.GONE);
            viewHolder.progressLayout.setVisibility(View.GONE);
        } else {
            PrefetchVideo prefetchVideo = DownloadManager.getInstance().getPrefetchVideos().get(videoIndex);

            if (prefetchVideo.getVideoStatus() == PrefetchVideo.VideoStatus.Downloaded) {
                viewHolder.progressLayout.setVisibility(View.GONE);
                viewHolder.progressImageView.setVisibility(View.VISIBLE);

                viewHolder.progressImageView.setBackgroundResource(R.drawable.circle_blue);
                viewHolder.progressImageView.setImageResource(R.drawable.check);
                viewHolder.progressImageView.setPadding(
                        UtilBox.dip2px(context, 3), UtilBox.dip2px(context, 3),
                        UtilBox.dip2px(context, 3), UtilBox.dip2px(context, 3));
            } else if (prefetchVideo.getVideoStatus() == PrefetchVideo.VideoStatus.Paused
                    || prefetchVideo.getVideoStatus() == PrefetchVideo.VideoStatus.Error) {
                viewHolder.progressLayout.setVisibility(View.GONE);
                viewHolder.progressImageView.setVisibility(View.VISIBLE);

                viewHolder.progressImageView.setBackgroundResource(R.drawable.circle_orange);
                viewHolder.progressImageView.setImageResource(R.drawable.downward);
                viewHolder.progressImageView.setPadding(
                        UtilBox.dip2px(context, 4), UtilBox.dip2px(context, 4),
                        UtilBox.dip2px(context, 4), UtilBox.dip2px(context, 4));
            } else {
                viewHolder.progressLayout.setVisibility(View.VISIBLE);
                viewHolder.progressImageView.setVisibility(View.GONE);

                viewHolder.progressBar.setMax(100);
                viewHolder.progressBar.setProgress(
                        (int) (prefetchVideo.getSoFarSize() * 1.0 / prefetchVideo.getTotalSize() * 100));
            }
        }

        if (video.isAllowWatch()) {
            viewHolder.lockImageView.setVisibility(View.GONE);
        } else {
            viewHolder.lockImageView.setVisibility(View.VISIBLE);
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = DownloadManager.getInstance().getPrefetchVideoByVideoId(video.getId());
                if (video.isAllowWatch() && index == -1) {
                    DownloadManager.getInstance().downloadVideo(video.getId(), video.getName(),
                            Config.ResourceUrl + video.getVideo(),
                            Config.ResourceUrl + video.getHeadImg());

                    notifyDataSetChanged();

                    if (listener != null) {
                        listener.onItemClick(position);
                    }
                } else if (index != -1) {
                    PrefetchVideo prefetchVideo = DownloadManager.getInstance().getPrefetchVideos().get(index);

                    if (prefetchVideo.getVideoStatus() == PrefetchVideo.VideoStatus.Paused) {
                        DownloadManager.getInstance().changeVideoStatus(prefetchVideo, PrefetchVideo.VideoStatus.Downloading);
                        notifyDataSetChanged();
                        Toast.makeText(context, "继续下载", Toast.LENGTH_SHORT).show();
                    } else if (prefetchVideo.getVideoStatus() == PrefetchVideo.VideoStatus.Downloading) {
                        DownloadManager.getInstance().changeVideoStatus(prefetchVideo, PrefetchVideo.VideoStatus.Paused);
                        notifyDataSetChanged();
                        Toast.makeText(context, "已暂停", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "该视频已缓存", Toast.LENGTH_SHORT).show();
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

        @Bind(R.id.circleImageView_progress)
        ImageView progressImageView;

        @Bind(R.id.imageView_lock)
        ImageView lockImageView;

        @Bind(R.id.progressBar)
        RingProgressBar progressBar;

        @Bind(R.id.layout_progress)
        RelativeLayout progressLayout;

        public ItemViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnStateChangeListener {
        void onItemClick(int position);
    }
}
