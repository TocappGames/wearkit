package com.tocapp.dyn4jtest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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

    private SharedPrefsUtil sharedPrefs;

    private Button buttonSelect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_map);

        sharedPrefs = new SharedPrefsUtil(getApplication());

        ImageButton buttonLeft = findViewById(R.id.buttonLeft);
        ImageButton buttonRight = findViewById(R.id.buttonRight);
        buttonSelect = findViewById(R.id.selectBtn);
        image = findViewById(R.id.image);

        images = new ArrayList<>();
        images.add(R.drawable.mapa1);
        images.add(R.drawable.mapa2);
        images.add(R.drawable.mapa3);
        images.add(R.drawable.mapa4);
        images.add(R.drawable.mapa5);

        image.setImageResource(images.get(selection));

        buttonRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selection < numMaps -1) {
                    selection++;
                    mapChanged();
                }

            }
        });

        buttonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selection > 0) {
                    selection--;
                    mapChanged();
                }
            }
        });


        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onMapSelected(view);

            }
        });

        buttonSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onMapSelected(view);
            }
        });

    }

    private void onMapSelected(View view) {
        Intent mainActivityIntent = new Intent(SelectMap.this, NewActivity.class);

        switch (selection) {
            case 0:
                MainActivity.ballColor = R.color.white;
                MainActivity.sticksColor = R.color.blue;
                MainActivity.boxColor = R.color.white;
                MainActivity.goalsColor = R.color.red;
                startActivity(mainActivityIntent);
                break;
            case 1:
                if (checkMap("1")) {
                    MainActivity.ballColor = R.color.cyan;
                    MainActivity.sticksColor = R.color.yellow;
                    MainActivity.boxColor = R.color.purple;
                    MainActivity.goalsColor = R.color.green;

                    MainActivity.backgroundImage = R.drawable.fondo_2;
                    startActivity(mainActivityIntent);
                } else {
                    showAlert();
                    sharedPrefs.setViewedMap( "1" );

                }
                break;
            case 2:
                // get if video is viewed
                if (checkMap("2")) {
                    MainActivity.ballColor = R.color.green;
                    MainActivity.sticksColor = R.color.purple;
                    MainActivity.boxColor = R.color.white;
                    MainActivity.goalsColor = R.color.red;

                    MainActivity.backgroundImage = R.drawable.fondo_3;
                    startActivity(mainActivityIntent);
                }else {
                    showAlert();

                    sharedPrefs.setViewedMap( "2" );
        }

                break;
            case 3:
                // get if video is viewed
                if (checkMap("3")) {
                    MainActivity.ballColor = R.color.black;
                    MainActivity.sticksColor = R.color.green;
                    MainActivity.boxColor = R.color.yellow;
                    MainActivity.goalsColor = R.color.purple;

                    MainActivity.backgroundImage = R.drawable.fondo_4;
                    startActivity(mainActivityIntent);

                }else {
                    showAlert();

                    sharedPrefs.setViewedMap( "3" );
                }

                break;
            case 4:
                if (checkMap("4")) {
                    MainActivity.ballColor = Color.BLACK;
                    MainActivity.sticksColor = Color.GREEN;
                    MainActivity.boxColor = Color.YELLOW;
                    MainActivity.goalsColor = Color.MAGENTA;

                    MainActivity.backgroundImage = R.drawable.fondo_5;
                    startActivity(mainActivityIntent);
                }else {
                    showAlert();
                    sharedPrefs.setViewedMap( "4" );
                }

                break;
        }

    }

    private boolean checkMap(String mapId) {
        return (sharedPrefs.getViewedMap(mapId));
    }

    // Check text on button
    private void mapChanged() {
        image.setImageResource(images.get(selection));
        boolean mapIsUnlocked = sharedPrefs.getViewedMap( Integer.toString(  selection) );
        if (selection == 0) mapIsUnlocked = true;
        if (mapIsUnlocked) {
            buttonSelect.setText( R.string.select_map);
        } else {
            buttonSelect.setText( R.string.you_need_see_video);
        }
    }
    private void showAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder( this );

        builder.setMessage("Do you wanna go mobile?" )
                .setTitle( "Are you sure?" );

        builder.setPositiveButton("Go mobile", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                onStartMobileActivity();
            }
        });
        builder.setNegativeButton("Go back", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                onBackPressed();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private class StartMobileActivity extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... args) {
            Collection<String> nodes = getNodes();
            for (String node : nodes) {
                sendStartActivityMessage(node);
            }
            return null;
        }
    }


    @WorkerThread
    private void sendStartActivityMessage(String node) {
        System.out.println("Sending message to start activity");
            // Open play store with remote intent, it works
        /* RemoteIntent.startRemoteActivity(getApplicationContext(), new Intent(Intent.ACTION_VIEW).addCategory(Intent.CATEGORY_BROWSABLE)
                        .setData(Uri.parse("https://play.google.com/store/apps/details?id=com.mobirix.airhockey&hl")),
                null, node);*/

        // Send normal message, and mobile will open intent
        Task<Integer> sendMessageTask =
                Wearable.getMessageClient(this).sendMessage(node, OPEN_MAP_SELECTOR, new byte[0]);

        try {
            // Block on a task and get the result synchronously (because this is on a background
            // thread).
            Integer result = Tasks.await(sendMessageTask);
            System.out.println("Message start activity sent: " + result);

        } catch (ExecutionException exception) {
            System.out.println("Task failed: " + exception);

        } catch (InterruptedException exception) {
            System.out.println("Interrupt occurred: " + exception);
        }

    }

    /** Sends an RPC to start a fullscreen Activity on the wearable. */
    public void onStartMobileActivity() {
        new StartMobileActivity().execute();
    }

    @WorkerThread
    private Collection<String> getNodes() {
        HashSet<String> results = new HashSet<>();

        Task<List<Node>> nodeListTask =
                Wearable.getNodeClient(getApplicationContext()).getConnectedNodes();

        try {
              List<Node> nodes = Tasks.await(nodeListTask);
            for (Node node : nodes) {
                results.add(node.getId());
            }

        } catch (ExecutionException exception) {
            System.out.println("Task failed: " + exception);

        } catch (InterruptedException exception) {
            System.out.println("Interrupt occurred: " + exception);
        }

        return results;
    }

}
