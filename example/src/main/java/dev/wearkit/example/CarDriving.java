package dev.wearkit.example;

import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;

import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;

import java.util.ArrayDeque;
import java.util.Queue;

import dev.wearkit.core.data.Loader;
import dev.wearkit.core.engine.AbstractGame;
import dev.wearkit.core.exceptions.LoadException;
import dev.wearkit.core.rendering.Body;
import dev.wearkit.core.rendering.Ornament;
import dev.wearkit.core.rendering.shape.Circle;

public class CarDriving extends AbstractGame {

    private static final String TAG = "FloatingBalls";
    private final Loader<Body> loader;
    private Body car;
    private Vector2 turnForce;
    private Queue<Ornament> wheelPointsLeft = new ArrayDeque<>();
    private Queue<Ornament> wheelPointsRight = new ArrayDeque<>();
    private static Paint wheelMarksPaint = new Paint(Color.RED);
    static {
        wheelMarksPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    public CarDriving(Loader<Body> loader){
        this.loader = loader;
    }

    @Override
    public void init() {
        world.setGravity(World.ZERO_GRAVITY);
        world.getSettings().setMaximumTranslation(10);
        Vector2 wc = world.getSize().copy().divide(2);

        try {

            Body circuit = this.loader.load("circuit.png");
            circuit.setMass(MassType.INFINITE);
            circuit.translate(wc);
            circuit.stamp(null);
            world.addOrnament(circuit);

            this.car = this.loader.load("top-car-50.png");
            for(BodyFixture fixture: this.car.getFixtures()){
                fixture.setDensity(0.002);
            }
            //this.car.getFixtures().forEach(f -> f.setDensity(0.02));
            this.car.setMass(MassType.NORMAL);
            this.car.translate(wc);

            this.car.setLinearDamping(3);
            this.car.setAngularDamping(3);
            world.addBody(this.car);

        } catch (LoadException e) {
            e.printStackTrace();
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
        accelForce.setMagnitude(5000);
        this.car.applyForce(accelForce);//, rearAxis);

        if (this.turnForce != null){
            Vector2 frontAxis = new Vector2( 25, 0);
            this.car.applyForce(this.turnForce, frontAxis);
        }

    }

    @Override
    public void finish() {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "ACTION_DOWN");
                if(event.getX() > this.world.getSize().x / 2){
                    this.turnForce = new Vector2( 200, 0);

                }
                else {
                    this.turnForce = new Vector2(-200, 0);
                }
                break;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "ACTION_UP");
                this.turnForce = null;
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "ACTION_MOVE");
                break;
        }
        return true;
    }
}
