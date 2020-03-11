package com.tocapp.touchround;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.*;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.tocapp.sdk.rendering.GameObject;
import com.tocapp.sdk.engine.AbstractGame;
import com.tocapp.sdk.rendering.Renderable;
import com.tocapp.sdk.rendering.shape.Circle;
import com.tocapp.sdk.rendering.shape.Rectangle;

import org.dyn4j.collision.manifold.Manifold;
import org.dyn4j.collision.narrowphase.Penetration;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.CollisionListener;
import org.dyn4j.dynamics.Step;
import org.dyn4j.dynamics.StepAdapter;
import org.dyn4j.dynamics.World;
import org.dyn4j.dynamics.contact.ContactConstraint;
import org.dyn4j.geometry.Mass;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

public class TouchRound extends AbstractGame {

    public static final Vector2 MY_GRAVITY = new Vector2(0.0, 9.8);


    private static final String TAG = "TouchRound";
    private static Boolean touch = false;
    private GameObject stick;
    private GameObject ball;
    private long start;


    Paint paint = new Paint();
    Paint paint2 = new Paint();
    GameObject box = new GameObject(paint);
    GameObject goals = new GameObject(paint2);

    @Override
    public double getScale() {
        return 10;
    }

    @Override
    public void init() {

        this.landscape.add(new Renderable() {
            @Override
            public void render(Canvas canvas, double scale) {

            }

            @Override
            public void render(Canvas canvas, Paint paint, double scale) {

            }
        });

        this.world.setGravity(World.ZERO_GRAVITY);
        paint.setColor(Color.RED);
        paint2.setColor(Color.BLUE);

        Rectangle rightBorderGoalIa = new Rectangle(17, 2);
        rightBorderGoalIa.translate(62, 5);

        Rectangle leftBorderGoalIa = new Rectangle(17, 2);
        leftBorderGoalIa.translate(10, 5);

        final Rectangle goalUser = new Rectangle(35, 5);
        goalUser.translate(36, 137);

        Rectangle rightBorderGoalUser = new Rectangle(17, 2);
        rightBorderGoalUser.translate(62, 135);

        Rectangle leftBorderGoalUser = new Rectangle(17, 2);
        leftBorderGoalUser.translate(10, 135);


        final Rectangle goalIa = new Rectangle(35, 5);
        goalIa.translate(36, 2);

        Rectangle left = new Rectangle(2, 129);
        left.translate(2, 70);
        Rectangle right = new Rectangle(2, 129);
        right.translate(70, 70);

        goals.addFixture(goalUser);
        box.addFixture(leftBorderGoalUser);
        box.addFixture(rightBorderGoalUser);
        goals.addFixture(goalIa);
        box.addFixture(leftBorderGoalIa);
        box.addFixture(rightBorderGoalIa);

        box.addFixture(left);
        box.addFixture(right);
        box.setMass(MassType.INFINITE);
        this.world.addBody(goals);
        this.world.addBody(box);
        ball = addBall();

        this.world.addListener(new CollisionListener() {
            @Override
            public boolean collision(Body body1, BodyFixture fixture1, Body body2, BodyFixture fixture2) {
                if (body1 == goals && body2 == ball) {
                    if (fixture1.getShape() == goalIa && fixture2.getShape() == ball.getFixture(0).getShape()) {
                        System.out.println("Gol del Usuario");
                    }

                    if (fixture1.getShape() == goalUser && fixture2.getShape() == ball.getFixture(0).getShape()) {
                        System.out.println("Gol de la ia");
                    }
                }
                return true;
            }

            @Override
            public boolean collision(Body body1, BodyFixture fixture1, Body body2, BodyFixture fixture2, Penetration penetration) {
                return true;
            }

            @Override
            public boolean collision(Body body1, BodyFixture fixture1, Body body2, BodyFixture fixture2, Manifold manifold) {
                return true;
            }

            @Override
            public boolean collision(ContactConstraint contactConstraint) {
                return true;
            }
        });


        this.start = System.currentTimeMillis();
    }

    private int getRandomByte(){
        double n = Math.random() * 255;
        return (int) Math.round(n);
    }

    private GameObject addBall(){

        Paint paint = new Paint();
        paint.setColor(Color.rgb(getRandomByte(),getRandomByte(),getRandomByte()));

        GameObject ball = new GameObject(paint);
        ball.addFixture(new Circle(4), 3.0, 0.0, 10.0);

        ball.translate(50, 50);
        ball.setMass(MassType.NORMAL);
        //ball.setLinearVelocity(new Vector2((Math.random() - 0.5) * 1e10, (Math.random() - 0.5) * 1e10));

        this.world.addBody(ball);

        return ball;
    }

    private GameObject addMasterStick(float x, float y){

      /*  Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawPaint(paint);

        paint.setColor(Color.BLACK);
        paint.setTextSize(20);
        canvas.drawText("Some Text", 10, 25, paint);*/


        Paint paint = new Paint();
        paint.setColor(Color.RED);

        GameObject ball = new GameObject(paint);
        ball.addFixture(new Circle(8), 20, 0.0, 0.1);
        ball.setMass(MassType.NORMAL);
        ball.translate(x, y);

        this.world.addBody(ball);
        return ball;
    }

    @Override
    public void update() {
        if(System.currentTimeMillis() -this.start > 2000){
            Log.d("", "");
        }
    }

    @Override
    public void finish() {

    }

    @Override
    public void touchEvent(MotionEvent event, double scale) {

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                if (!touch) {
                    stick = addMasterStick((float) (event.getX()/ scale), (float) (event.getY()/scale));
                    touch = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                System.out.println("Action up");
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                System.out.println("Pointer down");

                break;
            case MotionEvent.ACTION_POINTER_UP:
                System.out.println("Action Pointer up");

                break;
            case MotionEvent.ACTION_MOVE:
                //tick.removeAllFixtures();
                //this.world.removeBody(stick);
                //stick.translate(event.getRawX(), event.getRawY());
                //stick = addMasterStick(event.getRawX(), event.getRawY());

                //stick.setLinearVelocity(new Vector2(stick.getMass().getMass()*1e9*(event.getX()-stick.getWorldCenter().x),stick.getMass().getMass()*1e9*(event.getY()-stick.getWorldCenter().y)));
                double dx = event.getX() / scale -stick.getWorldCenter().x;
                double dy = event.getY() / scale -stick.getWorldCenter().y;
                double radius = stick.getFixture(0).getShape().getRadius();
                // Si el dedo se encuentra dentro de la bola
                if ( dx * dx + dy * dy < radius * radius) {
                    stick.setLinearVelocity(0, 0);
                }
                else
                stick.applyForce(new Vector2(stick.getMass().getMass()*1e9*(event.getX() / scale -stick.getWorldCenter().x) * 10,stick.getMass().getMass()*1e9*(event.getY() / scale-stick.getWorldCenter().y) * 10));
                Log.d("", "");
                break;
        }
    }
}



