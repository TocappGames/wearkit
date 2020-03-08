package com.tocapp.sdk.body;

import android.graphics.Canvas;
import android.graphics.Paint;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.Vector2;


public class Rectangle extends Body implements Drawable {

    private static final String TAG = "Circle";
    private org.dyn4j.geometry.Rectangle rectangle;
    private Paint paint;

    public Rectangle(double width, double height, Paint paint) {
        this.rectangle = Geometry.createRectangle(width, height);
        this.addFixture(this.rectangle);
        this.paint = paint;
    }

    @Override
    public void draw(Canvas canvas) {
        Vector2 center = this.getWorldCenter();
        double halfWidth = this.rectangle.getWidth() / 2;
        double halfHeight = this.rectangle.getHeight() / 2;
        canvas.drawRect(
                (float) (center.x - halfWidth),
                (float) (center.y - halfHeight),
                (float) (center.x + halfWidth),
                (float) (center.y + halfHeight),
                this.paint
        );
    }
}
