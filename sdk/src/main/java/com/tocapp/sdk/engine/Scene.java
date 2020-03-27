package com.tocapp.sdk.engine;

import com.tocapp.sdk.rendering.Renderable;

import java.util.SortedSet;

public interface Scene {
    SortedSet<Renderable> getDecoration();
}
