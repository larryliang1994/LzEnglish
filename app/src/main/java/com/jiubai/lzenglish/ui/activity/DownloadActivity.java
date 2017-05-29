package com.jiubai.lzenglish.ui.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jiubai.lzenglish.R;
import com.jiubai.lzenglish.adapter.DownloadAdapter;
import com.jiubai.lzenglish.bean.PrefetchVideo;
import com.jiubai.lzenglish.common.StatusBarUtil;
import com.jiubai.lzenglish.common.UtilBox;
import com.jiubai.lzenglish.config.Config;
import com.jiubai.lzenglish.manager.DownloadManager;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DownloadActivity extends BaseActivity implements DownloadAdapter.OnCheckedListener {

    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @Bind(R.id.textView_memory)
    TextView mMemoryTextView;

    @Bind(R.id.textView_prefetchSize)
    TextView mPrefetchSizeTextView;

    @Bind(R.id.view_used)
    View mUsedView;

    @Bind(R.id.layout_bottom)
    LinearLayout mBottomLayout;

    @Bind(R.id.textView_edit)
    TextView mEditTextView;

    @Bind(R.id.button_delete)
    Button mDeleteButton;

    @Bind(R.id.layout_toolbar)
    LinearLayout mToolbarLayout;

    private DownloadAdapter mAdapter;
    private DownloadManager mDownloadManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_download);

        StatusBarUtil.StatusBarLightMode(this);

        ButterKnife.bind(this);

        mDownloadManager = DownloadManager.getInstance();

        initView();
    }

    private void initView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new DownloadAdapter(this, this);
        mRecyclerView.setAdapter(mAdapter);

        setMemoryInfo();

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mToolbarLayout.getLayoutParams();
        params.height = Config.AppbarHeight + Config.StatusbarHeight;
        mToolbarLayout.setLayoutParams(params);
        mToolbarLayout.setPadding(0, Config.StatusbarHeight, 0, 0);

        if (mDownloadManager.getPrefetchVideos().size() == 0) {
            mEditTextView.setTextColor(ContextCompat.getColor(this, R.color.lightText));
            mEditTextView.setClickable(false);
        } else {
            mEditTextView.setTextColor(ContextCompat.getColor(this, R.color.primaryText));
            mEditTextView.setClickable(true);
        }
    }

    private void setMemoryInfo() {
        double prefetchSize = 0;
        for (PrefetchVideo prefetchVideo : mDownloadManager.getPrefetchVideos()) {
            prefetchSize += prefetchVideo.getTotalSize();
        }

        double totalMemory = UtilBox.getAvailableInternalMemorySize();

        mPrefetchSizeTextView.setText(String.format("%.2f", prefetchSize / 1024.0 / 1024.0) + "M");
        mMemoryTextView.setText("剩余" + String.format("%.2f", totalMemory) + "G可用");
        mUsedView.getLayoutParams().width = (int) ((
                prefetchSize / 1024.0 / 1024.0 / totalMemory) * UtilBox.getWidthPixels(this));
    }

    @OnClick({R.id.textView_edit, R.id.button_check_all, R.id.imageView_back, R.id.button_delete, R.id.textView_memory})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textView_memory:
                String url1 = "http://180.153.105.144/dd.myapp.com/16891/E2F3DEBB12A049ED921C6257C5E9FB11.apk";
                String url2 = "http://180.153.105.144/dd.myapp.com/16891/E2F3DEBB12A049ED921C6257C5E9FB11.apk";
                String url3 = "http://180.153.105.144/dd.myapp.com/16891/E2F3DEBB12A049ED921C6257C5E9FB11.apk";
                String image = "http://images5.fanpop.com/image/photos/25800000/Miku-hatsune-miku-25858641-1280-1024.jpg";

                mDownloadManager.downloadVideo(8870, "小笨熊1", url1, image);
                mDownloadManager.downloadVideo(8871, "小笨熊2", url2, image);
                mDownloadManager.downloadVideo(8872, "小笨熊3", url3, image);
                break;

            case R.id.textView_edit:

                if (!mAdapter.editing) {
                    mAdapter.editing = true;
                    mAdapter.notifyDataSetChanged();

                    mBottomLayout.setVisibility(View.VISIBLE);

                    mEditTextView.setText("取消");
                } else {
                    mAdapter.editing = false;

                    for (int i = 0; i < mDownloadManager.getPrefetchVideos().size(); i++) {
                        PrefetchVideo prefetchVideo = mDownloadManager.getPrefetchVideos().get(i);
                        prefetchVideo.setChecked(false);

                        mDownloadManager.getPrefetchVideos().set(i, prefetchVideo);
                    }

                    mAdapter.notifyDataSetChanged();

                    mBottomLayout.setVisibility(View.GONE);

                    mEditTextView.setText("编辑");
                }
                break;

            case R.id.button_check_all:
                boolean allChecked = false;

                for (int i = 0; i < mDownloadManager.getPrefetchVideos().size(); i++) {
                    allChecked = true;
                    if (!mDownloadManager.getPrefetchVideos().get(i).isChecked()) {
                        allChecked = false;
                        break;
                    }
                }

                for (int i = 0; i < mDownloadManager.getPrefetchVideos().size(); i++) {
                    PrefetchVideo prefetchVideo = mDownloadManager.getPrefetchVideos().get(i);
                    prefetchVideo.setChecked(!allChecked);

                    mDownloadManager.getPrefetchVideos().set(i, prefetchVideo);
                }

                mDeleteButton.setEnabled(true);
                mDeleteButton.setTextColor(ContextCompat.getColor(this, allChecked ? R.color.lightText : R.color.mainText));
                mAdapter.notifyDataSetChanged();
                break;

            case R.id.imageView_back:
                mAdapter.editing = false;
                onBackPressed();
                break;

            case R.id.button_delete:
                if (mDeleteButton.isEnabled()) {
                    UtilBox.alert(this, "确定要删除所有视频？",
                            "删除", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    mDownloadManager.deleteCheckedVideo();
                                    mAdapter.editing = false;
                                    mAdapter.notifyDataSetChanged();
                                    mBottomLayout.setVisibility(View.GONE);
                                    mEditTextView.setText("编辑");
                                    mEditTextView.setTextColor(ContextCompat.getColor(DownloadActivity.this, R.color.lightText));
                                    mEditTextView.setClickable(false);
                                    setMemoryInfo();
                                }
                            },
                            "取消", null);
                }
                break;
        }
    }

    @Override
    public void onCheckChanged() {
        for (PrefetchVideo prefetchVideo : mDownloadManager.getPrefetchVideos()) {
            if (prefetchVideo.isChecked()) {
                mDeleteButton.setEnabled(true);
                mDeleteButton.setTextColor(ContextCompat.getColor(this, R.color.mainText));
                return;
            }
        }

        mDeleteButton.setEnabled(false);
        mDeleteButton.setTextColor(ContextCompat.getColor(this, R.color.lightText));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            onBackPressed();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if (mAdapter.editing) {
            mAdapter.editing = false;

            for (int i = 0; i < mDownloadManager.getPrefetchVideos().size(); i++) {
                PrefetchVideo prefetchVideo = mDownloadManager.getPrefetchVideos().get(i);
                prefetchVideo.setChecked(false);

                mDownloadManager.getPrefetchVideos().set(i, prefetchVideo);
            }

            mAdapter.notifyDataSetChanged();

            mBottomLayout.setVisibility(View.GONE);

            mEditTextView.setText("编辑");
        } else {
            super.onBackPressed();
        }
    }
}
