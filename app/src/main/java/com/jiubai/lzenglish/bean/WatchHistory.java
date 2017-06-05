package com.jiubai.lzenglish.bean;

/**
 * Created by Leunghowell on 2017/5/29.
 */

public class WatchHistory {
    private int videoId;
    private String name;
    private long totalTime;
    private long watchedTime;
    private int nextVideoId;
    private long time;
    private String image;
    private boolean checked;

    public WatchHistory() {
    }

    public WatchHistory(int videoId, String name, long totalTime, long watchedTime, int nextVideoId, long time, String image, boolean checked) {
        this.videoId = videoId;
        this.name = name;
        this.totalTime = totalTime;
        this.watchedTime = watchedTime;
        this.nextVideoId = nextVideoId;
        this.time = time;
        this.image = image;
        this.checked = checked;
    }

    public int getVideoId() {
        return videoId;
    }

    public void setVideoId(int videoId) {
        this.videoId = videoId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
    }

    public long getWatchedTime() {
        return watchedTime;
    }

    public void setWatchedTime(long watchedTime) {
        this.watchedTime = watchedTime;
    }

    public int getNextVideoId() {
        return nextVideoId;
    }

    public void setNextVideoId(int nextVideoId) {
        this.nextVideoId = nextVideoId;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
