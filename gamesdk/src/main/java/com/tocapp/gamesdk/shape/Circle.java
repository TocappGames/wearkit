package com.tocapp.gamesdk.shape;

import android.graphics.Canvas;
import android.graphics.Paint;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.Vector2;


public class Circle extends Body implements Drawable {

    private BodyFixture fixture;
    private Paint paint;

    public Circle(double radius, Paint paint) {
        this.fixture = this.addFixture(Geometry.createCircle(radius));
        this.paint = paint;
    }

    @Override
    public void draw(Canvas canvas) {
        Vector2 center = this.fixture.getShape().getCenter();
        double radius = this.fixture.getShape().getRadius();
        canvas.drawCircle((float) center.x, (float) center.y, (float) radius, this.paint);
    }
}
