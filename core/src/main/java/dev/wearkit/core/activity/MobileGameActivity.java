package dev.wearkit.core.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import dev.wearkit.core.R;
import dev.wearkit.core.display.GameView;
import dev.wearkit.core.engine.Game;
import dev.wearkit.core.display.GameRenderer;

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
