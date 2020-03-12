package com.tocapp.touchround;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.*;
import android.util.Log;
import android.view.MotionEvent;

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
import org.dyn4j.dynamics.World;
import org.dyn4j.dynamics.contact.ContactConstraint;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;

public class TouchRound extends AbstractGame {

    public static final Vector2 MY_GRAVITY = new Vector2(0.0, 9.8);


    private static final String TAG = "TouchRound";
    private static Boolean touch = false;
    private GameObject userStick;
    private GameObject iaStick;
    private GameObject ball;
    private long start;
    Paint paint = new Paint();
    Paint paint2 = new Paint();
    GameObject box;
    GameObject goals;
    Integer userGoals = 0;
    Integer iaGoals = 0;

    @Override
    public double getScale() {
        return 40;
    }

    @Override
    public void init() {

        this.landscape.add(new Renderable() {
            @Override
            public void render(Canvas canvas, double scale) {
                Paint paint = new Paint();
                //paint.setColor(Color.WHITE);
                //paint.setStyle(Paint.Style.FILL);
                //canvas.drawPaint(paint);

                paint.setColor(Color.BLACK);
                paint.setTextSize(50);
                canvas.drawText(userGoals.toString(), 2000/ (float)scale, (float)35, paint);
                canvas.drawText(iaGoals.toString(), 25000/ (float)scale, (float)35, paint);
            }

            @Override
            public void render(Canvas canvas, Paint paint, double scale) {

            }
        });

        this.world.setGravity(World.ZERO_GRAVITY);
        paint.setColor(Color.RED);
        paint2.setColor(Color.BLUE);

        Rectangle rightBorderGoalIa = new Rectangle(170 / getScale(), 20/ getScale());
        rightBorderGoalIa.translate(620/getScale(), 50/getScale());

        Rectangle leftBorderGoalIa = new Rectangle(170/ getScale(), 20/ getScale());
        leftBorderGoalIa.translate(100/ getScale(), 50/ getScale());

        final Rectangle goalUser = new Rectangle(350/ getScale(), 50/ getScale());
        goalUser.translate(360/ getScale(), 1370/ getScale());

        Rectangle rightBorderGoalUser = new Rectangle(170/ getScale(), 20/ getScale());
        rightBorderGoalUser.translate(620/ getScale(), 1350/ getScale());

        Rectangle leftBorderGoalUser = new Rectangle(170/ getScale(), 20/ getScale());
        leftBorderGoalUser.translate(100 / getScale(), 1350 / getScale());


        final Rectangle goalIa = new Rectangle(350 / getScale(), 50 / getScale());
        goalIa.translate(360 / getScale(), 20 / getScale());

        Rectangle left = new Rectangle(20 / getScale(), 1290 / getScale());
        left.translate(20 / getScale(), 700 / getScale());
        Rectangle right = new Rectangle(20 / getScale(), 1290 / getScale());
        right.translate(700 / getScale(), 700 / getScale());
        goals = new GameObject(paint2);
        box = new GameObject(paint);

        goals.addFixture(goalIa);
        box.addFixture(leftBorderGoalIa);
        box.addFixture(rightBorderGoalIa);

        goals.addFixture(goalUser);
        box.addFixture(leftBorderGoalUser);
        box.addFixture(rightBorderGoalUser);

        box.addFixture(left);
        box.addFixture(right);

        box.setMass(MassType.INFINITE);

        this.world.addBody(goals);
        this.world.addBody(box);
        
        ball = addBall();
        iaStick = addIaStick();

        this.world.addListener(new CollisionListener() {
            @Override
            public boolean collision(Body body1, BodyFixture fixture1, Body body2, BodyFixture fixture2) {
                if (body1 == goals && body2 == ball) {
                    if (fixture1.getShape() == goalIa && fixture2.getShape() == ball.getFixture(0).getShape()) {
                        userGoals++;
                    }

                    if (fixture1.getShape() == goalUser && fixture2.getShape() == ball.getFixture(0).getShape()) {
                        iaGoals++;
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
        ball.addFixture(new Circle(0.7), 0.1, 0.0, 0.5);

        ball.translate(12 , 25);
        ball.setMass(MassType.NORMAL);
        //ball.setBullet(true);
        //ball.setLinearVelocity(new Vector2((Math.random() - 0.5) * 1e10, (Math.random() - 0.5) * 1e10));
        ball.setLinearDamping(1);

        this.world.addBody(ball);

        return ball;
    }

    private GameObject addIaStick(){

        Paint paint = new Paint();
        paint.setColor(Color.MAGENTA);
        double x = this.goals.getLocalCenter().x / getScale();
        double y = this.goals.getLocalCenter().y / getScale();

        GameObject ball = new GameObject(paint);
        ball.addFixture(new Circle(1.5), 1000, 0, 1);

        //ball.translate(x,y);
        ball.translate(360 / getScale(), (20 + 120) / getScale());
        ball.setMass(MassType.NORMAL);
        ball.setLinearDamping(1);
        // ball.setBullet(true);
        ball.setLinearVelocity(new Vector2((10), 0));
        //ball.setLinearDamping(1);

        this.world.addBody(ball);
        return ball;
    }

    private GameObject addUserStick(float x, float y){

        Paint paint = new Paint();
        paint.setColor(Color.BLUE);

        GameObject ball = new GameObject(paint);
        ball.addFixture(new Circle(1.5), 30, 0.0, 0.05);
        ball.setMass(MassType.NORMAL);
        ball.translate(x, y);
        ball.setLinearDamping(1);
        this.world.addBody(ball);
        return ball;
    }

    @Override
    public void update() {
        calculaIaStick(4);
        if(System.currentTimeMillis() -this.start > 2000){
            Log.d("", "");
        }
    }

    @Override
    public void finish() {

    }

    private void calculaIaStick(double scale) {
        double dx = ball.getWorldCenter().x - iaStick.getWorldCenter().x;
        double dy = ball.getWorldCenter().y - iaStick.getWorldCenter().y;
        double radius = ball.getFixture(0).getShape().getRadius();
        double distancia2 = dx * dx + dy * dy;
        // Si el dedo se encuentra dentro de la bola se frena
        if (ball.getWorldCenter().y > 725/ getScale()) {
            iaStick.applyForce(new Vector2(iaStick.getMass().getMass() * distancia2 * 1 * (ball.getWorldCenter().x - iaStick.getWorldCenter().x) * 1, 0));

            iaStick.setLinearVelocity(0, 0);
            iaStick.setAngularVelocity(0);
        } else {
            if (distancia2 < radius * radius ) {
                iaStick.applyForce(new Vector2(0, 0));
            } else {
                iaStick.applyForce(new Vector2(iaStick.getMass().getMass() * distancia2 * 1 * (ball.getWorldCenter().x - iaStick.getWorldCenter().x) * 1, iaStick.getMass().getMass() * distancia2 * 1 * (ball.getWorldCenter().y - iaStick.getWorldCenter().y) * 1));
                iaStick.setLinearVelocity(0, 0);

            }
        }




        }

    private void calculaMovimiento(MotionEvent event, double scale) {
        double dx = event.getX() / scale - userStick.getWorldCenter().x;
        double dy = event.getY() / scale - userStick.getWorldCenter().y;
        double radius = userStick.getFixture(0).getShape().getRadius();
        double distancia2 = dx * dx + dy * dy;
        // Si el dedo se encuentra dentro de la bola se frena
        if (distancia2 < radius * radius / 4) {
            userStick.setLinearVelocity(0, 0);
            userStick.setAngularVelocity(0);
        }
        else
            userStick.applyForce(new Vector2(userStick.getMass().getMass()*distancia2 * 100 *(event.getX() / scale - userStick.getWorldCenter().x) * 10, userStick.getMass().getMass()*distancia2* 100 *(event.getY() / scale- userStick.getWorldCenter().y) * 10));
    }

    @Override
    public void touchEvent(MotionEvent event, double scale) {
        System.out.println("X:" + event.getX() + " Y:" + event.getY());

        if (touch) calculaMovimiento(event, scale);

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                if (!touch) {
                    userStick = addUserStick((float) (event.getX()/ scale), (float) (event.getY()/scale));
                    touch = true;
                }
                //calculaMovimiento(event,scale);

                Log.d("", "");
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
                //this.world.removeBody(stick);
                //stick.translate(event.getRawX(), event.getRawY());
                  //  calculaMovimiento(event, scale);
//                stick.removeAllFixtures();
//                stick = addMasterStick( event.getX() / (float) scale, event.getY() / (float) scale);

                //stick.setLinearVelocity(new Vector2(stick.getMass().getMass()*1e9*(event.getX()-stick.getWorldCenter().x),stick.getMass().getMass()*1e9*(event.getY()-stick.getWorldCenter().y)));

                break;
        }
    }
}



