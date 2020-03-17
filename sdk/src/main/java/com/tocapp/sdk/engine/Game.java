package com.tocapp.sdk.engine;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;

import com.tocapp.sdk.rendering.GameObject;
import com.tocapp.sdk.rendering.Renderable;

import org.dyn4j.dynamics.World;

import java.util.List;

public interface Game {

    double getScale();
    void init();
    void update();
    void setDimensions(int width, int height);
    void setContext(Context context);
    void finish();
    void touchEvent(MotionEvent event, double scale);

    World getWorld();
    List<Renderable> getLandscape();
}
