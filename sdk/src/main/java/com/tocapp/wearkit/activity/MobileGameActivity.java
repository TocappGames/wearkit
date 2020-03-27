package com.tocapp.wearkit.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.tocapp.wearkit.R;
import com.tocapp.wearkit.display.GameView;
import com.tocapp.wearkit.engine.Game;
import com.tocapp.wearkit.display.GameRenderer;

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

        setContentView(R.layout.activity_main);

        this.gameView = findViewById(R.id.game_view);
        this.gameView.setGame(this.getGame());
        new GameRenderer(this.gameView, this, 60).start();
    }

    abstract protected Game getGame();
}
