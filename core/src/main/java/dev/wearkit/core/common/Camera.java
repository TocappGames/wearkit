package dev.wearkit.core.common;

import org.dyn4j.geometry.Vector2;

public interface Camera {
    double ZOOM_NONE = 1.0;
    double ANGLE_NONE = 0.0;
    double getZoom();
    Vector2 getPosition();
    double getAngle();
    Camera zoom(double zoom);
}
