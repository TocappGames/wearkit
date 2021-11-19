package dev.wearkit.core.engine;

public interface Game extends Physics, ViewCallbacks, InputCallbacks, LifeCycleCallbacks, PlaybackControls, Chrono {
    void init();
    void update();
    void finish();
}
