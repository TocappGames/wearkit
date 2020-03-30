package com.tocapp.dyn4jtest;

import android.content.Intent;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

public class ListenerServiceFromWear extends WearableListenerService {

    private static final String OPEN_MAP_SELECTOR = "/open_map_selector";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {

        /*
         * Receive the message from wear
         */
        if (messageEvent.getPath().equals(OPEN_MAP_SELECTOR)) {

            //For example you can start an Activity
            Intent startIntent = new Intent(this, SelectMap.class);
            startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startIntent);
        }

    }     
}