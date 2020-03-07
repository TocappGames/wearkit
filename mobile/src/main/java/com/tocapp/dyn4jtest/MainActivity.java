package com.tocapp.dyn4jtest;

import com.tocapp.gamesdk.activity.MobileGameActivity;
import com.tocapp.gamesdk.engine.Game;
import com.tocapp.touchround.TouchRound;

public class MainActivity extends MobileGameActivity {
    protected Game getGame(){
        return new TouchRound();
    }
}
