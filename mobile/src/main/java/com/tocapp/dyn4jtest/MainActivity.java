package com.tocapp.dyn4jtest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.tocapp.gamesdk.TestDyn4j;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TestDyn4j t = new TestDyn4j();

        t.test();
    }
}
