package dev.wearkit.core.common;

import dev.wearkit.core.engine.Scene;

public interface Viewport extends Printable, Scene, Renderable {
    Camera getCamera();
    void setCamera(Camera camera);
}
