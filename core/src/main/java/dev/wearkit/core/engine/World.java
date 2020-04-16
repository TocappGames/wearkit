package dev.wearkit.core.engine;

import dev.wearkit.core.common.Camera;
import dev.wearkit.core.common.Measurable;
import dev.wearkit.core.common.Renderable;
import dev.wearkit.core.common.Viewport;
import dev.wearkit.core.rendering.DefaultCamera;

import org.dyn4j.geometry.Vector2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class World extends org.dyn4j.dynamics.World implements Measurable, Scene, Viewport {

    private Vector2 size;
    private Map<Integer, List<Renderable>> decoration;
    private Camera camera;

    World() {
        this.decoration = new TreeMap<>();
        this.camera = new DefaultCamera();
    }

    public void setSize(Vector2 size) {
        this.size = size;
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
        this.addOrnament(renderable, -1);
    }

    @Override
    public void addOrnament(Renderable renderable, int zIndex) {
        if(!this.decoration.containsKey(zIndex)){
            this.decoration.put(zIndex, new ArrayList<Renderable>());
        }
        this.decoration.get(zIndex).add(renderable);
    }

    public void removeOrnament(Renderable ornament, int zIndex) {
        this.decoration.get(zIndex).remove(ornament);
    }

    public void removeOrnament(Renderable ornament) {
        this.removeOrnament(ornament, -1);
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    @Override
    public Camera getCamera() {
        return this.camera;
    }
}
