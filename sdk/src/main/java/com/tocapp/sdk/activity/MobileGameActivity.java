package com.tocapp.sdk.activity;

import androidx.annotation.Dimension;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

import com.tocapp.sdk.R;
import com.tocapp.sdk.display.GameView;
import com.tocapp.sdk.engine.Game;
import com.tocapp.sdk.display.GameRenderer;

import java.util.Vector;

public abstract class MobileGameActivity extends AppCompatActivity {

    private GameView gameView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        getWindow().

        setContentView(R.layout.activity_main);

        this.gameView = findViewById(R.id.game_view);
        this.gameView.setBackgroundColor(Color.BLACK);
        this.gameView.setGame(this.getGame());
        this.gameView.setContext(this);
        new GameRenderer(this.gameView, this, 60).start();
    }

    abstract protected Game getGame();
}
