package dev.wearkit.core.display;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;

import dev.wearkit.core.common.Camera;
import dev.wearkit.core.engine.Game;
import dev.wearkit.core.engine.World;
import dev.wearkit.core.exceptions.PaintRequiredException;
import dev.wearkit.core.common.Renderable;

import org.dyn4j.geometry.Vector2;

import java.util.List;
import java.util.Map;

public class GameView extends View {

    private static final String TAG = "GameView";
    private static final String STATUS_READY = "ready";
    private static final String STATUS_INITIALIZED = "initialized";
    private Game game;
    private String status;
    private Vector2 viewSize = new Vector2(0, 0);
    private Matrix matrix;
    private double lastUpdateTime = 0;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.matrix = new Matrix();
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
            this.status = STATUS_INITIALIZED;;
        }

        World world = this.game.getWorld();
        this.game.onPreUpdate();
        double now = this.game.getElapsedTime();
        double elapsedTime = now - this.lastUpdateTime;
        if(!this.game.isPaused()) {
            world.update(elapsedTime);
            this.game.update();
        }
        this.lastUpdateTime = now;

        this.game.onPreRender();

        Camera camera = world.getViewport().getCamera();
        float zoom = (float) camera.getZoom();
        Vector2 center = world.getSize().copy().divide(2);
        Vector2 camPos = camera.getPosition();
        if(camPos == null) camPos = center;
        Vector2 dp = center.difference(camPos);

        this.matrix.reset();
        this.matrix.postScale(
                zoom,
                zoom,
                (float) camPos.x,
                (float) camPos.y
        );
        this.matrix.postRotate(
                (float) (Math.toDegrees(camera.getAngle())),
                (float) camPos.x,
                (float) camPos.y
        );
        this.matrix.postTranslate((float) dp.x, (float) dp.y);
        canvas.setMatrix(this.matrix);

        Map<Integer, List<Renderable>> decoration = world.getDecoration();
        for(Integer zIndex: decoration.keySet()) {
            this.drawList(decoration.get(zIndex), canvas);
        }

        try {
            world.getViewport().render(canvas);
        } catch (PaintRequiredException e) {
            e.printStackTrace();
        }

        this.game.onPostRender();
    }

    private void drawList(@NonNull List<?> renderables, Canvas canvas){
        for(Object r: renderables){
            try {
                ((Renderable) r).render(canvas);
            } catch (PaintRequiredException e) {
                throw new NullPointerException("Paint is required: " + e.getMessage());
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return this.game.onMotionEvent(event);
    }

    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        return this.game.onMotionEvent(event);
    }
}