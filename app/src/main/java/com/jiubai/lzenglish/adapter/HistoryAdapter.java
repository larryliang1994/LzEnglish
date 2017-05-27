package com.jiubai.lzenglish.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.jiubai.lzenglish.R;
import com.jiubai.lzenglish.common.UtilBox;
import com.jiubai.lzenglish.config.Constants;

import java.util.ArrayList;

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
            return;
        } else {
            ItemViewHolder viewHolder = (ItemViewHolder) holder;

            if (position == 1) {
                viewHolder.itemView.setPadding(
                        UtilBox.dip2px(context, 16), UtilBox.dip2px(context, 24),
                        UtilBox.dip2px(context, 16), UtilBox.dip2px(context, 8));
            } else if (position == 2) {
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
        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }

    class PresentViewHolder extends RecyclerView.ViewHolder {
        public PresentViewHolder(View itemView) {
            super(itemView);
        }
    }
}
