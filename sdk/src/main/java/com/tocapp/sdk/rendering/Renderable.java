package com.tocapp.sdk.rendering;

import android.graphics.Canvas;

import com.tocapp.sdk.exceptions.PaintRequiredException;

public interface Renderable {
    void render(Canvas canvas) throws PaintRequiredException;
}
