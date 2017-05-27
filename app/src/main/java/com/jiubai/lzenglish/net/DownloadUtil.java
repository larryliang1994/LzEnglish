package com.jiubai.lzenglish.net;

import android.content.Context;
import android.util.Log;

import com.danikula.videocache.CacheListener;
import com.danikula.videocache.HttpProxyCacheServer;
import com.jiubai.lzenglish.App;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloadQueueSet;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.util.FileDownloadUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Larry Liang on 25/05/2017.
 */

public class DownloadUtil {
    public static int singleDownload(String url, String fileName, FileDownloadListener listener) {
        Log.i("text", getFileName(fileName));

        return FileDownloader.getImpl()
                .create(url)
                .setPath(getFileName(fileName))
                .setListener(listener)
                .setAutoRetryTimes(2)
                .setCallbackProgressTimes(10000)
                .setCallbackProgressMinInterval(500)
                .start();
    }

    public static String getFileName(String fileName) {
        return FileDownloadUtils.getDefaultSaveRootPath() + File.separator + fileName;
    }

    public static void MultiDownload(String[] urls) {
        final FileDownloadListener queueTarget = new FileDownloadListener() {
            @Override
            protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            }

            @Override
            protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
            }

            @Override
            protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            }

            @Override
            protected void blockComplete(BaseDownloadTask task) {
            }

            @Override
            protected void retry(final BaseDownloadTask task, final Throwable ex, final int retryingTimes, final int soFarBytes) {
            }

            @Override
            protected void completed(BaseDownloadTask task) {
            }

            @Override
            protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            }

            @Override
            protected void error(BaseDownloadTask task, Throwable e) {
            }

            @Override
            protected void warn(BaseDownloadTask task) {
            }
        };

        final FileDownloadQueueSet queueSet = new FileDownloadQueueSet(queueTarget);

        final List<BaseDownloadTask> tasks = new ArrayList<>();
        for (int i = 0; i < urls.length; i++) {
            tasks.add(FileDownloader.getImpl().create(urls[i]).setTag(i + 1));
        }

        queueSet.disableCallbackProgressTimes(); // 由于是队列任务, 这里是我们假设了现在不需要每个任务都回调`FileDownloadListener#progress`, 我们只关系每个任务是否完成, 所以这里这样设置可以很有效的减少ipc.

        // 所有任务在下载失败的时候都自动重试一次
        queueSet.setAutoRetryTimes(1);

        queueSet.downloadTogether(tasks);
    }

    public void download(Context context) {
        String videoUrl = "";

        HttpProxyCacheServer proxy = App.getProxy(context);
        proxy.registerCacheListener(new CacheListener() {
            @Override
            public void onCacheAvailable(File cacheFile, String url, int percentsAvailable) {

            }
        }, videoUrl);

        InputStream is = null;
        try {
            URL url = new URL(proxy.getProxyUrl(videoUrl));
            is = url.openStream();

            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            int length = 0;
            while ((length = is.read(buffer)) != -1) {

            }
        } catch (Exception e) {
            Log.d("Failed ", videoUrl);
        } finally {
            if(is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    // Long hair, don't care.
                }
            }
        }
    }
}
