package com.tocapp.dyn4jtest;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;

public class SelectMap extends WearableActivity {
    int selection = 0;
    int numMaps = 5;

    int ballColor;
    int sticksColor;
    int boxColor;
    int goalsColor;
    private ArrayList<Integer> images;
    private ImageButton image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_map);

        ImageButton buttonLeft = findViewById(R.id.buttonLeft);
        ImageButton buttonRight = findViewById(R.id.buttonRight);
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
                if (selection < numMaps -1) selection++;
                image.setImageResource(images.get(selection));
            }
        });

        buttonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selection > 0) selection--;
                image.setImageResource(images.get(selection));
            }
        });


        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (selection) {
                    case 0:
                        MainActivity.ballColor = Color.WHITE;
                        MainActivity.sticksColor = Color.BLUE;
                        MainActivity.boxColor = Color.WHITE;
                        MainActivity.goalsColor = Color.RED;
                        break;
                case 1:
                    MainActivity.ballColor = Color.YELLOW;
                    MainActivity.sticksColor = Color.GREEN;
                    MainActivity.boxColor = Color.MAGENTA;
                    MainActivity.goalsColor = Color.CYAN;
                    MainActivity.backgroundImage = R.drawable.fondo_2;
                    break;
                    case 2:
                        MainActivity.ballColor = Color.CYAN;
                        MainActivity.sticksColor = Color.YELLOW;
                        MainActivity.boxColor = Color.MAGENTA;
                        MainActivity.goalsColor = Color.GREEN;
                        MainActivity.backgroundImage = R.drawable.fondo_3;

                        break;
                    case 3:
                        MainActivity.ballColor = Color.GREEN;
                        MainActivity.sticksColor = Color.MAGENTA;
                        MainActivity.boxColor = Color.WHITE;
                        MainActivity.goalsColor = Color.RED;
                        MainActivity.backgroundImage = R.drawable.fondo_4;

                        break;
                    case 4:
                        MainActivity.ballColor = Color.BLACK;
                        MainActivity.sticksColor = Color.GREEN;
                        MainActivity.boxColor = Color.YELLOW;
                        MainActivity.goalsColor = Color.MAGENTA;
                        MainActivity.backgroundImage = R.drawable.fondo_5;

                        break;
                }


                Intent i = new Intent(SelectMap.this, NewActivity.class);
                startActivity(i);

            }
        });



    }
}
