package com.tocapp.dyn4jtest;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;


public class MainActivity extends WearableActivity {
    ImageButton soundBtn;
    boolean sound = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            GameActivity.displayIsRound = getResources().getConfiguration().isScreenRound();
        }
        Button easy = findViewById(R.id.easy);
        Button medium = findViewById(R.id.medium);
        Button hard = findViewById(R.id.hard);
        Button map = findViewById(R.id.map);
        soundBtn = findViewById(R.id.soundBtn);
        soundBtn.setImageResource(R.drawable.sound_on);
        easy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                GameActivity.config.setLevel(1);

                Intent i = new Intent( MainActivity.this, GameActivity.class);
                startActivity(i);
            }
        });

        medium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GameActivity.config.setLevel(2);

                Intent i = new Intent( MainActivity.this, GameActivity.class);
                startActivity(i);
            }
        });
        hard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GameActivity.config.setLevel(3);
                Intent i = new Intent( MainActivity.this, GameActivity.class);
                startActivity(i);

            }
        });

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent i = new Intent( MainActivity.this, SelectMap.class);
            startActivity(i);
            }
        });

        soundBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sound) {
                    soundBtn.setImageResource(R.drawable.sound_off);
                } else {
                    soundBtn.setImageResource(R.drawable.sound_on);
                }
                sound = !sound;
                GameActivity.config.setSound(sound);
                System.out.println(sound);
            }
        });
    }


}