package com.tocapp.touchround;

import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.tocapp.sdk.engine.AbstractGame;
import com.tocapp.sdk.shape.Circle;

import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;

public class TouchRound extends AbstractGame {

    private static final String TAG = "TouchRound";
    private Circle center;

    @Override
    public void init() {
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        center = new Circle(100, paint);
        center.translate(0, 0);
        center.setMassType(MassType.NORMAL);
        center.setLinearVelocity(200, 200);
        this.world.addBody(center);
    }

    @Override
    public void update() {
        Log.d(TAG, "onUpdate is called!!");
    }

    @Override
    public void finish() {

    }
}
