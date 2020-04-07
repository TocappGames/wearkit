package dev.wearkit.core.rendering.shape;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;


import org.dyn4j.geometry.Vector2;

import dev.wearkit.core.exceptions.PaintRequiredException;
import dev.wearkit.core.rendering.Paintable;
import dev.wearkit.core.rendering.Renderable;

public class Polygon extends org.dyn4j.geometry.Polygon implements Paintable, Renderable {

    private Paint paint;

    public Polygon(Vector2... vertices) {
        super(vertices);
    }

    @Override
    public Renderable paint(Paint paint) {
        if(paint == null) throw new NullPointerException("Paint cannot be null");
        this.paint = paint;
        return this;
    }

    @Override
    public void render(Canvas canvas) throws PaintRequiredException {
        if (this.paint == null) {
            throw new PaintRequiredException("This shape needs to be painted before render");
        }
        Path path = new Path();
        for(Vector2 point: this.getVertices()){
            path.lineTo((float) point.x,(float) point.y);
        }

        canvas.drawPath(path, this.paint);
    }
}