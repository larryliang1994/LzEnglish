package com.jiubai.lzenglish.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jiubai.lzenglish.R;
import com.jiubai.lzenglish.adapter.SeasonAdapter;
import com.jiubai.lzenglish.bean.Cartoon;
import com.jiubai.lzenglish.bean.Season;
import com.jiubai.lzenglish.common.StatusBarUtil;
import com.jiubai.lzenglish.common.UtilBox;
import com.jiubai.lzenglish.config.Config;
import com.jiubai.lzenglish.presenter.GetCartoonInfoPresenterImpl;
import com.jiubai.lzenglish.ui.iview.IGetCartoonInfoView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SeasonListActivity extends AppCompatActivity implements IGetCartoonInfoView {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @Bind(R.id.textView_abstract)
    TextView mAbstractTextView;

    @Bind(R.id.textView_title)
    TextView mTitleTextView;

    @Bind(R.id.view_cover)
    View mCoverView;

    private SeasonAdapter mAdapter;
    private int cartoonId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_season_list);

        StatusBarUtil.StatusBarDarkMode(this, Config.DeviceType);

        ButterKnife.bind(this);

        cartoonId = getIntent().getIntExtra("cartoonId", 0);

        initView();
    }

    private void initView() {
        UtilBox.showLoading(this, false);

        mCoverView.setVisibility(View.VISIBLE);

        setSupportActionBar(mToolbar);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UtilBox.returnActivity(SeasonListActivity.this);
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        new GetCartoonInfoPresenterImpl(this).getCartoonSeason(cartoonId);
    }

    @Override
    public void onGetCartoonSeasonListResult(boolean result, String info, Object extras) {
        if (result) {
            Cartoon cartoon = (Cartoon) extras;

            mAbstractTextView.setText(cartoon.getBriefInfo());
            mTitleTextView.setText(cartoon.getName());

            mAdapter = new SeasonAdapter(this, (ArrayList<Season>) cartoon.getSeasonList());
            mRecyclerView.setAdapter(mAdapter);

            mCoverView.setVisibility(View.GONE);

            UtilBox.dismissLoading(false);
        } else {
            UtilBox.dismissLoading(false);

            UtilBox.returnActivity(this);

            Toast.makeText(this, info, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onGetVideoListResult(boolean result, String info, Object extras) {

    }

    @Override
    public void onGetCartoonListResult(boolean result, String info, Object extras) {

    }
}