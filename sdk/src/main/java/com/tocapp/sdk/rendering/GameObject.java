package com.tocapp.sdk.rendering;

import android.graphics.Canvas;
import android.graphics.Paint;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Vector2;

public class GameObject extends Body implements Renderable {

    private Paint paint;

    public GameObject(Paint paint) {
        this.paint = paint;
    }

    @Override
    public void render(Canvas canvas) {

        // keep the current coordinate system
        canvas.save();

        // go to Body's coordinate system
        Vector2 translation = this.transform.getTranslation();
        canvas.translate((float) translation.x, (float) translation.y);

        // render all the fixtures in the Body
        for(BodyFixture fixture: this.getFixtures()){
            Renderable renderable = (Renderable) fixture.getShape();
            renderable.render(canvas, this.paint);
        }

        // restore previous coordinate system
        canvas.restore();
    }

    @Override
    public void render(Canvas canvas, Paint ignored) {
        this.render(canvas);
    }
}
