package com.tocapp.sdk.engine;

import android.view.MotionEvent;
import android.view.View;

import com.tocapp.sdk.rendering.GameObject;

import org.dyn4j.dynamics.World;

public interface Game {

    double getScale();
    void init();
    void update();
    void finish();
    void touchEvent(MotionEvent event);

    World getWorld();
}
