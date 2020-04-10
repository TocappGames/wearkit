package dev.wearkit.core.rendering;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import dev.wearkit.core.common.Paintable;
import dev.wearkit.core.common.Renderable;
import dev.wearkit.core.common.Stampable;
import dev.wearkit.core.exceptions.PaintRequiredException;

import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Vector2;

public class Body extends org.dyn4j.dynamics.Body implements Renderable, Paintable, Stampable {

    private Paint paint;
    private Bitmap bitmap;

    public Body(){ }

    public Body(Paint paint) {
        this.paint = paint;
    }

    @Override
    public void render(Canvas canvas) throws PaintRequiredException {

        // keep the current coordinate system
        canvas.save();

        // go to Body's coordinate system
        Vector2 translation = this.transform.getTranslation();
        canvas.translate((float) translation.x, (float) translation.y);

        // render all the fixtures in the Body
        for(BodyFixture fixture: this.getFixtures()){
            if(this.bitmap != null){
                canvas.drawBitmap(this.bitmap, 0, 0, this.paint);
            }
            else {
                if(this.paint == null)
                    throw new PaintRequiredException("Either stamp(Bitmap) or paint(Paint) must be used before render");
                Paintable paintable = (Paintable) fixture.getShape();
                paintable.paint(this.paint);
                ((Renderable) paintable).render(canvas);
            }
        }

        // restore previous coordinate system
        canvas.restore();
    }

    @Override
    public void paint(Paint paint) {
        this.paint = paint;
    }


    @Override
    public void stamp(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
