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
    private Map<Integer, List<Renderable>> decoration;

    private Camera camera;

    public DefaultViewport() {
        this.camera = new DefaultCamera();
        this.decoration = new TreeMap<>();
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
        canvas.save();
        canvas.translate(
                (float) this.camera.getPosition().x,
                (float) this.camera.getPosition().y
        );
        canvas.rotate((float) -Math.toDegrees(camera.getAngle()));

        canvas.save();
        float inverseZoom = 1.0f / ((float)this.camera.getZoom());
        canvas.scale(inverseZoom, inverseZoom);
        for(Integer zIndex: this.decoration.keySet()) {
            this.drawList(decoration.get(zIndex), canvas);
        }
        canvas.restore();

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
