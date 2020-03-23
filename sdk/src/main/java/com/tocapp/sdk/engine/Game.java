package com.tocapp.sdk.engine;

import android.graphics.Canvas;

import org.dyn4j.dynamics.World;

public interface Game {

    void init();
    void update();
    void finish();

    World getWorld();
    Canvas getCanvas();
}
