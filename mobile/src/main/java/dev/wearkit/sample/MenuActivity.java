package dev.wearkit.sample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


public class MenuActivity extends AppCompatActivity {

    public static final String EXTRA_EXAMPLE_NAME = "EXAMPLE_NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        setContentView(R.layout.activity_menu);

        int[] buttonIds = {
                R.id.button_floatingballs,
                R.id.button_complexshapes,
                R.id.button_cardriving,
        };

        for(int i=0; i < buttonIds.length; i++){
            Button button = this.findViewById(buttonIds[i]);
            final int finalI = i;
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MenuActivity.this, ExamplesActivity.class);
                    intent.putExtra(EXTRA_EXAMPLE_NAME, ExamplesActivity.EXAMPLE_NAMES.get(finalI));
                    startActivity(intent);
                }
            });

        }

    }
}
