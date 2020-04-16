package dev.wearkit.core.rendering;

import android.graphics.Matrix;

import org.dyn4j.geometry.Vector2;

import dev.wearkit.core.common.Camera;

public class DefaultCamera implements Camera {

    private double zoom;
    private Matrix matrix;

    public DefaultCamera() {
        this.zoom = Camera.ZOOM_NONE;
    }

    public void setZoom(double zoom){
        this.zoom = zoom;
    }

    @Override
    public double getZoom() {
        return this.zoom;
    }

    @Override
    public Vector2 getPosition() {
        return null;
    }

    @Override
    public double getAngle() {
        return Camera.ANGLE_NONE;
    }
}
