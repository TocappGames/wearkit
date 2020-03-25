package com.tocapp.dyn4jtest;

import android.graphics.Color;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.widget.TextView;

import com.tocapp.sdk.activity.MobileGameActivity;
import com.tocapp.sdk.activity.WearGameActivity;
import com.tocapp.sdk.engine.Game;
import com.tocapp.touchround.AirHockey;

public class MainActivity extends WearGameActivity {

    private TextView mTextView;

    static public int level = 1;
    static public int ballColor = Color.WHITE;
    static public int sticksColor = Color.BLUE;
    static public int boxColor = Color.WHITE;
    static public int goalsColor = Color.RED;
    static public int backgroundImage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected Game getGame() {
        return new AirHockey(level,backgroundImage, ballColor, sticksColor, boxColor, goalsColor);
    }
}
