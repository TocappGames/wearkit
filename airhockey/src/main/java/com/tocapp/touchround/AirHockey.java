package com.tocapp.touchround;

import android.content.Context;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.tocapp.sdk.engine.AbstractGame;
import com.tocapp.sdk.rendering.GameObject;
import com.tocapp.sdk.rendering.shape.Circle;
import com.tocapp.sdk.rendering.shape.Rectangle;
import com.tocapp.sdk.rendering.shape.StrokeCircle;

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

import java.util.Random;

import static java.lang.Math.abs;

public class AirHockey extends AbstractGame {
    private final int level;
    private  int mobileWidth;
    private int mobileHeight;
    private final double MY_FACTOR;
    private final double DEFAULT_AREA = 78.5;
    private double area;
    private final boolean displayIsRound;

    private boolean sound;
    private DrawUtils drawUtils;
    private SoundUtils soundUtil;

    private static final String TAG = "TouchRound";
    private GameObject userStick;
    private GameObject iaStick;
    private double iaStickBallCol;
    private GameObject ball;
    private double lastBallX;
    private double lastBallY;
    private GameObject centerRect;
    private GameObject centerCirc;
    private long start;

    private long lastSoundTime;

    private Paint boxPaint = new Paint();
    private Paint goalsPaint = new Paint();
    private Paint ballPaint = new Paint();
    private Paint sticksPaint = new Paint();
    private Paint centerCirclePaint = new Paint();

    private int backgroundImage;

    private GameObject box;
    private GameObject goals;
    private Rectangle goalIa;
    private Rectangle goalUser;

    private int userGoals = 0;
    private int iaGoals = 0;

    private MotionEvent event;
    private final double MAX_BALL_VELOCITY;
    private final double DELAY_TIME = 1500;
    private final int MAX_GOALS = 7;


    private Context context;

    private boolean userScores;
    private boolean iaScores;
    private boolean goalUserCollision;
    private boolean goalIaCollision;
    private boolean userWin;
    private boolean iaWin;
    private double goalTime;

    final double BORDER_GOAL_WIDTH_PERCENT = 0.35;
    final double GOAL_WIDTH_PERCENT = 0.3;
    final double BOX_HEIGHT_PERCENT = 0.03;
    private double BOX_HEIGHT;
    private double WIDTH_SCALED;
    private double HEIGHT_SCALED;
    private double CENTER_WIDTH;
    private double CENTER_HEIGHT;

    public AirHockey(Config config) {
        this.area = config.getArea();
        MAX_BALL_VELOCITY = this.area / 2;
        MY_FACTOR = area / DEFAULT_AREA;

        System.out.println( area );
        this.level = config.getLevel();
        this.sound = config.haveSound();
        this.displayIsRound = config.isDisplayIsRound();
        this.backgroundImage = config.getBackgroundImage();
        this.ballPaint.setColor( config.getBallColor() );
        this.sticksPaint.setColor( config.getSticksColor() );
        this.boxPaint.setColor( config.getBoxColor() );
        this.goalsPaint.setColor( config.getGoalsColor() );
    }

    @Override
    public double getScale() {
        return 40;
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
    public boolean onGenericMotionEvent(MotionEvent ev) {
        System.out.println( ev );
        return true;
    }

    private void win(String user) {
        if (user.equals("user")) {
            userWin = true;
        } else if (user.equals( "ia" )) {
            iaWin = true;
        }
    }


    @Override
    public void init() {
        initWorld();
        soundUtil = new SoundUtils( context, sound );
        Settings settings = this.world.getSettings();
        this.world.setUpdateRequired( false );
        settings.setContinuousDetectionMode( ContinuousDetectionMode.ALL );
        //settings.setMaximumTranslation(1);

        drawUtils = new DrawUtils( mobileWidth, mobileHeight, BOX_HEIGHT, 0, context, getScale() );
        drawUtils.drawBackground( backgroundLandscape, backgroundImage );

        this.world.setGravity( World.ZERO_GRAVITY );

        this.world.addListener( new CollisionListener() {
            @Override
            public boolean collision(Body body1, BodyFixture fixture1, Body body2, BodyFixture fixture2, Penetration penetration) {
                if (body1 == ball && body2 == centerRect
                        || body2 == ball && body1 == centerRect) {
                    return false;
                }

              /*  if (body1 == userStick && body2 == box
                || body1 == box && body2 == userStick) {
                    double boxX = box.getWorldCenter().x;
                    double boxY = box.getWorldCenter().y;
                    double boxRadius = box.getFixture( 0 ).getShape().getRadius();
                    double ballX = userStick.getWorldCenter().x;
                    double ballY = userStick.getWorldCenter().y;
                    double ballRadius = userStick.getFixture( 0 ).getShape().getRadius();

                    double distance = Math.sqrt((boxX-ballX)*(boxX-ballX) + (boxY-ballY)*(boxY-ballY));
                    if (distance > boxRadius - ballRadius)
                        return true;
                    else return false;
                }
*/
                /*if (body1 == iaStick && body2 == box
                        || body1 == box && body2 == iaStick) {
                    double boxX = box.getWorldCenter().x;
                    double boxY = box.getWorldCenter().y;
                    double boxRadius = box.getFixture( 0 ).getShape().getRadius();
                    double ballX = iaStick.getWorldCenter().x;
                    double ballY = iaStick.getWorldCenter().y;
                    double ballRadius = iaStick.getFixture( 0 ).getShape().getRadius();

                    double distance = Math.sqrt((boxX-ballX)*(boxX-ballX) + (boxY-ballY)*(boxY-ballY));
                    if (distance > boxRadius - ballRadius) return true;
                    else return false;
                }*/

                /*if (body1 == ball && body2 == box
                        || body1 == box && body2 == ball) {
                    double boxX = box.getWorldCenter().x;
                    double boxY = box.getWorldCenter().y;
                    double boxRadius = box.getFixture( 0 ).getShape().getRadius();
                    double ballX = ball.getWorldCenter().x;
                    double ballY = ball.getWorldCenter().y;
                    double ballRadius = ball.getFixture( 0 ).getShape().getRadius();

                    double distance = Math.sqrt((boxX-ballX)*(boxX-ballX) + (boxY-ballY)*(boxY-ballY));
                    if (distance >= boxRadius - ballRadius) return true;
                    else return false;
                }
*/
                if (body1 == ball && body2 == iaStick
                    ||body2 == ball && body1 == iaStick) {
                    iaStickBallCol = System.currentTimeMillis();
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
                    if (fixture1.getShape() == goalIa && fixture2.getShape() == ball.getFixture( 0 ).getShape() ||
                            fixture2.getShape() == goalIa && fixture1.getShape() == ball.getFixture( 0 ).getShape()) {
                        goalIaCollision = true;
                    }

                    if (fixture1.getShape() == goalUser && fixture2.getShape() == ball.getFixture( 0 ).getShape()
                            || fixture2.getShape() == goalUser && fixture1.getShape() == ball.getFixture( 0 ).getShape()) {
                        goalUserCollision = true;
                    }
                }

                // Sound on collision
                if (body1 == ball && body2 != centerRect
                        || body2 == ball && body1 != centerRect
                        || body1 == ball && body2 != centerCirc
                        || body2 == ball && body1 != centerCirc) {
                    soundUtil.startTapSound();

                }
                return true;
            }

            @Override
            public boolean collision(Body body1, BodyFixture fixture1, Body body2, BodyFixture fixture2) {
                // False collision ball with centerline
                return (body1 != ball || body2 != centerRect)
                        && (body2 != ball || body1 != centerRect);
            }

            @Override
            public boolean collision(Body body1, BodyFixture fixture1, Body body2, BodyFixture fixture2, Manifold manifold) {
                // False collision ball with centerline
                return (body1 != ball || body2 != centerRect)
                        && (body2 != ball || body1 != centerRect);
            }

            @Override
            public boolean collision(ContactConstraint contactConstraint) {
                return true;
            }
        } );

        this.start = System.currentTimeMillis();
        lastSoundTime = this.start;

    }

    private void goal(String goal) {
        soundUtil.startGoalSound();
        world.removeBody( ball );
        world.removeBody( iaStick );
        world.removeBody( userStick );
        iaStick = addIaStick();
        userStick = addUserStick();
        this.goalTime = System.currentTimeMillis();
        if (goal.equals( "user" )) {
            userScores = true;
            userGoals++;
        } else if (goal.equals( "ia" )) {
            iaScores = true;
            iaGoals++;
        }
    }

    private void initWorld() {
        WIDTH_SCALED = mobileWidth / getScale();
        HEIGHT_SCALED = mobileHeight / getScale();
        CENTER_WIDTH = WIDTH_SCALED / 2;
        CENTER_HEIGHT = HEIGHT_SCALED / 2;

        BOX_HEIGHT = WIDTH_SCALED * BOX_HEIGHT_PERCENT;
        final double BORDER_GOAL_WIDTH = (WIDTH_SCALED) * BORDER_GOAL_WIDTH_PERCENT;
        final double BORDER_GOAL_HEIGTH = BOX_HEIGHT;
        final double GOAL_WIDTH = (WIDTH_SCALED) * GOAL_WIDTH_PERCENT;
        final double GOAL_HEIGHT = BOX_HEIGHT;
        final double BOARD_WIDTH = WIDTH_SCALED;
        final double BOARD_HEIGHT = HEIGHT_SCALED;
        final double CENTER_CIRC_RADIUS = BOARD_WIDTH * 0.25;

        if (!displayIsRound) {
            // Ia map
            Rectangle leftBorderGoalIa = new Rectangle( BORDER_GOAL_WIDTH, BORDER_GOAL_HEIGTH );
            leftBorderGoalIa.translate( BORDER_GOAL_WIDTH * 0.5, GOAL_HEIGHT * 0.5 );

            goalIa = new Rectangle( GOAL_WIDTH, GOAL_HEIGHT );
            goalIa.translate( CENTER_WIDTH, GOAL_HEIGHT * 0.5 );

            Rectangle rightBorderGoalIa = new Rectangle( BORDER_GOAL_WIDTH, BORDER_GOAL_HEIGTH );
            rightBorderGoalIa.translate( BOARD_WIDTH - BORDER_GOAL_WIDTH * 0.5, GOAL_HEIGHT * 0.5 );

            // User map
            Rectangle leftBorderGoalUser = new Rectangle( BORDER_GOAL_WIDTH, BORDER_GOAL_HEIGTH );
            leftBorderGoalUser.translate( BORDER_GOAL_WIDTH * 0.5, BOARD_HEIGHT - GOAL_HEIGHT * 0.5 );

            goalUser = new Rectangle( GOAL_WIDTH, GOAL_HEIGHT );
            goalUser.translate( CENTER_WIDTH, BOARD_HEIGHT - GOAL_HEIGHT * 0.5 );

            Rectangle rightBorderGoalUser = new Rectangle( BORDER_GOAL_WIDTH, BORDER_GOAL_HEIGTH );
            rightBorderGoalUser.translate( BOARD_WIDTH - BORDER_GOAL_WIDTH * 0.5, BOARD_HEIGHT - GOAL_HEIGHT * 0.5 );

            // Sides walls
            Rectangle left = new Rectangle( BOX_HEIGHT, BOARD_HEIGHT );
            left.translate( BOX_HEIGHT * 0.5, CENTER_HEIGHT );

            Rectangle right = new Rectangle( BOX_HEIGHT, BOARD_HEIGHT );
            right.translate( BOARD_WIDTH - BOX_HEIGHT * 0.5, CENTER_HEIGHT );

            // Center objects
            Circle centerC = new Circle( CENTER_CIRC_RADIUS);
            centerC.translate( CENTER_WIDTH, CENTER_HEIGHT );

            Rectangle centerR = new Rectangle( BOARD_WIDTH, BOX_HEIGHT );
            centerR.translate( CENTER_WIDTH, CENTER_HEIGHT );

            centerCirclePaint.setColor( boxPaint.getColor() );
            centerCirclePaint.setStrokeWidth( (float) BOX_HEIGHT * (float) getScale() );
            centerCirclePaint.setStyle( Paint.Style.STROKE );

            centerRect = new GameObject( boxPaint );
            centerCirc = new GameObject( centerCirclePaint );
            centerRect.addFixture( centerR );
            centerCirc.addFixture( centerC );


            goals = new GameObject( goalsPaint );
            box = new GameObject( boxPaint );
            box.setMass( MassType.INFINITE );

            //Ia map
            box.addFixture( leftBorderGoalIa );
            goals.addFixture( goalIa );
            box.addFixture( rightBorderGoalIa );

            //User map
            box.addFixture( leftBorderGoalUser );
            goals.addFixture( goalUser );
            box.addFixture( rightBorderGoalUser );

            // Sides wallsa
            box.addFixture( left );
            box.addFixture( right );

            this.world.addBody( goals );
            this.world.addBody( box );
            this.world.addBody( centerRect );
            this.world.addBody( centerCirc );

        } else {
            box = new GameObject( boxPaint );
            box.addFixture( new StrokeCircle( CENTER_HEIGHT ) );
            box.translate( CENTER_WIDTH, CENTER_HEIGHT );
            this.world.addBody( box );
        }
        iaStick = addIaStick();
        userStick = addUserStick();
        ball = addBall( "center" );
    }

    private void addObject() {

    }


    /* ----------------------------------------------------------------------------------------- */

    private GameObject addBall(String position) {
        Random r = new Random();
        int BALL_RANGE_PERCENT = (int) WIDTH_SCALED / 4;
        double xRange = r.nextInt( (BALL_RANGE_PERCENT + BALL_RANGE_PERCENT) - BALL_RANGE_PERCENT );
        final double IA_HOME_Y = HEIGHT_SCALED * 0.4;
        final double USER_HOME_Y = HEIGHT_SCALED * 0.6;
        final double BALL_RADIUS = WIDTH_SCALED * 0.05;
        final double LINEAR_DAMPING = 0.2 * MY_FACTOR;
        final double DENSITY = 0.5 * MY_FACTOR;
        final double RESTITUTION = 1;

        GameObject ball = new GameObject( ballPaint );
        ball.addFixture( new Circle( BALL_RADIUS ), DENSITY, 0.0, RESTITUTION );

        switch (position) {
            case "user":
                ball.translate( CENTER_WIDTH, USER_HOME_Y );
                break;
            case "ia":
                ball.translate( CENTER_WIDTH + xRange, IA_HOME_Y );
                break;
            case "center":
                ball.translate( CENTER_WIDTH, CENTER_HEIGHT );
                break;
            case "last":
                ball.translate( lastBallX, lastBallY );
                break;
        }

        ball.setMass( MassType.NORMAL );
        System.out.println("Masa de la bola" +  ball.getMass() );
        ball.setLinearDamping( LINEAR_DAMPING );
        ball.setBullet( true );
        this.world.addBody( ball );
        return ball;
    }

    private GameObject addIaStick() {
        final double HOME_Y = HEIGHT_SCALED * 0.2;
        final double STICK_RADIUS = WIDTH_SCALED * 0.07;
        final double DENSITY = 1 * MY_FACTOR;
        final double RESTITUTION = 0.002;
        final double DAMPING = 3 * MY_FACTOR;

        Paint paint = new Paint( sticksPaint );

        GameObject iaStick = new GameObject( paint );
        iaStick.addFixture( new Circle( STICK_RADIUS ), DENSITY, 0, RESTITUTION );
        iaStick.translate( CENTER_WIDTH, HOME_Y );
        iaStick.setMass( MassType.NORMAL );
        iaStick.setLinearDamping( DAMPING );

        this.world.addBody( iaStick );
        return iaStick;
    }

    private GameObject addUserStick() {
        final double HOME_X = CENTER_WIDTH;
        final double HOME_Y = HEIGHT_SCALED * 0.8;
        final double STICK_RADIUS = WIDTH_SCALED * 0.07;
        final double LINEAR_DAMPING = 20 * MY_FACTOR;
        final double DENSITY = 1 * MY_FACTOR;
        final double RESTITUTION = 0.002;

        GameObject userStick = new GameObject( sticksPaint );
        userStick.addFixture( new Circle( STICK_RADIUS ), DENSITY, 0.0, RESTITUTION );
        userStick.setMass( MassType.NORMAL );
        userStick.translate( HOME_X, HOME_Y );
        userStick.setLinearDamping( LINEAR_DAMPING );
        this.world.addBody( userStick );
        return userStick;
    }

    @Override
    public void update() {

        drawUtils.drawDifficulty( landscape, level );
        drawUtils.drawGoals( landscape, userScores, iaScores, iaWin, userWin, goalTime );
        drawUtils.drawPuncuation( landscape, userGoals, iaGoals );
/*

        if (ball.getLinearVelocity().x >= MAX_BALL_VELOCITY)
            ball.setLinearVelocity( new Vector2( MAX_BALL_VELOCITY, ball.getLinearVelocity().y ) );
        else if (ball.getLinearVelocity().y >= MAX_BALL_VELOCITY)
            ball.setLinearVelocity( new Vector2( ball.getLinearVelocity().x, MAX_BALL_VELOCITY ) );
*/

        if (this.event != null) {
            calculaMovimiento( event, getScale() );
        }


        if (!userWin && !iaWin) {
            calculateIa();
            if (userScores) {
                if (System.currentTimeMillis() - goalTime > DELAY_TIME) {
                    ball = addBall( "ia" );
                    userScores = false;
                }
            }

            if (iaScores) {
                if (System.currentTimeMillis() - goalTime > DELAY_TIME) {
                    ball = addBall( "user" );
                    iaScores = false;
                }
            }

            checkBall();
            checkUserStick();
            checkIaStick();
        }
    }


    @Override
    public void postRender() {
        if (goalIaCollision) {
            goal( "user" );
            goalIaCollision = false;
            if (userGoals == MAX_GOALS) {
                win( "user" );
            }
        }

        if (goalUserCollision) {
            goal( "ia" );
            goalUserCollision = false;
            if (iaGoals == MAX_GOALS) {
                soundUtil.startLoseSound();
                win( "ia" );
            }
        }
    }

    private void checkBall() {
        final double CORNER = 0;
        final double CORNER_X = WIDTH_SCALED - CORNER;
        final double CORNER_Y = HEIGHT_SCALED - CORNER;
        final double BALL_RADIUS = ball.getFixture( 0 ).getShape().getRadius();

        if (ball.getWorldCenter().x < CORNER || ball.getWorldCenter().x > CORNER_X || ball.getWorldCenter().y < CORNER || ball.getWorldCenter().y > CORNER_Y) {
            if (ball.getWorldCenter().x < CORNER) lastBallX += BALL_RADIUS;
            if (ball.getWorldCenter().x > CORNER_X) lastBallX -= BALL_RADIUS;
            if (ball.getWorldCenter().y < CORNER) lastBallY += BALL_RADIUS;
            if (ball.getWorldCenter().y > CORNER_Y) lastBallY -= BALL_RADIUS;
            world.removeBody( ball );
            ball = addBall( "last" );
        } else {
            lastBallX = ball.getWorldCenter().x;
            lastBallY = ball.getWorldCenter().y;
        }
    }

    private void checkUserStick() {
        final double CORNER = 0;
        if (userStick.getWorldCenter().x < CORNER || userStick.getWorldCenter().x > mobileWidth / getScale() - CORNER || userStick.getWorldCenter().y < CORNER || userStick.getWorldCenter().y > mobileHeight / getScale() - CORNER) {
            world.removeBody( userStick );
            userStick = addUserStick();
        }
    }

    private void checkIaStick() {
        final double CORNER = 0;
        if (iaStick.getWorldCenter().x < CORNER || iaStick.getWorldCenter().x > WIDTH_SCALED - CORNER || iaStick.getWorldCenter().y < CORNER || iaStick.getWorldCenter().y > HEIGHT_SCALED - CORNER) {
            world.removeBody( iaStick );
            iaStick = addIaStick();
        }
    }

    @Override
    public void finish() {
    }

    private void calculateIa() {
        double homeX;
        double homeY;
        if (!displayIsRound) {
            homeX = goals.getFixture( 0 ).getShape().getCenter().x;
            homeY = goals.getFixture( 0 ).getShape().getCenter().y + HEIGHT_SCALED * 0.1;
        } else {
            homeX = WIDTH_SCALED * 0.5;
            homeY = HEIGHT_SCALED * 0.2;
        }
        double dx = ball.getWorldCenter().x - iaStick.getWorldCenter().x;
        double dy = ball.getWorldCenter().y - iaStick.getWorldCenter().y;
        double radius = ball.getFixture( 0 ).getShape().getRadius();
        double distancia2 = dx * dx + dy * dy;
        // Si pasa del centro la bola se dirige a su casa
        if (ball.getWorldCenter().y > CENTER_HEIGHT) {
            iaStick.applyForce( new Vector2( (iaStick.getMass().getMass() * distancia2 * 0.5 * (homeX - iaStick.getWorldCenter().x) * 0.5) * MY_FACTOR, (iaStick.getMass().getMass() * distancia2 * 0.5 * (homeY - iaStick.getWorldCenter().y) * 0.5) * MY_FACTOR ) );
            iaStick.setLinearVelocity( 0, 0 );
        } else {
            // Cuando la bola está en su campo y lejano al stick ataca
            if (distancia2 > radius) {
                if (ball.getWorldCenter().y < CENTER_HEIGHT) {
                    if (iaStickBallCol + 500 < System.currentTimeMillis())
                       /* if (ball.getWorldCenter().x < mobileWidth / getScale() * 0.9
                            && ball.getWorldCenter().x > mobileWidth / getScale() * 0.1
                            && ball.getWorldCenter().y > mobileWidth / getScale() * 0.1)*/
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
                ballVel = 45;
                double nextAttack = 10000;
                force = 350;
                break;
            case 2:
                ballVel = 55;
                nextAttack = 10000;
                force = 450;
                break;
            case 3:
                ballVel = 65;
                nextAttack = 3000;
                force = 600;
        }
        if (abs( ball.getLinearVelocity().x ) < ballVel && abs( ball.getLinearVelocity().y ) < ballVel)
            iaStick.applyForce( new Vector2( (iaStick.getMass().getMass() * force * (ball.getWorldCenter().x - iaStick.getWorldCenter().x)) * MY_FACTOR, (iaStick.getMass().getMass() * force * (ball.getWorldCenter().y - iaStick.getWorldCenter().y)) * MY_FACTOR ) );
        iaStick.setLinearVelocity( 0, 0 );
    }


    private void calculaMovimiento(MotionEvent event, double scale) {
        final double RADIUS = userStick.getFixture( 0 ).getShape().getRadius();
        final double DX = event.getX() / scale - userStick.getWorldCenter().x;
        final double DY = event.getY() / scale - userStick.getWorldCenter().y - RADIUS;
        final double DISTANCIA2 = DX * DX + DY * DY;
        final double STICK_MASS = userStick.getMass().getMass();
        final double EXTRA_FORCE = 10000;
        // Si el dedo se encuentra fuera de la bola, se mueve
        final double MOVE_DISTANCE = RADIUS * RADIUS / 22;
        if (DISTANCIA2 > MOVE_DISTANCE)
            userStick.applyForce( new Vector2( STICK_MASS * DISTANCIA2 * DX * EXTRA_FORCE, STICK_MASS * DISTANCIA2 * DY * EXTRA_FORCE ) );
        else
            this.event = null;
        userStick.setLinearVelocity( 0, 0 );
    }


    @Override
    public void touchEvent(MotionEvent event, double scale) {
        // Mientras nadie haya ganado
        double ballRadius = ball.getFixture( 0 ).getShape().getRadius();
        if (!userWin && !iaWin) {
            double eventX = event.getX() / scale;
            double eventY = event.getY() / scale;
            // Si el evento se encuentra dentro del tablero
            if (eventX < WIDTH_SCALED - BOX_HEIGHT - ballRadius
                    && eventX > BOX_HEIGHT + ballRadius
                    && eventY < HEIGHT_SCALED - BOX_HEIGHT
                    && eventY > CENTER_HEIGHT + BOX_HEIGHT + ballRadius * 2) {
                // Si el evento se encuentra en su campo
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                        this.event = event;
                        break;
                }
            } else {
                this.event = null;
            }
        }
    }


}



