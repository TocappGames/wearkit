package com.tocapp.gamesdk.engine;


import org.dyn4j.dynamics.World;

abstract public class BaseGame implements Game {

    protected World world;

    public BaseGame() {
        this.world = new World();
    }

    @Override
    public World getWorld() {
        return this.world;
    }
}
