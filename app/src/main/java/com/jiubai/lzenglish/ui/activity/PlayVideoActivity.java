package com.jiubai.lzenglish.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.danikula.videocache.HttpProxyCacheServer;
import com.jiubai.lzenglish.App;
import com.jiubai.lzenglish.R;
import com.jiubai.lzenglish.adapter.PlayVideoAdapter;
import com.jiubai.lzenglish.adapter.PlayVideoRecommendAdapter;
import com.jiubai.lzenglish.adapter.PopupDownloadVideoAdapter;
import com.jiubai.lzenglish.bean.DetailedSeason;
import com.jiubai.lzenglish.bean.PrefetchVideo;
import com.jiubai.lzenglish.bean.Video;
import com.jiubai.lzenglish.common.StatusBarUtil;
import com.jiubai.lzenglish.common.UtilBox;
import com.jiubai.lzenglish.config.Config;
import com.jiubai.lzenglish.net.DownloadManager;
import com.jiubai.lzenglish.net.DownloadUtil;
import com.jiubai.lzenglish.presenter.GetCartoonInfoPresenterImpl;
import com.jiubai.lzenglish.ui.iview.IGetCartoonInfoView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fm.jiecao.jcvideoplayer_lib.JCMediaManager;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import me.shaohui.bottomdialog.BottomDialog;

public class PlayVideoActivity extends BaseActivity implements IGetCartoonInfoView, PlayVideoAdapter.OnStateChangeListener {

    @Bind(R.id.videoplayer)
    JCVideoPlayerStandard mJVideoPlayer;

    @Bind(R.id.recyclerView_video)
    RecyclerView mVideoRecyclerView;

    @Bind(R.id.recyclerView_recommend)
    RecyclerView mRecommendRecyclerView;

    @Bind(R.id.layout_abstract_content)
    LinearLayout mAbstractContentLayout;

    @Bind(R.id.textView_abstract_content)
    TextView mAbstractContentTextView;

    @Bind(R.id.imageView_abstract)
    ImageView mAbstractImageView;

    @Bind(R.id.textView_ep)
    TextView mEPTextView;

    @Bind(R.id.imageView_ep)
    ImageView mEPImageView;

    @Bind(R.id.textView_title)
    TextView mTitleTextView;

    @Bind(R.id.textView_keywords)
    TextView mKeywordsTextView;

    @Bind(R.id.view_cover)
    View mCoverView;

    @Bind(R.id.imageView_lock)
    ImageView mLockImageView;

    @Bind(R.id.textView_abstract_text)
    TextView mAbstractTextTextView;

    @Bind(R.id.framelayout_bottom)
    FrameLayout mBottomLayout;

    private ArrayList<String> list = new ArrayList<>();
    private PlayVideoAdapter mVideoAdapter;
    private PlayVideoRecommendAdapter mRecommendAdapter;

    private DetailedSeason mDetailedSeason;
    private ArrayList<Video> videoList;

    private int seasonId;
    private int currentVideoIndex = 0;

    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);

        StatusBarUtil.StatusBarDarkMode(this, Config.DeviceType);

        ButterKnife.bind(this);

        seasonId = getIntent().getIntExtra("seasonId", 0);

        initView();
    }

    private void initView() {
        UtilBox.showLoading(this, false);

        mCoverView.setVisibility(View.VISIBLE);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 5);
        mVideoRecyclerView.setLayoutManager(gridLayoutManager);
        mVideoRecyclerView.setItemAnimator(new DefaultItemAnimator());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecommendRecyclerView.setLayoutManager(linearLayoutManager);
        mRecommendRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecommendAdapter = new PlayVideoRecommendAdapter(this, list);
        mRecommendRecyclerView.setAdapter(mRecommendAdapter);

        mJVideoPlayer.setUp("", JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "");

        new GetCartoonInfoPresenterImpl(this).getVideoList(seasonId);
    }

    @Override
    public void onGetVideoListResult(boolean result, String info, Object extras) {
        if (result) {
            mDetailedSeason = (DetailedSeason) extras;

            videoList = (ArrayList<Video>) mDetailedSeason.getVideoList();

            mTitleTextView.setText(videoList.get(currentVideoIndex).getName());
            mKeywordsTextView.setText(mDetailedSeason.getSeoKeywords());
            mKeywordsTextView.setVisibility(View.GONE);
            mAbstractContentTextView.setText(videoList.get(currentVideoIndex).getNote());

            setupPlayer(currentVideoIndex);

            mVideoAdapter = new PlayVideoAdapter(this, (ArrayList<Video>) mDetailedSeason.getVideoList());
            mVideoAdapter.setListener(this);
            mVideoAdapter.setCurrentVideo(currentVideoIndex);
            mVideoRecyclerView.setAdapter(mVideoAdapter);

            mEPTextView.setText("第1集/共" + mDetailedSeason.getVideoList().size() + "集");

            if (TextUtils.isEmpty(mAbstractContentTextView.getText())) {
                mAbstractTextTextView.setVisibility(View.GONE);
                mAbstractImageView.setVisibility(View.GONE);
            }

            if (videoList.get(currentVideoIndex).isAllowReview()) {
                mLockImageView.setVisibility(View.GONE);
            } else {
                mLockImageView.setVisibility(View.VISIBLE);
            }

            if (videoList.get(currentVideoIndex).isHasReview()) {
                mBottomLayout.setVisibility(View.VISIBLE);
            } else {
                mBottomLayout.setVisibility(View.GONE);
            }

            if (timer != null) {
                timer.cancel();
            }
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    PlayVideoActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (JCMediaManager.instance().mediaPlayer.isPlaying()) {
                                    if (JCMediaManager.instance().mediaPlayer.getCurrentPosition() * 1.0
                                            / JCMediaManager.instance().mediaPlayer.getDuration() >= 0.8) {
                                        new GetCartoonInfoPresenterImpl(null).finishedWatched(
                                                videoList.get(currentVideoIndex).getId());
                                    } else {
                                        new GetCartoonInfoPresenterImpl(null).saveWatchHistory(
                                                videoList.get(currentVideoIndex).getId(),
                                                JCMediaManager.instance().mediaPlayer.getCurrentPosition());
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }, 5000, 5000);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mCoverView.setVisibility(View.GONE);

                    UtilBox.dismissLoading(false);
                }
            }, 500);
        } else {
            UtilBox.dismissLoading(false);

            UtilBox.returnActivity(this);

            Toast.makeText(this, info, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemClick(int position) {
        currentVideoIndex = position;

        mEPTextView.setText("第" + (position + 1) + "集/共" + mDetailedSeason.getVideoList().size() + "集");

        setupPlayer(position);

        mTitleTextView.setText(videoList.get(position).getName());
        mKeywordsTextView.setText(mDetailedSeason.getSeoKeywords());
        mKeywordsTextView.setVisibility(View.GONE);
        mAbstractContentTextView.setText(videoList.get(position).getNote());

        if (videoList.get(position).isHasReview()) {
            mBottomLayout.setVisibility(View.VISIBLE);
        } else {
            mBottomLayout.setVisibility(View.GONE);
        }

        if (videoList.get(position).isAllowReview()) {
            mLockImageView.setVisibility(View.GONE);
        } else {
            mLockImageView.setVisibility(View.VISIBLE);
        }
    }

    private void setupPlayer(int position) {

        int index = DownloadManager.getInstance().getPrefetchVideoByVideoId(videoList.get(position).getId());

        if (index != -1
                && DownloadManager.getInstance().getPrefetchVideos().get(index).getVideoStatus()
                == PrefetchVideo.VideoStatus.Downloaded) {
            Log.i("prefetch", DownloadUtil.getFileName(DownloadManager.getInstance().getPrefetchVideos().get(index).getVideoId() + ".mp4"));
            mJVideoPlayer.setUp(
                    DownloadUtil.getFileName(DownloadManager.getInstance().getPrefetchVideos().get(index).getVideoId() + ".mp4"),
                    JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "");
        } else {
            HttpProxyCacheServer proxy = App.getProxy(this);
            String proxyUrl = proxy.getProxyUrl(Config.ResourceUrl + videoList.get(position).getVideo());
            mJVideoPlayer.setUp(proxyUrl, JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "");
        }
        mJVideoPlayer.thumbImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ImageLoader.getInstance().displayImage(Config.ResourceUrl + videoList.get(position).getHeadImg(), mJVideoPlayer.thumbImageView);
    }

    @OnClick({R.id.layout_abstract})
    public void setAbstractVisibility(View view) {
        if (TextUtils.isEmpty(mAbstractContentTextView.getText())) {
            mAbstractTextTextView.setVisibility(View.GONE);
            mAbstractImageView.setVisibility(View.GONE);
        } else {
            if (mAbstractContentTextView.getVisibility() == View.VISIBLE) {
                mAbstractContentTextView.setVisibility(View.GONE);
                mAbstractContentLayout.setVisibility(View.GONE);
                mAbstractImageView.setImageResource(R.drawable.to_right);
            } else {
                mAbstractContentTextView.setVisibility(View.VISIBLE);
                mAbstractContentLayout.setVisibility(View.VISIBLE);
                mAbstractImageView.setImageResource(R.drawable.to_down);
            }
        }
    }

    @OnClick({R.id.layout_ep})
    public void setEPHeight(View view) {
        if (mVideoRecyclerView.isNestedScrollingEnabled()) {
            UtilBox.setViewParams(mVideoRecyclerView, mVideoRecyclerView.getWidth(), UtilBox.dip2px(this, 68));
            mEPImageView.setImageResource(R.drawable.to_right);
            mVideoRecyclerView.setNestedScrollingEnabled(false);
        } else {
            ViewGroup.LayoutParams layoutParams = mVideoRecyclerView.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            mVideoRecyclerView.setLayoutParams(layoutParams);
            mEPImageView.setImageResource(R.drawable.to_down);
            mVideoRecyclerView.setNestedScrollingEnabled(true);
        }
    }

    @OnClick({R.id.layout_download})
    public void showDownloadPopup(View view) {
        final BottomDialog dialog = BottomDialog.create(getSupportFragmentManager());

        dialog.setLayoutRes(R.layout.popup_download_video)
                .setHeight(UtilBox.getHeightPixels(this) - UtilBox.dip2px(this, 180 + 40))
                .setViewListener(new BottomDialog.ViewListener() {
                    @Override
                    public void bindView(View v) {
                        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView_popup);
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(PlayVideoActivity.this, 5);
                        recyclerView.setLayoutManager(gridLayoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());

                        final Button checkDownloadButton = (Button)v.findViewById(R.id.button_check_downloaded);

                        PopupDownloadVideoAdapter adapter = new PopupDownloadVideoAdapter(
                                PlayVideoActivity.this, (ArrayList<Video>) mDetailedSeason.getVideoList());
                        adapter.setListener(new PopupDownloadVideoAdapter.OnStateChangeListener() {
                            @Override
                            public void onItemClick(int position) {
                                checkDownloadButton.setText("查看缓存视频(" + DownloadManager.getInstance().getPrefetchVideos().size() + ")");
                                Toast.makeText(PlayVideoActivity.this, "正在缓存...", Toast.LENGTH_SHORT).show();
                            }
                        });
                        recyclerView.setAdapter(adapter);

                        v.findViewById(R.id.imageView_close).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                        if (DownloadManager.getInstance().getPrefetchVideos().size() == 0) {
                            checkDownloadButton.setText("查看缓存视频");
                        } else {
                            checkDownloadButton.setText("查看缓存视频(" + DownloadManager.getInstance().getPrefetchVideos().size() + ")");
                        }

                        checkDownloadButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                Intent intent = new Intent(PlayVideoActivity.this, DownloadActivity.class);
                                UtilBox.startActivity(PlayVideoActivity.this, intent, false);
                            }
                        });
                    }
                })
                .show();
    }

    private boolean listening = false;

    @OnClick({R.id.button_shadowing, R.id.imageView_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageView_back:
                onBackPressed();
                break;

            case R.id.button_shadowing:
                if (mDetailedSeason.isAllowShadowing()) {
                    Intent intent = new Intent(this, ShadowingActivity.class);

                    intent.putExtra("videoUrl", videoList.get(currentVideoIndex).getVideo());
                    intent.putExtra("videoId", videoList.get(currentVideoIndex).getId());
                    intent.putExtra("videoImage", Config.ResourceUrl + videoList.get(currentVideoIndex).getHeadImg());

                    startActivityForResult(intent, 99);
                    overridePendingTransition(R.anim.in_right_left, R.anim.out_right_left);
                } else {
                    Toast.makeText(this, "您还未购买跟读", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 99:
                if(resultCode == RESULT_OK){
                    UtilBox.showLoading(this, false);

                    new GetCartoonInfoPresenterImpl(this).getVideoList(seasonId);
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

    @Override
    protected void onDestroy() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }

        super.onDestroy();
    }

    @Override
    public void onGetCartoonListResult(boolean result, String info, Object extras) {

    }

    @Override
    public void onGetCartoonSeasonListResult(boolean result, String info, Object extras) {

    }
}
