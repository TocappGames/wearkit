package dev.wearkit.core.rendering;

import org.dyn4j.geometry.Vector2;
import dev.wearkit.core.common.Camera;

public class DefaultCamera implements Camera {

    private final double zoom;
    private Vector2 position;

    public DefaultCamera() {
        this(Camera.ZOOM_NONE);
    }

    public DefaultCamera(double zoom) {
        this.zoom = zoom;
    }

    public DefaultCamera zoom(double zoom){
        return new DefaultCamera(zoom);
    }

    @Override
    public double getZoom() {
        return this.zoom;
    }

    @Override
    public Vector2 getPosition() {
        return position;
    }

    @Override
    public double getAngle() {
        return Camera.ANGLE_NONE;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }
}
