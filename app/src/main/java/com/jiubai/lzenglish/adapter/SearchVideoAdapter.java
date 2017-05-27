package com.jiubai.lzenglish.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jiubai.lzenglish.R;
import com.jiubai.lzenglish.bean.Cartoon;
import com.jiubai.lzenglish.common.UtilBox;
import com.jiubai.lzenglish.config.Constants;
import com.jiubai.lzenglish.ui.activity.PlayVideoActivity;
import com.jiubai.lzenglish.ui.activity.SeasonListActivity;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Larry Liang on 15/05/2017.
 */

public class SearchVideoAdapter extends RecyclerView.Adapter {

    private ArrayList<Cartoon> list;
    private Context context;
    private boolean editing = false;
    private String hint;

    public SearchVideoAdapter(Context context, ArrayList<Cartoon> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (!editing) {
            if (viewType == Constants.ListHeader) {
                View view = LayoutInflater.from(context).inflate(R.layout.item_search_history_header, null);
                return new HeaderViewHolder(view);
            } else {
                View view = LayoutInflater.from(context).inflate(R.layout.item_search_history, parent, false);
                return new HistoryItemViewHolder(view);
            }
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_search_video, parent, false);
            return new ItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (!editing) {
            if (holder instanceof HistoryItemViewHolder) {
                HistoryItemViewHolder viewHolder = (HistoryItemViewHolder) holder;

                if (position == getItemCount() - 1 && list.size() % 2 == 1) {
                    viewHolder.mHistoryTextView2.setVisibility(View.INVISIBLE);
                }

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, PlayVideoActivity.class);
                        UtilBox.startActivity((Activity) context, intent, true);
                    }
                });
            }
        } else {
            ItemViewHolder viewHolder = (ItemViewHolder)holder;

            String text = list.get(position).getName();
            viewHolder.mVideoNameTextView.setText(text);

            ForegroundColorSpan span = new ForegroundColorSpan(ContextCompat.getColor(context, R.color.colorPrimary));

            SpannableStringBuilder builder = new SpannableStringBuilder(viewHolder.mVideoNameTextView.getText().toString());
            builder.setSpan(span, text.indexOf(hint), text.indexOf(hint) + hint.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            viewHolder.mVideoNameTextView.setText(builder);

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, SeasonListActivity.class);
                    intent.putExtra("cartoonId", list.get(position).getId());
                    UtilBox.startActivity((Activity) context, intent, true);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return editing ? list.size() : (int)(Math.ceil(list.size() / 2.0) + 1);
    }

    @Override
    public int getItemViewType(int position) {
        if (editing) {
            return Constants.ListItem;
        } else {
            if (position == 0) {
                return Constants.ListHeader;
            } else {
                return Constants.ListItem;
            }
        }
    }

    public void setEditing(boolean editing) {
        this.editing = editing;
    }

    public boolean getEditing() {
        return editing;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }

    class HistoryItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.textView_history_1)
        TextView mHistoryTextView1;

        @Bind(R.id.textView_history_2)
        TextView mHistoryTextView2;

        public HistoryItemViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.textView_video_name)
        TextView mVideoNameTextView;

        public ItemViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
