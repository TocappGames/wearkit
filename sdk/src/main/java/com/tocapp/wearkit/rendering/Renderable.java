package com.tocapp.wearkit.rendering;

import android.graphics.Canvas;

import com.tocapp.wearkit.exceptions.PaintRequiredException;

public interface Renderable {
    void render(Canvas canvas, double scale) throws PaintRequiredException;
}
