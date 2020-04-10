package dev.wearkit.core.common;

import android.graphics.Matrix;

public interface Camera {
    void setZoom(double zoom);
    Matrix getMatrix();
}
