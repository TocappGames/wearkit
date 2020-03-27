package com.tocapp.sdk.engine;

import com.tocapp.sdk.rendering.Renderable;

import org.dyn4j.geometry.Vector2;

import java.util.SortedSet;
import java.util.TreeSet;

public class World extends org.dyn4j.dynamics.World implements Measurable, Scene, Scalable {

    private Vector2 size;
    private SortedSet<Renderable> decoration;
    private double scale;

    World(double scale) {
        this.scale = scale;
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

    @Override
    public double getScale() {
        return this.scale;
    }
}
