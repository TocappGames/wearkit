package dev.wearkit.core.rendering;

import android.graphics.Paint;

import dev.wearkit.core.common.Indexable;

public class Ornament extends Body implements Indexable {

    private int zIndex = -1;

    public Ornament(Paint paint) {
        super(paint);
    }

    public Ornament(Paint paint, int zIndex) {
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