package com.tocapp.dyn4jtest;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.tocapp.sdk.activity.MobileGameActivity;
import com.tocapp.sdk.engine.Game;
import com.tocapp.touchround.AirHockey;

import java.util.Locale;

public class MainActivity extends MobileGameActivity {

    static public int level = 1;
    static public int ballColor = Color.WHITE;
    static public int sticksColor = Color.BLUE;
    static public int boxColor = Color.WHITE;
    static public int goalsColor = Color.RED;
    static public int backgroundImage = 0;
    public static boolean displayIsRound = false;
    public static boolean sound = true;

    static String getDisplaySize(Activity activity) {
        double x = 0, y = 0;
        int mWidthPixels, mHeightPixels;
        try {
            WindowManager windowManager = activity.getWindowManager();
            Display display = windowManager.getDefaultDisplay();
            DisplayMetrics displayMetrics = new DisplayMetrics();
            display.getMetrics(displayMetrics);
            Point realSize = new Point();
            Display.class.getMethod("getRealSize", Point.class).invoke(display, realSize);
            mWidthPixels = realSize.x;
            System.out.println("Pixels X: " + mWidthPixels);
            mHeightPixels = realSize.y;
            System.out.println("Pixels Y: " + mHeightPixels);
            DisplayMetrics dm = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
            System.out.println("X Dpi: " + dm.xdpi);
            System.out.println("Y Dpi: " + dm.ydpi);
            x = Math.pow(mWidthPixels / dm.xdpi, 2);
            y = Math.pow(mHeightPixels / dm.ydpi, 2);
            System.out.println("X Cm: " + x);
            System.out.println("Y CM: " + y);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return String.format(Locale.US, "%.2f", Math.sqrt(x + y));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("Inches: " + getDisplaySize(this));
    }


    protected Game getGame() {

        return new AirHockey(level, sound, displayIsRound, backgroundImage, ballColor, sticksColor, boxColor, goalsColor);
    }

}
