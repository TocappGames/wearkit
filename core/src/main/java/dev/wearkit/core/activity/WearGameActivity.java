package dev.wearkit.core.activity;


import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.Window;
import android.view.WindowManager;

import dev.wearkit.core.R;
import dev.wearkit.core.display.GameView;
import dev.wearkit.core.engine.Game;
import dev.wearkit.core.display.GameRenderer;

public abstract class WearGameActivity extends WearableActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        setContentView(R.layout.activity_game);
        setAmbientEnabled();

        GameView gameView = findViewById(R.id.game_view);
        gameView.setGame(this.getGame());
        new GameRenderer(gameView, this, 60).start();
    }

    abstract protected Game getGame();
}
