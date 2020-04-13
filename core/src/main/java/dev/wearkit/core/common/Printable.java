package dev.wearkit.core.common;


import android.graphics.Paint;

public interface Printable {

    Paint DEFAULT_PAINT = new Paint();

    default void print(String text){
        print(text, 0, 0, DEFAULT_PAINT);
    }

    default void print(String text, double xPos, double yPos, Paint paint){
        print(text, xPos, yPos, paint, null);
    }

    void print(String text, double xPos, double yPos, Paint paint, Double angle);
}
