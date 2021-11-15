package dev.wearkit.core.engine;

import android.graphics.Paint;

import dev.wearkit.core.common.Camera;
import dev.wearkit.core.common.Measurable;
import dev.wearkit.core.common.Renderable;
import dev.wearkit.core.common.Viewport;
import dev.wearkit.core.rendering.DefaultCamera;
import dev.wearkit.core.rendering.DefaultViewport;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Vector2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class World extends org.dyn4j.world.World<Body> implements Measurable, Scene {

    private Vector2 size;
    private Map<Integer, List<Renderable>> decoration;
    public static final int DEFAULT_DECORATION_ZINDEX = -1;
    public static final int DEFAULT_BODY_ZINDEX = 0;
    private final Viewport viewport;

    private boolean paused = false;

    World() {
        this.decoration = new TreeMap<>();
        this.viewport = new DefaultViewport();
    }

    public void setSize(Vector2 size) {
        this.size = size;
    }

    public void pause() {
        this.paused = true;
    }

    public void resume() {
        this.paused = false;
    }

    @Override
    public boolean update(double elapsedTime, double stepElapsedTime, int maximumSteps) {
        if (!this.paused) return super.update(elapsedTime, stepElapsedTime, maximumSteps);
        return false;
    }

    @Override
    public Vector2 getSize() {
        return this.size;
    }

    @Override
    public Map<Integer, List<Renderable>> getDecoration() {
        return decoration;
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
    public void addBody(Body body) {
        super.addBody(body);
        this.addOrnament((Renderable) body, DEFAULT_BODY_ZINDEX);
    }

    public void addBody(Body body, int zIndex) {
        super.addBody(body);
        this.addOrnament((Renderable) body, zIndex);
    }

    @Override
    public boolean removeBody(Body body) {
        this.removeOrnament((Renderable) body, DEFAULT_BODY_ZINDEX);
        return super.removeBody(body);
    }

    public boolean removeBody(Body body, int zIndex) {
        this.removeOrnament((Renderable) body, zIndex);
        return super.removeBody(body);
    }

    @Override
    public void removeOrnament(Renderable ornament) {
        this.removeOrnament(ornament, DEFAULT_DECORATION_ZINDEX);
    }

    public Viewport getViewport() {
        return viewport;
    }
}
