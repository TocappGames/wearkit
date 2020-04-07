package com.tocapp.sdk.rendering.shape;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.tocapp.sdk.rendering.Renderable;

import org.dyn4j.geometry.Vector2;

public class StrokeCircle extends org.dyn4j.geometry.Circle implements Renderable {

    private static final String TAG = "Circle";


    /**
     * Full constructor.
     * <p>
     * Creates a new {@link Circle} centered on the origin with the given radius.
     *
     * @param radius the radius
     * @throws IllegalArgumentException if the given radius is less than or equal to zero
     */
    public StrokeCircle(double radius) {
        super(radius);
    }

    @Override
    public void render(Canvas canvas, double scale) {
        this.render(canvas, new Paint(), scale);
    }

    @Override
    public void render(Canvas canvas, Paint paint, double scale) {
        paint.setStrokeWidth(5);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        Vector2 center = this.getCenter();
        double radius = this.getRadius();
        canvas.drawCircle((float) (center.x * scale), (float) (center.y * scale), (float) (radius * scale), paint);

    }
}
