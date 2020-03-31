package com.tocapp.sdk.engine;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.View;

import com.tocapp.sdk.rendering.GameObject;
import com.tocapp.sdk.rendering.Renderable;

import org.dyn4j.dynamics.World;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public interface Game {

    double getScale();
    void init();
    void update();
    void setDimensions(int width, int height, float dpi);
    void setContext(Context context);
    void postRender();
    void finish();
    void touchEvent(MotionEvent event, double scale);
    void rotatoryEvent(View v, MotionEvent ev);
    World getWorld();
    List<Renderable> getBackgroundLandscape();
    List<Renderable> getLandscape();
}
