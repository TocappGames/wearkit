package com.tocapp.dyn4jtest;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.widget.LinearLayout;

import com.tocapp.touchround.AirHockey;
import com.tocapp.touchround.Config;

import dev.wearkit.core.activity.MobileGameActivity;
import dev.wearkit.core.engine.Game;

import java.util.Locale;


public class GameActivity extends MobileGameActivity {

    public static boolean displayIsRound = false;
    static public Config config = new Config();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
    }

    static double getDisplayArea(Activity activity) {
        double xCm = 0, yCm = 0;
        double area = 0;
        final double INCH_TO_CM = 2.54;
        try {
            Display display = activity.getWindowManager().getDefaultDisplay();
            DisplayMetrics displayMetrics = new DisplayMetrics();
            display.getMetrics(displayMetrics);
            Point realSize = new Point();
            Display.class.getMethod("getRealSize", Point.class).invoke(display, realSize);
            DisplayMetrics dm = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
            xCm = realSize.x / dm.xdpi * INCH_TO_CM;
            yCm = realSize.y / dm.ydpi * INCH_TO_CM;
            Resources resources = activity.getApplicationContext().getResources();
            int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
            double navBarCm;
            if (resourceId > 0) {
                double navBarPx = resources.getDimensionPixelSize(resourceId);
                navBarCm = navBarPx / dm.ydpi * INCH_TO_CM;
                yCm = yCm -navBarCm;
            }
            area = xCm * yCm;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return area;
    }



    protected Game getGame() {
        config.setContext( getApplicationContext() );
        if (!config.haveMap()) {
            config.setMap0();
        }
        config.setArea(getDisplayArea( this ));
        config.setDisplayIsRound( displayIsRound );
        return new AirHockey(config);
    }

}
