package com.tocapp.touchround;

import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.tocapp.sdk.rendering.GameObject;
import com.tocapp.sdk.engine.AbstractGame;
import com.tocapp.sdk.rendering.shape.Circle;
import com.tocapp.sdk.rendering.shape.Rectangle;

import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

public class TouchRound extends AbstractGame {

    private static final String TAG = "TouchRound";
    private static Boolean touch = false;
    private GameObject stick;
    private long start;


    @Override
    public void init() {
        this.world.setGravity(World.ZERO_GRAVITY);
        Paint paint = new Paint();
        paint.setColor(Color.RED);

        Paint paint2 = new Paint();
        paint2.setColor(Color.BLUE);

        GameObject box = new GameObject(paint);
        Rectangle floor = new Rectangle(720, 20);
        floor.translate(389, 1280);
        Rectangle ceiling = new Rectangle(700, 20);
        ceiling.translate(360, 20);

        Rectangle left = new Rectangle(20, 1380);
        left.translate(20, 720);
        Rectangle right = new Rectangle(20, 1380);
        right.translate(700, 720);

        box.addFixture(floor);
        box.addFixture(ceiling);
        box.addFixture(left);
        box.addFixture(right);
        box.setMass(MassType.INFINITE);
        this.world.addBody(box);

        for(int i = 0; i < 25; i++){
            addRandomBall();
        }

        this.start = System.currentTimeMillis();
    }

    private int getRandomByte(){
        double n = Math.random() * 255;
        return (int) Math.round(n);
    }

    private void addRandomBall(){

        Paint paint = new Paint();
        paint.setColor(Color.rgb(getRandomByte(),getRandomByte(),getRandomByte()));

        GameObject ball = new GameObject(paint);
        ball.addFixture(new Circle(10), 1.0, 0.0, 5.0);

        ball.translate(40 + Math.random() * 600 , 100 + Math.random() * 800);
        ball.setMass(MassType.NORMAL);
        ball.setLinearVelocity(new Vector2((Math.random() - 0.5) * 1e10, (Math.random() - 0.5) * 1e10));

        this.world.addBody(ball);
    }

    private GameObject addMasterStick(float x, float y){

        Paint paint = new Paint();
        paint.setColor(Color.RED);

        GameObject ball = new GameObject(paint);
        ball.addFixture(new Circle(30), 0.4, 0.0, 5.0);

        ball.translate(x, y);
        ball.setMass(MassType.NORMAL);
       // ball.setLinearVelocity(new Vector2((Math.random() - 0.5) * 200, (Math.random() - 0.5) * 200));
        this.world.addBody(ball);
        return ball;
    }

    private float previousX;
    private float previousY;


    @Override
    public void update() {
        if(System.currentTimeMillis() -this.start > 2000){
            Log.d("", "");
        }
    }


    @Override
    public void finish() {

    }
    @Override
    public void touchEvent(MotionEvent event) {

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                if (!touch) {
                    stick = addMasterStick(event.getX(), event.getY());
                    touch = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                //tick.removeAllFixtures();
                //this.world.removeBody(stick);
                //stick.translate(event.getRawX(), event.getRawY());
                //stick = addMasterStick(event.getRawX(), event.getRawY());

                stick.setLinearVelocity(new Vector2(stick.getMass().getMass()*1e9*(event.getX()-stick.getWorldCenter().x),stick.getMass().getMass()*1e9*(event.getY()-stick.getWorldCenter().y)));
                //stick.applyForce(new Vector2(stick.getMass().getMass()*1e9*(event.getX()-stick.getWorldCenter().x),stick.getMass().getMass()*1e9*(event.getY()-stick.getWorldCenter().y)));
                Log.d("", "");
                break;
        }
    }
}



