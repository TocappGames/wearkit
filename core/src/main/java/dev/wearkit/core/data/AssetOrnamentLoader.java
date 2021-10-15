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
import dev.wearkit.core.rendering.Ornament;
import dev.wearkit.core.rendering.shape.Polygon;


/**
 * Loads the data taken from Unity SpriteEditor as *.meta, located in assets directory
 * see https://wearkit.dev/tutorial#SpriteLoading
 */
public class AssetOrnamentLoader extends AssetLoader<Ornament> {

    private static final String TAG = "AssetOrnamentLoader";

    public AssetOrnamentLoader(Context ctx) {
        super(ctx);
    }

    @Override
    public Ornament load(String filename) throws LoadException {
        try {
            Ornament ornament = new Ornament();
            Bitmap bmp = readAsBitmap(filename);
            ornament.stamp(bmp);
            Paint paint = new Paint(Color.GREEN);
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            ornament.paint(paint);
            return ornament;
        } catch (IOException e) {
            throw new LoadException("Failed to load data from resource: " + e.getMessage());
        }
    }
}
