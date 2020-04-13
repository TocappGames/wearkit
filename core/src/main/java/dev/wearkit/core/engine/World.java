package dev.wearkit.core.engine;

import android.graphics.Matrix;

import dev.wearkit.core.common.Camera;
import dev.wearkit.core.common.Measurable;
import dev.wearkit.core.common.Renderable;
import dev.wearkit.core.common.Viewport;
import dev.wearkit.core.rendering.DefaultCamera;

import org.dyn4j.geometry.Vector2;

import java.util.SortedSet;
import java.util.TreeSet;

public class World extends org.dyn4j.dynamics.World implements Measurable, Scene, Viewport {

    private Vector2 size;
    private SortedSet<Renderable> decoration;
    private Camera camera;

    World() {
        this.decoration = new TreeSet<>();
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
    public SortedSet<Renderable> getDecoration() {
        return decoration;
    }

    @Override
    public void addOrnament(Renderable ornament) {
        this.decoration.add(ornament);
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    @Override
    public Camera getCamera() {
        return this.camera;
    }
}
