package dev.wearkit.example;

import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.google.android.wearable.input.RotaryEncoderHelper;

import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.Torque;
import org.dyn4j.world.World;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;

import java.util.ArrayDeque;
import java.util.Queue;

import dev.wearkit.core.data.Loader;
import dev.wearkit.core.engine.AbstractGame;
import dev.wearkit.core.exceptions.LoadException;
import dev.wearkit.core.rendering.Body;
import dev.wearkit.core.rendering.BodyCamera;
import dev.wearkit.core.rendering.Ornament;
import dev.wearkit.core.rendering.shape.Circle;

public class CarDriving extends AbstractGame {

    private static final String TAG = "CarDriving";
    private final Loader<Body> bodies;
    private final Loader<Ornament> ornaments;
    private Body car;
    private Torque turnTorque;
    private Queue<Ornament> wheelPointsLeft = new ArrayDeque<>();
    private Queue<Ornament> wheelPointsRight = new ArrayDeque<>();
    private static Paint wheelMarksPaint = new Paint(Color.RED);
    static {
        wheelMarksPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    private long startTimeRotary = 0;
    private static final long ROTARY_DURATION_MILLIS = 250;

    public CarDriving(Loader<Body> bodies, Loader<Ornament> ornaments){
        this.bodies = bodies;
        this.ornaments = ornaments;
    }

    @Override
    public void init() {
        world.setGravity(World.ZERO_GRAVITY);
        world.getSettings().setMaximumTranslation(10);
        Vector2 wc = world.getSize().copy().divide(2);

        try {
/*
            Body circuit = this.loader.load("circuit.png").scale(4);
            circuit.setMass(MassType.INFINITE);
            circuit.translate(wc);
            world.addBody(circuit);
            */

            int circuScale = 4;

            Body inWall = this.bodies.load("circuit-touch-round.jpg").scale(circuScale);
            inWall.stamp(null);
            inWall.setMass(MassType.INFINITE);
            inWall.translate(wc);
            world.addBody(inWall);


            Body outWall = this.bodies.load("circuit-touch-round-out.jpg").scale(circuScale);
            outWall.setMass(MassType.INFINITE);
            outWall.translate(wc);
            world.addBody(outWall);

            this.car = this.bodies.load("top-car-50.png").scale(0.75);
            this.car.stamp(null);
            for(BodyFixture fixture: this.car.getFixtures()){
                fixture.setDensity(0.002);
                fixture.setRestitution(0.2);
            }

            /*
            this.car = new Body();
            Paint carPaint = new Paint(Color.RED);
            carPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            this.car.paint(carPaint);

            Circle circleFront = new Circle(25);
            Circle circleBack = new Circle(25);
            circleBack.translate(0, 50);
            this.car.addFixture(circleFront, 0.002, 0.0, 0.2);
            this.car.addFixture(circleBack, 0.002, 0.0, 0.2);
             */

            //this.car.getFixtures().forEach(f -> f.setDensity(0.02)); // java8
            this.car.setMass(MassType.NORMAL);
            this.car.translate(wc.copy().add(1300, 1000));

            this.car.setLinearDamping(3);
            this.car.setAngularDamping(3);
            world.addBody(this.car);

            BodyCamera bc = new BodyCamera(this.car);
            //bc.setAngleMode(BodyCamera.MODE_BODY_ANGLE);
            //bc.setZoom(4);
            this.world.setCamera(bc);

        } catch (LoadException e) {
            throw new NullPointerException("ERROR: " + e.getMessage());
        }


    }

    @Override
    public void update() {

        if(Math.abs(car.getAngularVelocity()) > Math.PI / 2) {
            Ornament wheelMarksRight = new Ornament();
            Ornament wheelMarksLeft = new Ornament();
            wheelMarksRight.paint(wheelMarksPaint);
            wheelMarksLeft.paint(wheelMarksPaint);
            Circle circle = new Circle(2);
            wheelMarksRight.addFixture(circle);
            wheelMarksLeft.addFixture(circle);

            Vector2 carRearAxlePosition = new Vector2(this.car.getTransform().getRotationAngle());
            carRearAxlePosition.rotate(-Math.PI / 2);
            carRearAxlePosition.multiply(-25);

            Vector2 carRightDirectionVector = carRearAxlePosition.getRightHandOrthogonalVector();
            carRightDirectionVector.setMagnitude(12);
            Vector2 carLeftDirectionVector = carRearAxlePosition.getLeftHandOrthogonalVector();
            carLeftDirectionVector.setMagnitude(12);

            Vector2 rearRightWheelPosition = car.getWorldCenter().copy().add(carRearAxlePosition).add(carRightDirectionVector);
            Vector2 rearLeftWheelPosition = car.getWorldCenter().copy().add(carRearAxlePosition).add(carLeftDirectionVector);

            wheelMarksRight.translate(rearRightWheelPosition);
            wheelMarksLeft.translate(rearLeftWheelPosition);

            world.addOrnament(wheelMarksRight);
            world.addOrnament(wheelMarksLeft);

            wheelPointsRight.add(wheelMarksRight);
            wheelPointsLeft.add(wheelMarksLeft);
        }

        if(wheelPointsRight.size() > 100)
            world.removeOrnament(wheelPointsRight.poll());

        if(wheelPointsLeft.size() > 100)
            world.removeOrnament(wheelPointsLeft.poll());

        Vector2 accelForce = new Vector2(this.car.getTransform().getRotationAngle());
        accelForce.rotate(-Math.PI / 2);
        accelForce.setMagnitude(20000 / 3.0);
        this.car.applyForce(accelForce);//, rearAxis);

        if (this.startTimeRotary > 0 && System.currentTimeMillis() > this.startTimeRotary + ROTARY_DURATION_MILLIS) {
            this.turnTorque = null;
            this.startTimeRotary = 0;
        }
        if (this.turnTorque != null){
            this.car.applyTorque(this.turnTorque);
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
                this.turnTorque = new Torque(-200000 / 4.0 * rotation);
                this.startTimeRotary = System.currentTimeMillis();
                break;

            case MotionEvent.ACTION_DOWN:
                this.startTimeRotary = 0;

                if(event.getX() > this.world.getSize().x / 2){
                    this.turnTorque = new Torque(200000 / 4.0);
                }
                else {
                    this.turnTorque = new Torque(-200000 / 4.0);
                }
                break;
            case MotionEvent.ACTION_UP:
                this.turnTorque = null;
                break;
        }
        return true;
    }



}
