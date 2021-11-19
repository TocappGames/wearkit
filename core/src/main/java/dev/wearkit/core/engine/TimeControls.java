package dev.wearkit.core.engine;

interface TimeControls {
    long getElapsedTime();
    void resume();
    void pause();
    boolean isPaused();
}
