package dev.wearkit.core.activity;


import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import dev.wearkit.core.R;
import dev.wearkit.core.display.GameView;
import dev.wearkit.core.engine.Game;
import dev.wearkit.core.display.GameRenderer;

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

        setContentView(R.layout.activity_game);
        setAmbientEnabled();


        this.gameView = findViewById(R.id.game_view);
        this.gameView.setGame(this.getGame());
        this.gameView.setFocusableInTouchMode(true);
        new GameRenderer(this.gameView, this, 60).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (gameView != null){
            gameView.requestFocus();
        }
    }

    abstract protected Game getGame();
}
