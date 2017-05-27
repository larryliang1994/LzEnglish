package com.jiubai.lzenglish.bean;

/**
 * Created by Larry Liang on 25/05/2017.
 */

public class PrefetchVideo {
    private int videoId;
    private int downloadId;
    private String name;
    private String url;
    private String image;
    private long soFarSize;
    private long totalSize;
    private VideoStatus videoStatus;
    private int speed;
    private boolean checked;

    public PrefetchVideo() {
    }

    public PrefetchVideo(int videoId, int downloadId, String name, String url, String image,
                         long soFarSize, long totalSize, VideoStatus videoStatus,
                         int speed, boolean checked) {
        this.videoId = videoId;
        this.downloadId = downloadId;
        this.name = name;
        this.url = url;
        this.image = image;
        this.totalSize = totalSize;
        this.soFarSize = soFarSize;
        this.videoStatus = videoStatus;
        this.speed = speed;
        this.checked = checked;
    }

    public int getVideoId() {
        return videoId;
    }

    public void setVideoId(int videoId) {
        this.videoId = videoId;
    }

    public int getDownloadId() {
        return downloadId;
    }

    public void setDownloadId(int downloadId) {
        this.downloadId = downloadId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

    public long getSoFarSize() {
        return soFarSize;
    }

    public void setSoFarSize(long soFarSize) {
        this.soFarSize = soFarSize;
    }

    public VideoStatus getVideoStatus() {
        return videoStatus;
    }

    public void setVideoStatus(VideoStatus videoStatus) {
        this.videoStatus = videoStatus;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public enum VideoStatus {
        Downloading,
        Paused,
        Downloaded,
        Error
    }
}
