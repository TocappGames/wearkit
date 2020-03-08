package com.tocapp.sdk.geometry;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import com.tocapp.sdk.body.Renderable;

import org.dyn4j.geometry.Vector2;


public class Circle extends org.dyn4j.geometry.Circle implements Renderable {

    private static final String TAG = "Circle";

    private final Paint paint;

    /**
     * Full constructor.
     * <p>
     * Creates a new {@link Circle} centered on the origin with the given radius.
     *
     * @param radius the radius
     * @throws IllegalArgumentException if the given radius is less than or equal to zero
     */
    public Circle(double radius, Paint paint) {
        super(radius);
        this.paint = paint;
    }

    @Override
    public void render(Canvas canvas) {
        Vector2 center = this.getCenter();
        Log.d(TAG, "center: " + center);
        double radius = this.getRadius();
        canvas.drawCircle((float) center.x, (float) center.y, (float) radius, this.paint);
    }
}
