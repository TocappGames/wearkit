package dev.wearkit.example;

import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;

import org.dyn4j.world.World;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;

import java.util.Map;

import dev.wearkit.core.data.Loader;
import dev.wearkit.core.engine.AbstractGame;
import dev.wearkit.core.exceptions.LoadException;
import dev.wearkit.core.rendering.Body;
import dev.wearkit.core.rendering.BodyCamera;
import dev.wearkit.core.rendering.Ornament;
import dev.wearkit.core.rendering.shape.Circle;
import dev.wearkit.core.rendering.shape.Rectangle;

public class ComplexBodies extends AbstractGame {

    private static final String TAG = "FloatingBalls";
    private Loader<Map<String, Body>> loader;

    public ComplexBodies(Loader<Map<String, Body>> loader){
        this.loader = loader;
    }

    @Override
    public void init() {
        super.init();
        world.getSettings().setMaximumTranslation(10);

        double borderWeight = world.getSize().x / 50;

        Paint bgCircleColor = new Paint();
        bgCircleColor.setColor(Color.BLACK);
        Ornament bg = new Ornament();
        bg.paint(bgCircleColor);
        double radius = Math.min(world.getSize().x, world.getSize().y) / 2 - borderWeight;
        Circle bgCentre = new Circle(radius);
        bgCentre.translate(world.getSize().x / 2, world.getSize().y / 2);
        bg.addFixture(bgCentre);

        this.world.addOrnament(bg, -2);

        Paint bgCircleColor2 = new Paint();
        bgCircleColor2.setColor(Color.BLUE);

        Ornament bg2 = new Ornament();
        bg2.paint(bgCircleColor2);

        TextPaint bg2tp = new TextPaint();
        bg2tp.setTextAlign(Paint.Align.CENTER);
        bg2tp.setColor(Color.RED);
        bg2tp.setTextSize(50);
        bg2.print("BG_TEXT", 0, 0, bg2tp);

        Circle bgCentre2 = new Circle(Math.min(world.getSize().x, world.getSize().y) / 4);
        bg2.translate(world.getSize().x / 2, world.getSize().y / 2 - radius);
        bg2.addFixture(bgCentre2);

        this.world.addOrnament(bg2, -3);


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

        try {
            Map<String, Body> bodies = loader.load("bodies.json");
            Body circuit = bodies.get("circuit");
            circuit.rotate(Math.PI / 4);
            circuit.translate(world.getSize().x / 2, world.getSize().y / 2);
            circuit.setMass(MassType.INFINITE);

            world.addBody(circuit);

            Body soccerBall = bodies.get("soccer");
            TextPaint tp = new TextPaint();
            tp.setColor(Color.RED);
            tp.setTextSize(20);
            soccerBall.print("BALLTEXT", 0, 0, tp, 0.0);
            soccerBall.translate(world.getSize().x / 2, world.getSize().y / 2);
            soccerBall.setMass(MassType.NORMAL);
            world.addBody(soccerBall);
            BodyCamera soccerBallCam = new BodyCamera(soccerBall).zoom(2);
            this.world.getViewport().setCamera(soccerBallCam);

        } catch (LoadException e) {
            e.printStackTrace();
        }

        for(int i = 0; i < 10; i++){
            addRandomBall(10);
        }

    }

    private int getRandomByte(){
        double n = Math.random() * 255;
        return (int) Math.round(n);
    }

    private Body addRandomBall(double radius){

        Paint paint = new Paint();
        paint.setColor(Color.rgb(getRandomByte(),getRandomByte(),getRandomByte()));

        Body ball = new Body(paint);
        ball.addFixture(new Circle(radius), 1.0, 0.0, 2.0);
        ball.translate(world.getSize().x / 2, world.getSize().y / 2);
        ball.setMass(MassType.NORMAL);
        ball.setLinearVelocity(new Vector2((Math.random() - 0.5) * 100, (Math.random() - 0.5) * 100));

        this.world.addBody(ball);
        return ball;
    }


    @Override
    public void update() {

    }

    @Override
    public void finish() {

    }

}
