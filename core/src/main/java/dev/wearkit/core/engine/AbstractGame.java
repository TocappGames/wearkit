package dev.wearkit.core.engine;

import android.view.MotionEvent;

abstract public class AbstractGame implements Game {

    protected World world;
    private double paused_at;
    private double start;

    public AbstractGame() {
        this.world = new World();
    }

    @Override
    public void init() {
        this.start = System.currentTimeMillis() / 1000.0;
    }

    @Override
    public double getStartTime() {
        return this.start;
    }

    @Override
    public double getElapsedTime() {
        return (System.currentTimeMillis() / 1000.0) - this.start;
    }

    @Override
    public void pause() {
        this.getWorld().pause();
        this.paused_at = (System.currentTimeMillis() / 1000.0);
    }

    @Override
    public void resume() {
        this.getWorld().resume();
        this.start += (System.currentTimeMillis() / 1000.0 - this.paused_at);
    }

    @Override
    public boolean isPaused() {
        return this.getWorld().isPaused();
    }

    @Override
    public World getWorld() {
        return this.world;
    }

    @Override
    public boolean onMotionEvent(MotionEvent ignored) {
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
