package com.jiubai.lzenglish.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiubai.lzenglish.R;
import com.jiubai.lzenglish.bean.Season;
import com.jiubai.lzenglish.common.UtilBox;
import com.jiubai.lzenglish.config.Config;
import com.jiubai.lzenglish.ui.activity.PlayVideoActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Larry Liang on 16/05/2017.
 */

public class SeasonAdapter extends RecyclerView.Adapter {
    private ArrayList<Season> seasonList;
    private Context context;

    public SeasonAdapter(Context context, ArrayList<Season> list) {
        this.context = context;
        this.seasonList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_season, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        int width = (UtilBox.getWidthPixels(context) - UtilBox.dip2px(context, 16 + 16 + 16)) / 2;

        ItemViewHolder viewHolder = (ItemViewHolder) holder;

        ViewGroup.LayoutParams itemParams = viewHolder.itemView.getLayoutParams();
        itemParams.height = width;
        viewHolder.itemView.setLayoutParams(itemParams);

        final ViewGroup.LayoutParams image1Params = viewHolder.imageView1.getLayoutParams();
        image1Params.height = width - UtilBox.dip2px(context, 36);
        viewHolder.imageView1.setLayoutParams(image1Params);

        ViewGroup.LayoutParams image2Params = viewHolder.imageView2.getLayoutParams();
        image2Params.height = width - UtilBox.dip2px(context, 36);
        viewHolder.imageView2.setLayoutParams(image2Params);

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .displayer(new RoundedBitmapDisplayer(UtilBox.dip2px(context, 5)))
                .build();
        ImageLoader.getInstance().displayImage(
                Config.ResourceUrl + seasonList.get(position * 2).getImage(),
                viewHolder.imageView1, options);
        viewHolder.textView1.setText(seasonList.get(position * 2).getName());

        viewHolder.layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PlayVideoActivity.class);
                intent.putExtra("seasonId", seasonList.get(position * 2).get_itemId());
                UtilBox.startActivity((Activity) context, intent, false);
            }
        });

        if (seasonList.size() % 2 == 1 && position == seasonList.size() / 2) {
            viewHolder.layout2.setVisibility(View.INVISIBLE);

            viewHolder.layout2.setOnClickListener(null);
        } else {
            viewHolder.layout2.setVisibility(View.VISIBLE);

            ImageLoader.getInstance().displayImage(
                    Config.ResourceUrl + seasonList.get(position * 2 + 1).getImage(),
                    viewHolder.imageView2, options);
            viewHolder.textView2.setText(seasonList.get(position * 2 + 1).getName());

            viewHolder.layout2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PlayVideoActivity.class);
                    intent.putExtra("seasonId", seasonList.get(position * 2 + 1).get_itemId());
                    UtilBox.startActivity((Activity) context, intent, false);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return (int) Math.ceil(seasonList.size() / 2.0);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.textView1)
        TextView textView1;

        @Bind(R.id.textView2)
        TextView textView2;

        @Bind(R.id.imageView1)
        ImageView imageView1;

        @Bind(R.id.imageView2)
        ImageView imageView2;

        @Bind(R.id.layout1)
        FrameLayout layout1;

        @Bind(R.id.layout2)
        FrameLayout layout2;

        public ItemViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
