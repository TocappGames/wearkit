package com.tocapp.touchround;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.tocapp.sdk.engine.AbstractGame;
import com.tocapp.sdk.rendering.GameObject;
import com.tocapp.sdk.rendering.Renderable;
import com.tocapp.sdk.rendering.shape.Circle;
import com.tocapp.sdk.rendering.shape.Rectangle;

import org.dyn4j.collision.continuous.ConservativeAdvancement;
import org.dyn4j.collision.manifold.ClippingManifoldSolver;
import org.dyn4j.collision.manifold.Manifold;
import org.dyn4j.collision.narrowphase.Penetration;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.CollisionListener;
import org.dyn4j.dynamics.ContinuousDetectionMode;
import org.dyn4j.dynamics.Settings;
import org.dyn4j.dynamics.World;
import org.dyn4j.dynamics.contact.ContactConstraint;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;

public class TouchRound extends AbstractGame {

    private static final int mobileWidth = 1080;
    private static final int mobileHeight = 1920;
    private static final Vector2 MY_GRAVITY = new Vector2(0.0, 9.8);
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
                canvas.drawText(userGoals.toString(), mobileWidth / 8, mobileHeight / 2 - 100 , paint);
                canvas.drawText(iaGoals.toString(), mobileWidth / 8, mobileHeight / 2 + 100, paint);
            }

            @Override
            public void render(Canvas canvas, Paint paint, double scale) {

            }
        });

        this.world.setGravity(World.ZERO_GRAVITY);

        initWorld();

        this.world.addListener(new CollisionListener() {
            @Override
            public boolean collision(Body body1, BodyFixture fixture1, Body body2, BodyFixture fixture2, Penetration penetration) {

                if (body1 == ball && body2 == iaStick || body1 == iaStick && body2 == ball) {
                    System.out.println("Returning home");
                    System.out.println("Penetration: " + penetration);

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
            public boolean collision(Body body1, BodyFixture fixture1, Body body2, BodyFixture fixture2) {
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
        double sidesMargin = 20 / getScale();
        double boxHeight = 20 / getScale();

        boxPaint.setColor(Color.RED);
        iaPaint.setColor(Color.BLUE);
        userPaint.setColor(Color.MAGENTA);

        double borderGoalWidth = 400 / getScale();
        double borderGoalHeight = boxHeight;
        double goalWidth = 240 /getScale();
        double goalHeight = 20/getScale();

        Rectangle leftBorderGoalIa = new Rectangle(borderGoalWidth, borderGoalHeight);
        leftBorderGoalIa.translate(borderGoalWidth / 2 + sidesMargin, goalHeight);

        Rectangle rightBorderGoalIa = new Rectangle(borderGoalWidth, borderGoalHeight);
        rightBorderGoalIa.translate(mobileWidth/ getScale() - borderGoalWidth/ 2 - sidesMargin , goalHeight);

        goalUser = new Rectangle(goalWidth, goalHeight);
        goalUser.translate(mobileWidth / 2 / getScale(), mobileHeight /getScale() - sidesMargin);

        Rectangle rightBorderGoalUser = new Rectangle(borderGoalWidth, borderGoalHeight);
        rightBorderGoalUser.translate(mobileWidth/ getScale() - borderGoalWidth/ 2 - sidesMargin, mobileHeight /getScale() - sidesMargin);

        Rectangle leftBorderGoalUser = new Rectangle(borderGoalWidth, borderGoalHeight);
        leftBorderGoalUser.translate(borderGoalWidth / 2 + sidesMargin, mobileHeight /getScale() - sidesMargin);


        goalIa = new Rectangle(goalWidth, goalHeight);
        goalIa.translate(mobileWidth / 2 / getScale(), sidesMargin);

        Rectangle left = new Rectangle(sidesMargin, mobileHeight / getScale() - sidesMargin);
        left.translate(sidesMargin, mobileHeight / 2 / getScale());

        Rectangle right = new Rectangle(sidesMargin, mobileHeight / getScale() - sidesMargin);
        right.translate(mobileWidth / getScale() - sidesMargin, mobileHeight / 2 / getScale());
        goals = new GameObject(iaPaint);
        box = new GameObject(boxPaint);
        box.setMass(MassType.INFINITE);
        goals.addFixture(goalIa);
        box.addFixture(leftBorderGoalIa);
        box.addFixture(rightBorderGoalIa);
        goals.addFixture(goalUser);
        box.addFixture(leftBorderGoalUser);
        box.addFixture(rightBorderGoalUser);

        box.addFixture(left);
        box.addFixture(right);

        // TODO PREGUNTAR POR QUE NO SE PUEDE RENDERIZAR ESTO
       /* GameObject something = new GameObject(new Paint(Color.LTGRAY));
        something.addFixture(Geometry.createIsoscelesTriangle(300 /getScale(), 300/getScale()));
        something.translate(300, 700);
        box.setMass(MassType.INFINITE);*/

        //this.world.addBody(something);
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
        ball.addFixture(new Circle(0.7), 10, 0.0, 0.5);
        ball.translate(mobileWidth / 2 /getScale(), mobileHeight / 2 /getScale());
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
        iaStick.addFixture(new Circle(2), 30, 0, 0.002);
        iaStick.translate(x, y);
        iaStick.setMass(MassType.NORMAL);
        iaStick.setLinearDamping(5);

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
        userStick.addFixture(new Circle(2), 30, 0.0, 0.002 );
        userStick.setMass(MassType.NORMAL);
        userStick.translate(x, y);
        userStick.setLinearDamping(5);
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
        double corner = 20 / getScale() + 20 / getScale();
        double cornerX = mobileWidth / getScale() - corner;
        double cornerY = mobileHeight / getScale() - corner;
        double ballX = ball.getWorldCenter().x;
        double ballY = ball.getWorldCenter().y;

        if (ballX < corner || ballX > cornerX  || ballY < corner || ballY > cornerY) {
            System.out.println("Bola recolocada por haber salido");
            System.out.println(corner);
            System.out.println(cornerX);
            System.out.println(cornerY);

            world.removeBody(ball);
            ball = addBall();
        }
    }

    private void checkUserStick() {
        double corner = 20 / getScale() + 20 / getScale();
        double userStickX = userStick.getWorldCenter().x;
        double userStickY = userStick.getWorldCenter().y;
        if (userStickX < corner || userStickX > mobileWidth / getScale() - corner  || userStickY < corner || userStickY > mobileHeight / getScale() - corner) {
            world.removeBody(userStick);
            userStick = addUserStick();
        }
    }

    private void checkIaStick() {
        double corner = 20 / getScale() + 20 / getScale();
        double iaStickX = iaStick.getWorldCenter().x;
        double iaStickY = iaStick.getWorldCenter().y;
        if (iaStickX < corner || iaStickX > mobileWidth / getScale() - corner  || iaStickY < corner || iaStickY > mobileHeight / getScale() - corner) {
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
            // Si está más lejos del radio de la porteria se acerca
            if (bdistancia2 > bradius * bradius) {
                iaStick.applyForce(new Vector2(iaStick.getMass().getMass() * bdistancia2 * (goals.getFixture(0).getShape().getCenter().x - iaStick.getWorldCenter().x), iaStick.getMass().getMass() * bdistancia2 * (goals.getFixture(0).getShape().getCenter().y - iaStick.getWorldCenter().y)));

            } else {
                // En cuanto se acerca , se aplica la misma fuerza contraria
                iaIsHome = true;
                iaStickColliedWithBall = false;
                iaStick.applyForce(new Vector2(iaStick.getWorldCenter().x - iaStick.getMass().getMass() * bdistancia2 * (goals.getFixture(0).getShape().getCenter().x), iaStick.getWorldCenter().y - iaStick.getMass().getMass() * bdistancia2 * (goals.getFixture(0).getShape().getCenter().y)));
                iaStick.setAngularVelocity(0);
            }
        } else {
            double dx = ball.getWorldCenter().x - iaStick.getWorldCenter().x;
            double dy = ball.getWorldCenter().y - iaStick.getWorldCenter().y;
            double radius = ball.getFixture(0).getShape().getRadius();
            double distancia2 = dx * dx + dy * dy;
            // Si pasa del centro la bola, el stick la sigue en el eje y
            if (ball.getWorldCenter().y > 725 / getScale()) {
                iaStick.applyForce(new Vector2(iaStick.getMass().getMass() * distancia2 * 0.5 * (ball.getWorldCenter().x - iaStick.getWorldCenter().x) * 0.5, 0));
            } else {
                // Cuando la bola está en su campo y lejano al stick ataca
                if (distancia2 > radius) {
                    if ((System.currentTimeMillis() - this.start) > lastAttack) {
                        iaStick.applyForce(new Vector2(iaStick.getMass().getMass() * distancia2 * 5 * (ball.getWorldCenter().x - iaStick.getWorldCenter().x) * 5, iaStick.getMass().getMass() * distancia2 * 5 * (ball.getWorldCenter().y - iaStick.getWorldCenter().y) * 5));
                        iaStick.setLinearVelocity(0, 0);
                        lastAttack += 2500;
                    }
                } else {
                    iaStick.applyForce(new Vector2(iaStick.getForce().x * (-1), iaStick.getForce().y * (-1)));
                    iaStick.applyForce(new Vector2(iaStick.getMass().getMass() * distancia2 * 0.5 * (ball.getWorldCenter().x - iaStick.getWorldCenter().x) * 0.5, 0));

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
        if (distancia2 < radius * radius /3) {
            userStick.setLinearVelocity(0, 0);
        } else
            userStick.applyForce(new Vector2(userStick.getMass().getMass() * distancia2 * 10 * (event.getX() / scale - userStick.getWorldCenter().x) * 10, userStick.getMass().getMass() * distancia2 * 10 * (event.getY() / scale - userStick.getWorldCenter().y) * 10));
    }

    @Override
    public void touchEvent(MotionEvent event, double scale) {
        if (event.getY() / scale > mobileHeight / 2 / getScale()) {
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_MOVE:
                    calculaMovimiento(event, scale);
                    break;
            }
        }
    }
}



