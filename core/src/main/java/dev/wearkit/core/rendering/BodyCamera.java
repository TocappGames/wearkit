package dev.wearkit.core.rendering;

import android.graphics.Matrix;

import org.dyn4j.geometry.Vector2;

import dev.wearkit.core.common.Camera;

public class BodyCamera implements Camera {

    private double zoom;
    private Body body;
    private Matrix matrix;
    private Vector2 firstPos;

    public BodyCamera(Body body, double zoom) {
        this.body = body;
        this.zoom = zoom;
        this.matrix = new Matrix();
        this.firstPos = this.body.getWorldCenter();
    }

    public BodyCamera(Body body) {
        this(body, Camera.ZOOM_NONE);
    }

    @Override
    public void setZoom(double zoom){
        if (zoom < 0) zoom = Camera.ZOOM_NONE;
        this.zoom = zoom;
    }

    @Override
    public Matrix getMatrix() {
        Vector2 currentPos = this.body.getWorldCenter();
        Vector2 dp = this.firstPos.difference(currentPos);
        this.matrix.reset();
        this.matrix.postScale(
                (float) this.zoom,
                (float) this.zoom,
                (float) currentPos.x,
                (float) currentPos.y
        );
        this.matrix.postTranslate((float) dp.x, (float) dp.y);
        return this.matrix;
    }
}
