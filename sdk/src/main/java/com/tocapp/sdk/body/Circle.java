package com.tocapp.sdk.body;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.Vector2;

public class Circle extends Body implements Drawable {

    private static final String TAG = "Circle";
    private org.dyn4j.geometry.Circle circle;
    private Paint paint;

    public Circle(double radius, Paint paint) {
        this.circle = Geometry.createCircle(radius);
        this.addFixture(this.circle, 1.0, 0.2, 1.0);
        this.paint = paint;
    }

    @Override
    public void draw(Canvas canvas) {
        Vector2 center = this.getWorldCenter();
        Log.d(TAG, "Circle center: [" + center.x + ", " + center.y + "]");
        double radius = this.circle.getRadius();
        canvas.drawCircle((float) center.x, (float) center.y, (float) radius, this.paint);
    }
}
