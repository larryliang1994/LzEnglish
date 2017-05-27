package com.jiubai.lzenglish.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jiubai.lzenglish.R;
import com.jiubai.lzenglish.adapter.HistoryAdapter;
import com.jiubai.lzenglish.adapter.RecommendAdapter;
import com.jiubai.lzenglish.common.StatusBarUtil;
import com.jiubai.lzenglish.config.Config;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HistoryActivity extends BaseActivity {

    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private HistoryAdapter mAdapter;
    private ArrayList<String> list = new ArrayList<>();

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

        initData(10);
        mAdapter = new HistoryAdapter(this, list);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initData(int num) {
        for(int i = 0; i < num; i++) {
            list.add("");
        }
    }

    @OnClick({R.id.imageView_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageView_back:
                onBackPressed();
                break;
        }
    }
}
