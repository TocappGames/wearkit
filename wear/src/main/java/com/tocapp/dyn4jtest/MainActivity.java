package com.tocapp.dyn4jtest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.tocapp.sdk.engine.Game;
import com.tocapp.touchround.AirHockey;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class MainActivity extends WearGameActivity implements AmbientModeSupport.AmbientCallbackProvider,
        DataClient.OnDataChangedListener,
        MessageClient.OnMessageReceivedListener,
        CapabilityClient.OnCapabilityChangedListener {

    private TextView mTextView;

    static public int level = 1;
    static public int ballColor = Color.WHITE;
    static public int sticksColor = Color.BLUE;
    static public int boxColor = Color.WHITE;
    static public int goalsColor = Color.RED;
    static public int backgroundImage = 0;
    public static boolean displayIsRound = false;


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

    @Override
    protected Game getGame() {
        return new AirHockey(level, displayIsRound, backgroundImage, ballColor, sticksColor, boxColor, goalsColor);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Instantiates clients without member variables, as clients are inexpensive to create and
        // won't lose their listeners. (They are cached and shared between GoogleApi instances.)
        Wearable.getDataClient(this).addListener(this);
        Wearable.getMessageClient(this).addListener(this);
        Wearable.getCapabilityClient(this)
                .addListener(this, Uri.parse("wear://"), CapabilityClient.FILTER_REACHABLE);
    }

    @Override
    protected void onPause() {
        super.onPause();

        Wearable.getDataClient(this).removeListener(this);
        Wearable.getMessageClient(this).removeListener(this);
        Wearable.getCapabilityClient(this).removeListener(this);
    }



    @Override
    public AmbientModeSupport.AmbientCallback getAmbientCallback() {
        return null;
    }

    @Override
    public void onCapabilityChanged(@NonNull CapabilityInfo capabilityInfo) {

    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
       System.out.println( "onDataChanged(): " + dataEvents);

        for (DataEvent event : dataEvents) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                String path = event.getDataItem().getUri().getPath();
                if (DataLayerListenerService.VIDEO_CONFIRMATION_PATH.equals(path)) {
                    DataMapItem dataMapItem = DataMapItem.fromDataItem(event.getDataItem());
                    long confirmationTime =
                            dataMapItem.getDataMap().getLong(DataLayerListenerService.VIDEO_CONFIRMATION_TIME);
                    DateFormat formatter = new SimpleDateFormat("dd MMM yyyy HH:mm:ss:SSS Z");
                   String date = formatter.format(new Date(confirmationTime));
                    Toast.makeText(getApplicationContext(), "Confirmation recived. Time =" + date, Toast.LENGTH_LONG).show();
                } else if (DataLayerListenerService.COUNT_PATH.equals(path)) {

                } else {
                    System.out.println("Unrecognized path: " + path);
                }

            } else if (event.getType() == DataEvent.TYPE_DELETED) {
                System.out.println("Data item deleted");

            } else {
               System.out.println("Unknown data event Type = " + event.getType());
            }
        }
    }

    @Override
    public void onMessageReceived(@NonNull MessageEvent messageEvent) {
    System.out.println("Message recived: " + messageEvent.getData().toString());
    }
}
