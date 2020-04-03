package com.tocapp.dyn4jtest;

import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.ViewTreeObserver;

import com.tocapp.sdk.activity.MobileGameActivity;
import com.tocapp.sdk.display.GameView;
import com.tocapp.sdk.engine.Game;
import com.tocapp.touchround.AirHockey;


public class MainActivity extends MobileGameActivity {

    static public int level = 1;
    static public int ballColor;
    static public int sticksColor;
    static public int boxColor;
    static public int goalsColor;
    static public int backgroundImage;
    public static boolean displayIsRound = false;
    public static boolean sound = true;
    private GameView gameView;
    private double widthCm;
    private double heightCm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        gameView = findViewById( R.id.game_view );
        gameView.getViewTreeObserver().addOnGlobalLayoutListener( new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                findViewById( R.id.game_view ).getViewTreeObserver().removeGlobalOnLayoutListener( this );
                DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
                int viewHeight = gameView.getHeight();
                int viewWidth = gameView.getWidth();
                double wi = (double) viewWidth / (double) displayMetrics.xdpi;
                double hi = (double) viewHeight / (double) displayMetrics.ydpi;
                widthCm =  wi * 2.54;
                heightCm  = hi * 2.54;
            }
        } );
    }

    protected Game getGame() {
        return new AirHockey(widthCm, heightCm, level, sound, displayIsRound, backgroundImage, ballColor, sticksColor, boxColor, goalsColor );
    }

}
