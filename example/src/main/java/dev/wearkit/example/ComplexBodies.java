package dev.wearkit.example;

import android.graphics.Color;
import android.graphics.Paint;

import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.Convex;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Triangle;
import org.dyn4j.geometry.Vector2;
import org.dyn4j.geometry.decompose.Bayazit;
import org.dyn4j.geometry.decompose.Decomposer;
import org.dyn4j.geometry.decompose.EarClipping;
import org.dyn4j.geometry.decompose.SweepLine;
import org.dyn4j.geometry.decompose.Triangulator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import dev.wearkit.core.engine.AbstractGame;
import dev.wearkit.core.rendering.Body;
import dev.wearkit.core.rendering.Ornament;
import dev.wearkit.core.rendering.shape.Circle;
import dev.wearkit.core.rendering.shape.Polygon;
import dev.wearkit.core.rendering.shape.Rectangle;

public class ComplexBodies extends AbstractGame {

    private static final String TAG = "FloatingBalls";
    private JSONArray circuit;

    public ComplexBodies(JSONArray circuit){
        this.circuit = circuit;
    }


    @Override
    public void init() {
        world.getSettings().setMaximumTranslation(10);

        double borderWeight = world.getSize().x / 50;

        Paint bgCircleColor = new Paint();
        bgCircleColor.setColor(Color.GRAY);
        Ornament bg = new Ornament(bgCircleColor, -2);
        double radius = Math.min(world.getSize().x, world.getSize().y) / 2 - borderWeight;
        Circle bgCentre = new Circle(radius);
        bgCentre.translate(world.getSize().x / 2, world.getSize().y / 2);
        bg.addFixture(bgCentre);
        this.world.getDecoration().add(bg);

        Paint bgCircleColor2 = new Paint();
        bgCircleColor2.setColor(Color.RED);
        Ornament bg2 = new Ornament(bgCircleColor2, -3);
        Circle bgCentre2 = new Circle(Math.min(world.getSize().x, world.getSize().y) / 4);
        bgCentre2.translate(world.getSize().x / 2, world.getSize().y / 2 - radius);
        bg2.addFixture(bgCentre2);

        this.world.getDecoration().add(bg2);


        this.world.setGravity(World.ZERO_GRAVITY);
        Paint paint = new Paint();
        paint.setColor(Color.RED);


        Body frame = new Body(paint);
        Rectangle floor = new Rectangle(world.getSize().x, borderWeight);
        floor.translate(world.getSize().x / 2, world.getSize().y - borderWeight / 2);

        Rectangle ceiling = new Rectangle(world.getSize().x, borderWeight);
        ceiling.translate(world.getSize().x / 2, borderWeight / 2);

        Rectangle left = new Rectangle(borderWeight, world.getSize().y);
        left.translate(borderWeight / 2, world.getSize().y / 2);

        Rectangle right = new Rectangle(borderWeight, world.getSize().y);
        right.translate(world.getSize().x - borderWeight / 2, world.getSize().y / 2);

        frame.addFixture(floor);
        frame.addFixture(ceiling);
        frame.addFixture(left);
        frame.addFixture(right);
        frame.setMass(MassType.INFINITE);

        this.world.addBody(frame);

        Paint p = new Paint();
        p.setColor(Color.BLUE);
        p.setStyle(Paint.Style.FILL_AND_STROKE);
        Body u = new Body(p);

        try {
            JSONArray firstBody = circuit.getJSONArray(0);

            for(int i=0; i < firstBody.length(); i++){
                JSONArray jsonShape = firstBody.getJSONArray(i);
                Vector2[] vertexes = new Vector2[jsonShape.length()];
                for(int j=0; j < jsonShape.length(); j++){
                    JSONArray jsonVertex = jsonShape.getJSONArray(j);
                    Vector2 vertex = new Vector2(
                            jsonVertex.getDouble(0),
                            jsonVertex.getDouble(1)
                    );
                    vertexes[j] = vertex;
                }
                Geometry.reverseWinding(vertexes);
                Polygon polygon = new Polygon(vertexes);
                u.addFixture(polygon);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        u.setMass(MassType.INFINITE);
        u.translate(borderWeight, borderWeight + 200);

        world.addBody(u);


        for(int i = 0; i < 20; i++){
            addRandomBall();
        }

    }

    private int getRandomByte(){
        double n = Math.random() * 255;
        return (int) Math.round(n);
    }

    private void addRandomBall(){

        Paint paint = new Paint();
        paint.setColor(Color.rgb(getRandomByte(),getRandomByte(),getRandomByte()));

        Body ball = new Body(paint);
        ball.addFixture(new Circle(10), 1.0, 0.0, 2.0);
        ball.translate(world.getSize().x / 2, world.getSize().y / 2);
        ball.setMass(MassType.NORMAL);
        ball.setLinearVelocity(new Vector2((Math.random() - 0.5) * 100, (Math.random() - 0.5) * 100));

        this.world.addBody(ball);
    }


    @Override
    public void update() {
    }

    @Override
    public void finish() {

    }

}
