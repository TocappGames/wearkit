package com.tocapp.dyn4jtest;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.tocapp.sdk.activity.MobileGameActivity;
import com.tocapp.sdk.display.GameView;
import com.tocapp.sdk.engine.Game;
import com.tocapp.touchround.TouchRound;

public class MainActivity extends MobileGameActivity {

    static public int level = 1;

    protected Game getGame() {
        return new TouchRound(level);
    }

}
