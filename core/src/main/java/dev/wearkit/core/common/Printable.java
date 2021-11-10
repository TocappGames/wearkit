package dev.wearkit.core.common;


import android.graphics.Paint;

public interface Printable {

    Paint DEFAULT_PAINT = new Paint();

    void print(String text);

    void print(String text, double xPos, double yPos, Paint paint);

    void print(String text, double xPos, double yPos, Paint paint, Double angle);
}
