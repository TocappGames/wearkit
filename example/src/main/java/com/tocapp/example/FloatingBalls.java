package com.tocapp.example;

import android.graphics.Color;
import android.graphics.Paint;

import com.tocapp.sdk.rendering.GameBody;
import com.tocapp.sdk.rendering.GameThing;
import com.tocapp.sdk.engine.AbstractGame;
import com.tocapp.sdk.rendering.shape.Circle;
import com.tocapp.sdk.rendering.shape.Rectangle;

import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;

public class FloatingBalls extends AbstractGame {

    private static final String TAG = "FloatingBalls";

    @Override
    public void init() {

        Paint bgCircleColor = new Paint();
        bgCircleColor.setColor(Color.GRAY);
        GameThing bg = new GameThing(bgCircleColor, -1);
        bg.addFixture(new Circle(500));
        this.world.getDecoration().add(bg);

        this.world.setGravity(World.ZERO_GRAVITY);
        Paint paint = new Paint();
        paint.setColor(Color.RED);

        Paint paint2 = new Paint();
        paint2.setColor(Color.BLUE);

        GameBody box = new GameBody(paint);
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

        for(int i = 0; i < 50; i++){
            addRandomBall();
        }

    }

    private int getRandomByte(){
        double n = Math.random() * 255;
        return (int) Math.round(n);
    }

    private void addRandomBall(){

        Paint paint = new Paint();
        paint.setColor(Color.rgb(getRandomByte(),getRandomByte(),getRandomByte()));

        GameBody ball = new GameBody(paint);
        ball.addFixture(new Circle(10), 1.0, 0.0, 5.0);
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
