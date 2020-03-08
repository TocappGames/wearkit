package com.tocapp.sdk.rendering;

import android.graphics.Canvas;
import android.graphics.Paint;

public interface Renderable {
    void render(Canvas canvas);
    void render(Canvas canvas, Paint paint);
}
