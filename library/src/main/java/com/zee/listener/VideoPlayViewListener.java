package com.zee.listener;

import android.media.MediaPlayer;

public interface VideoPlayViewListener extends MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {

    void onProgress(int value, int max);
}
