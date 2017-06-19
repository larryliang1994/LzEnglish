package com.jiubai.lzenglish.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.NestedScrollView;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.badoo.mobile.util.WeakHandler;
import com.danikula.videocache.HttpProxyCacheServer;
import com.jiubai.lzenglish.App;
import com.jiubai.lzenglish.R;
import com.jiubai.lzenglish.adapter.PlayVideoAdapter;
import com.jiubai.lzenglish.adapter.PlayVideoRecommendAdapter;
import com.jiubai.lzenglish.adapter.PopupDownloadVideoAdapter;
import com.jiubai.lzenglish.bean.DetailedSeason;
import com.jiubai.lzenglish.bean.PrefetchVideo;
import com.jiubai.lzenglish.bean.Video;
import com.jiubai.lzenglish.bean.WatchHistory;
import com.jiubai.lzenglish.common.StatusBarUtil;
import com.jiubai.lzenglish.common.UtilBox;
import com.jiubai.lzenglish.config.Config;
import com.jiubai.lzenglish.manager.DownloadManager;
import com.jiubai.lzenglish.manager.WatchHistoryManager;
import com.jiubai.lzenglish.net.DownloadUtil;
import com.jiubai.lzenglish.presenter.GetCartoonInfoPresenterImpl;
import com.jiubai.lzenglish.ui.iview.IGetCartoonInfoView;
import com.jiubai.lzenglish.widget.JCVideoPlayerStandard;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fm.jiecao.jcvideoplayer_lib.JCMediaManager;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import me.shaohui.bottomdialog.BottomDialog;

import static android.view.View.FOCUS_UP;

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

    @Bind(R.id.imageView_back)
    ImageView mBackImageView;

    @Bind(R.id.scrollView)
    NestedScrollView mScrollView;

    private ArrayList<String> list = new ArrayList<>();
    private PlayVideoAdapter mVideoAdapter;
    private PlayVideoRecommendAdapter mRecommendAdapter;

    private DownloadManager mDownloadManager;

    private WeakHandler handler;
    private WeakHandler popupHandler;

    private DetailedSeason mDetailedSeason;
    private ArrayList<Video> videoList;

    private int videoId;
    private int currentVideoIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);

        StatusBarUtil.StatusBarDarkMode(this, Config.DeviceType);

        ButterKnife.bind(this);

        mDownloadManager = DownloadManager.getInstance();

        videoId = getIntent().getIntExtra("videoId", 0);

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

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mBackImageView.getLayoutParams();
        params.setMargins(0, Config.StatusbarHeight, 0, 0);
        mBackImageView.setLayoutParams(params);

        new GetCartoonInfoPresenterImpl(this).getVideoList(videoId);
    }

    @Override
    public void onGetVideoListResult(boolean result, String info, Object extras) {
        if (result) {
            mDetailedSeason = (DetailedSeason) extras;

            videoList = (ArrayList<Video>) mDetailedSeason.getVideoList();

            for (int i = 0; i < videoList.size(); i++) {
                if (videoList.get(i).getId() == videoId) {
                    currentVideoIndex = i;
                    break;
                }
            }

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

            mScrollView.fullScroll(FOCUS_UP);

            handler = new WeakHandler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message message) {
                    Log.i("text", "working");

                    PlayVideoActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (JCMediaManager.instance().mediaPlayer.isPlaying()) {
                                    Log.i("text", JCMediaManager.instance().mediaPlayer.getCurrentPosition() / 1000
                                            + " -- " + JCMediaManager.instance().mediaPlayer.getDuration() / 1000);

                                    Video video = videoList.get(currentVideoIndex);

                                    WatchHistory watchHistory = new WatchHistory(
                                            video.getId(),
                                            video.getName(),
                                            JCMediaManager.instance().mediaPlayer.getDuration(),
                                            JCMediaManager.instance().mediaPlayer.getCurrentPosition(),
                                            -99,
                                            new Date().getTime(),
                                            video.getHeadImg(),
                                            false
                                    );

                                    if (currentVideoIndex + 1 < videoList.size()
                                            && videoList.get(currentVideoIndex + 1).isAllowWatch()) {
                                        watchHistory.setNextVideoId(videoList.get(currentVideoIndex + 1).getId());
                                    }

                                    WatchHistoryManager.getInstance().saveHistory(watchHistory);

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
                            } catch (IllegalStateException e) {
                                e.printStackTrace();
                                JCMediaManager.instance().mediaPlayer = new MediaPlayer();
                            }
                        }
                    });

                    handler.sendEmptyMessageDelayed(0, 5000);

                    return false;
                }
            });

            handler.sendEmptyMessage(0);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mCoverView.setVisibility(View.GONE);

                    UtilBox.dismissLoading(false);
                }
            }, 500);
        } else {
            UtilBox.dismissLoading(false);

            UtilBox.alert(this, info,
                    "重试", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new GetCartoonInfoPresenterImpl(PlayVideoActivity.this).getVideoList(videoId);
                        }
                    },
                    "返回", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            UtilBox.returnActivity(PlayVideoActivity.this);
                        }
                    });
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

        int index = mDownloadManager.getPrefetchVideoByVideoId(videoList.get(position).getId());

        if (index != -1
                && mDownloadManager.getPrefetchVideos().get(index).getVideoStatus()
                == PrefetchVideo.VideoStatus.Downloaded) {
            Log.i("prefetch", DownloadUtil.getFileName(mDownloadManager.getPrefetchVideos().get(index).getVideoId() + ".mp4"));
            mJVideoPlayer.setUp(
                    DownloadUtil.getFileName(mDownloadManager.getPrefetchVideos().get(index).getVideoId() + ".mp4"),
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

    //@OnClick({R.id.layout_ep})
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
                .setDimAmount(0.5f)
                .setHeight(UtilBox.getHeightPixels(this) - UtilBox.dip2px(this, 180 + 40))
                .setViewListener(new BottomDialog.ViewListener() {
                    @Override
                    public void bindView(View v) {
                        RecyclerView recyclerView = v.findViewById(R.id.recyclerView_popup);
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(PlayVideoActivity.this, 5);
                        recyclerView.setLayoutManager(gridLayoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());

                        final Button checkDownloadButton = v.findViewById(R.id.button_check_downloaded);

                        final PopupDownloadVideoAdapter adapter = new PopupDownloadVideoAdapter(
                                PlayVideoActivity.this, (ArrayList<Video>) mDetailedSeason.getVideoList());
                        adapter.setListener(new PopupDownloadVideoAdapter.OnStateChangeListener() {
                            @Override
                            public void onItemClick(int position) {
                                checkDownloadButton.setText("查看缓存视频(" + mDownloadManager.getPrefetchVideos().size() + ")");
                                Toast.makeText(PlayVideoActivity.this, "正在缓存...", Toast.LENGTH_SHORT).show();
                            }
                        });
                        recyclerView.setAdapter(adapter);

                        popupHandler = new WeakHandler(new Handler.Callback() {
                            @Override
                            public boolean handleMessage(Message msg) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (adapter != null) {
                                            adapter.notifyDataSetChanged();
                                        }
                                    }
                                });

                                popupHandler.sendEmptyMessageDelayed(0, 1000);
                                return false;
                            }
                        });

                        popupHandler.sendEmptyMessage(0);

                        v.findViewById(R.id.imageView_close).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                        if (mDownloadManager.getPrefetchVideos().size() == 0) {
                            checkDownloadButton.setText("查看缓存视频");
                        } else {
                            checkDownloadButton.setText("查看缓存视频(" + mDownloadManager.getPrefetchVideos().size() + ")");
                        }

                        checkDownloadButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                Intent intent = new Intent(PlayVideoActivity.this, DownloadActivity.class);
                                UtilBox.startActivity(PlayVideoActivity.this, intent, false);
                            }
                        });

                        final Button downloadAllButton = v.findViewById(R.id.button_download_all);

                        boolean hasDownloadedAll = true;

                        for (int i = 0; i < mDetailedSeason.getVideoList().size(); i++) {
                            if (DownloadManager.getInstance().getPrefetchVideoByVideoId(mDetailedSeason.getVideoList().get(i).getId()) == -1
                                    && mDetailedSeason.getVideoList().get(i).isAllowWatch()) {
                                hasDownloadedAll = false;
                                break;
                            }
                        }

                        if (hasDownloadedAll) {
                            downloadAllButton.setTextColor(Color.parseColor("#999999"));
                            downloadAllButton.setEnabled(false);
                            downloadAllButton.setClickable(false);
                        } else {
                            downloadAllButton.setTextColor(Color.parseColor("#484848"));
                            downloadAllButton.setEnabled(true);
                            downloadAllButton.setClickable(true);

                            downloadAllButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    UtilBox.alert(PlayVideoActivity.this,
                                            "确定要下载所有未缓存的视频？",
                                            "下载", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    for (int j = 0; j < mDetailedSeason.getVideoList().size(); j++) {
                                                        Video video = mDetailedSeason.getVideoList().get(j);

                                                        if (DownloadManager.getInstance().getPrefetchVideoByVideoId(video.getId()) == -1
                                                                && video.isAllowWatch()) {
                                                            DownloadManager.getInstance().downloadVideo(video.getId(), video.getName(),
                                                                    Config.ResourceUrl + video.getVideo(),
                                                                    Config.ResourceUrl + video.getHeadImg());
                                                        }
                                                    }

                                                    downloadAllButton.setTextColor(Color.parseColor("#484848"));
                                                    downloadAllButton.setEnabled(false);
                                                    downloadAllButton.setClickable(false);

                                                    adapter.notifyDataSetChanged();

                                                    checkDownloadButton.setText("查看缓存视频(" + mDownloadManager.getPrefetchVideos().size() + ")");
                                                }
                                            },
                                            "取消", null);
                                }
                            });
                        }
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
                    UtilBox.purchaseAlert(this, "您还未购买跟读功能", videoList.get(0).getIdCartoon());
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 99:
                if (resultCode == RESULT_OK) {
                    UtilBox.showLoading(this, false);

                    new GetCartoonInfoPresenterImpl(this).getVideoList(videoId);
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

        UtilBox.returnActivity(this);
    }

    @Override
    public void onResume() {
        if (TextUtils.isEmpty(Config.ThirdSession)) {
            finish();
        }

        super.onResume();
        if (handler != null) {
            handler.sendEmptyMessage(0);
        }
        if (popupHandler != null) {
            popupHandler.sendEmptyMessage(0);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
        if (handler != null) {
            handler.removeMessages(0);
        }
        if (popupHandler != null) {
            popupHandler.removeMessages(0);
        }
    }

    @Override
    protected void onDestroy() {
        if (handler != null) {
            handler.removeMessages(0);
        }
        if (popupHandler != null) {
            popupHandler.removeMessages(0);
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
