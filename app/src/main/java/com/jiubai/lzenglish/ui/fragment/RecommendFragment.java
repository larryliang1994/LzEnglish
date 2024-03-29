package com.jiubai.lzenglish.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jiubai.lzenglish.R;
import com.jiubai.lzenglish.adapter.GuessRecommendAdapter;
import com.jiubai.lzenglish.adapter.RecommendAdapter;
import com.jiubai.lzenglish.bean.AgeRecommend;
import com.jiubai.lzenglish.bean.InterestRecommend;
import com.jiubai.lzenglish.common.UtilBox;
import com.jiubai.lzenglish.config.Config;
import com.jiubai.lzenglish.presenter.RecommendPresenterImpl;
import com.jiubai.lzenglish.ui.activity.DownloadActivity;
import com.jiubai.lzenglish.ui.activity.HistoryActivity;
import com.jiubai.lzenglish.ui.activity.SearchVideoActivity;
import com.jiubai.lzenglish.ui.iview.AppBarStateChangeListener;
import com.jiubai.lzenglish.ui.iview.IRecommendView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RecommendFragment extends Fragment implements IRecommendView {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @Bind(R.id.textView_search)
    TextView mSearchTextView;

    @Bind(R.id.imageView_downloaded)
    ImageView mDownloadedImageView;

    @Bind(R.id.imageView_history)
    ImageView mHistoryImageView;

    @Bind(R.id.layout_toolbar)
    LinearLayout mToolbarLayout;

    @Bind(R.id.recyclerView_guess)
    RecyclerView mGuessRecyclerView;

    @Bind(R.id.appbar)
    AppBarLayout mAppBarLayout;

    @Bind(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout mCollapsingToolbarLayout;

    @Bind(R.id.imageView_banner)
    ImageView mBannerImageView;

    @Bind(R.id.layout_guess)
    FrameLayout mGuessLayout;

    private RecommendAdapter mAdapter;
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

        mAppBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state, int offset) {
                if (state == State.COLLAPSED) {
                    // 折叠状态
                    mHistoryImageView.setImageTintList(ColorStateList.valueOf(Color.WHITE));
                    mDownloadedImageView.setImageTintList(ColorStateList.valueOf(Color.WHITE));
                    mSearchTextView.setTextColor(Color.WHITE);
                } else {
                    mHistoryImageView.setImageTintList(ColorStateList.valueOf(Color.parseColor("#DBDBDB")));
                    mDownloadedImageView.setImageTintList(ColorStateList.valueOf(Color.parseColor("#DBDBDB")));
                    mSearchTextView.setTextColor(Color.parseColor("#999999"));
                }
            }
        });

        mBannerImageView.setImageBitmap(UtilBox.readBitMap(getActivity(), R.drawable.banner));

        new RecommendPresenterImpl(this).getHomeInfo();
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

        final LinearLayoutManager guessLinearLayoutManager = new LinearLayoutManager(
                getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mGuessRecyclerView.setLayoutManager(guessLinearLayoutManager);
        mGuessRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onGetHomeInfoResult(boolean result, String info, Object extras) {
        if (result) {
            Object[] objects = (Object[]) extras;

            ArrayList<AgeRecommend> ageRecommends = (ArrayList<AgeRecommend>) objects[0];
            ArrayList<InterestRecommend> interestRecommends = (ArrayList<InterestRecommend>) objects[1];

            mAdapter = new RecommendAdapter(getActivity(), interestRecommends);
            mRecyclerView.setAdapter(mAdapter);

            if (ageRecommends.size() == 0) {
                mGuessLayout.setVisibility(View.GONE);
            } else {
                mGuessLayout.setVisibility(View.VISIBLE);
                mGuessAdapter = new GuessRecommendAdapter(getActivity(), ageRecommends);
                mGuessRecyclerView.setAdapter(mGuessAdapter);
            }
        } else {
            UtilBox.alert(getActivity(), info,
                    "重试", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new RecommendPresenterImpl(RecommendFragment.this).getHomeInfo();
                        }
                    },
                    "", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
        }
    }
}
