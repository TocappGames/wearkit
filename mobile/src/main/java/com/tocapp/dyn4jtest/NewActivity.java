package com.tocapp.dyn4jtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class NewActivity extends AppCompatActivity {
    ImageButton soundBtn;
    boolean sound = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);
        Button easy = findViewById(R.id.easy);
        Button medium = findViewById(R.id.medium);
        Button hard = findViewById(R.id.hard);
        Button map = findViewById(R.id.map);
        soundBtn = findViewById(R.id.sound);
        soundBtn.setImageResource(R.drawable.sound_on);
        easy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.level = 1;

                Intent i = new Intent(NewActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

        medium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.level = 2;

                Intent i = new Intent(NewActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
        hard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.level = 3;
                Intent i = new Intent(NewActivity.this, MainActivity.class);
                startActivity(i);

            }
        });

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(NewActivity.this, SelectMap.class);
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
                MainActivity.sound = sound;
                System.out.println(sound);
            }
        });
    }


}
