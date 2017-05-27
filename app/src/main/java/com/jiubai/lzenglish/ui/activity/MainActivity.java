package com.jiubai.lzenglish.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.danikula.videocache.HttpProxyCacheServer;
import com.jiubai.lzenglish.App;
import com.jiubai.lzenglish.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

public class MainActivity extends BaseActivity {

    @Bind(R.id.videoplayer)
    JCVideoPlayerStandard jcVideoPlayerStandard;

    String url = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        HttpProxyCacheServer proxy = App.getProxy(this);
        String proxyUrl = proxy.getProxyUrl(url);
        jcVideoPlayerStandard.setUp(proxyUrl
                , JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "Bunny");
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
}
