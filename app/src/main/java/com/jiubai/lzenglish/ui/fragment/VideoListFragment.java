package com.jiubai.lzenglish.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.ContentFrameLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jiubai.lzenglish.R;
import com.jiubai.lzenglish.adapter.VideoListAdapter;
import com.jiubai.lzenglish.bean.Cartoon;
import com.jiubai.lzenglish.config.Config;
import com.jiubai.lzenglish.presenter.GetCartoonInfoPresenterImpl;
import com.jiubai.lzenglish.ui.iview.IGetCartoonInfoView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Larry Liang on 17/05/2017.
 */

public class VideoListFragment extends Fragment implements IGetCartoonInfoView {

    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private VideoListAdapter mAdapter;

    private int ageGroupsIndex = 0;

    public VideoListFragment() {
    }

    @SuppressLint("ValidFragment")
    public VideoListFragment(int ageGroupsIndex) {
        this.ageGroupsIndex = ageGroupsIndex;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_detail, container, false);

        ButterKnife.bind(this, view);

        initView();

        new GetCartoonInfoPresenterImpl(this).getCartoonList(ageGroupsIndex);

        return view;
    }

    private void initView() {
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onGetCartoonListResult(boolean result, String info, Object extras) {
        if (result) {
            mAdapter = new VideoListAdapter(getActivity(), (ArrayList<Cartoon>) extras);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            Toast.makeText(getActivity(), info, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onGetCartoonSeasonListResult(boolean result, String info, Object extras) {

    }

    @Override
    public void onGetVideoListResult(boolean result, String info, Object extras) {

    }
}
