package com.jiubai.lzenglish.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiubai.lzenglish.R;
import com.jiubai.lzenglish.common.UtilBox;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Larry Liang on 15/05/2017.
 */

public class GuessRecommendAdapter extends RecyclerView.Adapter {

    private ArrayList<String> list;
    private Context context;

    public GuessRecommendAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recommend_guess, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder viewHolder = (ItemViewHolder) holder;

        if (position == getItemCount() - 1) {
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) viewHolder.itemView.getLayoutParams();
            params.setMargins(UtilBox.dip2px(context, 16), 0, UtilBox.dip2px(context, 16), 0);
            viewHolder.itemView.setLayoutParams(params);
        } else {
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) viewHolder.itemView.getLayoutParams();
            params.setMargins(UtilBox.dip2px(context, 16), 0, 0, 0);
            viewHolder.itemView.setLayoutParams(params);
        }

        if (position % 2 == 0) {
            viewHolder.imageView.setImageBitmap(UtilBox.readBitMap(context, R.drawable.bb));
            viewHolder.titleTextView.setText("小猪佩奇的故事");
            viewHolder.engTextView.setText("Peppa Pig");
            viewHolder.descTextView.setText("'' 最具教育意义情感动画 ''");
        } else {
            viewHolder.imageView.setImageBitmap(UtilBox.readBitMap(context, R.drawable.cc));
            viewHolder.titleTextView.setText("疯狂动物城动画");
            viewHolder.engTextView.setText("Crazy animals");
            viewHolder.descTextView.setText("'' 最具情感意义情感动画 ''");
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.imageView)
        ImageView imageView;

        @Bind(R.id.textView_title)
        TextView titleTextView;

        @Bind(R.id.textView_eng)
        TextView engTextView;

        @Bind(R.id.textView_desc)
        TextView descTextView;

        public ItemViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
