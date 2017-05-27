package com.jiubai.lzenglish.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.jiubai.lzenglish.R;
import com.jiubai.lzenglish.common.UtilBox;
import com.jiubai.lzenglish.ui.activity.PlayVideoActivity;

import java.util.ArrayList;

/**
 * Created by Larry Liang on 16/05/2017.
 */

public class PlayVideoRecommendAdapter extends RecyclerView.Adapter {

    private ArrayList<String> list;
    private Context context;

    public PlayVideoRecommendAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_play_video_recommend, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder viewHolder = (ItemViewHolder) holder;

        if (position == 0) {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(UtilBox.dip2px(context, 16), UtilBox.dip2px(context, 4),
                    UtilBox.dip2px(context, 4), UtilBox.dip2px(context, 4));
            viewHolder.itemView.setLayoutParams(lp);
        } else if (position == getItemCount() - 1) {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(UtilBox.dip2px(context, 4), UtilBox.dip2px(context, 4),
                    UtilBox.dip2px(context, 16), UtilBox.dip2px(context, 4));
            viewHolder.itemView.setLayoutParams(lp);
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PlayVideoActivity.class);
                UtilBox.startActivity((Activity) context, intent, false);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        public ItemViewHolder(View itemView) {
            super(itemView);
        }
    }
}
