package dev.wearkit.core.rendering;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Convex;

import dev.wearkit.core.common.Scalable;

public class Ornament extends Body {

    @Override
    public Ornament scale(double rate) {
        Ornament body = new Ornament();
        body.paint = this.paint;
        if(this.chunks != null) {
            Bitmap result = Bitmap.createBitmap(this.stampSize[0], this.stampSize[1], this.chunks[0][0].getConfig());
            Canvas canvas = new Canvas(result);
            for(int i=0; i<this.chunks.length; i++){
                for(int j=0; j<this.chunks[0].length; j++){
                    canvas.drawBitmap(this.chunks[i][j], i*CHUNK_SIZE_X, j*CHUNK_SIZE_Y, this.paint);
                    this.chunks[i][j].recycle();
                }
            }
            body.stamp(Bitmap.createScaledBitmap(
                    result,
                    (int) Math.round(result.getWidth() * rate),
                    (int) Math.round(result.getHeight() * rate),
                    false
            ));
        }
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
