package dev.wearkit.core.common;

import android.graphics.Matrix;

public interface Camera {
    double ZOOM_NONE = 1.0;

    void setZoom(double zoom);
    Matrix getMatrix();
}
