package com.tocapp.gamesdk;

import android.util.Log;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.World;
import org.dyn4j.dynamics.joint.PinJoint;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;


public class Renderer {


    private static final String TAG = "TestDyn4j";

    public void test(){
        World world = new World();

        Body body = new Body();
        body.addFixture(Geometry.createCircle(1.0));
        body.translate(1.0, 0.0);
        body.setMass(MassType.NORMAL);
        world.addBody(body);
        Log.d(TAG, "this is a test");

        PinJoint joint = new PinJoint(body, new Vector2(0, 0), 4, 0.7, 1000);
        world.addJoint(joint);

        for (int i = 0; i < 100; i++) {
            world.step(1);
        }
    }
}
