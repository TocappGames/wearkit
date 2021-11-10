package dev.wearkit.core.rendering.shape;

import android.graphics.Canvas;
import org.dyn4j.geometry.Vector2;


public class Text extends Rectangle {

    private static final String TAG = "Text";

    private String text;

    public Text(String text) {
        super(1, 1);
        this.text = text;
    }


    @Override
    public void render(Canvas canvas) {
        Vector2 center = this.getCenter();
        float x = (float) center.x;
        float y = (float) center.y;
        for (String line: this.text.split("\n")) {
            canvas.drawText(line, x, y, this.paint);
            y += this.paint.descent() - this.paint.ascent();
        }
    }
}
