package dev.wearkit.core.rendering;

import android.graphics.Matrix;

import org.dyn4j.geometry.Vector2;

import dev.wearkit.core.common.Camera;

public class DefaultCamera implements Camera {

    private double zoom;
    private Matrix matrix;

    public DefaultCamera() {
        this.zoom = 1.0;
        this.matrix = new Matrix();
    }

    @Override
    public void setZoom(double zoom){
        this.zoom = zoom;
    }

    @Override
    public Matrix getMatrix() {
        this.matrix.reset();
        this.matrix.postScale(
                (float) this.zoom,
                (float) this.zoom
        );
        return this.matrix;
    }
}
