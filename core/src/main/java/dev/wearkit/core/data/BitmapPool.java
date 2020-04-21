package dev.wearkit.core.data;

import android.graphics.Bitmap;

import java.util.HashMap;
import java.util.Map;

public class BitmapPool implements Pool<Bitmap> {

    private Map<Long, Bitmap> storage = new HashMap<>();

    @Override
    public Bitmap get(Bitmap object) {
        return this.storage.get(computeHash(object));
    }

    @Override
    public boolean has(Bitmap object) {
        return this.storage.containsKey(computeHash(object));
    }

    @Override
    public Bitmap store(Bitmap object) {
        long hash = computeHash(object);
        if(!this.storage.containsKey(hash))
            this.storage.put(computeHash(object), object);
        return this.storage.get(hash);
    }

    private static long computeHash(Bitmap bmp){
        long hash = 31;
        for(int x = 0; x < bmp.getWidth(); x++){
            for (int y = 0; y < bmp.getHeight(); y++){
                hash *= (bmp.getPixel(x,y) + 31);
            }
        }
        return hash;
    }

}
