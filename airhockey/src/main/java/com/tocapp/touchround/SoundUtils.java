package com.tocapp.touchround;

import android.content.Context;
import android.media.MediaPlayer;

class SoundUtils {
    private MediaPlayer goalSound;
    private MediaPlayer tapSound;
    private MediaPlayer loseSound;
    boolean soundActive;

    public SoundUtils(Context context, boolean  soundActive) {
        this.soundActive = soundActive;
        goalSound = MediaPlayer.create(context, R.raw.goal);
        tapSound = MediaPlayer.create(context, R.raw.tap);
        loseSound = MediaPlayer.create(context, R.raw.lose);
    }

    protected void startGoalSound() {
        if (soundActive)
        goalSound.start();
    }
    protected void startTapSound() {
        if (soundActive)
            tapSound.start();
    }
    protected void startLoseSound() {
        if (soundActive)
            loseSound.start();
    }
}
