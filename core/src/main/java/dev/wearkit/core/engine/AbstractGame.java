package dev.wearkit.core.engine;

import android.view.MotionEvent;

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
    public boolean onTouchEvent(MotionEvent ignored) {
        return true;
    }

    @Override
    public void onWorldMeasureChange(int widht, int height) { }

    @Override
    public void onPreUpdate() { }

    @Override
    public void onPreRender() { }

    @Override
    public void onPostRender() { }
}
