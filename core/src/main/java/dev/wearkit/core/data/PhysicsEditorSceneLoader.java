package dev.wearkit.core.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;

import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.Vector2;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

import dev.wearkit.core.exceptions.LoadException;
import dev.wearkit.core.exceptions.MalformedDataException;
import dev.wearkit.core.rendering.Body;
import dev.wearkit.core.rendering.shape.Polygon;

/**
 * Loads the data exported from PhysicsEditor(TM) located in assets directory
 * see https://wearkit.dev/tutorial#SpriteLoading
 */
public class PhysicsEditorSceneLoader extends AssetLoader<Map<String, Body>> {

    public PhysicsEditorSceneLoader(Context ctx) {
        super(ctx);
    }

    @Override
    public Map<String, Body> load(String filename) throws LoadException {
        try {
            String data = this.readAsString(filename);
            return this.extractBodies(data);
        } catch (IOException e) {
            throw new LoadException("Failed to load data from resource: " + e.getMessage());
        }
    }

    private Map<String, Body> extractBodies(String data) throws MalformedDataException {

        Map<String, Body> bodies = new HashMap<>();

        try {
            JSONObject jsonBodies = new JSONObject(data);
            Iterator<String> bodyNamesIterator = jsonBodies.keys();
            while (bodyNamesIterator.hasNext()){
                String bodyName = bodyNamesIterator.next();
                JSONObject jsonBody = jsonBodies.getJSONObject(bodyName);
                String bitmapFile = jsonBody.getString("bitmap");

                JSONArray jsonFixtures = jsonBody.getJSONArray("fixtures");
                Body body = new Body();
                if(!bitmapFile.equals("")) {
                    Bitmap bitmap = this.readAsBitmap(bitmapFile);
                    body.stamp(bitmap);
                }
                else {
                    Paint paint = new Paint(Color.GREEN);
                    paint.setStyle(Paint.Style.FILL_AND_STROKE);
                    body.paint(paint);
                }
                for(int i=0; i<jsonFixtures.length(); i++){
                    JSONObject jsonFixture = jsonFixtures.getJSONObject(i);
                    JSONObject jsonFixtureParams = jsonFixture.getJSONObject("parameters");
                    JSONArray jsonFixtureShape = jsonFixture.getJSONArray("shape");

                    Vector2[] vertexes = new Vector2[jsonFixtureShape.length()];
                    for(int j=0; j<jsonFixtureShape.length(); j++){
                        JSONObject jsonVertex = jsonFixtureShape.getJSONObject(j);
                        Vector2 vertex = new Vector2(
                                jsonVertex.getDouble("x"),
                                jsonVertex.getDouble("y")
                        );
                        vertexes[j] = vertex;
                    }
                    Geometry.reverseWinding(vertexes);
                    body.addFixture(
                            new Polygon(vertexes),
                            jsonFixtureParams.getDouble("density"),
                            jsonFixtureParams.getDouble("friction"),
                            jsonFixtureParams.getDouble("restitution")
                    );
                    bodies.put(bodyName, body);
                }
            }


        } catch (JSONException e) {
            throw new MalformedDataException("Malformed load JSON data, please check the contents: " + e.getMessage());
        } catch (IOException e) {
            throw new MalformedDataException("Error loading bitmap: " + e.getMessage());
        }

        return bodies;
    }

    private String readAsString(String filename) throws IOException {
        InputStream inputStream = this.ctx.getAssets().open(filename);

        InputStreamReader inputreader = new InputStreamReader(inputStream);
        BufferedReader buffreader = new BufferedReader(inputreader);
        String line;
        StringBuilder text = new StringBuilder();

        while (( line = buffreader.readLine()) != null) {
            text.append(line);
            text.append('\n');
        }
        return text.toString();
    }

    private boolean assetExists(String filename) throws IOException {
        return Arrays.asList(
                Objects.requireNonNull(this.ctx.getAssets().list(""))
        ).contains(filename);
    }


}
