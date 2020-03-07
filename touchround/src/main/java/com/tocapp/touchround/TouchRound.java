package com.tocapp.touchround;

import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.tocapp.gamesdk.engine.BaseGame;
import com.tocapp.gamesdk.shape.Circle;

import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;

public class TouchRound extends BaseGame {

    private static final String TAG = "TouchRound";

    public TouchRound() {
        super();
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        Circle center = new Circle(100, paint);
        center.translate(0, 0);
        center.setMassType(MassType.NORMAL);
        center.applyForce(new Vector2(3,4), new Vector2(0, 0));
        this.world.addBody(center);
    }

    @Override
    public void onUpdate() {
        Log.d(TAG, "onUpdate is called!!");
    }
}
