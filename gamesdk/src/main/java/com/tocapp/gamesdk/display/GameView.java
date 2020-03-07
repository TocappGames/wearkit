package com.tocapp.gamesdk.display;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.tocapp.gamesdk.engine.Game;
import com.tocapp.gamesdk.shape.Drawable;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.World;

public class GameView extends View {

    private Game game;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setGame(Game game){
        this.game = game;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        World world = this.game.getWorld();
        world.update(System.currentTimeMillis() / 1000.0);
        this.game.onUpdate();
        for(Body body: world.getBodies()){
            ((Drawable) body).draw(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}