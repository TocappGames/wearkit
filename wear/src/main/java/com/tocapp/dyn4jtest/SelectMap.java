package com.tocapp.dyn4jtest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.WorkerThread;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;
import com.tocapp.utils.SharedPrefsUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SelectMap extends WearableActivity {
    int selection = 0;
    int numMaps = 5;
    private static final String OPEN_MAP_SELECTOR = "/open_map_selector";
    private ArrayList<Integer> images;
    private ImageButton image;

    static final String GAME_PATH = "com.tocapp.dyn4jtest.SelectMap";
    Intent mainActivityIntent;

    private SharedPrefsUtil sharedPrefs;

    private Button buttonSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.select_map_activity );
        mainActivityIntent = new Intent( SelectMap.this, MainActivity.class );
        sharedPrefs = new SharedPrefsUtil( getApplication() );

        ImageButton buttonLeft = findViewById( R.id.buttonLeft );
        ImageButton buttonRight = findViewById( R.id.buttonRight );
        buttonSelect = findViewById( R.id.selectBtn );
        image = findViewById( R.id.image );

        images = new ArrayList<>();
        images.add( R.drawable.mapa1 );
        images.add( R.drawable.mapa2 );
        images.add( R.drawable.mapa3 );
        images.add( R.drawable.mapa4 );
        images.add( R.drawable.mapa5 );

        image.setImageResource( images.get( selection ) );

        buttonRight.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selection < numMaps - 1) {
                    selection++;
                    mapChanged();
                }

            }
        } );

        buttonLeft.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selection > 0) {
                    selection--;
                    mapChanged();
                }
            }
        } );


        image.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onMapSelected( view );

            }
        } );

        buttonSelect.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onMapSelected( view );
            }
        } );

    }

    private void onMapSelected(View view) {

        switch (selection) {
            case 0:
                GameActivity.config.setMap0();
                onBackPressed();
                break;

            case 1:
                if (checkMap( "1" )) {
                    GameActivity.config.setMap1();
                    onBackPressed();
                } else {
                    showAlert();
                    //sharedPrefs.setViewedMap( "1" );
                }
                break;

            case 2:
                // get if video is viewed
                if (checkMap( "2" )) {
                    GameActivity.config.setMap2();
                    onBackPressed();

                } else {
                    showAlert();
                    //sharedPrefs.setViewedMap( "2" );
                }
                break;

            case 3:
                // get if video is viewed
                if (checkMap( "3" )) {
                    GameActivity.config.setMap3();
                    onBackPressed();
                } else {
                    showAlert();
                    //sharedPrefs.setViewedMap( "3" );
                }
                break;

            case 4:
                if (checkMap( "4" )) {
                    GameActivity.config.setMap4();
                    onBackPressed();
                } else {
                    showAlert();
                    //sharedPrefs.setViewedMap( "4" );
                }
                break;
        }
    }

    private boolean checkMap(String mapId) {
        return (sharedPrefs.getViewedMap( mapId ));
    }

    // Check text on button
    private void mapChanged() {
        image.setImageResource( images.get( selection ) );
        boolean mapIsUnlocked = sharedPrefs.getViewedMap( Integer.toString( selection ) );
        if (selection == 0) mapIsUnlocked = true;
        if (mapIsUnlocked) {
            buttonSelect.setText( R.string.select_map );
        } else {
            buttonSelect.setText( R.string.you_need_see_video );
        }
    }

    private void showAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder( this );

        builder.setMessage( "Do you wanna go mobile?" )
                .setTitle( "Are you sure?" );

        builder.setPositiveButton( "Go mobile", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                onStartMobileActivity();
                startActivity( mainActivityIntent );
            }
        } );
        builder.setNegativeButton( "Go back", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                onBackPressed();
            }
        } );
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private class StartMobileActivity extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... args) {
            Collection<String> nodes = getNodes();
            for (String node : nodes) {
                sendStartActivityMessage( node );
            }
            return null;
        }
    }


    @WorkerThread
    private void sendStartActivityMessage(String node) {
        System.out.println( "Sending message to start activity" );
        // Open play store with remote intent, it works
        /* RemoteIntent.startRemoteActivity(getApplicationContext(), new Intent(Intent.ACTION_VIEW).addCategory(Intent.CATEGORY_BROWSABLE)
                        .setData(Uri.parse("https://play.google.com/store/apps/details?id=com.mobirix.airhockey&hl")),
                null, node);*/

        // Send normal message, and mobile will open intent
        Task<Integer> sendMessageTask =
                Wearable.getMessageClient( this ).sendMessage( node, OPEN_MAP_SELECTOR, new byte[0] );

        try {
            // Block on a task and get the result synchronously (because this is on a background
            // thread).
            Integer result = Tasks.await( sendMessageTask );
            System.out.println( "Message start activity sent: " + result );

        } catch (ExecutionException exception) {
            System.out.println( "Task failed: " + exception );

        } catch (InterruptedException exception) {
            System.out.println( "Interrupt occurred: " + exception );
        }

    }

    /**
     * Sends an RPC to start a fullscreen Activity on the wearable.
     */
    public void onStartMobileActivity() {
        new StartMobileActivity().execute();
    }

    @WorkerThread
    private Collection<String> getNodes() {
        HashSet<String> results = new HashSet<>();

        Task<List<Node>> nodeListTask =
                Wearable.getNodeClient( getApplicationContext() ).getConnectedNodes();

        try {
            List<Node> nodes = Tasks.await( nodeListTask );
            for (Node node : nodes) {
                results.add( node.getId() );
            }

        } catch (ExecutionException exception) {
            System.out.println( "Task failed: " + exception );

        } catch (InterruptedException exception) {
            System.out.println( "Interrupt occurred: " + exception );
        }

        return results;
    }

}
