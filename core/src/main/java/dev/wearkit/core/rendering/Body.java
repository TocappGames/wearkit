package dev.wearkit.core.rendering;

import android.graphics.Canvas;
import android.graphics.Paint;

import dev.wearkit.core.exceptions.PaintRequiredException;

import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Vector2;

public class Body extends org.dyn4j.dynamics.Body implements Renderable {

    private Paint paint;

    public Body(Paint paint) {
        this.paint = paint;
    }

    @Override
    public void render(Canvas canvas, double scale) {

        // keep the current coordinate system
        canvas.save();

        // go to Body's coordinate system
        Vector2 translation = this.transform.getTranslation();
        canvas.translate((float) (translation.x * scale), (float) (translation.y * scale));

        // render all the fixtures in the Body
        for(BodyFixture fixture: this.getFixtures()){
            Paintable paintable = (Paintable) fixture.getShape();
            try {
                paintable.paint(this.paint).render(canvas, scale);
            } catch (PaintRequiredException e) {
                e.printStackTrace();
            }
        }

        // restore previous coordinate system
        canvas.restore();
    }
}
