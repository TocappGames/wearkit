package dev.wearkit.core.rendering;

import static dev.wearkit.core.engine.World.DEFAULT_DECORATION_ZINDEX;

import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import dev.wearkit.core.common.Camera;
import dev.wearkit.core.common.Renderable;
import dev.wearkit.core.common.Viewport;
import dev.wearkit.core.exceptions.PaintRequiredException;

public class DefaultViewport implements Viewport {

    private String text;
    private double textX;
    private double textY;
    private Paint textPaint;
    private Double textAngle;

    private Map<Integer, List<Renderable>> decoration;

    private Camera camera;

    public DefaultViewport() {
        this.camera = new DefaultCamera();
        this.decoration = new TreeMap<>();
    }

    @Override
    public void print(String text) {
        this.print(text, 0, 0, DEFAULT_PAINT);
    }

    @Override
    public void print(String text, double xPos, double yPos, Paint paint) {
        this.print(text, xPos, yPos, paint, null);
    }

    @Override
    public void print(String text, double xPos, double yPos, Paint paint, Double textAngle) {
        this.text = text;
        this.textX = xPos;
        this.textY = yPos;
        this.textPaint = paint;
        this.textAngle = textAngle;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }


    @Override
    public Camera getCamera() {
        return this.camera;
    }


    @Override
    public Map<Integer, List<Renderable>> getDecoration() {
        return this.decoration;
    }

    @Override
    public void addOrnament(Renderable renderable) {
        this.addOrnament(renderable, DEFAULT_DECORATION_ZINDEX);
    }

    @Override
    public void addOrnament(Renderable renderable, int zIndex) {
        if(!this.decoration.containsKey(zIndex)){
            this.decoration.put(zIndex, new ArrayList<>());
        }
        this.decoration.get(zIndex).add(renderable);
    }

    @Override
    public void removeOrnament(Renderable ornament, int zIndex) {
        this.decoration.get(zIndex).remove(ornament);
    }

    @Override
    public void clear() {
        this.decoration.clear();
    }

    @Override
    public void clear(int zIndex) {
        this.decoration.get(zIndex).clear();
    }

    @Override
    public void removeOrnament(Renderable ornament) {
        this.removeOrnament(ornament, DEFAULT_DECORATION_ZINDEX);
    }

    @Override
    public void render(Canvas canvas) throws PaintRequiredException {

        /* TEXT rendering ornaments*/
        canvas.save();
        canvas.translate(
                (float) this.camera.getPosition().x,
                (float) this.camera.getPosition().y
        );
        canvas.rotate((float) -(camera.getAngle() / Math.PI * 180));

        for(Integer zIndex: this.decoration.keySet()) {
            this.drawList(decoration.get(zIndex), canvas);
        }

        /* TEXT rendering print() function*/
        double angle = 0;
        if(this.textAngle != null) {
            angle = this.textAngle;
        }
        if(this.text != null){
            //angle -= camera.getAngle();
            canvas.rotate((float) (angle / Math.PI * 180));

            float x = (float) this.textX; //(this.camera.getPosition().x + this.textX);
            float y = (float) this.textY; //(this.camera.getPosition().y + this.textY);
            for (String line: this.text.split("\n")) {
                canvas.drawText(line, x, y, this.textPaint);
                y += this.textPaint.descent() - this.textPaint.ascent();
            }
        }
        canvas.restore();
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

}
