package com.tocapp.dyn4jtest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NewActivity extends AppCompatActivity {

    public static int ballColor;
    public static int sticksColor;
    public static int boxColor;
    public static int goalsColor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);
        Button easy = findViewById(R.id.easy);
        Button medium = findViewById(R.id.medium);
        Button hard = findViewById(R.id.hard);
        Button map = findViewById(R.id.map);
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

    }
}
