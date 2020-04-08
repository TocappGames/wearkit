package dev.wearkit.sample;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import dev.wearkit.core.activity.MobileGameActivity;
import dev.wearkit.core.engine.Game;
import dev.wearkit.example.ComplexBodies;

public class MainActivity extends MobileGameActivity {
    protected Game getGame(){
        try {
            JSONArray circuit = this.readRawJSON(R.raw.circuit);
            return new ComplexBodies(circuit);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONArray readRawJSON(int resId) throws IOException, JSONException {
        InputStream inputStream = this.getResources().openRawResource(resId);

        InputStreamReader inputreader = new InputStreamReader(inputStream);
        BufferedReader buffreader = new BufferedReader(inputreader);
        String line;
        StringBuilder text = new StringBuilder();

        while (( line = buffreader.readLine()) != null) {
            text.append(line);
            text.append('\n');
        }
        return new JSONArray(text.toString());
    }
}
