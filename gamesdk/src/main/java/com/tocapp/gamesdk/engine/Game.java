package com.tocapp.gamesdk.engine;

import org.dyn4j.dynamics.World;

public interface Game {

    void onUpdate();

    World getWorld();
}
