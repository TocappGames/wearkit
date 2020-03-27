package com.tocapp.dyn4jtest;

import android.graphics.Color;

import com.tocapp.sdk.activity.MobileGameActivity;
import com.tocapp.sdk.engine.Game;
import com.tocapp.touchround.AirHockey;

public class MainActivity extends MobileGameActivity {

    static public int level = 1;
    static public int ballColor = Color.WHITE;
    static public int sticksColor = Color.BLUE;
    static public int boxColor = Color.WHITE;
    static public int goalsColor = Color.RED;
    static public int backgroundImage = 0;
    public static boolean displayIsRound = false;

    protected Game getGame() {

        return new AirHockey(level, displayIsRound, backgroundImage, ballColor, sticksColor, boxColor, goalsColor);
    }

}
