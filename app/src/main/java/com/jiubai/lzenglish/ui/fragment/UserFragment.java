package com.jiubai.lzenglish.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jiubai.lzenglish.R;
import com.jiubai.lzenglish.adapter.SearchVideoAdapter;
import com.jiubai.lzenglish.common.UtilBox;
import com.jiubai.lzenglish.ui.activity.DownloadActivity;
import com.jiubai.lzenglish.ui.activity.HistoryActivity;
import com.jiubai.lzenglish.ui.activity.SearchVideoActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Larry Liang on 17/05/2017.
 */

public class UserFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        ButterKnife.bind(this, view);

        return view;
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
