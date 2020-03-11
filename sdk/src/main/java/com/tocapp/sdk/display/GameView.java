package com.tocapp.sdk.display;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.tocapp.sdk.engine.Game;
import com.tocapp.sdk.rendering.GameObject;
import com.tocapp.sdk.rendering.Renderable;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.World;

public class GameView extends View {

    private static final String TAG = "GameView";
    private static final String STATUS_READY = "ready";
    private static final String STATUS_INITIALIZED = "initialized";
    private final Matrix matrix;
    private Game game;
    private String status;
    private double startTime;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.status = STATUS_READY;
        this.matrix = new Matrix();
    }

    public void setGame(Game game){
        this.game = game;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(this.status.equals(STATUS_READY)){
            this.game.init();
            this.status = STATUS_INITIALIZED;
            this.startTime = System.currentTimeMillis() / 1000.0;
        }
        double time = System.currentTimeMillis() / 1000.0 - this.startTime;
        World world = this.game.getWorld();
        world.update(time);
        this.game.update();
        for(Body body: world.getBodies()){
            ((Renderable) body).render(canvas, game.getScale());
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.game.touchEvent(event);
        return true;
       // return super.onTouchEvent(event);
    }

}