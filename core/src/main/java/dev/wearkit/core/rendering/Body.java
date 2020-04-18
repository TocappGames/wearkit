package dev.wearkit.core.rendering;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import dev.wearkit.core.common.Paintable;
import dev.wearkit.core.common.Printable;
import dev.wearkit.core.common.Renderable;
import dev.wearkit.core.common.Scalable;
import dev.wearkit.core.common.Stampable;
import dev.wearkit.core.exceptions.PaintRequiredException;

import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Convex;
import org.dyn4j.geometry.Vector2;

public class Body extends org.dyn4j.dynamics.Body implements Renderable, Paintable, Stampable, Printable, Scalable {

    private static final double RAD_TO_DEG_RATE = 180.0 / Math.PI;

    protected Paint paint;
    protected Bitmap bitmap;
    private String text;
    private double textX;
    private double textY;
    private Paint textPaint;
    private Double textAngle;

    public Body(){ }

    public Body(Paint paint) {
        this.paint = paint;
    }

    @Override
    public void render(Canvas canvas) throws PaintRequiredException {

        // keep the current transformation
        canvas.save();

        Vector2 translation = this.transform.getTranslation();
        canvas.translate((float) translation.x, (float) translation.y);
        double rotationRad = this.transform.getRotationAngle();
        canvas.rotate((float) (rotationRad * RAD_TO_DEG_RATE));

        if(this.bitmap != null){
            canvas.drawBitmap(this.bitmap, -this.bitmap.getWidth() / 2.0f,  -this.bitmap.getHeight() / 2.0f, this.paint);
        }
        else {
            // render all the fixtures in the Body
            for(BodyFixture fixture: this.getFixtures()){
                if(this.paint == null)
                    throw new PaintRequiredException(String.format(
                            "%s.paint(Paint paint) must be used before render",
                            getClass().getName()
                    ));
                ((Paintable) fixture.getShape()).paint(this.paint);
                ((Renderable) fixture.getShape()).render(canvas);
            }
        }
        if(this.textAngle != null){
            double newRotationRad = this.textAngle - rotationRad;
            canvas.rotate((float) (newRotationRad * RAD_TO_DEG_RATE));
        }
        if(this.text != null){
            canvas.drawText(
                    this.text,
                    (float) this.textX,
                    (float) this.textY,
                    this.textPaint
            );
        }

        // restore previous transformation
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

    @Override
    public void print(String text) {
        this.print(text, 0, 0, DEFAULT_PAINT);
    }

    @Override
    public void print(String text, double xPos, double yPos, Paint paint) {
        this.print(text, xPos, yPos, paint, null);
    }

    @Override
    public void print(String text, double xPos, double yPos, Paint paint, Double textAngle) {
        this.text = text;
        this.textX = xPos;
        this.textY = yPos;
        this.textPaint = paint;
        this.textAngle = textAngle;
    }

    @Override
    public Scalable scale(double rate) {
        Body body = new Body();
        body.paint = this.paint;
        if(this.bitmap != null)
            body.bitmap = Bitmap.createScaledBitmap(
                    this.bitmap,
                    (int) Math.round(this.bitmap.getWidth() * rate),
                    (int) Math.round(this.bitmap.getHeight() * rate),
                    false
            );
        for(BodyFixture fixture: this.getFixtures()){
            double density = fixture.getDensity();
            double friction = fixture.getFriction();
            double restitution = fixture.getRestitution();
            body.addFixture(
                    (Convex) ((Scalable) fixture.getShape()).scale(rate),
                    density,
                    friction,
                    restitution
            );
        }

        return body;
    }
}
