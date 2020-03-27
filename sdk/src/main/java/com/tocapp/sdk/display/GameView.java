package com.tocapp.sdk.display;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import com.tocapp.sdk.engine.Game;
import com.tocapp.sdk.rendering.Renderable;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.World;

public class GameView extends View {

    private static final String TAG = "GameView";
    private static final String STATUS_READY = "ready";
    private static final String STATUS_INITIALIZED = "initialized";
    private Game game;
    private String status;
    private double startTime;
    DisplayMetrics display = getResources().getDisplayMetrics();

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.status = STATUS_READY;
        double cmX = Math.pow(display.widthPixels/display.xdpi,2);
        double cmY = Math.pow(display.heightPixels/display.ydpi,2);
        double screenInches = Math.sqrt(cmX+ cmY);
        double area = cmX * cmY;
        System.out.println(display.widthPixels);
        System.out.println(display.heightPixels);
        System.out.println(display.xdpi);
        System.out.println(display.ydpi);
        System.out.println(cmX);
        System.out.println(cmY);
        System.out.println(screenInches);
        System.out.println(area);


    }

    public void setGame(Game game){
        this.game = game;
    }

    public void setContext(Context context) {
        this.game.setContext(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.game.setDimensions(display.widthPixels,display.heightPixels, display.density);
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

        for (Renderable r: this.game.getBackgroundLandscape()) {
            r.render(canvas, this.game.getScale());
        }

        for(Body body: world.getBodies()){
            ((Renderable) body).render(canvas, game.getScale());
        }

        for (Renderable r: this.game.getLandscape()) {
            r.render(canvas, this.game.getScale());
        }


        this.game.postRender();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        double scale = this.game.getScale();
        this.game.touchEvent(event, scale);
        return true;
    }
}