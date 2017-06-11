package com.jiubai.lzenglish.ui.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.danikula.videocache.HttpProxyCacheServer;
import com.jiubai.lzenglish.App;
import com.jiubai.lzenglish.R;
import com.jiubai.lzenglish.adapter.ShadowingAdapter;
import com.jiubai.lzenglish.bean.PrefetchVideo;
import com.jiubai.lzenglish.bean.Record;
import com.jiubai.lzenglish.bean.Shadowing;
import com.jiubai.lzenglish.bean.Voice;
import com.jiubai.lzenglish.common.StatusBarUtil;
import com.jiubai.lzenglish.common.UtilBox;
import com.jiubai.lzenglish.config.Config;
import com.jiubai.lzenglish.manager.DownloadManager;
import com.jiubai.lzenglish.net.DownloadUtil;
import com.jiubai.lzenglish.presenter.ShadowingPresenterImpl;
import com.jiubai.lzenglish.ui.iview.IShadowingView;
import com.jiubai.lzenglish.widget.JCVideoPlayerStandard;
import com.jiubai.lzenglish.widget.recorder.AudioPlayer;
import com.jiubai.lzenglish.widget.recorder.manager.AudioRecordButton;
import com.jiubai.lzenglish.widget.recorder.manager.MediaManager;
import com.jiubai.lzenglish.widget.recorder.utils.PermissionHelper;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

public class ShadowingActivity extends AppCompatActivity implements IShadowingView {

    @Bind(R.id.videoplayer)
    JCVideoPlayerStandard jcVideoPlayerStandard;

    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @Bind(R.id.button)
    AudioRecordButton mRecordButton;

    @Bind(R.id.view_video_cover)
    View mVideoCoverView;

    @Bind(R.id.view_cover)
    View mCoverView;

    @Bind(R.id.imageView_back)
    ImageView mBackImageView;

    private ArrayList<String> list = new ArrayList<>();
    private ShadowingAdapter mAdapter;

    private List<Record> mRecords = new ArrayList<>();
    private PermissionHelper mHelper;

    private DownloadManager mDownloadManager;

    private ArrayList<Shadowing> shadowingList;

    private int videoId;
    private String videoUrl;
    private String videoImage;
    private int currentShadowingIndex;

    private boolean changed = false;

    private View.OnTouchListener trueTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return true;
        }
    };

    private View.OnTouchListener falseTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shadowing);

        StatusBarUtil.StatusBarDarkMode(this, Config.DeviceType);

        ButterKnife.bind(this);

        videoId = getIntent().getIntExtra("videoId", 0);
        videoUrl = getIntent().getStringExtra("videoUrl");
        videoImage = getIntent().getStringExtra("videoImage");

        mDownloadManager = DownloadManager.getInstance();

        initView();

        initListener();

        new ShadowingPresenterImpl(this).getShadowingList(videoId);
    }

    private void initView() {
        UtilBox.showLoading(this, false);

        mCoverView.setVisibility(View.VISIBLE);

        jcVideoPlayerStandard.setUp("", JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "");

        initPlayer();
        mVideoCoverView.setOnTouchListener(trueTouchListener);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mBackImageView.getLayoutParams();
        params.setMargins(0, Config.StatusbarHeight, 0, 0);
        mBackImageView.setLayoutParams(params);
    }

    private void initPlayer() {
        int index = mDownloadManager.getPrefetchVideoByVideoId(videoId);

        if (index != -1
                && mDownloadManager.getPrefetchVideos().get(index).getVideoStatus()
                == PrefetchVideo.VideoStatus.Downloaded) {
            Log.i("prefetch", DownloadUtil.getFileName(mDownloadManager.getPrefetchVideos().get(index).getVideoId() + ".mp4"));
            jcVideoPlayerStandard.setUp(
                    DownloadUtil.getFileName(mDownloadManager.getPrefetchVideos().get(index).getVideoId() + ".mp4"),
                    JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "");
        } else {
            HttpProxyCacheServer proxy = App.getProxy(this);
            String proxyUrl = proxy.getProxyUrl(Config.ResourceUrl + videoUrl);
            jcVideoPlayerStandard.setUp(proxyUrl
                    , JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "");
        }

        jcVideoPlayerStandard.progressBar.setOnTouchListener(trueTouchListener);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, 0);
        jcVideoPlayerStandard.progressBar.setLayoutParams(params);
        jcVideoPlayerStandard.fullscreenButton.setLayoutParams(params);
        jcVideoPlayerStandard.currentTimeTextView.setLayoutParams(params);
        jcVideoPlayerStandard.totalTimeTextView.setLayoutParams(params);

        //RelativeLayout.LayoutParams buttonParams = new RelativeLayout.LayoutParams(0, 0);
        //jcVideoPlayerStandard.startButton.setLayoutParams(buttonParams);

        jcVideoPlayerStandard.thumbImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ImageLoader.getInstance().displayImage(videoImage, jcVideoPlayerStandard.thumbImageView);
    }

    private void initListener() {
        mRecordButton.setHasRecordPromission(false);

        mHelper = new PermissionHelper(this);
        mHelper.requestPermissions("请授予[录音]，[读写]权限，否则无法录音",
                new PermissionHelper.PermissionListener() {
                    @Override
                    public void doAfterGrand(String... permission) {
                        mRecordButton.setHasRecordPromission(true);
                        mRecordButton.setAudioFinishRecorderListener(new AudioRecordButton.AudioFinishRecorderListener() {
                            @Override
                            public void onFinished(float seconds, String filePath) {
                                Voice voice = new Voice(
                                        -99,
                                        new Date().getTime(),
                                        0,
                                        shadowingList.get(currentShadowingIndex).getIdCartoonItem(),
                                        shadowingList.get(currentShadowingIndex).getId(),
                                        -1, -1, -1, -1,
                                        "", "",
                                        (int) seconds <= 0 ? 1 : (int) seconds,
                                        filePath,
                                        "",
                                        new Date().getTime() + "",
                                        "",
                                        filePath
                                );

                                Log.i("text", filePath);

                                shadowingList.get(currentShadowingIndex).getVoiceList().add(voice);

                                mAdapter.initIndex();

                                mAdapter.notifyDataSetChanged();

                                mRecyclerView.smoothScrollToPosition(
                                        mAdapter.arrangeIndex.lastIndexOf(currentShadowingIndex)
                                );

                                new ShadowingPresenterImpl(ShadowingActivity.this)
                                        .saveVoice(ShadowingActivity.this, voice);
                            }
                        });
                    }

                    @Override
                    public void doAfterDenied(String... permission) {
                        mRecordButton.setHasRecordPromission(false);
                        Toast.makeText(ShadowingActivity.this, "请授权,否则无法录音", Toast.LENGTH_SHORT).show();
                    }
                }, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE);

    }

    @Override
    public void onSaveVoiceResult(boolean result, String info, Object extras) {
        if (result) {
            changed = true;

            Voice voice = (Voice) extras;

            shadowingList.get(currentShadowingIndex).getVoiceList().set(
                    shadowingList.get(currentShadowingIndex).getVoiceList().size() - 1, voice);

            mAdapter.initIndex();

            mAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(this, info, Toast.LENGTH_SHORT).show();

            new ShadowingPresenterImpl(this).deleteVoice(
                    shadowingList.get(currentShadowingIndex).getVoiceList().get(
                            shadowingList.get(currentShadowingIndex).getVoiceList().size() - 1).getId()
            );

            shadowingList.get(currentShadowingIndex).getVoiceList().remove(
                    shadowingList.get(currentShadowingIndex).getVoiceList().size() - 1);

            mAdapter.initIndex();

            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onScoringVoiceResult(boolean result, String info, Object extras) {
    }

    @Override
    public void onGetShadowingListResult(boolean result, String info, Object extras) {
        if (result) {
            shadowingList = (ArrayList<Shadowing>) extras;

            if (shadowingList.size() == 0) {
                UtilBox.dismissLoading(false);

                UtilBox.returnActivity(this);

                Toast.makeText(this, "该视频没有跟读", Toast.LENGTH_SHORT).show();

                return;
            }

            mAdapter = new ShadowingAdapter(this, shadowingList);
            mAdapter.setListener(new ShadowingAdapter.OnItemClickListener() {
                @Override
                public void onLeftItemClick(int position) {
                    currentShadowingIndex = position;
                    mRecordButton.setContent(shadowingList.get(position).getSentenceEng());
                    setPlayerProgress(position);
                    jcVideoPlayerStandard.startButton.performClick();

                    mRecordButton.setHandler(mAdapter.leftHandler);
                }

                @Override
                public void onRightItemClick(int position) {
                    mRecordButton.setHandler(mAdapter.rightHandler);
                }
            });
            mAdapter.jcVideoPlayerStandard = jcVideoPlayerStandard;
            mRecyclerView.setAdapter(mAdapter);

            if (shadowingList.size() != 0) {
                setPlayerProgress(0);
            }

            mRecordButton.setContent(shadowingList.get(0).getSentenceEng());
            mRecordButton.jcVideoPlayerStandard = jcVideoPlayerStandard;

            mCoverView.setVisibility(View.GONE);

            UtilBox.dismissLoading(false);
        } else {
            UtilBox.dismissLoading(false);

            UtilBox.alert(this, info,
                    "重试", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new ShadowingPresenterImpl(ShadowingActivity.this).getShadowingList(videoId);
                        }
                    },
                    "返回", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            UtilBox.returnActivity(ShadowingActivity.this);
                        }
                    });
        }
    }

    private void setPlayerProgress(final int index) {
        jcVideoPlayerStandard.release();
        initPlayer();
        jcVideoPlayerStandard.seekToInAdvance = (int) shadowingList.get(index).getStartSecond() * 1000;
        jcVideoPlayerStandard.progressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress * 0.01 * jcVideoPlayerStandard.getDuration() >= shadowingList.get(index).getEndSecond() * 1000) {
                    jcVideoPlayerStandard.startButton.performClick();
                    mVideoCoverView.setOnTouchListener(trueTouchListener);
                } else {
                    mVideoCoverView.setOnTouchListener(falseTouchListener);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    //直接把参数交给mHelper就行了
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mHelper.handleRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @OnClick({R.id.imageView_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageView_back:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        } else {
            Intent data = getIntent();
            data.putExtra("changed", changed);

            if (changed) {
                setResult(RESULT_OK, data);
            } else {
                setResult(RESULT_CANCELED, data);
            }

            UtilBox.returnActivity(this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        MediaManager.release();//保证在退出该页面时，终止语音播放
        JCVideoPlayer.releaseAllVideos();
    }

    @Override
    protected void onDestroy() {
        if (AudioPlayer.getInstance().mediaPlayer != null && AudioPlayer.getInstance().mediaPlayer.isPlaying()) {
            AudioPlayer.getInstance().pause();
            AudioPlayer.getInstance().stop();
        }

        if (mAdapter.rightHandler != null) {
            mAdapter.rightHandler.removeMessages(0);
        }

        super.onDestroy();
    }
}
