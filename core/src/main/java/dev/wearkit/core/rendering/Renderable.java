package dev.wearkit.core.rendering;

import android.graphics.Canvas;

import dev.wearkit.core.exceptions.PaintRequiredException;

public interface Renderable {
    void render(Canvas canvas, double scale) throws PaintRequiredException;
}
