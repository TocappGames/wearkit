package dev.wearkit.core.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;


import org.dyn4j.geometry.Convex;
import org.dyn4j.geometry.Vector2;
import org.dyn4j.geometry.decompose.Bayazit;
import org.dyn4j.geometry.decompose.Decomposer;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import dev.wearkit.core.exceptions.LoadException;
import dev.wearkit.core.rendering.Body;
import dev.wearkit.core.rendering.shape.Polygon;


/**
 * Loads the data taken from Unity SpriteEditor as *.meta, located in assets directory
 * see https://wearkit.dev/tutorial#SpriteLoading
 */
public class UnityAssetBodyLoader extends AssetLoader<Body> {

    private static final String TAG = "UnityAssetLoader";
    private static final String[] META_PATH = {"TextureImporter", "spriteSheet", "physicsShape"};
    private Decomposer decomposer;

    public UnityAssetBodyLoader(Context ctx, Decomposer decomposer) {
        super(ctx);
        this.decomposer = decomposer;
    }

    public UnityAssetBodyLoader(Context ctx) {
        this(ctx, new Bayazit());
    }

    @Override
    public Body load(String filename) throws LoadException {

        Body body = new Body();
        try {
            InputStream is = this.ctx.getAssets().open(filename + ".meta");
            Yaml yaml = new Yaml();
            Object node = yaml.load(is);
            for(String step: META_PATH){
                node = ((Map) node).get(step);
            }
            List path = (List) ((List) node).get(0);
            Vector2[] vertexes = new Vector2[path.size()];
            for(int i=0; i<path.size(); i++){
                Map<String, ?> point = (Map<String, ?>) path.get(i);
                vertexes[i] = new Vector2();
                Object x = point.get("x");
                Object y = point.get("y");
                vertexes[i] = new Vector2(
                        x instanceof Integer? (Integer) x: (Double) x,
                        -(y instanceof Integer? (Integer) y: (Double) y)
                );
            }
            for(Convex convex: this.decomposer.decompose(vertexes)){
                Polygon polygon = new Polygon(((org.dyn4j.geometry.Polygon) convex).getVertices());
                body.addFixture(polygon);
            }
        } catch (IOException ignored){}
        try {
            Bitmap bmp = readAsBitmap(filename);
            body.stamp(bmp);
            Paint paint = new Paint(Color.GREEN);
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            body.paint(paint);
            return body;
        } catch (IOException e) {
            throw new LoadException("Failed to load data from resource: " + e.getMessage());
        }
    }

}
