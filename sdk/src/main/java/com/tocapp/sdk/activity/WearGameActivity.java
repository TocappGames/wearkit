package com.tocapp.sdk.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.tocapp.sdk.R;
import com.tocapp.sdk.display.GameRenderer;
import com.tocapp.sdk.display.GameView;
import com.tocapp.sdk.engine.Game;
import android.support.wearable.activity.WearableActivity;

public abstract class WearGameActivity extends WearableActivity {

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