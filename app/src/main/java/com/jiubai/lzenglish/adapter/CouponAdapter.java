package com.jiubai.lzenglish.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jiubai.lzenglish.R;
import com.jiubai.lzenglish.bean.Coupon;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Larry Liang on 15/05/2017.
 */

public class CouponAdapter extends RecyclerView.Adapter {

    private ArrayList<Coupon> coupons;
    private Context context;

    private ItemClickListener itemClickListener;

    public int currentIndex = -1;

    public CouponAdapter(Context context, ArrayList<Coupon> list) {
        this.context = context;
        this.coupons = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_coupon, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ItemViewHolder viewHolder = (ItemViewHolder) holder;

        Coupon coupon = coupons.get(position);

        viewHolder.nameTextView.setText((int)coupon.getPrice() + "元代金券");

        if (position == currentIndex) {
            viewHolder.checkedImageView.setVisibility(View.VISIBLE);
        } else {
            viewHolder.checkedImageView.setVisibility(View.GONE);
        }

        if (position == coupons.size() - 1) {
            viewHolder.dividerView.setVisibility(View.GONE);
        } else {
            viewHolder.dividerView.setVisibility(View.VISIBLE);
        }

        viewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentIndex == position) {
                    currentIndex = -1;
                } else {
                    currentIndex = position;
                }

                notifyDataSetChanged();

                if (itemClickListener != null) {
                    itemClickListener.onItemClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return coupons.size();
    }

    public ItemClickListener getItemClickListener() {
        return itemClickListener;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.textView_name)
        TextView nameTextView;

        @Bind(R.id.imageView_checked)
        ImageView checkedImageView;

        @Bind(R.id.view_divider)
        View dividerView;

        @Bind(R.id.layout)
        LinearLayout layout;

        public ItemViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

    public interface ItemClickListener {
        void onItemClick(int position);
    }
}
