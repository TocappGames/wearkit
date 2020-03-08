package com.tocapp.dyn4jtest;

import com.tocapp.sdk.activity.MobileGameActivity;
import com.tocapp.sdk.engine.Game;
import com.tocapp.touchround.TouchRound;

public class MainActivity extends MobileGameActivity {
    protected Game getGame(){
        return new TouchRound();
    }
}
