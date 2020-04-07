package dev.wearkit.core.engine;

import dev.wearkit.core.rendering.Renderable;

import org.dyn4j.geometry.Vector2;

import java.util.SortedSet;
import java.util.TreeSet;

public class World extends org.dyn4j.dynamics.World implements Measurable, Scene {

    private Vector2 size;
    private SortedSet<Renderable> decoration;

    World() {
        this.decoration = new TreeSet<>();
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
}
