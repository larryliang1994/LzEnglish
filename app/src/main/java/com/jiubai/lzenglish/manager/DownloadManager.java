package com.jiubai.lzenglish.manager;

import android.content.SharedPreferences;
import android.util.Log;

import com.jiubai.lzenglish.App;
import com.jiubai.lzenglish.bean.PrefetchVideo;
import com.jiubai.lzenglish.net.DownloadUtil;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Larry Liang on 25/05/2017.
 */

public class DownloadManager {
    private ArrayList<PrefetchVideo> prefetchVideos;
    private OnProgressChangedListener listener;

    private static DownloadManager instance = null;

    private DownloadManager() {
        prefetchVideos = new ArrayList<>();
    }

    public static DownloadManager getInstance() {
        if (instance == null) {
            instance = new DownloadManager();
        }

        return instance;
    }

    public void downloadVideo(int videoId, String name, String url, String image) {
        Log.i("addTask", url);
        Log.i("addTask", DownloadUtil.getFileName(videoId + ".mp4"));

        int downloadId = DownloadUtil.singleDownload(url, videoId + ".mp4",
                new FileDownloadListener() {
                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                    }

                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        int index = getPrefetchVideoByDownloadId(task.getId());

                        PrefetchVideo prefetchVideo = prefetchVideos.get(index);

                        prefetchVideo.setVideoStatus(PrefetchVideo.VideoStatus.Downloading);
                        prefetchVideo.setSoFarSize(soFarBytes);
                        prefetchVideo.setTotalSize(totalBytes);
                        prefetchVideo.setSpeed(task.getSpeed());

                        prefetchVideos.set(index, prefetchVideo);

                        if (listener != null) {
                            listener.onChanged(index);
                        }
                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
                        int index = getPrefetchVideoByDownloadId(task.getId());

                        PrefetchVideo prefetchVideo = prefetchVideos.get(index);

                        prefetchVideo.setVideoStatus(PrefetchVideo.VideoStatus.Downloaded);
                        prefetchVideo.setSoFarSize(prefetchVideo.getTotalSize());
                        prefetchVideo.setSpeed(0);

                        prefetchVideos.set(index, prefetchVideo);

                        if (listener != null) {
                            listener.onChanged(index);
                            listener.onCompletion(prefetchVideo.getVideoId());
                        }

                        writeVideoSharedPreferences();
                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        int index = getPrefetchVideoByDownloadId(task.getId());

                        PrefetchVideo prefetchVideo = prefetchVideos.get(index);

                        prefetchVideo.setVideoStatus(PrefetchVideo.VideoStatus.Paused);
                        prefetchVideo.setSoFarSize(soFarBytes);
                        prefetchVideo.setTotalSize(totalBytes);
                        prefetchVideo.setSpeed(0);

                        prefetchVideos.set(index, prefetchVideo);

                        if (listener != null) {
                            listener.onChanged(index);
                        }

                        writeVideoSharedPreferences();
                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                        int index = getPrefetchVideoByDownloadId(task.getId());

                        PrefetchVideo prefetchVideo = prefetchVideos.get(index);

                        prefetchVideo.setVideoStatus(PrefetchVideo.VideoStatus.Error);
                        prefetchVideo.setSpeed(0);

                        prefetchVideos.set(index, prefetchVideo);

                        if (listener != null) {
                            listener.onChanged(index);
                        }

                        Log.i("error", e.getMessage());
                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {

                    }
                });

        boolean exist = false;
        for (int i = 0; i < prefetchVideos.size(); i++) {
            if (downloadId == prefetchVideos.get(i).getDownloadId()) {
                exist = true;
                PrefetchVideo prefetchVideo = prefetchVideos.get(i);
                prefetchVideo.setVideoStatus(PrefetchVideo.VideoStatus.Downloading);
                break;
            }
        }

        if (!exist) {
            PrefetchVideo prefetchVideo = new PrefetchVideo(videoId, downloadId, name, url,
                    image, 0, 0, PrefetchVideo.VideoStatus.Downloading, 0, false);

            prefetchVideos.add(0, prefetchVideo);

            writeVideoSharedPreferences();
        }
    }

    public void changeVideoStatus(PrefetchVideo prefetchVideo, PrefetchVideo.VideoStatus targetStatus) {
        switch (targetStatus) {
            case Downloading:
                downloadVideo(
                        prefetchVideo.getVideoId(),
                        prefetchVideo.getName(),
                        prefetchVideo.getUrl(),
                        prefetchVideo.getImage());
                break;

            case Paused:
                FileDownloader.getImpl().pause(prefetchVideo.getDownloadId());
                break;
        }
    }

    public void deleteVideo(int downloadId) {
        for (int i = 0; i < prefetchVideos.size(); i++) {
            if (prefetchVideos.get(i).getDownloadId() == downloadId) {
                new File(DownloadUtil.getFileName(prefetchVideos.get(i).getVideoId() + ".mp4")).delete();
                break;
            }
        }
    }

    public void deleteCheckedVideo() {
        int index = 0;
        while (!prefetchVideos.isEmpty()
                && prefetchVideos.size() != 0
                && index < prefetchVideos.size()) {
            if (prefetchVideos.get(index).isChecked()) {
                new File(DownloadUtil.getFileName(prefetchVideos.get(index).getVideoId() + ".mp4")).delete();
                prefetchVideos.remove(index);
            } else {
                index ++;
            }
        }

        writeVideoSharedPreferences();
    }

    public void pausedAll() {
        FileDownloader.getImpl().pauseAll();
    }

    public void writeVideoSharedPreferences() {
        SharedPreferences.Editor editor = App.sharedPreferences.edit();

        JSONArray jsonArray = new JSONArray();

        try {
            for (PrefetchVideo prefetchVideo : prefetchVideos) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("videoId", prefetchVideo.getVideoId());
                jsonObject.put("downloadId", prefetchVideo.getDownloadId());
                jsonObject.put("name", prefetchVideo.getName());
                jsonObject.put("url", prefetchVideo.getUrl());
                jsonObject.put("image", prefetchVideo.getImage());
                jsonObject.put("soFarSize", prefetchVideo.getSoFarSize() + "");
                jsonObject.put("totalSize", prefetchVideo.getTotalSize() + "");
                jsonObject.put("downloaded", prefetchVideo.getVideoStatus() == PrefetchVideo.VideoStatus.Downloaded);

                jsonArray.put(jsonObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("text", jsonArray.toString());

        editor.putString("video", jsonArray.toString());

        editor.apply();
    }

    public void readVideoSharedPreferences() {
        SharedPreferences sp = App.sharedPreferences;

        try {
            JSONArray jsonArray = new JSONArray(sp.getString("video", "[]"));

            Log.i("text", jsonArray.toString());

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                PrefetchVideo prefetchVideo = new PrefetchVideo(
                        jsonObject.getInt("videoId"),
                        jsonObject.getInt("downloadId"),
                        jsonObject.getString("name"),
                        jsonObject.getString("url"),
                        jsonObject.getString("image"),
                        Long.valueOf(jsonObject.getString("soFarSize")),
                        Long.valueOf(jsonObject.getString("totalSize")),
                        jsonObject.getBoolean("downloaded") ?
                                PrefetchVideo.VideoStatus.Downloaded : PrefetchVideo.VideoStatus.Paused,
                        0, false
                );

                prefetchVideos.add(prefetchVideo);
            }

            // 检查莫名删除
            int index = 0;
            while (!prefetchVideos.isEmpty()
                    && prefetchVideos.size() != 0
                    && index < prefetchVideos.size()) {

                File file = null;

                if (prefetchVideos.get(index).getVideoStatus() == PrefetchVideo.VideoStatus.Downloaded) {
                    file = new File(DownloadUtil.getFileName(prefetchVideos.get(index).getVideoId() + ".mp4"));
                } else {
                    file = new File(DownloadUtil.getFileName(prefetchVideos.get(index).getVideoId() + ".mp4.temp"));
                }

                Log.i("text", file.getAbsolutePath());

                // 已被莫名删除
                if (!file.exists()) {
                    Log.i("text", "deleted : " + DownloadUtil.getFileName(prefetchVideos.get(index).getVideoId() + ".mp4"));
                    prefetchVideos.remove(index);
                    continue;
                } else {
                    index++;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getPrefetchVideoByVideoId(int videoId) {
        for (int i = 0; i < prefetchVideos.size(); i++) {
            PrefetchVideo prefetchVideo = prefetchVideos.get(i);

            if (prefetchVideo.getVideoId() == videoId) {
                return i;
            }
        }

        return -1;
    }

    public int getPrefetchVideoByDownloadId(int downloadId) {
        for (int i = 0; i < prefetchVideos.size(); i++) {
            PrefetchVideo prefetchVideo = prefetchVideos.get(i);

            if (prefetchVideo.getDownloadId() == downloadId) {
                return i;
            }
        }

        return -1;
    }

    public ArrayList<PrefetchVideo> getPrefetchVideos() {
        return prefetchVideos;
    }

    public void setPrefetchVideos(ArrayList<PrefetchVideo> prefetchVideos) {
        this.prefetchVideos = prefetchVideos;
    }

    public OnProgressChangedListener getListener() {
        return listener;
    }

    public void setListener(OnProgressChangedListener listener) {
        this.listener = listener;
    }

    public interface OnProgressChangedListener {
        void onChanged(int index);
        void onCompletion(int id);
    }
}
