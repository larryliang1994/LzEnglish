package com.jiubai.lzenglish.widget.recorder;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.util.Log;

import java.io.IOException;

public class AudioPlayer implements OnCompletionListener, MediaPlayer.OnPreparedListener{
    public MediaPlayer mediaPlayer;

    public int currentId = -99;

    private onAudioStateChangedListener listener;

    private static AudioPlayer instance = null;

    public static AudioPlayer getInstance() {
        if (instance == null) {
            instance = new AudioPlayer();
        }

        return instance;
    }

    private AudioPlayer() {
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnPreparedListener(this);
        } catch (Exception e) {
            Log.e("mediaPlayer", "error", e);
        }
    }

    public void play() {
        mediaPlayer.start();
    }

    public void playUrl(String videoUrl, int currentId) {
        if (this.currentId == currentId) {
            this.currentId = -99;
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                pause();
                stop();
            }
            return;
        }

        this.currentId = currentId;

        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnCompletionListener(this);

            if (videoUrl.contains("http")) {
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            }

            mediaPlayer.reset();
            mediaPlayer.setDataSource(videoUrl);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IllegalArgumentException | IllegalStateException | IOException e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        mediaPlayer.pause();
    }

    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public onAudioStateChangedListener getListener() {
        return listener;
    }

    public void setListener(onAudioStateChangedListener listener) {
        this.listener = listener;
    }

    @Override
    /**
     * 通过onPrepared播放
     */
    public void onPrepared(MediaPlayer arg0) {
        arg0.start();
        Log.e("mediaPlayer", "onPrepared");
    }

    @Override
    public void onCompletion(MediaPlayer arg0) {
        Log.e("mediaPlayer", "onCompletion");

        this.currentId = -99;

        if (listener != null) {
            listener.onCompleted();
        }
    }

    public interface onAudioStateChangedListener {
        void onCompleted();
    }
}