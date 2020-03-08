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
        for(BodyFixture fixture: this.getFixtures()){
            Renderable renderable = (Renderable) fixture.getShape();
            renderable.render(canvas);
        }
    }
}
