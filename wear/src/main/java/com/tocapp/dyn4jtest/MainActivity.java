package com.tocapp.dyn4jtest;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;

import com.tocapp.sdk.activity.WearGameActivity;
import com.tocapp.sdk.display.GameView;
import com.tocapp.sdk.engine.Game;
import com.tocapp.touchround.AirHockey;
import com.tocapp.touchround.Config;

public class MainActivity extends WearGameActivity{


    public static boolean displayIsRound = false;
    public static Config config = new Config();
    private GameView gameView;
    private double widthCm;
    private double heightCm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // AmbientModeSupport.attach(this);
        ImageButton exitButton = findViewById(R.id.butt);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
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

    @Override
    protected Game getGame() {
        if (!config.haveMap()) {
            config.setMap0();
        }
        config.setArea(getDisplayArea(this));
        config.setDisplayIsRound( displayIsRound );

        return new AirHockey(config);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

}
