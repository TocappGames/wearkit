package com.tocapp.sdk.body;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Convex;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.Vector2;

public class GameObject extends Body implements Renderable {
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
            renderable.render(canvas);
        }

        // restore previous coordinate system
        canvas.restore();
    }
}
