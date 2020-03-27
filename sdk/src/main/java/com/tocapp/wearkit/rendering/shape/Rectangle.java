package com.tocapp.wearkit.rendering.shape;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.tocapp.wearkit.rendering.Paintable;
import com.tocapp.wearkit.rendering.Renderable;
import com.tocapp.wearkit.exceptions.PaintRequiredException;

import org.dyn4j.geometry.Vector2;

public class Rectangle extends org.dyn4j.geometry.Rectangle implements Paintable, Renderable {

    private static final String TAG = "Rectangle";
    private Paint paint;
    private int index = -1;

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

    /**
     *
     * @param width
     * @param height
     */
    public Rectangle(double width, double height, int index) {
        this(width, height);
        this.index = index;
    }

    @Override
    public void render(Canvas canvas, double scale) throws PaintRequiredException {
        if (this.paint == null) {
            throw new PaintRequiredException("This shape needs to be painted before render");
        }
        Vector2 center = this.getCenter();

        double halfWidth = this.getWidth() / 2 * scale;
        double halfHeight = this.getHeight() / 2 * scale;
        canvas.drawRect(
                (float) (center.x * scale - halfWidth),
                (float) (center.y * scale - halfHeight),
                (float) (center.x * scale + halfWidth),
                (float) (center.y * scale + halfHeight),
                this.paint
        );
    }

    @Override
    public Renderable paint(Paint paint) {
        this.paint = paint;
        return this;
    }

}
