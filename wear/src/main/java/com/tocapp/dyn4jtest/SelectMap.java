package com.tocapp.dyn4jtest;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.WorkerThread;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;
import com.google.android.wearable.intent.RemoteIntent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.provider.CalendarContract.EXTRA_EVENT_ID;

public class SelectMap extends WearableActivity {
    int selection = 0;
    int numMaps = 5;
    private static final String OPEN_MAP_SELECTOR = "/open_map_selector";

    int ballColor;
    int sticksColor;
    int boxColor;
    int goalsColor;
    private ArrayList<Integer> images;
    private ImageButton image;

    static final String GAME_PATH = "com.tocapp.dyn4jtest.SelectMap";

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    private Button buttonSelect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_map);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplication());
        editor = sharedPref.edit();

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
                    image.setImageResource(images.get(selection));
                }

            }
        });

        buttonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selection > 0) {
                    selection--;
                    mapChanged();
                    image.setImageResource(images.get(selection));

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
        switch (selection) {
            case 0:
                MainActivity.ballColor = Color.WHITE;
                MainActivity.sticksColor = Color.BLUE;
                MainActivity.boxColor = Color.WHITE;
                MainActivity.goalsColor = Color.RED;
                break;
            case 1:
                boolean mapa1Viewed = sharedPref.getBoolean("1", false);
                if (mapa1Viewed) {
                    MainActivity.ballColor = Color.YELLOW;
                    MainActivity.sticksColor = Color.GREEN;
                    MainActivity.boxColor = Color.MAGENTA;
                    MainActivity.goalsColor = Color.CYAN;
                    MainActivity.backgroundImage = R.drawable.fondo_2;
                } else {
                    onStartMobileActivity(view);
                }
                break;
            case 2:
                // get if video is viewed
                boolean mapa2Viewed = sharedPref.getBoolean("2", false);
                if (mapa2Viewed) {
                    MainActivity.ballColor = Color.CYAN;
                    MainActivity.sticksColor = Color.YELLOW;
                    MainActivity.boxColor = Color.MAGENTA;
                    MainActivity.goalsColor = Color.GREEN;
                    MainActivity.backgroundImage = R.drawable.fondo_3;
                }else {
                    onStartMobileActivity(view);
                }

                break;
            case 3:
                // get if video is viewed
                boolean mapa3Viewed = sharedPref.getBoolean("3", false);
                if (mapa3Viewed) {
                    MainActivity.ballColor = Color.GREEN;
                    MainActivity.sticksColor = Color.MAGENTA;
                    MainActivity.boxColor = Color.WHITE;
                    MainActivity.goalsColor = Color.RED;
                    MainActivity.backgroundImage = R.drawable.fondo_4;
                }else {
                    onStartMobileActivity(view);
                }

                break;
            case 4:
                boolean mapa4Viewed = sharedPref.getBoolean("4", false);
                if (mapa4Viewed) {
                    MainActivity.ballColor = Color.BLACK;
                    MainActivity.sticksColor = Color.GREEN;
                    MainActivity.boxColor = Color.YELLOW;
                    MainActivity.goalsColor = Color.MAGENTA;
                    MainActivity.backgroundImage = R.drawable.fondo_5;
                }else {
                    onStartMobileActivity(view);
                }

                break;
        }

        Intent i = new Intent(SelectMap.this, NewActivity.class);
        startActivity(i);
    }

    private void mapChanged() {
        boolean mapIsUnlocked = sharedPref.getBoolean(Integer.toString(selection), false);
        if (selection == 0) mapIsUnlocked = true;
        if (mapIsUnlocked) {
            buttonSelect.setText("Select map");
        } else {
            buttonSelect.setText("Unlock in phone");
        }
    }

    private class StartMobileActivity extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... args) {
            Collection<String> nodes = getNodes();
            System.out.println("Nodes: " + nodes);

            for (String node : nodes) {
                System.out.println("Nodes: " + node);
                sendStartActivityMessage(node);
            }
            return null;
        }
    }


    @WorkerThread
    private void sendStartActivityMessage(String node) {
        System.out.println("Sending message to start activity");
       /* RemoteIntent.startRemoteActivity(getApplicationContext(), new Intent(Intent.ACTION_VIEW).addCategory(Intent.CATEGORY_BROWSABLE)
                        .setData(Uri.parse("https://play.google.com/store/apps/details?id=com.mobirix.airhockey&hl")),
                null, node);*/

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
    public void onStartMobileActivity(View view) {
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
