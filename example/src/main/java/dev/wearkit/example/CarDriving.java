package dev.wearkit.example;

import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;

import org.dyn4j.collision.manifold.Manifold;
import org.dyn4j.collision.narrowphase.Penetration;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.CollisionListener;
import org.dyn4j.dynamics.World;
import org.dyn4j.dynamics.contact.ContactAdapter;
import org.dyn4j.dynamics.contact.ContactConstraint;
import org.dyn4j.dynamics.contact.ContactListener;
import org.dyn4j.dynamics.contact.ContactPoint;
import org.dyn4j.dynamics.contact.PersistedContactPoint;
import org.dyn4j.dynamics.contact.SolvedContactPoint;
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

            //Body circuit = (Body) this.loader.load("circuit.png").scale(4);
            Body inWall = (Body) this.loader.load("circuit1_in.png");//.scale(4);
            inWall.stamp(null);
            inWall = (Body) inWall.scale(4);
            inWall.setMass(MassType.INFINITE);
            inWall.translate(wc);
            world.addBody(  inWall);


            Body outWall = (Body) this.loader.load("circuit1_out.png");//.scale(4);
            outWall.stamp(null);
            outWall = (Body) outWall.scale(4);
            outWall.setMass(MassType.INFINITE);
            outWall.translate(wc);
            world.addBody(  outWall);

            /*
            this.car = this.loader.load("top-car-50.png");
            for(BodyFixture fixture: this.car.getFixtures()){
                fixture.setDensity(0.002);
                fixture.setRestitution(0.2);
            }
            */
            this.car = new Body();
            Paint carPaint = new Paint(Color.RED);
            carPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            this.car.paint(carPaint);
            this.car.addFixture(new Circle(25), 0.002, 0.0, 0.2);
            //this.car.getFixtures().forEach(f -> f.setDensity(0.02));
            this.car.setMass(MassType.NORMAL);
            this.car.translate(wc.copy().add(400, 500));

            this.car.setLinearDamping(3);
            this.car.setAngularDamping(3);
            world.addBody(this.car);

            BodyCamera bc = new BodyCamera(this.car);
            //bc.setAngleMode(BodyCamera.MODE_BODY_ANGLE);
            //bc.setZoom(1.1);
            this.world.setCamera(bc);

        } catch (LoadException e) {
            throw new NullPointerException("ERROR: " + e.getMessage());
        }

        world.addListener(new ContactListener() {
            @Override
            public void sensed(ContactPoint point) {

            }

            @Override
            public boolean begin(ContactPoint point) {
                return true;
            }

            @Override
            public void end(ContactPoint point) {

            }

            @Override
            public boolean persist(PersistedContactPoint point) {
                return true;
            }

            @Override
            public boolean preSolve(ContactPoint point) {
                return true;
            }

            @Override
            public void postSolve(SolvedContactPoint point) {
                Log.d(TAG, "POSTSOLVE");
            }
        });


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
        accelForce.setMagnitude(20000);
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
                if(event.getX() > this.world.getSize().x / 2){
                    this.turnForce = new Vector2( 200, 0);
                }
                else {
                    this.turnForce = new Vector2(-200, 0);
                }
                break;
            case MotionEvent.ACTION_UP:
                this.turnForce = null;
                break;
        }
        return true;
    }
}
