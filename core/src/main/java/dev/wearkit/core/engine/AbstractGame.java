package dev.wearkit.core.engine;

import android.view.MotionEvent;

abstract public class AbstractGame implements Game {

    protected World world;

    public AbstractGame() {
        this(1.0);
    }

    public AbstractGame(double scale) {
        this.world = new World(scale);
    }

    @Override
    public World getWorld() {
        return this.world;
    }

    @Override
    public void onTouchEvent(MotionEvent ignored) { }

    @Override
    public void onWorldMeasureChange(int widht, int height) { }
}
