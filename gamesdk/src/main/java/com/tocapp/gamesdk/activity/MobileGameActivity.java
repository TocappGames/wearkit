package com.tocapp.gamesdk.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.tocapp.gamesdk.R;
import com.tocapp.gamesdk.display.GameView;
import com.tocapp.gamesdk.engine.Game;
import com.tocapp.gamesdk.engine.GameRunner;

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
