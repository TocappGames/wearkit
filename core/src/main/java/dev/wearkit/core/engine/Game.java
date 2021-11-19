package dev.wearkit.core.engine;

public interface Game extends Physics, ViewCallbacks, InputCallbacks, LifeCycleCallbacks, TimeControls {
    void init();
    void update();
    void finish();
}
