package com.tocapp.touchround;

import android.graphics.Color;
import android.graphics.Paint;

import com.tocapp.sdk.engine.AbstractGame;
import com.tocapp.sdk.body.Circle;
import com.tocapp.sdk.body.Rectangle;

import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;

public class TouchRound extends AbstractGame {

    private static final String TAG = "TouchRound";
    private Circle ball1;
    private Circle ball2;
    private Rectangle floor;

    @Override
    public void init() {
        this.world.setGravity(World.ZERO_GRAVITY);
        Paint paint = new Paint();
        paint.setColor(Color.RED);

        Paint paint2 = new Paint();
        paint2.setColor(Color.BLUE);

        this.floor = new Rectangle(500, 20, paint);
        this.floor.translate(500, 1000);
        this.floor.setMass(MassType.INFINITE);
        this.world.addBody(this.floor);

        this.ball1 = new Circle(50, paint);
        this.ball1.translate(500, 790);
        this.ball1.setMass(MassType.NORMAL);
        this.ball1.setLinearVelocity(new Vector2(50, 500));
        this.world.addBody(this.ball1);

        this.ball2 = new Circle(50, paint2);
        this.ball2.translate(500, 900);
        this.ball2.setMass(MassType.NORMAL);
        this.world.addBody(this.ball2);

    }

    @Override
    public void update() {
    }

    @Override
    public void finish() {

    }
}
