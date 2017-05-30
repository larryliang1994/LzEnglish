package com.jiubai.lzenglish.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jiubai.lzenglish.R;
import com.jiubai.lzenglish.adapter.GuessRecommendAdapter;
import com.jiubai.lzenglish.adapter.RecommendAdapter;
import com.jiubai.lzenglish.common.UtilBox;
import com.jiubai.lzenglish.config.Config;
import com.jiubai.lzenglish.ui.activity.DownloadActivity;
import com.jiubai.lzenglish.ui.activity.HistoryActivity;
import com.jiubai.lzenglish.ui.activity.SearchVideoActivity;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RecommendFragment extends Fragment {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @Bind(R.id.textView_search)
    TextView mSearchTextView;

    @Bind(R.id.layout_toolbar)
    LinearLayout mToolbarLayout;

    @Bind(R.id.viewPager_guess)
    ViewPager mGuessViewPager;

    private boolean loading = false;
    private RecommendAdapter mAdapter;
    private ArrayList<String> list = new ArrayList<>();

    private ArrayList<Fragment> fragments;
    private GuessRecommendAdapter mGuessAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recommend, container, false);

        ButterKnife.bind(this, view);

        initView();

        return view;
    }

    private void initView() {
        setupRecyclerView();

        CoordinatorLayout.LayoutParams params = new CoordinatorLayout.LayoutParams(
                CoordinatorLayout.LayoutParams.MATCH_PARENT, Config.AppbarHeight);
        params.setMargins(0, Config.StatusbarHeight, 0, 0);
        mToolbarLayout.setLayoutParams(params);

        CollapsingToolbarLayout.LayoutParams layoutParams = new CollapsingToolbarLayout.LayoutParams(
                CollapsingToolbarLayout.LayoutParams.MATCH_PARENT, Config.AppbarHeight);
        layoutParams.setMargins(0, Config.StatusbarHeight, 0, 0);
        mToolbar.setLayoutParams(layoutParams);

        fragments = new ArrayList<>();
        fragments.add(new GuessRecommendFragment());
        fragments.add(new GuessRecommendFragment());
        fragments.add(new GuessRecommendFragment());
        fragments.add(new GuessRecommendFragment());
        fragments.add(new GuessRecommendFragment());

        mGuessAdapter = new GuessRecommendAdapter(getActivity(), getChildFragmentManager(), fragments);
        //mGuessViewPager.setPageMargin(UtilBox.dip2px(getActivity(), 16));
        mGuessViewPager.setAdapter(mGuessAdapter);
    }

    @OnClick({R.id.textView_search, R.id.imageView_history, R.id.imageView_downloaded})
    public void onClick(View view) {
        Intent intent;

        switch (view.getId()) {
            case R.id.textView_search:
                intent = new Intent(getActivity(), SearchVideoActivity.class);
                UtilBox.startActivityWithTransition(getActivity(), intent, false, mSearchTextView, "search");
                break;

            case R.id.imageView_history:
                intent = new Intent(getActivity(), HistoryActivity.class);
                UtilBox.startActivity(getActivity(), intent, false);
                break;

            case R.id.imageView_downloaded:
                intent = new Intent(getActivity(), DownloadActivity.class);
                UtilBox.startActivity(getActivity(), intent, false);
                break;
        }
    }

    private void setupRecyclerView() {
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView,
                                   int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int totalItemCount = linearLayoutManager.getItemCount();
                int lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                if (!mAdapter.getNoMore() && !loading && totalItemCount <= lastVisibleItem + 1) {
                    loading = true;
                    loadMore();
                }
            }
        });

        initData(10);
        mAdapter = new RecommendAdapter(getActivity(), list);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void loadMore() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initData(5);
                mAdapter.notifyDataSetChanged();
                loading = false;
            }
        }, 2000);
    }

    private void initData(int num) {
        if (list.size() >= 20) {
            mAdapter.setNoMore();
            return;
        }

        for(int i = 0; i < num; i++) {
            list.add("");
        }
    }
}
