package com.tocapp.dyn4jtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {
    ImageButton soundBtn;
    boolean sound = true;
    private String SOUND_STATE = "SOUND_STATE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            sound = savedInstanceState.getBoolean( SOUND_STATE );
        }
        setContentView(R.layout.main_activity );
        Button easy = findViewById(R.id.easy);
        Button medium = findViewById(R.id.medium);
        Button hard = findViewById(R.id.hard);
        Button map = findViewById(R.id.map);
        soundBtn = findViewById(R.id.sound);
        soundBtn.setImageResource(R.drawable.sound_on);
        easy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean( SOUND_STATE, sound );
    }


}
