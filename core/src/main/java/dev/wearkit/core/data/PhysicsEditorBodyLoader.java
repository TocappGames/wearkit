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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Objects;

import dev.wearkit.core.exceptions.LoadException;
import dev.wearkit.core.exceptions.MalformedDataException;
import dev.wearkit.core.rendering.Body;
import dev.wearkit.core.rendering.shape.Polygon;

/**
 * Loads the data exported from PhysicsEditor(TM) located in assets directory
 * see https://wearkit.dev/tutorial#SpriteLoading
 */
public class PhysicsEditorBodyLoader implements Loader<Body> {

    private Context ctx;

    public PhysicsEditorBodyLoader(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    public Body load(String filename) throws LoadException {
        try {
            String vertexData = String.format("%s.json", filename);
            String bitmapData = String.format("%s.png", filename);
            String data = this.readAsString(vertexData);
            Body body = this.buildBody(data);
            if(this.assetExists(bitmapData)) {
                Bitmap bitmap = this.readAsBitmap(String.format("%s.png", filename));
                body.stamp(bitmap);
            }
            else {
                Paint paint = new Paint(Color.GREEN);
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                body.paint(paint);
            }
            return body;
        } catch (IOException e) {
            throw new LoadException("Failed to load data from resource: " + e.getMessage());
        }
    }

    private Body buildBody(String data) throws MalformedDataException {

        Body body = new Body();

        try {
            JSONArray jsonBody = new JSONArray(data);
            if(jsonBody.length() != 1)
                throw new MalformedDataException(String.format("Invalid load data, JSON must have exactly one (1) body (%d given)", jsonBody.length()));
            JSONArray firstBody = jsonBody.getJSONArray(0);
            for(int i=0; i < firstBody.length(); i++){
                JSONArray jsonShape = firstBody.getJSONArray(i);
                Vector2[] vertexes = new Vector2[jsonShape.length()];
                for(int j=0; j < jsonShape.length(); j++){
                    JSONArray jsonVertex = jsonShape.getJSONArray(j);
                    Vector2 vertex = new Vector2(
                            jsonVertex.getDouble(0),
                            jsonVertex.getDouble(1)
                    );
                    vertexes[j] = vertex;
                }
                Geometry.reverseWinding(vertexes);
                body.addFixture(new Polygon(vertexes));
            }

        } catch (JSONException e) {
            throw new MalformedDataException("Malformed load JSON data, please check the contents: " + e.getMessage());
        }

        return body;
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

    private Bitmap readAsBitmap(String filename) throws IOException {
        return BitmapFactory.decodeStream(this.ctx.getAssets().open(filename));
    }

    private boolean assetExists(String filename) throws IOException {
        return Arrays.asList(
                Objects.requireNonNull(this.ctx.getAssets().list(""))
        ).contains(filename);
    }


}
