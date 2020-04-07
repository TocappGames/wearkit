package com.tocapp.dyn4jtest;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.ViewTreeObserver;

import com.tocapp.sdk.activity.MobileGameActivity;
import com.tocapp.sdk.display.GameView;
import com.tocapp.sdk.engine.Game;
import com.tocapp.touchround.AirHockey;
import com.tocapp.touchround.Config;


public class MainActivity extends MobileGameActivity {

    public static boolean displayIsRound = false;
    private GameView gameView;
    static public Config config = new Config();
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
        if (!config.haveMap()) {
            config.setMap0();
        }
        config.setDisplayIsRound( displayIsRound );
        config.setWidthCm( widthCm );
        config.setHeightCm( heightCm );
        return new AirHockey(config);
    }

}
