package com.tocapp.sdk.rendering;

import android.graphics.Paint;

public class GameThing extends GameBody implements Indexable {

    private int index = -1;

    public GameThing(Paint paint) {
        super(paint);
        this.index = -1;
    }

    public GameThing(Paint paint, int index) {
        super(paint);
        this.index = index;
    }

    @Override
    public int getIndex() {
        return this.index;
    }

    @Override
    public int compareTo(Object o) {
        return this.index - ((Indexable) o).getIndex();
    }
}
