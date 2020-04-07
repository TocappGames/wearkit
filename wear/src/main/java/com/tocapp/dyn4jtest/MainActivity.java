package com.tocapp.dyn4jtest;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;

import com.tocapp.sdk.activity.WearGameActivity;
import com.tocapp.sdk.display.GameView;
import com.tocapp.sdk.engine.Game;
import com.tocapp.touchround.AirHockey;
import com.tocapp.touchround.Config;

public class MainActivity extends WearGameActivity{


    public static boolean displayIsRound = false;
    public static Config config = new Config();
    private GameView gameView;
    private double widthCm;
    private double heightCm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // AmbientModeSupport.attach(this);
        if (!config.haveMap()) {
            config.setMap0();
        }

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
        ImageButton exitButton = findViewById(R.id.butt);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    protected Game getGame() {
        if (!config.haveMap()) {
            config.setMap0();
        }
        config.setWidthCm( widthCm );
        config.setHeightCm( heightCm );
        config.setDisplayIsRound( displayIsRound );

        return new AirHockey(config);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

}
