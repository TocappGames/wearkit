package com.tocapp.sdk.rendering;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public interface Renderable {
    void render(Canvas canvas, double scale);
    void render(Canvas canvas, Paint paint, double scale);
}
