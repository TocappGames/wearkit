package dev.wearkit.core.common;

import android.graphics.Canvas;

import dev.wearkit.core.exceptions.PaintRequiredException;

public interface Renderable {
    void render(Canvas canvas) throws PaintRequiredException;
}
