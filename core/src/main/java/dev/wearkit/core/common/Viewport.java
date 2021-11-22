package dev.wearkit.core.common;

import dev.wearkit.core.engine.Scene;

public interface Viewport extends Scene, Renderable {
    Camera getCamera();
    void setCamera(Camera camera);
}
