package com.tocapp.dyn4jtest;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.ViewTreeObserver;
import android.view.WindowManager;

import com.tocapp.sdk.activity.MobileGameActivity;
import com.tocapp.sdk.display.GameView;
import com.tocapp.sdk.engine.Game;
import com.tocapp.touchround.AirHockey;
import com.tocapp.touchround.Config;

import java.util.Locale;


public class MainActivity extends MobileGameActivity {

    public static boolean displayIsRound = false;
    private GameView gameView;
    static public Config config = new Config();
    private double widthCm;
    private double heightCm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

    }

    static double getDisplayArea(Activity activity) {
        double xCm = 0, yCm = 0;
        try {
            Display display = activity.getWindowManager().getDefaultDisplay();
            DisplayMetrics displayMetrics = new DisplayMetrics();
            display.getMetrics(displayMetrics);
            Point realSize = new Point();
            Display.class.getMethod("getRealSize", Point.class).invoke(display, realSize);
            DisplayMetrics dm = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
            xCm = realSize.x / dm.xdpi * 2.54;
            yCm = realSize.y / dm.ydpi * 2.54;

            Resources resources = activity.getApplicationContext().getResources();
            int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
            double navBarCm = 0;
            if (resourceId > 0) {
                double navBarPx = resources.getDimensionPixelSize(resourceId);
                navBarCm = navBarPx / dm.ydpi * 2.54;
            }
            yCm = yCm -navBarCm;
            System.out.println("X Cm: " + xCm);
            System.out.println("Y CM: " + yCm);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return xCm * yCm;
    }


    protected Game getGame() {
        if (!config.haveMap()) {
            config.setMap0();
        }
        config.setArea(getDisplayArea( this ));
        config.setDisplayIsRound( displayIsRound );
        return new AirHockey(config);
    }

}
