package com.jiubai.lzenglish.ui.activity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jiubai.lzenglish.R;
import com.jiubai.lzenglish.adapter.HistoryAdapter;
import com.jiubai.lzenglish.bean.PrefetchVideo;
import com.jiubai.lzenglish.bean.WatchHistory;
import com.jiubai.lzenglish.common.StatusBarUtil;
import com.jiubai.lzenglish.common.UtilBox;
import com.jiubai.lzenglish.config.Config;
import com.jiubai.lzenglish.manager.WatchHistoryManager;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HistoryActivity extends BaseActivity implements HistoryAdapter.OnCheckedListener {

    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @Bind(R.id.layout_toolbar)
    LinearLayout mToolbarLayout;

    @Bind(R.id.textView_edit)
    TextView mEditTextView;

    @Bind(R.id.layout_bottom)
    LinearLayout mBottomLayout;

    @Bind(R.id.button_delete)
    Button mDeleteButton;

    private HistoryAdapter mAdapter;
    private WatchHistoryManager mWatchHistoryManager = WatchHistoryManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        StatusBarUtil.StatusBarLightMode(this);

        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mWatchHistoryManager.initTimeHeader();

        mAdapter = new HistoryAdapter(this);
        mAdapter.setListener(this);
        mRecyclerView.setAdapter(mAdapter);

        ViewGroup.LayoutParams params = mToolbarLayout.getLayoutParams();
        params.height = Config.AppbarHeight + Config.StatusbarHeight;
        mToolbarLayout.setLayoutParams(params);
        mToolbarLayout.setPadding(0, Config.StatusbarHeight, 0, 0);

        if (mWatchHistoryManager.watchHistoryList.size() == 0) {
            mEditTextView.setTextColor(Color.parseColor("#999999"));
            mEditTextView.setClickable(false);
        } else {
            mEditTextView.setTextColor(Color.parseColor("#151515"));
            mEditTextView.setClickable(true);
        }
    }

    @OnClick({R.id.imageView_back, R.id.textView_edit, R.id.button_check_all, R.id.button_delete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageView_back:
                onBackPressed();
                break;

            case R.id.button_check_all:
                boolean allChecked = false;

                for (int i = 0; i < mWatchHistoryManager.watchHistoryList.size(); i++) {
                    allChecked = true;
                    if (!mWatchHistoryManager.watchHistoryList.get(i).isChecked()) {
                        allChecked = false;
                        break;
                    }
                }

                for (int i = 0; i < mWatchHistoryManager.watchHistoryList.size(); i++) {
                    WatchHistory history = mWatchHistoryManager.watchHistoryList.get(i);
                    history.setChecked(!allChecked);

                    mWatchHistoryManager.watchHistoryList.set(i, history);
                }

                mDeleteButton.setEnabled(true);
                mDeleteButton.setTextColor(Color.parseColor(allChecked ? "#999999" : "#484848"));
                mAdapter.notifyDataSetChanged();
                break;

            case R.id.textView_edit:
                if (!mAdapter.editing) {
                    mAdapter.editing = true;
                    mAdapter.notifyDataSetChanged();

                    mBottomLayout.setVisibility(View.VISIBLE);

                    mEditTextView.setText("取消");
                } else {
                    mAdapter.editing = false;

                    for (int i = 0; i < mWatchHistoryManager.watchHistoryList.size(); i++) {
                        WatchHistory history = mWatchHistoryManager.watchHistoryList.get(i);
                        history.setChecked(false);

                        mWatchHistoryManager.watchHistoryList.set(i, history);
                    }

                    mAdapter.notifyDataSetChanged();

                    mBottomLayout.setVisibility(View.GONE);

                    mEditTextView.setText("编辑");
                }

                if (mWatchHistoryManager.watchHistoryList.size() != 0) {
//                    UtilBox.alert(this, "确定要清空观看历史？",
//                            "清空", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    mEditTextView.setTextColor(Color.parseColor("#999999"));
//                                    mEditTextView.setClickable(false);
//
//                                    mWatchHistoryManager.clearHistory();
//
//                                    mAdapter.notifyDataSetChanged();
//                                }
//                            },
//                            "取消", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                }
//                            });
                }
                break;

            case R.id.button_delete:
                if (mDeleteButton.isEnabled()) {
                    UtilBox.alert(this, "确定要删除选中的观看历史？",
                            "删除", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    mWatchHistoryManager.deleteCheckedHistory();
                                    mAdapter.editing = false;
                                    mAdapter.notifyDataSetChanged();
                                    mBottomLayout.setVisibility(View.GONE);
                                    mEditTextView.setText("编辑");
                                    mEditTextView.setTextColor(Color.parseColor("#151515"));
                                    mEditTextView.setClickable(false);
                                }
                            },
                            "取消", null);
                }
                break;
        }
    }

    @Override
    public void onCheckChanged() {
        for (WatchHistory history : mWatchHistoryManager.watchHistoryList) {
            if (history.isChecked()) {
                mDeleteButton.setEnabled(true);
                mDeleteButton.setTextColor(Color.parseColor("#484848"));
                return;
            }
        }

        mDeleteButton.setEnabled(false);
        mDeleteButton.setTextColor(Color.parseColor("#999999"));
    }

    @Override
    public void onBackPressed() {
        if (mAdapter.editing) {
            mAdapter.editing = false;

            for (int i = 0; i < mWatchHistoryManager.watchHistoryList.size(); i++) {
                WatchHistory history = mWatchHistoryManager.watchHistoryList.get(i);
                history.setChecked(false);

                mWatchHistoryManager.watchHistoryList.set(i, history);
            }

            mAdapter.notifyDataSetChanged();

            mBottomLayout.setVisibility(View.GONE);

            mEditTextView.setText("编辑");
        } else {
            super.onBackPressed();
        }
    }
}
