package dev.wearkit.core.engine;

public interface Game extends Physics, ViewCallbacks, InputCallbacks, LifeCycleCallbacks {
    void init();
    void update();
    void finish();
}
