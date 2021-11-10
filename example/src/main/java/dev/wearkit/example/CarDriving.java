package dev.wearkit.example;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.view.MotionEvent;

import com.google.android.wearable.input.RotaryEncoderHelper;

import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.Torque;
import org.dyn4j.world.BroadphaseCollisionData;
import org.dyn4j.world.ManifoldCollisionData;
import org.dyn4j.world.NarrowphaseCollisionData;
import org.dyn4j.world.World;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;
import org.dyn4j.world.listener.CollisionListener;

import java.util.ArrayDeque;
import java.util.Queue;

import dev.wearkit.core.data.Loader;
import dev.wearkit.core.engine.AbstractGame;
import dev.wearkit.core.exceptions.LoadException;
import dev.wearkit.core.rendering.Body;
import dev.wearkit.core.rendering.BodyCamera;
import dev.wearkit.core.rendering.Ornament;
import dev.wearkit.core.rendering.shape.Circle;
import dev.wearkit.core.rendering.shape.Polygon;
import dev.wearkit.core.rendering.shape.Text;

public class CarDriving extends AbstractGame {

    private static final String TAG = "CarDriving";
    private final Loader<Body> bodies;
    private final Loader<Ornament> ornaments;
    private Body me;
    private Torque turnTorque;
    private Queue<Ornament> wheelPointsLeft = new ArrayDeque<>();
    private Queue<Ornament> wheelPointsRight = new ArrayDeque<>();
    private static Paint wheelMarksPaint = new Paint(Color.RED);

    private TextPaint dashTextPaint = new TextPaint();
    private long start = System.currentTimeMillis();

    static {
        wheelMarksPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    private long startTimeRotary = 0;
    private static final long ROTARY_DURATION_MILLIS = 200;
    private int laps = -1;
    private double motor = 200;
    private int gear = 3;
    private int rpm = 4000;
    private double best = -1;

    public CarDriving(Loader<Body> bodies, Loader<Ornament> ornaments){
        this.bodies = bodies;
        this.ornaments = ornaments;
    }

    @Override
    public void init() {
        world.setGravity(World.ZERO_GRAVITY);
        world.getSettings().setMaximumTranslation(10);
        Vector2 wc = world.getSize().copy().divide(2);

        dashTextPaint.setColor(Color.RED);
        dashTextPaint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD_ITALIC));
        dashTextPaint.setTextSize(50);
        dashTextPaint.setTextAlign(Paint.Align.CENTER);

        //double finishAngle = - Math.PI / 4; // circuit1
        //double finishAngle = 0; // circuit1

        // c1 finish: 546x1288
        // c1 finish vector: 656x1124
        // c1 size: 2000x2000
        Vector2 circuSize = new Vector2(2000, 2000);
        Vector2 finishCircuPos = new Vector2(546, 1288);
        Vector2 finishVector = new Vector2(656, 1124);

        double finishAngle = finishCircuPos.copy().subtract(finishVector).rotate(Math.PI / 2).getDirection();

        Vector2 finishPos = new Vector2(
                finishCircuPos.x - circuSize.x / 2,
                finishCircuPos.y - circuSize.y / 2
        ); // circuit1
        //Vector2 finishPos = wc.copy().add(new Vector2(310, 773-1000)); // circuit2

        try {
/*
            Body circuit = this.loader.load("circuit.png").scale(4);
            circuit.setMass(MassType.INFINITE);
            circuit.translate(wc);
            world.addBody(circuit);
            */

            //double circuScale = 1.5;

            //Body circuit = this.bodies.load("circuit_round.png");//.scale(circuScale);
            Body circuit = this.bodies.load("circuit1-background.png");//.scale(circuScale);
            //Body circuit = this.bodies.load("circuit2-background.png").scale(circuScale);
            circuit.setMass(MassType.INFINITE);
            circuit.translate(wc);
            world.addBody(circuit);

            Body finish = this.bodies.load("finish-line.png").scale(0.1);
            finish.rotate(finishAngle);
            finish.translate(wc);
            finish.translate(finishPos);
            world.addBody(finish, 1);

            world.addCollisionListener(new CollisionListener<org.dyn4j.dynamics.Body, BodyFixture>() {
                @Override
                public boolean collision(BroadphaseCollisionData<org.dyn4j.dynamics.Body, BodyFixture> collision) {
                    org.dyn4j.dynamics.Body b1 = collision.getBody1();
                    org.dyn4j.dynamics.Body b2 = collision.getBody2();
                    if (b1.equals(me) && b2.equals(finish) || b1.equals(finish) && b2.equals(me)) {
                        long millisSinceCollision = System.currentTimeMillis() - start;
                        if (millisSinceCollision > 10000) {
                            double lapTime = millisSinceCollision / 1000.0;
                            if (best > 0) best = Math.min(lapTime, best);
                            else best = lapTime;
                            start = System.currentTimeMillis();

                            double expectedAngle = finishAngle - Math.PI / 2;
                            Vector2 carAngle = me.getLinearVelocity();
                            if (Math.abs(carAngle.getAngleBetween(expectedAngle)) < Math.PI) {
                                laps++;
                            } else {
                                laps--;
                            }
                        }
                        return false;
                    }
                    return true;
                }

                @Override
                public boolean collision(NarrowphaseCollisionData<org.dyn4j.dynamics.Body, BodyFixture> collision) {
                    org.dyn4j.dynamics.Body b1 = collision.getBody1();
                    org.dyn4j.dynamics.Body b2 = collision.getBody2();
                    if (b1.equals(me) && b2.equals(circuit) || b1.equals(circuit) && b2.equals(me)) {
                        //me.setLinearVelocity(0, 0);
                        return true;
                    }
                    return true;
                }

                @Override
                public boolean collision(ManifoldCollisionData<org.dyn4j.dynamics.Body, BodyFixture> collision) {
                    return true;
                }
            });


            this.me = this.bodies.load("car3.png").scale(0.55);
            //this.me.stamp(null);
            for(BodyFixture fixture: this.me.getFixtures()){
                fixture.setDensity(0.002);
                fixture.setRestitution(0.2);
            }

            //this.car.getFixtures().forEach(f -> f.setDensity(0.02)); // java8
            this.me.setMass(MassType.NORMAL);
            this.me.rotate(finishAngle);
            this.me.translate(wc.copy().add(finishPos.x, finishPos.y));

            this.me.setLinearDamping(3);
            this.me.setAngularDamping(3);

            world.addBody(this.me, 2);

            BodyCamera bc = new BodyCamera(this.me);//.zoom(4);
            bc.setAngleMode(BodyCamera.MODE_BODY_ANGLE);
            this.world.getViewport().setCamera(bc);


        } catch (LoadException e) {
            throw new NullPointerException("ERROR: " + e.getMessage());
        }

    }

    private void print(String text, int color, int size, double theta, double radius) {

        Ornament speedText = new Ornament();
        Paint speedTextPaint = new Paint();
        speedTextPaint.setColor(color);
        speedTextPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        speedTextPaint.setTextSize(size);
        speedTextPaint.setTextAlign(Paint.Align.CENTER);
        speedTextPaint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD_ITALIC));

        speedText.paint(speedTextPaint);

        Text speedTextText = new Text(text);
        speedText.addFixture(speedTextText);

        Vector2 place = Vector2.create(radius, theta);
        speedText.rotate(theta - Math.PI / 2);
        speedText.translate(place);
        this.world.getViewport().addOrnament(speedText);
    }

    private void gearBox() {
        int maxGear = 7;
        int minGear = 4;

        if (this.gear < 0) return;

        if(Math.abs(me.getAngularVelocity()) > Math.PI / 3) {
            this.rpm = Math.max(this.rpm - 120, 0);
        }
        else {
            this.rpm = Math.min(this.rpm + 50, 7500);
        }

        if (this.rpm >= 7000) {
            if (this.gear < maxGear)
                this.rpm = 3000;
            this.gear = Math.min(this.gear + 1, maxGear);
        }
        else if (this.rpm <= 3000) {
            if (this.gear > minGear)
                this.rpm = 7000;
            this.gear = Math.max(this.gear - 1, minGear);
        }
    }

    @Override
    public void update() {

        this.gearBox();

        double time = (System.currentTimeMillis() - this.start) / 1000.0;
        double velocity = this.me.getLinearVelocity().getMagnitude();

        this.world.getViewport().clear();


        String stringGear = String.format("%d", this.gear);
        if (this.gear < 0) stringGear = "R";
        this.print(
                stringGear,
                Color.RED,
                30,
                5 * Math.PI / 16,
                -this.world.getSize().y / 2 + 40
        );

        this.print(
                "gear",
                Color.GRAY,
                20,
                5 * Math.PI / 16,
                -this.world.getSize().y / 2 + 60
        );

        this.print(
                String.format("%3.0f", velocity),
                Color.RED,
                50,
                Math.PI / 2,
                -this.world.getSize().y / 2 + 50
        );

        this.print(
                "km/h",
                Color.GRAY,
                20,
                Math.PI / 2,
                -this.world.getSize().y / 2 + 70
        );

        this.print(
                String.format("%3.2f", time),
                Color.BLUE,
                30,
                11 * Math.PI / 16,
                -this.world.getSize().y / 2 + 40
        );

        this.print(
                "time",
                Color.GRAY,
                20,
                11 * Math.PI / 16,
                -this.world.getSize().y / 2 + 60
        );

        String bestStr =  String.format("%3.2f", this.best);
        if (this.best < 0) bestStr = "--.--";
        this.print(
                bestStr,
                Color.DKGRAY,
                30,
                7 * Math.PI / 8,
                -this.world.getSize().y / 2 + 40
        );

        this.print(
                "best",
                Color.GRAY,
                20,
                7 * Math.PI / 8,
                -this.world.getSize().y / 2 + 60
        );


        Ornament speedometer = new Ornament();
        Paint speedMarkerPaint = new Paint();
        speedMarkerPaint.setColor(Color.RED);
        speedMarkerPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        speedometer.paint(speedMarkerPaint);

        Polygon speedMarker = new Polygon(
                new Vector2(0, 0),
                new Vector2(-10, -50),
                new Vector2(10, -50)
        );

        speedometer.addFixture(speedMarker);
        speedometer.translate(0, - this.world.getSize().y / 2 + 50);

        //rpm
        speedometer.rotate(- 4 * Math.PI / 8 + this.rpm / 8000.0 * (Math.PI / 4), 0, 0);

        //kmh
        //speedometer.rotate(- 5 * Math.PI / 8 + velocity / 300 * Math.PI / 2, 0, 0);

        this.world.getViewport().addOrnament(speedometer);

        if(Math.abs(me.getAngularVelocity()) > Math.PI / 3) {
            Ornament wheelMarksRight = new Ornament();
            Ornament wheelMarksLeft = new Ornament();
            wheelMarksRight.paint(wheelMarksPaint);
            wheelMarksLeft.paint(wheelMarksPaint);
            Circle circle = new Circle(2);
            wheelMarksRight.addFixture(circle);
            wheelMarksLeft.addFixture(circle);

            Vector2 carRearAxlePosition = new Vector2(this.me.getTransform().getRotationAngle());
            carRearAxlePosition.rotate(-Math.PI / 2);
            carRearAxlePosition.multiply(-13);

            Vector2 carRightDirectionVector = carRearAxlePosition.getRightHandOrthogonalVector();
            carRightDirectionVector.setMagnitude(12 / 2.0);
            Vector2 carLeftDirectionVector = carRearAxlePosition.getLeftHandOrthogonalVector();
            carLeftDirectionVector.setMagnitude(12 / 2.0);

            Vector2 rearRightWheelPosition = me.getWorldCenter().copy().add(carRearAxlePosition).add(carRightDirectionVector);
            Vector2 rearLeftWheelPosition = me.getWorldCenter().copy().add(carRearAxlePosition).add(carLeftDirectionVector);

            wheelMarksRight.translate(rearRightWheelPosition);
            wheelMarksLeft.translate(rearLeftWheelPosition);

            world.addOrnament(wheelMarksRight, 1);
            world.addOrnament(wheelMarksLeft, 1);

            wheelPointsRight.add(wheelMarksRight);
            wheelPointsLeft.add(wheelMarksLeft);
        }

        if(wheelPointsRight.size() > 100)
            world.removeOrnament(wheelPointsRight.poll(), 1);

        if(wheelPointsLeft.size() > 100)
            world.removeOrnament(wheelPointsLeft.poll(), 1);

        Vector2 accelForce = new Vector2(this.me.getTransform().getRotationAngle());
        accelForce.rotate(-Math.PI / 2);
        accelForce.setMagnitude(motor * gear);
        this.me.applyForce(accelForce);//, rearAxis);

        if (this.startTimeRotary > 0 && System.currentTimeMillis() > this.startTimeRotary + ROTARY_DURATION_MILLIS) {
            this.turnTorque = null;
            this.startTimeRotary = 0;
        }
        if (this.turnTorque != null){
            this.me.applyTorque(this.turnTorque);
        }

    }

    @Override
    public void finish() {

    }

    @Override
    public boolean onMotionEvent(MotionEvent event) {


        switch (event.getActionMasked()){

            case MotionEvent.ACTION_SCROLL:
                float rotation = RotaryEncoderHelper.getRotaryAxisValue(event);
                this.turnTorque = new Torque(-200000 / 50.0 * rotation);
                this.startTimeRotary = System.currentTimeMillis();
                break;

            case MotionEvent.ACTION_DOWN:
                this.startTimeRotary = 0;

                // TOUCH UP
                if (event.getY() < this.world.getSize().y * 0.2) {
                    this.gear = 1;
                    //if (this.gear < 6) this.gear++; // = 1000; //this.me.getForce().getMagnitude() * 1.5;
                    //this.me.applyForce(this.me.getForce().multiply(1.5));
                }
                // TOUCH DOWN
                else if (event.getY() > this.world.getSize().y * 0.8) {
                    this.gear = -1;
                    //if (this.gear > -1) this.gear--; // = -200;
                    //this.me.applyForce(this.me.getForce().multiply(-2));
                }
                // TOUCH RIGHT
                else if (event.getX() > this.world.getSize().x * 0.5){
                    if (this.gear > 0)
                        this.turnTorque = new Torque(200000 / 100.0);
                    else
                        this.turnTorque = new Torque(-200000 / 100.0);
                }

                // TOUCH LEFT
                else {
                    if (this.gear > 0)
                        this.turnTorque = new Torque(-200000 / 100.0);
                    else
                        this.turnTorque = new Torque(200000 / 100.0);
                }
                break;
            case MotionEvent.ACTION_UP:
                this.turnTorque = null;
                break;
        }
        return true;
    }



}
