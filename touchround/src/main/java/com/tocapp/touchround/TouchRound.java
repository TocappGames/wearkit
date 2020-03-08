package com.tocapp.touchround;

import android.graphics.Color;
import android.graphics.Paint;

import com.tocapp.sdk.rendering.GameObject;
import com.tocapp.sdk.engine.AbstractGame;
import com.tocapp.sdk.rendering.shape.Circle;
import com.tocapp.sdk.rendering.shape.Rectangle;

import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;

public class TouchRound extends AbstractGame {

    private static final String TAG = "TouchRound";
    private GameObject ball1;
    private GameObject ball2;
    private GameObject box;

    @Override
    public void init() {
        this.world.setGravity(World.ZERO_GRAVITY);
        Paint paint = new Paint();
        paint.setColor(Color.RED);

        Paint paint2 = new Paint();
        paint2.setColor(Color.BLUE);

        this.box = new GameObject(paint);
        Rectangle floor = new Rectangle(768, 20);
        floor.translate(389, 1000);
        Rectangle ceiling = new Rectangle(768, 20);
        ceiling.translate(389, 100);

        Rectangle left = new Rectangle(20, 1000);
        left.translate(20, 500);
        Rectangle right = new Rectangle(20, 1000);
        right.translate(740, 500);
        this.box.addFixture(floor);
        this.box.addFixture(ceiling);
        this.box.addFixture(left);
        this.box.addFixture(right);
        this.box.setMass(MassType.INFINITE);

        this.ball1 = new GameObject(paint);
        this.ball1.addFixture(new Circle(50), 1.0, 0.0, 5.0);
        this.ball1.translate(500, 500);
        this.ball1.setMass(MassType.NORMAL);
        this.ball1.setLinearVelocity(new Vector2(0, 500));

        this.ball2 = new GameObject(paint2);
        this.ball2.addFixture(new Circle(50), 1.0, 0.2, 5.0);
        this.ball2.translate(500, 900);
        this.ball2.setMass(MassType.NORMAL);

        this.world.addBody(this.box);
        this.world.addBody(this.ball1);
        this.world.addBody(this.ball2);

    }

    @Override
    public void update() {
    }

    @Override
    public void finish() {

    }
}
