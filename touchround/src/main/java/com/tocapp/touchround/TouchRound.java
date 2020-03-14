package com.tocapp.touchround;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;

import com.tocapp.sdk.engine.AbstractGame;
import com.tocapp.sdk.rendering.GameObject;
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
    Paint boxPaint = new Paint();
    Paint iaPaint = new Paint();
    Paint userPaint = new Paint();
    GameObject box;
    GameObject goals;
    Rectangle goalIa;
    Rectangle goalUser;
    Integer userGoals = 0;
    Integer iaGoals = 0;
    double lastAttack = 1000;
    boolean iaStickColliedWithBall;
    boolean iaIsHome;

    @Override
    public double getScale() {
        return 40;
    }

    @Override
    public void init() {

        this.landscape.add(new Renderable() {
            // Render goal numbers on screen
            @Override
            public void render(Canvas canvas, double scale) {
                Paint paint = new Paint();
                paint.setColor(Color.BLACK);
                paint.setTextSize(50);
                canvas.drawText(userGoals.toString(), 2000 / (float) scale, (float) 35, paint);
                canvas.drawText(iaGoals.toString(), 25000 / (float) scale, (float) 35, paint);
            }

            @Override
            public void render(Canvas canvas, Paint paint, double scale) {
            }
        });

        this.world.setGravity(World.ZERO_GRAVITY);
        initWorld();

        this.world.addListener(new CollisionListener() {
            @Override
            public boolean collision(Body body1, BodyFixture fixture1, Body body2, BodyFixture fixture2) {
                if (body1 == ball && body2 == iaStick || body1 == iaStick && body2 == ball) {
                    System.out.println("Returning home");
                    iaStickColliedWithBall = true;
                    iaIsHome = false;
                }

                if (body1 == goals && body2 == ball || body1 == ball && body2 == goals) {
                    if (fixture1.getShape() == goalIa && fixture2.getShape() == ball.getFixture(0).getShape()) {
                        System.out.println("Gol del jugador");
                        world.removeBody(ball);
                        ball = addBall();
                        userGoals++;
                        System.out.println("Bola añadida por gol");
                    }

                    if (fixture1.getShape() == goalUser && fixture2.getShape() == ball.getFixture(0).getShape()) {
                        iaGoals++;
                        System.out.println("Gol de la ia");
                        world.removeBody(ball);
                        ball = addBall();
                        System.out.println("Bola añadida por gol");
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

    private void initWorld() {
        boxPaint.setColor(Color.RED);
        iaPaint.setColor(Color.BLUE);
        userPaint.setColor(Color.MAGENTA);

        Rectangle rightBorderGoalIa = new Rectangle(170 / getScale(), 20 / getScale());
        rightBorderGoalIa.translate(620 / getScale(), 50 / getScale());

        Rectangle leftBorderGoalIa = new Rectangle(170 / getScale(), 20 / getScale());
        leftBorderGoalIa.translate(100 / getScale(), 50 / getScale());

        goalUser = new Rectangle(350 / getScale(), 50 / getScale());
        goalUser.translate(360 / getScale(), 1370 / getScale());

        Rectangle rightBorderGoalUser = new Rectangle(170 / getScale(), 20 / getScale());
        rightBorderGoalUser.translate(620 / getScale(), 1350 / getScale());

        Rectangle leftBorderGoalUser = new Rectangle(170 / getScale(), 20 / getScale());
        leftBorderGoalUser.translate(100 / getScale(), 1350 / getScale());


        goalIa = new Rectangle(350 / getScale(), 50 / getScale());
        goalIa.translate(360 / getScale(), 20 / getScale());

        Rectangle left = new Rectangle(20 / getScale(), 1290 / getScale());
        left.translate(20 / getScale(), 700 / getScale());
        Rectangle right = new Rectangle(20 / getScale(), 1290 / getScale());
        right.translate(700 / getScale(), 700 / getScale());
        goals = new GameObject(iaPaint);
        box = new GameObject(boxPaint);

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
        iaStick = addIaStick();
        userStick = addUserStick();
        ball = addBall();
    }


    /* ----------------------------------------------------------------------------------------- */


    private int getRandomByte() {
        double n = Math.random() * 255;
        return (int) Math.round(n);
    }

    private GameObject addBall() {
        double iaGoalX = goals.getFixture(0).getShape().getCenter().x;
        double leftWallY = box.getFixture(4).getShape().getCenter().y;

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);

        GameObject ball = new GameObject(paint);
        ball.addFixture(new Circle(0.7), 0.1, 0.0, 0.5);
        ball.translate(iaGoalX, leftWallY);
        ball.setMass(MassType.NORMAL);
        ball.setLinearDamping(1);

        this.world.addBody(ball);
        return ball;
    }

    private GameObject addIaStick() {
        double x = goals.getFixture(0).getShape().getCenter().x;
        double y = goals.getFixture(0).getShape().getCenter().y + 5;

        Paint paint = new Paint();
        paint.setColor(Color.BLUE);

        GameObject iaStick = new GameObject(paint);
        iaStick.addFixture(new Circle(1.5), 1000, 0, 0.1);
        iaStick.translate(x, y);
        iaStick.setMass(MassType.NORMAL);
        iaStick.setLinearDamping(1);

        this.world.addBody(iaStick);
        return iaStick;
    }

    private GameObject addUserStick() {
        double x = goals.getFixture(1).getShape().getCenter().x;
        double y = goals.getFixture(1).getShape().getCenter().y - 5;

        Paint paint = new Paint();
        paint.setColor(Color.MAGENTA);

        // System.out.println("Posicion del stick: X - " + x + " Y: " + y);

        GameObject userStick = new GameObject(paint);
        userStick.addFixture(new Circle(1.5), 30, 0.0, 0.05);
        userStick.setMass(MassType.NORMAL);
        userStick.translate(x, y);
        userStick.setLinearDamping(1);
        this.world.addBody(userStick);
        return userStick;
    }

    @Override
    public void update() {
        calculateIaStickPosition();
        checkBall();
        checkUserStick();
        checkIaStick();
    }

    private void checkBall() {
        double ballX = ball.getWorldCenter().x;
        double ballY = ball.getWorldCenter().y;
        if (ballX < 0 || ballX > 700 / getScale() || ballY < 0 || ballY > 1400 / getScale()) {
            System.out.println("Bola recolocada por haber salido");

            world.removeBody(ball);
            ball = addBall();
        }
    }

    private void checkUserStick() {
        double userStickX = userStick.getWorldCenter().x;
        double userStickY = userStick.getWorldCenter().y;
        if (userStickX < 0 || userStickX > 700 / getScale() || userStickY < 0 || userStickY > 1400 / getScale()) {
            world.removeBody(userStick);
            userStick = addUserStick();
        }
    }

    private void checkIaStick() {
        double iaStickX = iaStick.getWorldCenter().x;
        double iaStickY = iaStick.getWorldCenter().y;
        if (iaStickX < 0 || iaStickX > 700 / getScale() || iaStickY < 0 || iaStickY > 1400 / getScale()) {
            world.removeBody(iaStick);
            iaStick = addIaStick();
        }
    }

    @Override
    public void finish() {
    }


    private void calculateIaStickPosition() {
        if (!iaIsHome && iaStickColliedWithBall) {
            double bdx = goals.getFixture(0).getShape().getCenter().x - iaStick.getWorldCenter().x;
            double bdy = goals.getFixture(0).getShape().getCenter().y - iaStick.getWorldCenter().y;
            double bradius = goals.getFixture(0).getShape().getRadius();
            double bdistancia2 = bdx * bdx + bdy * bdy / 2;
            if (bdistancia2 > bradius) {
                iaStick.applyForce(new Vector2(iaStick.getMass().getMass() * bdistancia2 * (goals.getFixture(0).getShape().getCenter().x - iaStick.getWorldCenter().x), iaStick.getMass().getMass() * bdistancia2 * (goals.getFixture(0).getShape().getCenter().y - iaStick.getWorldCenter().y)));
            } else {
                iaIsHome = true;
                iaStickColliedWithBall = false;
                iaStick.applyForce(new Vector2(0, 0));
            }
        } else {
            double dx = ball.getWorldCenter().x - iaStick.getWorldCenter().x;
            double dy = ball.getWorldCenter().y - iaStick.getWorldCenter().y;
            double radius = ball.getFixture(0).getShape().getRadius();
            double distancia2 = dx * dx + dy * dy;
            // Si pasa del centro la bola, el stick la sigue en el eje y
            if (ball.getWorldCenter().y > 725 / getScale()) {
                iaStick.applyForce(new Vector2(iaStick.getMass().getMass() * distancia2 * 1 * (ball.getWorldCenter().x - iaStick.getWorldCenter().x) * 1, 0));
                iaStick.setLinearVelocity(0, 0);
                iaStick.setAngularVelocity(0);
            } else {
                // Cuando la bola está en su campo y lejano al stick ataca
                if (distancia2 > radius) {
                    if ((System.currentTimeMillis() - this.start) > lastAttack) {
                        iaStick.applyForce(new Vector2(iaStick.getMass().getMass() * distancia2 * 10 * (ball.getWorldCenter().x - iaStick.getWorldCenter().x) * 10, iaStick.getMass().getMass() * distancia2 * 10 * (ball.getWorldCenter().y - iaStick.getWorldCenter().y) * 10));
                        iaStick.setLinearVelocity(0, 0);
                        lastAttack += 2500;
                    }
                } else {
                    iaStick.applyForce(new Vector2(0, 0));
                }
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
        } else
            userStick.applyForce(new Vector2(userStick.getMass().getMass() * distancia2 * 100 * (event.getX() / scale - userStick.getWorldCenter().x) * 10, userStick.getMass().getMass() * distancia2 * 100 * (event.getY() / scale - userStick.getWorldCenter().y) * 10));
    }

    @Override
    public void touchEvent(MotionEvent event, double scale) {
        if (event.getY()/scale > box.getFixture(4).getShape().getCenter().y) {
        calculaMovimiento(event, scale);
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                //calculaMovimiento(event,scale);

                Log.d("", "");
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                break;
            case MotionEvent.ACTION_POINTER_UP:
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
}



