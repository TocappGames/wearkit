package com.tocapp.sdk.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.tocapp.sdk.R;
import com.tocapp.sdk.display.GameView;
import com.tocapp.sdk.engine.Game;
import com.tocapp.sdk.engine.GameRunner;

public abstract class MobileGameActivity extends AppCompatActivity {

    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.gameView = findViewById(R.id.game_view);
        this.gameView.setGame(this.getGame());
        new GameRunner(this.gameView, 30).start();
    }

    abstract protected Game getGame();
}
