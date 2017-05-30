package com.jiubai.lzenglish.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jiubai.lzenglish.R;
import com.jiubai.lzenglish.common.UtilBox;
import com.jiubai.lzenglish.config.Constants;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Larry Liang on 15/05/2017.
 */

public class HistoryAdapter extends RecyclerView.Adapter {

    private ArrayList<String> list;
    private Context context;

    public HistoryAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == Constants.ListHeader) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_history_header, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_history, parent, false);
            return new ItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == 0 || position == 3 || position == 7) {

            HeaderViewHolder viewHolder = (HeaderViewHolder) holder;

            if (position == 0) {
                viewHolder.mDataTextView.setText("今天");
                viewHolder.mDotImageView.setImageTintList(ColorStateList.valueOf(Color.parseColor("#FA8919")));
            } else if (position == 3) {
                viewHolder.mDataTextView.setText("昨天");
                viewHolder.mDotImageView.setImageTintList(ColorStateList.valueOf(Color.parseColor("#1B95B5")));
            } else {
                viewHolder.mDataTextView.setText("更早");
                viewHolder.mDotImageView.setImageTintList(ColorStateList.valueOf(Color.parseColor("#CACACA")));
            }

        } else {
            ItemViewHolder viewHolder = (ItemViewHolder) holder;

            if (position == 1 || position == 4 || position == 8) {
                viewHolder.itemView.setPadding(
                        UtilBox.dip2px(context, 16), UtilBox.dip2px(context, 24),
                        UtilBox.dip2px(context, 16), UtilBox.dip2px(context, 8));
            } else if (position == 2 || position == 6 || position == 11) {
                viewHolder.itemView.setPadding(
                        UtilBox.dip2px(context, 16), UtilBox.dip2px(context, 8),
                        UtilBox.dip2px(context, 16), UtilBox.dip2px(context, 24));
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size() + 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 || position == 3 || position == 7) {
            return Constants.ListHeader;
        } else {
            return Constants.ListItem;
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        public ItemViewHolder(View itemView) {
            super(itemView);
        }
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.textView_date)
        TextView mDataTextView;

        @Bind(R.id.imageView_dot)
        CircleImageView mDotImageView;

        public HeaderViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

    class PresentViewHolder extends RecyclerView.ViewHolder {
        public PresentViewHolder(View itemView) {
            super(itemView);
        }
    }
}
