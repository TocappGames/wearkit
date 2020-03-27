package com.tocapp.wearkit.display;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.tocapp.wearkit.engine.Game;
import com.tocapp.wearkit.engine.World;
import com.tocapp.wearkit.exceptions.PaintRequiredException;
import com.tocapp.wearkit.rendering.Indexable;
import com.tocapp.wearkit.rendering.Renderable;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Vector2;

import java.util.SortedSet;

public class GameView extends View {

    private static final String TAG = "GameView";
    private static final String STATUS_READY = "ready";
    private static final String STATUS_INITIALIZED = "initialized";
    private Game game;
    private String status;
    private double startTime;
    private Vector2 viewSize = new Vector2(0, 0);

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.status = STATUS_READY;
    }

    public void setGame(Game game){
        this.game = game;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        this.viewSize.x = widthSize;
        this.viewSize.y = heightSize;
        this.game.getWorld().setSize(this.viewSize);
        this.game.onWorldMeasureChange(widthSize, heightSize);
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
        SortedSet<Renderable> decoration = world.getDecoration();
        boolean isWorldDrawn = false;
        for(Renderable renderable: decoration){
            if (((Indexable) renderable).getIndex() > 0){
                isWorldDrawn = true;
                this.drawWorld(world, canvas);
            }
            try {
                renderable.render(canvas, world.getScale());
            } catch (PaintRequiredException e) {
                e.printStackTrace();
            }
        }
        if(!isWorldDrawn){
            this.drawWorld(world, canvas);
        }
        world.update(time);
        this.game.update();
    }

    private void drawWorld(World world, Canvas canvas){
        for(Body body: world.getBodies()){
            try {
                ((Renderable) body).render(canvas, world.getScale());
            } catch (PaintRequiredException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.game.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}