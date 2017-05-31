package com.jiubai.lzenglish.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jiubai.lzenglish.R;
import com.jiubai.lzenglish.bean.Cartoon;
import com.jiubai.lzenglish.common.UtilBox;
import com.jiubai.lzenglish.ui.activity.SeasonListActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Larry Liang on 16/05/2017.
 */

public class VideoListAdapter extends RecyclerView.Adapter {
    private ArrayList<Cartoon> cartoonList;
    private Context context;

    public VideoListAdapter(Context context, ArrayList<Cartoon> cartoonList) {
        this.context = context;
        this.cartoonList = cartoonList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_video_list, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ItemViewHolder viewHolder = (ItemViewHolder) holder;

        ImageLoader.getInstance().displayImage(cartoonList.get(position).get_image(), viewHolder.imageView);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SeasonListActivity.class);
                intent.putExtra("cartoonId", cartoonList.get(position).getId());
                UtilBox.startActivity((Activity) context, intent, false);
            }
        });

        if (position == getItemCount() - 1) {
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) viewHolder.itemView.getLayoutParams();
            params.setMargins(0, 0, 0, UtilBox.dip2px(context, 16));
            viewHolder.itemView.setLayoutParams(params);
        } else if (position == 0) {
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) viewHolder.itemView.getLayoutParams();
            params.setMargins(0, UtilBox.dip2px(context, 8), 0, 0);
            viewHolder.itemView.setLayoutParams(params);
        }
    }

    @Override
    public int getItemCount() {
        return cartoonList.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.imageView)
        ImageView imageView;

        public ItemViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
