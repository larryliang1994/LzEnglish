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
import com.jiubai.lzenglish.bean.AgeRecommend;
import com.jiubai.lzenglish.common.UtilBox;
import com.jiubai.lzenglish.ui.activity.SeasonListActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Larry Liang on 15/05/2017.
 */

public class GuessRecommendAdapter extends RecyclerView.Adapter {

    private ArrayList<AgeRecommend> ageRecommends;
    private Context context;

    public GuessRecommendAdapter(Context context, ArrayList<AgeRecommend> list) {
        this.context = context;
        this.ageRecommends = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recommend_guess, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
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

//        if (position % 2 == 0) {
//            viewHolder.imageView.setImageBitmap(UtilBox.readBitMap(context, R.drawable.bb));
//        } else {
//            viewHolder.imageView.setImageBitmap(UtilBox.readBitMap(context, R.drawable.cc));
//        }

        final AgeRecommend ageRecommend = ageRecommends.get(position);

        ImageLoader.getInstance().displayImage(ageRecommend.getImage(), viewHolder.imageView);
        viewHolder.titleTextView.setText(ageRecommend.getMainTitle());
        viewHolder.engTextView.setText(ageRecommend.getSubTitle());
        viewHolder.descTextView.setText("'' " +  ageRecommend.getFooterText() + " ''");

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SeasonListActivity.class);
                intent.putExtra("cartoonId", ageRecommend.getId());
                UtilBox.startActivity((Activity) context, intent, false);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ageRecommends.size();
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
