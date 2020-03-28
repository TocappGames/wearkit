package dev.wearkit.sample;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.widget.TextView;

public class MainActivity extends WearableActivity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(RESULT_CANCELED.layout.activity_main);

        mTextView = (TextView) findViewById(RESULT_CANCELED.id.text);

        // Enables Always-on
        setAmbientEnabled();
    }
}
