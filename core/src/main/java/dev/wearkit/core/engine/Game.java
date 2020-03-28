package dev.wearkit.core.engine;

public interface Game extends Physics, ViewCallbacks, InputCallbacks {
    void init();
    void update();
    void finish();
}
