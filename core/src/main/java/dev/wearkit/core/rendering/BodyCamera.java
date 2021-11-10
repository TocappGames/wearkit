package dev.wearkit.core.rendering;

import android.graphics.Matrix;

import org.dyn4j.geometry.Vector2;

import dev.wearkit.core.common.Camera;

public class BodyCamera implements Camera {

    public static final int MODE_FIXED_ANGLE = 0;
    public static final int MODE_BODY_ANGLE = 1;

    private final double zoom;
    private final Body body;
    private int angleMode;
    private double angle;

    public BodyCamera(Body body, double zoom, double angle, int angleMode) {
        this.body = body;
        this.zoom = zoom;
        this.angle = angle;
        this.angleMode = angleMode;
    }

    public BodyCamera(Body body) {
        this(body, Camera.ZOOM_NONE, Camera.ANGLE_NONE, BodyCamera.MODE_FIXED_ANGLE);
    }

    public void setAngleMode(int angleMode){
        this.angleMode = angleMode;
    }

    public void setAngle(double angle){
        this.angle = angle;
    }

    public BodyCamera zoom(double zoom){
        if (zoom < 0) zoom = Camera.ZOOM_NONE;
        return new BodyCamera(this.body, zoom, this.angle, this.angleMode);
    }

    @Override
    public double getZoom() {
        return this.zoom;
    }

    @Override
    public Vector2 getPosition() {
        return this.body.getWorldCenter();
    }

    @Override
    public double getAngle() {
        if(this.angleMode == MODE_BODY_ANGLE)
            return -this.body.getTransform().getRotationAngle();
        return this.angle;
    }
}
