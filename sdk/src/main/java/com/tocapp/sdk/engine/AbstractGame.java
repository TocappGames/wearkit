package com.tocapp.sdk.engine;


import android.content.Context;

import com.tocapp.sdk.rendering.Renderable;

import org.dyn4j.dynamics.World;

import java.util.ArrayList;
import java.util.List;

abstract public class AbstractGame implements Game {

    protected Context context;
    protected World world;
    protected List<Renderable> landscape;
    protected List<Renderable> backgroundLandscape;


    public AbstractGame() {
        this.landscape = new ArrayList<>();
        this.backgroundLandscape = new ArrayList<>();
        this.world = new World();
    }

    @Override
    public List<Renderable> getLandscape() {
        return landscape;
    }

    @Override
    public List<Renderable> getBackgroundLandscape() {return backgroundLandscape;}

    @Override
    public World getWorld() {
        return this.world;
    }


    @Override
    public double getScale() {
        return 1.0;
    }

}
