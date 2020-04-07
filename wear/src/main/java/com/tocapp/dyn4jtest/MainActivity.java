package com.tocapp.dyn4jtest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.wear.ambient.AmbientModeSupport;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.CapabilityClient;
import com.google.android.gms.wearable.CapabilityInfo;
import com.google.android.gms.wearable.DataClient;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;
import com.tocapp.sdk.activity.WearGameActivity;
import com.tocapp.sdk.display.GameView;
import com.tocapp.sdk.engine.Game;
import com.tocapp.touchround.AirHockey;
import com.tocapp.touchround.Config;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class MainActivity extends WearGameActivity{

    private TextView mTextView;

    static public int level = 1;
    static public int ballColor;
    static public int sticksColor;
    static public int boxColor;
    static public int goalsColor;
    static public int backgroundImage;
    public static boolean displayIsRound = false;
    public static boolean sound = true;
    static public double width = 0;
    static public double height = 0;
    public static Config config = new Config();
    private GameView gameView;
    private double widthCm;
    private double heightCm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // AmbientModeSupport.attach(this);
        if (!config.isHasConfiguration()) {
            config.setMap0();
        }
        config.setWidthCm( widthCm );
        config.setHeightCm( heightCm );
        config.setDisplayIsRound( displayIsRound );

        gameView = findViewById( R.id.game_view );
        gameView.getViewTreeObserver().addOnGlobalLayoutListener( new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                findViewById( R.id.game_view ).getViewTreeObserver().removeGlobalOnLayoutListener( this );
                DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
                int viewHeight = gameView.getHeight();
                int viewWidth = gameView.getWidth();
                double wi = (double) viewWidth / (double) displayMetrics.xdpi;
                double hi = (double) viewHeight / (double) displayMetrics.ydpi;
                widthCm =  wi * 2.54;
                heightCm  = hi * 2.54;
            }
        } );
        ImageButton exitButton = findViewById(R.id.butt);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    protected Game getGame() {
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
