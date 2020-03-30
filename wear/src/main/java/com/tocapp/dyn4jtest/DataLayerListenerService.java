/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tocapp.dyn4jtest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/** Listens to DataItems and Messages from the local node. */
public class DataLayerListenerService extends WearableListenerService {

    private static final String TAG = "DataLayerService";

    private static final String START_ACTIVITY_PATH = "/start-activity";
    private static final String DATA_ITEM_RECEIVED_PATH = "/data-item-received";
    public static final String COUNT_PATH = "/count";
    public static final String VIDEO_CONFIRMATION_PATH = "/confirmation";
    private static final String UNLOCKED_MAP_ID = "id";
    public static final String VIDEO_CONFIRMATION_TIME = "time";
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        for (DataEvent event : dataEvents) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                String path = event.getDataItem().getUri().getPath();
                if (DataLayerListenerService.VIDEO_CONFIRMATION_PATH.equals(path)) {

                    sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplication());
                    editor = sharedPref.edit();
                    DataMapItem dataMapItem = DataMapItem.fromDataItem(event.getDataItem());
                    long confirmationTime =
                            dataMapItem.getDataMap().getLong(DataLayerListenerService.VIDEO_CONFIRMATION_TIME);
                    int mapId = dataMapItem.getDataMap().getInt(DataLayerListenerService.UNLOCKED_MAP_ID);
                    editor.putBoolean(Integer.toString(mapId), true);
                    editor.commit();
                    DateFormat formatter = new SimpleDateFormat("dd MMM yyyy HH:mm:ss:SSS Z");
                    String date = formatter.format(new Date(confirmationTime));
                    System.out.println("Evento de confirmacion de video recibido, datos:" + dataMapItem.getDataMap().getLong(DataLayerListenerService.VIDEO_CONFIRMATION_TIME) + "Id del mapa: " + mapId);
                    Toast.makeText(getApplicationContext(), "Confirmation recived. Time =" + date, Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        System.out.println( "onMessageReceived: " + messageEvent);
        // Check to see if the message is to start an activity
        if (messageEvent.getPath().equals(START_ACTIVITY_PATH)) {
            Intent startIntent = new Intent(this, MainActivity.class);
            startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startIntent);
        }
    }
}
