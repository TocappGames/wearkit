package com.tocapp.sdk.rendering.shape;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.tocapp.sdk.rendering.Renderable;

import org.dyn4j.geometry.Vector2;

public class Rectangle extends org.dyn4j.geometry.Rectangle implements Renderable {

    private static final String TAG = "Rectangle";

    /**
     * Full constructor.
     * <p>
     * The center of the rectangle will be the origin.
     * <p>
     * A rectangle must have a width and height greater than zero.
     *
     * @param width  the width
     * @param height the height
     * @throws IllegalArgumentException if width or height is less than or equal to zero
     */
    public Rectangle(double width, double height) {
        super(width, height);
    }

    @Override
    public void render(Canvas canvas, double scale) {
        this.render(canvas, new Paint(), scale);
    }

    @Override
    public void render(Canvas canvas, Paint paint, double scale) {
        Vector2 center = this.getCenter();
        double cx = center.x * scale;
        double cy = center.y * scale;
        double halfWidth = this.getWidth() / 2 * scale;
        double halfHeight = this.getHeight() / 2 *  scale;
        canvas.drawRect(
                (float) (cx - halfWidth),
                (float) (cy - halfHeight),
                (float) (cx + halfWidth),
                (float) (cy + halfHeight),
                paint
        );
    }
}
