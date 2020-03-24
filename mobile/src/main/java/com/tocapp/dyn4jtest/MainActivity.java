package com.tocapp.dyn4jtest;

import com.tocapp.sdk.activity.MobileGameActivity;
import com.tocapp.sdk.engine.Game;
import com.tocapp.touchround.AirHockey;

public class MainActivity extends MobileGameActivity {

    static public int level = 1;

    protected Game getGame() {
        return new AirHockey(level);
    }

}
