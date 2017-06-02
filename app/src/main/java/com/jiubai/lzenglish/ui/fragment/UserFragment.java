package com.jiubai.lzenglish.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jiubai.lzenglish.R;
import com.jiubai.lzenglish.common.UtilBox;
import com.jiubai.lzenglish.config.Config;
import com.jiubai.lzenglish.manager.WatchHistoryManager;
import com.jiubai.lzenglish.ui.activity.DownloadActivity;
import com.jiubai.lzenglish.ui.activity.HistoryActivity;
import com.jiubai.lzenglish.ui.activity.SearchVideoActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Larry Liang on 17/05/2017.
 */

public class UserFragment extends Fragment {

    @Bind(R.id.imageView_portrait)
    CircleImageView mPortraitImageView;

    @Bind(R.id.textView_userName)
    TextView mUserNameTextView;

    @Bind(R.id.appbar)
    AppBarLayout mAppBarLayout;

    @Bind(R.id.textView_history)
    TextView mHistoryTextView;

    private WatchHistoryManager mWatchHistoryManager = WatchHistoryManager.getInstance();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        ButterKnife.bind(this, view);

        initView();

        return view;
    }

    private void initView() {
        ImageLoader.getInstance().displayImage(Config.UserImage, mPortraitImageView);

        mUserNameTextView.setText(Config.UserName);

        mAppBarLayout.setPadding(0, Config.StatusbarHeight, 0, 0);

        if (mWatchHistoryManager.watchHistoryList.size() == 0) {
            mHistoryTextView.setText("");
        } else {
            mWatchHistoryManager.initTimeHeader();
            mHistoryTextView.setText(mWatchHistoryManager.watchHistoryList.get(1).getName());
        }
    }

    @OnClick({R.id.layout_history, R.id.layout_downloaded, R.id.imageView_search})
    public void onClick(View view) {
        Intent intent;

        switch (view.getId()) {
            case R.id.layout_history:
                intent = new Intent(getActivity(), HistoryActivity.class);
                UtilBox.startActivity(getActivity(), intent, false);
                break;

            case R.id.layout_downloaded:
                intent = new Intent(getActivity(), DownloadActivity.class);
                UtilBox.startActivity(getActivity(), intent, false);
                break;

            case R.id.imageView_search:
                intent = new Intent(getActivity(), SearchVideoActivity.class);
                UtilBox.startActivity(getActivity(), intent, false);
                break;
        }
    }
}
