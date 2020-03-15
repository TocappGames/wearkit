package com.tocapp.sdk.display;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Dimension;

import com.tocapp.sdk.R;
import com.tocapp.sdk.engine.Game;
import com.tocapp.sdk.rendering.GameObject;
import com.tocapp.sdk.rendering.Renderable;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.World;

import java.util.Vector;

public class GameView extends View {

    private static final String TAG = "GameView";
    private static final String STATUS_READY = "ready";
    private static final String STATUS_INITIALIZED = "initialized";
    private final Matrix matrix;
    private Vector<Integer> displaySize;
    private Game game;
    private String status;
    private double startTime;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.status = STATUS_READY;
        this.matrix = new Matrix();
    }

    public void setGame(Game game, Vector<Integer> displaySize){
        this.game = game;
        this.displaySize = displaySize;
    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
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

        for (Renderable r: this.game.getLandscape()) {
            r.render(canvas, this.game.getScale());
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //TODO set scale on touch event
        double scale = this.game.getScale();
        this.game.touchEvent(event, scale);
        return true;
       // return super.onTouchEvent(event);
    }

}