package com.tocapp.sdk.engine;


import org.dyn4j.dynamics.World;

abstract public class AbstractGame implements Game {

    protected World world;

    public AbstractGame() {
        this.world = new World();
    }

    @Override
    public World getWorld() {
        return this.world;
    }


    @Override
    public double getScale() {
        return 1.0;
    }

}
