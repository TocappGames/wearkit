package com.tocapp.touchround;

import android.content.Context;
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

import org.dyn4j.collision.manifold.Manifold;
import org.dyn4j.collision.narrowphase.Penetration;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.CollisionListener;
import org.dyn4j.dynamics.ContinuousDetectionMode;
import org.dyn4j.dynamics.Settings;
import org.dyn4j.dynamics.World;
import org.dyn4j.dynamics.contact.ContactConstraint;
import org.dyn4j.geometry.Mass;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;

import java.util.Random;

import static java.lang.Math.abs;

;

public class AirHockey extends AbstractGame {
    public int level;
    private static int mobileWidth;
    private static int mobileHeight;
    double sidesMargin;
    double boxHeight;

    private static final Vector2 MY_GRAVITY = new Vector2(0.0, 9.8);
    private static final String TAG = "TouchRound";
    private GameObject userStick;
    private GameObject iaStick;
    private GameObject ball;
    private GameObject centerRect;
    private GameObject centerCirc;
    private long start;

    private Paint boxPaint = new Paint();
    private Paint iaPaint = new Paint();
    private Paint userPaint = new Paint();
    private Paint centerLinePaint = new Paint();
    private Paint centerCirclePaint = new Paint();

    private GameObject box;
    private GameObject goals;
    private GameObject background;

    private Rectangle goalIa;
    private Rectangle goalUser;

    private Integer userGoals = 0;
    private Integer iaGoals = 0;

    private MotionEvent event;

    double nextAttack = 10000;

    private Context context;
    private Bitmap ballBmp;

    private boolean userScores;
    private boolean iaScores;
    private boolean goalUserCollision;
    private boolean goalIaCollision;
    private boolean userWin;
    private boolean iaWin;
    private double goalTime;

    public AirHockey(int level) {
        this.level = level;
    }

    @Override
    public double getScale() {
        return 40;
    }

    private void win(String user) {
        if (user == "user") {
            userWin = true;
        } else if (user == "ia") {
            iaWin = true;
        }
    }

    private Bitmap getImage(Context context, int resId, int[] sizes, int size) {
        Bitmap bmp = null;
        if (bmp == null) {
            Matrix matrix = new Matrix();
            float scale = ((float) (size) / sizes[0]) / (float) getScale();
            matrix.postScale(scale, scale);
            bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.ball);
            bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
        }
        return bmp;
    }

    @Override
    public void init() {

        Settings settings = this.world.getSettings();
        settings.setContinuousDetectionMode(ContinuousDetectionMode.ALL);
        //settings.setMaximumTranslation(1);

        // Una regla de tres para calcular en otros mobiles, siendo en el mio 800
        /*int[] sizes = {800 * mobileWidth / 1080};
        int size = mobileWidth;
        ballBmp = getImage(context, 0, sizes, size);*/

        this.landscape.add(new Renderable() {
            @Override
            public void render(Canvas canvas, double scale) {
                // Render difficulty on screen
                String text = "";
                switch (level) {
                    case 1:
                        text = "Easy";
                        break;
                    case 2:
                        text = "Medium";
                        break;
                    case 3:
                        text = "Hard";
                        break;
                }

                Paint paint2 = new Paint();
                paint2.setColor(Color.WHITE);
                paint2.setTextSize(5 / (int) getScale());
                canvas.drawText(text, mobileWidth / 8, mobileHeight / 8, paint2);

                // Render ball
               /* Paint paint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
                float radius = (float) ball.getFixture(0).getShape().getRadius();
                canvas.save();
                // Rotate the ball?
                // canvas.rotate((float) (180 * body.getAngle() / Math.PI), scale * position.x, scale * position.y);
                canvas.drawBitmap(ballBmp, (float) getScale() * (float) (ball.getWorldCenter().x - radius), (float) getScale() * (float) (ball.getWorldCenter().y - radius), paint1);
                canvas.restore();*/

                // Render punctuation
                Paint paint = new Paint();
                paint.setColor(Color.WHITE);
                paint.setTextSize(2000 / (float) getScale());
                canvas.drawText(userGoals.toString(), mobileWidth / 8, mobileHeight / 2 + 4000 / (float) getScale(), paint);
                canvas.drawText(iaGoals.toString(), mobileWidth / 8, (float) mobileHeight / 2 - 4000 / (float) getScale(), paint);

                // Render goal text
                if (userScores) {
                    if (System.currentTimeMillis() - goalTime < 1000) {
                        paint.setTextSize(1800 / (float) getScale());
                        canvas.drawText("USER GOAL", mobileWidth / 3 + 1000 / (int) getScale(), mobileHeight / 2 + 3000 / (int) getScale(), paint);
                    }
                }

                if (iaScores) {
                    if (System.currentTimeMillis() - goalTime < 1000) {
                        paint.setTextSize(1800 / (float) getScale());
                        canvas.drawText("MACHINE GOAL", mobileWidth / 3 + 1000 / (int) getScale(), mobileHeight / 2 - 3000 / (int) getScale(), paint);
                    }
                }

                if (userWin) {
                    paint.setTextSize(1800 / (float) getScale());
                    canvas.drawText("User win!", mobileWidth / 3 + 1000 / (int) getScale(), mobileHeight / 2 - 3000 / (int) getScale(), paint);
                }

                if (iaWin) {
                    paint.setTextSize(1800 / (float) getScale());
                    canvas.drawText("Ia Win!", mobileWidth / 3 + 1000 / (int) getScale(), mobileHeight / 2 - 3000 / (int) getScale(), paint);
                }

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
                // False collision ball with centerline
                if (body1 == ball && body2 == centerRect
                        || body2 == ball && body1 == centerRect) {
                    return false;
                }

                if (body1 == userStick && body2 == centerCirc
                        || body2 == userStick && body1 == centerCirc
                        || body1 == iaStick && body2 == centerCirc
                        || body2 == iaStick && body1 == centerCirc
                        || body1 == ball && body2 == centerCirc
                        || body2 == ball && body1 == centerCirc) {
                    return false;
                }

                if (body1 == goals && body2 == ball || body1 == ball && body2 == goals) {
                    if (fixture1.getShape() == goalIa && fixture2.getShape() == ball.getFixture(0).getShape() ||
                            fixture2.getShape() == goalIa && fixture1.getShape() == ball.getFixture(0).getShape()) {
                        goalIaCollision = true;
                    }

                    if (fixture1.getShape() == goalUser && fixture2.getShape() == ball.getFixture(0).getShape()
                            || fixture2.getShape() == goalUser && fixture1.getShape() == ball.getFixture(0).getShape()) {
                        goalUserCollision = true;
                    }
                }
                return true;
            }

            @Override
            public boolean collision(Body body1, BodyFixture fixture1, Body body2, BodyFixture fixture2) {
                // False collision ball with centerline
                if (body1 == ball && body2 == centerRect
                        || body2 == ball && body1 == centerRect) {
                    return false;
                }
                if (body1 == userStick && body2 == centerCirc
                        || body2 == userStick && body1 == centerCirc
                        || body1 == iaStick && body2 == centerCirc
                        || body2 == iaStick && body1 == centerCirc
                        || body1 == ball && body2 == centerCirc
                        || body2 == ball && body1 == centerCirc) {
                    return false;
                }
                return true;
            }

            @Override
            public boolean collision(Body body1, BodyFixture fixture1, Body body2, BodyFixture fixture2, Manifold manifold) {
                // False collision ball with centerline
                if (body1 == ball && body2 == centerRect
                        || body2 == ball && body1 == centerRect) {
                    return false;
                }
                if (body1 == userStick && body2 == centerCirc
                        || body2 == userStick && body1 == centerCirc
                        || body1 == iaStick && body2 == centerCirc
                        || body2 == iaStick && body1 == centerCirc
                        || body1 == ball && body2 == centerCirc
                        || body2 == ball && body1 == centerCirc) {
                    return false;
                }
                return true;
            }

            @Override
            public boolean collision(ContactConstraint contactConstraint) {
                return true;
            }
        });

        this.start = System.currentTimeMillis();
    }

    private void goal(String goal) {
        world.removeBody(ball);
        world.removeBody(iaStick);
        world.removeBody(userStick);
        iaStick = addIaStick();
        userStick = addUserStick();
        this.goalTime = System.currentTimeMillis();
        if (goal == "user") {
            userScores = true;
            userGoals++;
        } else if (goal == "ia") {
            iaScores = true;
            System.out.println(iaScores);
            iaGoals++;
        }
    }

    private void initWorld() {
        sidesMargin = mobileWidth / getScale() * 1 / 100;
        boxHeight = mobileWidth / getScale() * 3 / 100;


        double borderGoalWidth = (mobileWidth / getScale() - sidesMargin * 2) * 35 / 100;
        double borderGoalHeight = boxHeight;

        double goalWidth = (mobileWidth / getScale() - sidesMargin * 2) * 30 / 100;
        double goalHeight = boxHeight;

        boxPaint.setColor(Color.GRAY);
        iaPaint.setColor(Color.RED);
        userPaint.setColor(Color.MAGENTA);
        centerLinePaint.setColor(Color.GRAY);
        centerCirclePaint.setColor(Color.GRAY);
        centerCirclePaint.setStrokeWidth((float) boxHeight * (float) getScale());
        centerCirclePaint.setStyle(Paint.Style.STROKE);

        Rectangle backgroundRect = new Rectangle(mobileWidth / getScale() - sidesMargin, mobileHeight / getScale() - sidesMargin);
        backgroundRect.translate(mobileWidth / 2 / getScale(), mobileHeight / 2 / getScale());

        Rectangle leftBorderGoalIa = new Rectangle(borderGoalWidth, borderGoalHeight);
        leftBorderGoalIa.translate(borderGoalWidth / 2 + sidesMargin, sidesMargin);

        Rectangle rightBorderGoalIa = new Rectangle(borderGoalWidth, borderGoalHeight);
        rightBorderGoalIa.translate(mobileWidth / getScale() - borderGoalWidth / 2 - sidesMargin, sidesMargin);

        goalUser = new Rectangle(goalWidth, goalHeight);
        goalUser.translate(mobileWidth / 2 / getScale(), mobileHeight / getScale() - sidesMargin);

        Rectangle rightBorderGoalUser = new Rectangle(borderGoalWidth, borderGoalHeight);
        rightBorderGoalUser.translate(mobileWidth / getScale() - borderGoalWidth / 2 - sidesMargin, mobileHeight / getScale() - sidesMargin);

        Rectangle leftBorderGoalUser = new Rectangle(borderGoalWidth, borderGoalHeight);
        leftBorderGoalUser.translate(borderGoalWidth / 2 + sidesMargin, mobileHeight / getScale() - sidesMargin);

        goalIa = new Rectangle(goalWidth, goalHeight);
        goalIa.translate(mobileWidth / 2 / getScale(), sidesMargin);

        Rectangle left = new Rectangle(boxHeight, mobileHeight / getScale() - sidesMargin * 2);
        left.translate(sidesMargin, mobileHeight / 2 / getScale());

        Rectangle right = new Rectangle(boxHeight, mobileHeight / getScale() - sidesMargin * 2);
        right.translate(mobileWidth / getScale() - sidesMargin, mobileHeight / 2 / getScale());

        Circle centerC = new Circle(mobileWidth / 4 / getScale());
        centerC.translate(mobileWidth / 2 / getScale(), mobileHeight / 2 / getScale());

        Rectangle centerR = new Rectangle(mobileWidth / getScale() - sidesMargin * 2, boxHeight);
        centerR.translate(mobileWidth / 2 / getScale(), mobileHeight / 2 / getScale());
        centerRect = new GameObject(centerLinePaint);
        centerCirc = new GameObject(centerCirclePaint);
        centerRect.addFixture(centerR);
        centerCirc.addFixture(centerC);


        background = new GameObject(centerLinePaint);
        background.addFixture(backgroundRect);
        background.setMass(new Mass(new Vector2(0, 0), 0, 0));
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

        box.setMass(MassType.INFINITE);

        //this.world.addBody(background);
        this.world.addBody(goals);
        this.world.addBody(box);
        this.world.addBody(centerRect);
        this.world.addBody(centerCirc);
        iaStick = addIaStick();
        userStick = addUserStick();
        ball = addBall("user");
    }


    /* ----------------------------------------------------------------------------------------- */

    private GameObject addBall(String position) {
        Paint paint = new Paint();
        paint.setColor(Color.MAGENTA);
        GameObject ball = new GameObject(paint);
        Random r = new Random();
        double xRange = r.nextInt(((200 + 200) - 200) / (int) getScale());
        System.out.println("Rango: " + xRange);
        ball.addFixture(new Circle(mobileWidth / getScale() * 5 / 100), 0.5, 0.0, 1);
        if (position == "user") {
            ball.translate(mobileWidth / 2 / getScale(), mobileHeight / getScale() * 70 / 100 + sidesMargin);
        } else if (position == "ia") {
            ball.translate(mobileWidth / 2 / getScale() + xRange, mobileHeight / getScale() * 30 / 100 - sidesMargin);
        } else if (position == "relocate") {
            // Recolocar la bola a quien se le haya salido
        }
        ball.setMass(MassType.NORMAL);
        ball.setLinearDamping(0.3);
        ball.setBullet(true);
        this.world.addBody(ball);
        return ball;
    }

    private GameObject addIaStick() {
        double x = goals.getFixture(0).getShape().getCenter().x;
        double y = goals.getFixture(0).getShape().getCenter().y + mobileHeight / getScale() * 10 / 100;

        Paint paint = new Paint();
        paint.setColor(Color.BLUE);

        GameObject iaStick = new GameObject(paint);
        iaStick.addFixture(new Circle(mobileWidth / getScale() * 7 / 100), 2, 0, 0.002);
        iaStick.translate(x, y);
        iaStick.setMass(MassType.NORMAL);
        iaStick.setLinearDamping(3);

        this.world.addBody(iaStick);
        return iaStick;
    }

    private GameObject addUserStick() {
        double x = goals.getFixture(1).getShape().getCenter().x;
        double y = goals.getFixture(1).getShape().getCenter().y - mobileHeight / getScale() * 10 / 100;

        Paint paint = new Paint();
        paint.setColor(Color.BLUE);

        GameObject userStick = new GameObject(paint);
        userStick.addFixture(new Circle(mobileWidth / getScale() * 7 / 100), 2, 0.0, 0.002);
        userStick.setMass(MassType.NORMAL);
        userStick.translate(x, y);
        userStick.setLinearDamping(20);
        this.world.addBody(userStick);
        return userStick;
    }

    @Override
    public void update() {

        if (this.event != null) {
            calculaMovimiento(event, getScale());
        }

        if (userScores) {
            if (System.currentTimeMillis() - goalTime > 1500) {
                ball = addBall("ia");
                userScores = false;
            }
        }
        if (iaScores) {
            if (System.currentTimeMillis() - goalTime > 1500) {
                ball = addBall("user");
                iaScores = false;
            }
        }

        if (!userWin && !iaWin) {
            if (System.currentTimeMillis() - goalTime > 1000) {
                calculateIa();
            }
            checkBall();
            checkUserStick();
            checkIaStick();
        }
    }

    @Override
    public void setDimensions(int width, int height, float dpi) {
        mobileWidth = width;
        mobileHeight = height;

    }

    @Override
    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public void postRender() {
        if (goalIaCollision) {
            goal("user");
            goalIaCollision = false;
            if (userGoals == 7) {
                win("user");
            }
        }

        if (goalUserCollision) {
            goal("ia");
            goalUserCollision = false;
            if (iaGoals == 7) {
                win("ia");
            }
        }
    }

    private void checkBall() {
        double corner = sidesMargin;
        double cornerX = mobileWidth / getScale() - corner;
        double cornerY = mobileHeight / getScale() - corner;
        double ballX = ball.getWorldCenter().x;
        double ballY = ball.getWorldCenter().y;

        if (ballX < corner || ballX > cornerX || ballY < corner || ballY > cornerY) {
            System.out.println("Bola recolocada por haber salido");
            world.removeBody(ball);
            ball = addBall("user");
        }
    }

    private void checkUserStick() {
        double corner = 20 / getScale() + 20 / getScale();
        double userStickX = userStick.getWorldCenter().x;
        double userStickY = userStick.getWorldCenter().y;
        if (userStickX < corner || userStickX > mobileWidth / getScale() - corner || userStickY < corner || userStickY > mobileHeight / getScale() - corner) {
            world.removeBody(userStick);
            userStick = addUserStick();
        }
    }

    private void checkIaStick() {
        double corner = 20 / getScale() + 20 / getScale();
        double iaStickX = iaStick.getWorldCenter().x;
        double iaStickY = iaStick.getWorldCenter().y;
        if (iaStickX < corner || iaStickX > mobileWidth / getScale() - corner || iaStickY < corner || iaStickY > mobileHeight / getScale() - corner) {
            world.removeBody(iaStick);
            iaStick = addIaStick();
        }
    }

    @Override
    public void finish() {
    }

    public void calculateIa() {
        double homeX = goals.getFixture(0).getShape().getCenter().x;
        double homeY = goals.getFixture(0).getShape().getCenter().y + mobileHeight / getScale() * 10 / 100;
        double dx = ball.getWorldCenter().x - iaStick.getWorldCenter().x;
        double dy = ball.getWorldCenter().y - iaStick.getWorldCenter().y;
        double radius = ball.getFixture(0).getShape().getRadius();
        double distancia2 = dx * dx + dy * dy;
        // Si pasa del centro la bola se dirige a su casa
        if (ball.getWorldCenter().y > mobileHeight / 2 / getScale()) {
            iaStick.applyForce(new Vector2(iaStick.getMass().getMass() * distancia2 * 0.5 * (homeX - iaStick.getWorldCenter().x) * 0.5, iaStick.getMass().getMass() * distancia2 * 0.5 * (homeY - iaStick.getWorldCenter().y) * 0.5));
            iaStick.setLinearVelocity(0, 0);
        } else {
            // Cuando la bola está en su campo y lejano al stick ataca
            if (distancia2 > radius) {
                if (ball.getWorldCenter().y < mobileHeight / 2 / getScale()) {
                    if (ball.getWorldCenter().x < mobileWidth / getScale() * 80 / 100 + sidesMargin
                            && ball.getWorldCenter().x > mobileWidth / getScale() * 20 / 100 + sidesMargin
                            && ball.getWorldCenter().y > mobileWidth / getScale() * 20 / 100 + sidesMargin)
                        attack();
                }
            }
        }
    }

    private void attack() {
        double force = 1;
        double ballVel = 60;
        switch (level) {
            case 1:
                ballVel = 50;
                nextAttack = 10000;
                force = 500;
                break;
            case 2:
                ballVel = 60;
                nextAttack = 10000;
                force = 500;
                break;
            case 3:
                ballVel = 70;
                nextAttack = 3000;
                force = 550;
        }
        if (abs(ball.getLinearVelocity().x) < ballVel && abs(ball.getLinearVelocity().y) < ballVel)
            iaStick.applyForce(new Vector2(iaStick.getMass().getMass() * force * (ball.getWorldCenter().x - iaStick.getWorldCenter().x), iaStick.getMass().getMass() * force * (ball.getWorldCenter().y - iaStick.getWorldCenter().y)));
        iaStick.setLinearVelocity(0, 0);
    }


    private void calculaMovimiento(MotionEvent event, double scale) {
        double radius = userStick.getFixture(0).getShape().getRadius();
        double dx = event.getX() / scale - userStick.getWorldCenter().x;
        double dy = event.getY() / scale - userStick.getWorldCenter().y - radius;
        double distancia2 = dx * dx + dy * dy;
        double stickMass = userStick.getMass().getMass();

        // Si el dedo se encuentra fuera de la bola, se mueve
        if (distancia2 > radius * radius / 16)
            userStick.applyForce(new Vector2(stickMass * distancia2 * 60 * dx * 10, stickMass * distancia2 * 60 * dy * 10));
        else
            this.event = null;
            userStick.setLinearVelocity(0, 0);
    }

    @Override
    public void touchEvent(MotionEvent event, double scale) {
        // Mientras nadie haya ganado
        if (!userWin && !iaWin) {
            double eventX = event.getX() / scale;
            double eventY = event.getY() / scale;
            // Si el evento se encuentra dentro del tablero
            if (eventX < mobileWidth / scale - sidesMargin - boxHeight / 2
                    && eventX > sidesMargin + boxHeight / 2
                    && eventY < mobileHeight / scale - sidesMargin - boxHeight / 2
                    && eventY > sidesMargin + boxHeight / 2)
                // Si el evento se encuentra en su campo
                if (eventY > mobileHeight / 2 / getScale() + boxHeight / 2)
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_DOWN:
                        case MotionEvent.ACTION_MOVE:
                            this.event = event;
                            break;
                    }
        }
    }
}



