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

    @Override
    public void init() {
        this.world.setGravity(World.EARTH_GRAVITY);
        Paint paint = new Paint();
        paint.setColor(Color.RED);

        Paint paint2 = new Paint();
        paint2.setColor(Color.BLUE);

        GameObject box = new GameObject(paint);
        Rectangle floor = new Rectangle(720, 20);
        floor.translate(389, 1280);
        Rectangle ceiling = new Rectangle(720, 20);
        ceiling.translate(389, 20);

        Rectangle left = new Rectangle(20, 1380);
        left.translate(20, 720);
        Rectangle right = new Rectangle(20, 1380);
        right.translate(700, 720);

        box.addFixture(floor);
        box.addFixture(ceiling);
        box.addFixture(left);
        box.addFixture(right);
        box.setMass(MassType.INFINITE);

        this.world.addBody(box);

        for(int i = 0; i < 100; i++){
            addRandomBall();
        }

    }

    private void addRandomBall(){

        Paint paint = new Paint();
        paint.setColor(Color.BLUE);

        GameObject ball = new GameObject(paint);
        ball.addFixture(new Circle(10), 1.0, 0.08, 0.2);
        ball.translate(40 + Math.random() * 600 , 100 + Math.random() * 800);
        ball.setMass(MassType.NORMAL);
        ball.setLinearVelocity(new Vector2((Math.random() - 0.5) * 200, (Math.random() - 0.5) * 200));

        this.world.addBody(ball);
    }


    @Override
    public void update() {
    }

    @Override
    public void finish() {

    }
}
