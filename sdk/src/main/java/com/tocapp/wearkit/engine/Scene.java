package com.tocapp.wearkit.engine;

import com.tocapp.wearkit.rendering.Renderable;

import java.util.SortedSet;

public interface Scene {
    SortedSet<Renderable> getDecoration();
}
