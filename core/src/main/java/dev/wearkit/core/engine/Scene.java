package dev.wearkit.core.engine;

import dev.wearkit.core.rendering.Renderable;

import java.util.SortedSet;

public interface Scene {
    SortedSet<Renderable> getDecoration();
    void addOrnament(Renderable thing);
}
