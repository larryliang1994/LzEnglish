package com.jiubai.lzenglish;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.danikula.videocache.CacheListener;
import com.danikula.videocache.HttpProxyCacheServer;
import com.danikula.videocache.file.FileNameGenerator;
import com.jiubai.lzenglish.config.Constants;
import com.jiubai.lzenglish.manager.DownloadManager;
import com.jiubai.lzenglish.manager.SearchHistoryManager;
import com.jiubai.lzenglish.manager.WatchHistoryManager;
import com.jiubai.lzenglish.net.RequestUtil;
import com.liulishuo.filedownloader.FileDownloader;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.File;

/**
 * Created by Larry Liang on 12/05/2017.
 */

public class App extends Application {
    private HttpProxyCacheServer proxy;

    public static IWXAPI iwxapi;

    public static SharedPreferences sharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();

        sharedPreferences = getSharedPreferences("LzEnglish", MODE_PRIVATE);

        RequestUtil.initRequestQueue(getApplicationContext());

        initImageLoader();

        initWechat();

        FileDownloader.init(getApplicationContext());

        DownloadManager.getInstance().readVideoSharedPreferences();

        SearchHistoryManager.getInstance().readHistory();

        WatchHistoryManager.getInstance().readHistory();
    }

    private void initWechat() {
        iwxapi = WXAPIFactory.createWXAPI(this, Constants.WX_APP_ID, true);

        iwxapi.registerApp(Constants.WX_APP_ID);
    }

    private void initImageLoader() {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisk(true).cacheInMemory(true).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(this)
                .defaultDisplayImageOptions(defaultOptions)
                .threadPriority(Thread.NORM_PRIORITY)
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .build();
        ImageLoader.getInstance().init(config);
    }

    public static HttpProxyCacheServer getProxy(Context context) {
        App app = (App) context.getApplicationContext();
        return app.proxy == null ? (app.proxy = app.newProxy()) : app.proxy;
    }

    private HttpProxyCacheServer newProxy() {
        return new HttpProxyCacheServer.Builder(this)
                .maxCacheSize(1024 * 1024 * 1024)       // 1 Gb for cache
                .fileNameGenerator(new MyFileNameGenerator())
                .maxCacheFilesCount(20)
                .build();
    }

    private void proxyTest() {
        CacheListener listener = new CacheListener() {
            @Override
            public void onCacheAvailable(File cacheFile, String url, int percentsAvailable) {

            }
        };

        getProxy(this).registerCacheListener(listener, "url");

        getProxy(this).unregisterCacheListener(listener);

        getProxy(this).isCached("url");
    }

    public class MyFileNameGenerator implements FileNameGenerator {

        // Urls contain mutable parts (parameter 'sessionToken') and stable video_home's id (parameter 'videoId').
        // e. g. http://example.com?videoId=abcqaz&sessionToken=xyz987
        public String generate(String url) {
//            Uri uri = Uri.parse(url);
//            String videoId = uri.getQueryParameter("videoId");
            return url + ".mp4";
        }
    }
}
