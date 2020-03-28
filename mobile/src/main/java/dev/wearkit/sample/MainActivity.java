package dev.wearkit.sample;

import dev.wearkit.core.activity.MobileGameActivity;
import dev.wearkit.core.engine.Game;
import dev.wearkit.example.FloatingBalls;

public class MainActivity extends MobileGameActivity {
    protected Game getGame(){
        return new FloatingBalls();
    }
}
