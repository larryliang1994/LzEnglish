package com.jiubai.lzenglish;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.danikula.videocache.CacheListener;
import com.danikula.videocache.HttpProxyCacheServer;
import com.danikula.videocache.file.FileNameGenerator;
import com.jiubai.lzenglish.config.Config;
import com.jiubai.lzenglish.manager.DownloadManager;
import com.jiubai.lzenglish.manager.SearchHistoryManager;
import com.jiubai.lzenglish.net.RequestUtil;
import com.liulishuo.filedownloader.FileDownloader;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.L;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Larry Liang on 12/05/2017.
 */

public class App extends Application {
    private HttpProxyCacheServer proxy;
    //public IWXAPI api;

    public static SharedPreferences sharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();

        sharedPreferences = getSharedPreferences("LzEnglish", MODE_PRIVATE);

        initWeChat();

        RequestUtil.initRequestQueue(getApplicationContext());

        initImageLoader();

        FileDownloader.init(getApplicationContext());

        DownloadManager.getInstance().readVideoSharedPreferences();

        SearchHistoryManager.getInstance().readHistory();
    }

    private void initWeChat() {
        //api = WXAPIFactory.createWXAPI(this, "wx88888888", true);
        //api.registerApp("wx88888888");
    }

    private void initImageLoader() {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisk(true).cacheInMemory(true).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getApplicationContext()).defaultDisplayImageOptions(defaultOptions)
                .threadPriority(Thread.NORM_PRIORITY)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.FIFO).build();
        L.writeLogs(false);
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
