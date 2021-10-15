package dev.wearkit.core.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;

import org.dyn4j.geometry.decompose.Bayazit;
import org.dyn4j.geometry.decompose.Decomposer;

import java.io.IOException;

import dev.wearkit.core.exceptions.LoadException;
import dev.wearkit.core.rendering.Ornament;


/**
 * Loads the data taken from Unity SpriteEditor as *.meta, located in assets directory
 * see https://wearkit.dev/tutorial#SpriteLoading
 */
public abstract class AssetLoader<T> implements Loader<T> {

    protected Context ctx;

    public AssetLoader(Context ctx) {
        this.ctx = ctx;
    }

    protected Bitmap readAsBitmap(String filename) throws IOException {
        return BitmapFactory.decodeStream(this.ctx.getAssets().open(filename));
    }
}
