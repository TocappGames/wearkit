package dev.wearkit.core.rendering;

import android.graphics.Paint;

public class Thing extends Body implements Indexable {

    private int zIndex = -1;

    public Thing(Paint paint) {
        super(paint);
    }

    public Thing(Paint paint, int zIndex) {
        super(paint);
        this.zIndex = zIndex;
    }

    @Override
    public int getIndex() {
        return this.zIndex;
    }

    @Override
    public int compareTo(Object o) {
        return this.zIndex - ((Indexable) o).getIndex();
    }
}
