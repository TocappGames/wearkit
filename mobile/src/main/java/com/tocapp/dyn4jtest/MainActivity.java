package com.tocapp.dyn4jtest;

import com.tocapp.wearkit.activity.MobileGameActivity;
import com.tocapp.wearkit.engine.Game;
import com.tocapp.example.FloatingBalls;

public class MainActivity extends MobileGameActivity {
    protected Game getGame(){
        return new FloatingBalls();
    }
}
