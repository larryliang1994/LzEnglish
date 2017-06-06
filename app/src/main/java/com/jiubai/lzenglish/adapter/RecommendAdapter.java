package com.jiubai.lzenglish.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jiubai.lzenglish.R;
import com.jiubai.lzenglish.common.UtilBox;
import com.jiubai.lzenglish.config.Constants;
import com.jiubai.lzenglish.ui.activity.PlayVideoActivity;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Larry Liang on 15/05/2017.
 */

public class RecommendAdapter extends RecyclerView.Adapter {

    private ArrayList<String> list;
    private Context context;

    private boolean noMore = false;

    public RecommendAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == Constants.ListHeader) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_recommend_header, parent, false);
            return new HeaderViewHolder(view);
        } else if (viewType == Constants.ListItem) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_recommend_video, parent, false);
            return new ItemViewHolder(view);
        } else if (viewType == Constants.ListFooter) {
            View view = LayoutInflater.from(context).inflate(R.layout.bottom_progressbar, parent, false);
            return new PresentViewHolder(view);
        } else if (viewType == Constants.ListNoMore) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_recommend_footer, parent, false);
            return new PresentViewHolder(view);
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position != 0 && position != list.size() + 1) {
            ItemViewHolder viewHolder = (ItemViewHolder) holder;
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PlayVideoActivity.class);
                    UtilBox.startActivity((Activity) context, intent, false);
                }
            });

            if (position % 3 == 1) {
                viewHolder.videoImageView.setImageBitmap(UtilBox.readBitMap(context, R.drawable.recommend_video_example));
            } else if (position % 3 == 2) {
                viewHolder.videoImageView.setImageBitmap(UtilBox.readBitMap(context, R.drawable.a));
            } else if (position % 3 == 0) {
                viewHolder.videoImageView.setImageBitmap(UtilBox.readBitMap(context, R.drawable.b));
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size() + 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return Constants.ListHeader;
        } else if (position == list.size() + 1) {
            return noMore ? Constants.ListNoMore : Constants.ListFooter;
        } else {
            return Constants.ListItem;
        }
    }

    public void setNoMore() {
        noMore = true;
    }

    public boolean getNoMore() {
        return noMore;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.imageView_video)
        ImageView videoImageView;

        public ItemViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }

    class PresentViewHolder extends RecyclerView.ViewHolder {


        public PresentViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
