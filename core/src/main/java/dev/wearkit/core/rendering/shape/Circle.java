package dev.wearkit.core.rendering.shape;

import android.graphics.Canvas;
import android.graphics.Paint;

import org.dyn4j.geometry.Vector2;

import dev.wearkit.core.common.Paintable;
import dev.wearkit.core.common.Renderable;
import dev.wearkit.core.common.Scalable;


public class Circle extends org.dyn4j.geometry.Circle implements Paintable, Renderable, Scalable {

    private static final String TAG = "Circle";

    private Paint paint;

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


    @Override
    public void paint(Paint paint) {
        if(paint == null) throw new NullPointerException("paint cannot be null");
        this.paint = paint;
    }

    @Override
    public void render(Canvas canvas) {
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
    public Scalable scale(double rate) {
        Circle circle = new Circle(radius * rate);
        circle.translate(this.getCenter().copy().multiply(rate));
        return circle;
    }
}
