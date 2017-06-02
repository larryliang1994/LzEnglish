package com.jiubai.lzenglish.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiubai.lzenglish.R;
import com.jiubai.lzenglish.bean.WatchHistory;
import com.jiubai.lzenglish.common.UtilBox;
import com.jiubai.lzenglish.config.Config;
import com.jiubai.lzenglish.config.Constants;
import com.jiubai.lzenglish.manager.WatchHistoryManager;
import com.jiubai.lzenglish.ui.activity.PlayVideoActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Larry Liang on 15/05/2017.
 */

public class HistoryAdapter extends RecyclerView.Adapter {

    private WatchHistoryManager watchHistoryManager;
    private Context context;

    public HistoryAdapter(Context context) {
        this.context = context;
        this.watchHistoryManager = WatchHistoryManager.getInstance();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == Constants.ListHeader) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_history_header, parent, false);
            return new HeaderViewHolder(view);
        } else if (viewType == Constants.ListItem) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_history, parent, false);
            return new ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_history_footer, parent, false);
            return new FooterViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == getItemCount() - 1) {
            FooterViewHolder viewHolder = (FooterViewHolder) holder;

            if (watchHistoryManager.watchHistoryList.size() == 0) {
                viewHolder.itemView.setVisibility(View.GONE);
            } else {
                viewHolder.itemView.setVisibility(View.VISIBLE);
            }

            return;
        }

        final WatchHistory watchHistory = watchHistoryManager.watchHistoryList.get(position);

        if (watchHistory.getVideoId() == -100
                || watchHistory.getVideoId() == -101 || watchHistory.getVideoId() == -102) {

            HeaderViewHolder viewHolder = (HeaderViewHolder) holder;

            if (watchHistory.getVideoId() == -100) {
                viewHolder.mDataTextView.setText("今天");
                viewHolder.mDotImageView.setImageResource(R.drawable.circle_orange);
                viewHolder.topView.setVisibility(View.GONE);
                viewHolder.bottomView.setVisibility(View.VISIBLE);
            } else if (watchHistory.getVideoId() == -101) {
                viewHolder.mDataTextView.setText("昨天");
                viewHolder.mDotImageView.setImageResource(R.drawable.circle_blue);

                if (position == 0) {
                    viewHolder.topView.setVisibility(View.GONE);
                    viewHolder.bottomView.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.topView.setVisibility(View.VISIBLE);
                    viewHolder.bottomView.setVisibility(View.VISIBLE);
                }
            } else if (watchHistory.getVideoId() == -102) {
                viewHolder.mDataTextView.setText("更早");
                viewHolder.mDotImageView.setImageResource(R.drawable.circle_gray);
                if (position == 0) {
                    viewHolder.topView.setVisibility(View.GONE);
                    viewHolder.bottomView.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.topView.setVisibility(View.VISIBLE);
                    viewHolder.bottomView.setVisibility(View.VISIBLE);
                }
            }
        } else {
            ItemViewHolder viewHolder = (ItemViewHolder) holder;

            DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
                    .displayer(new RoundedBitmapDisplayer(UtilBox.dip2px(context, 2)))
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .build();

            ImageLoader.getInstance().displayImage(
                    Config.ResourceUrl + watchHistory.getImage(), viewHolder.imageView, displayImageOptions);

            viewHolder.nameTextView.setText(watchHistory.getName());

            final int progress = (int) (watchHistory.getWatchedTime() * 1.0 / watchHistory.getTotalTime() * 100);
            if (progress >= 80) {
                viewHolder.progressTextView.setText("已经看完");
                if (watchHistory.getNextVideoId() != -99) {
                    viewHolder.tipTextView.setText("下集");
                } else {
                    viewHolder.tipTextView.setText("回看");
                }
            } else {
                viewHolder.progressTextView.setText("观看至" + progress + "%");
                viewHolder.tipTextView.setText("回看");
            }

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (progress >= 80 && watchHistory.getNextVideoId() != -99) {
                        Intent intent = new Intent(context, PlayVideoActivity.class);
                        intent.putExtra("seasonId", watchHistory.getNextVideoId());
                        UtilBox.startActivity((Activity) context, intent, false);
                    } else {
                        Intent intent = new Intent(context, PlayVideoActivity.class);
                        intent.putExtra("seasonId", watchHistory.getVideoId());
                        UtilBox.startActivity((Activity) context, intent, false);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return watchHistoryManager.watchHistoryList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return Constants.ListFooter;
        }

        WatchHistory watchHistory = watchHistoryManager.watchHistoryList.get(position);

        if (watchHistory.getVideoId() == -100
                || watchHistory.getVideoId() == -101 || watchHistory.getVideoId() == -102) {
            return Constants.ListHeader;
        } else {
            return Constants.ListItem;
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.imageView)
        ImageView imageView;

        @Bind(R.id.textView_name)
        TextView nameTextView;

        @Bind(R.id.textView_progress)
        TextView progressTextView;

        @Bind(R.id.textView_tip)
        TextView tipTextView;

        public ItemViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.textView_date)
        TextView mDataTextView;

        @Bind(R.id.imageView_dot)
        ImageView mDotImageView;

        @Bind(R.id.view_top)
        View topView;

        @Bind(R.id.view_bottom)
        View bottomView;

        public HeaderViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {

        public FooterViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
