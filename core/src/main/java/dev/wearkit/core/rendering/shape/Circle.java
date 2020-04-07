package dev.wearkit.core.rendering.shape;

import android.graphics.Canvas;
import android.graphics.Paint;

import dev.wearkit.core.exceptions.PaintRequiredException;
import dev.wearkit.core.rendering.Paintable;
import dev.wearkit.core.rendering.Renderable;

import org.dyn4j.geometry.Vector2;


public class Circle extends org.dyn4j.geometry.Circle implements Paintable, Renderable {

    private static final String TAG = "Circle";
    private Paint paint;
    private int index = -1;

    /**
     * Full constructor.
     * <p>
     * Creates a new {@link Circle} centered on the origin with the given radius.
     *
     * @param radius the radius
     * @throws IllegalArgumentException if the given radius is less than or equal to zero
     */
    public Circle(double radius) {
        super(radius);
    }

    /**
     * Full constructor with z-index
     * @param radius
     * @param index
     */
    public Circle(double radius, int index) {
        super(radius);
        this.index = index;
    }


    @Override
    public void render(Canvas canvas) throws PaintRequiredException {
        if (this.paint == null) {
            throw new PaintRequiredException("This shape needs to be painted before render");
        }
        Vector2 center = this.getCenter();
        double radius = this.getRadius();
        canvas.drawCircle(
                (float) center.x,
                (float) center.y,
                (float) radius,
                this.paint
        );

    }

    @Override
    public Renderable paint(Paint paint) {
        if(paint == null) throw new NullPointerException("Paint cannot be null");
        this.paint = paint;
        return this;
    }
}