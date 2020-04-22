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
    private static final int CHUNK_SIZE_X = 128;
    private static final int CHUNK_SIZE_Y = 128;

    protected Paint paint;
    private String text;
    private double textX;
    private double textY;
    private Paint textPaint;
    private Double textAngle;
    protected Bitmap[][] chunks;
    private int[] stampSize;

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

        if (this.chunks != null) {
            float cx  = this.stampSize[0] / 2.0f;
            float cy = this.stampSize[1] / 2.0f;

            // If bitmap is smaller than the screen, draw all chunks
            if(this.stampSize[0] <= canvas.getWidth() && this.stampSize[0] <= canvas.getWidth()){
                for(int i=0; i<this.chunks.length; i++){
                    for(int j=0; j<this.chunks[0].length; j++){
                        float xPos = i * (float) CHUNK_SIZE_X - cx;
                        float yPos = j * (float) CHUNK_SIZE_Y - cy;
                        canvas.drawBitmap(this.chunks[i][j], xPos, yPos, this.paint);
                    }
                }
            }
            // If bitmap is bigger than the screen, draw only the required chunks
            else {
                Rect bounds = canvas.getClipBounds();
                int leftChunkId = (int) ((float) bounds.left / this.stampSize[0] * this.chunks.length) + this.chunks.length / 2;
                int topChunkId = (int) ((float) bounds.top / this.stampSize[1] * this.chunks[0].length) + this.chunks[0].length / 2;
                int rightChunkId = leftChunkId + canvas.getWidth() / CHUNK_SIZE_X;
                int bottomChunkId = topChunkId + canvas.getHeight() / CHUNK_SIZE_Y;

                int leftStart = Math.max(leftChunkId - 1, 0);
                int topStart = Math.max(topChunkId - 1, 0);
                int rightEnd = Math.min(rightChunkId + 2, this.chunks.length);
                int bottomEnd = Math.min(bottomChunkId + 2, this.chunks[0].length);

                for(int i=leftStart; i<rightEnd; i++){
                    for(int j=topStart; j<bottomEnd; j++){
                        float xPos = i * (float) CHUNK_SIZE_X - cx;
                        float yPos = j * (float) CHUNK_SIZE_Y - cy;
                        canvas.drawBitmap(this.chunks[i][j], xPos, yPos, this.paint);
                    }
                }
            }
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
        if(bitmap != null){
            int xChunks = bitmap.getWidth() / CHUNK_SIZE_X;
            int xRest = bitmap.getWidth() % CHUNK_SIZE_X;
            int yChunks = bitmap.getHeight() / CHUNK_SIZE_Y;
            int yRest = bitmap.getHeight() % CHUNK_SIZE_Y;

            int xSize = (int) (xChunks + Math.signum(xRest));
            int ySize = (int) (yChunks + Math.signum(yRest));
            this.chunks = new Bitmap[xSize][ySize];
            for(int i=0; i<xSize; i++){
                for(int j=0; j<ySize; j++){
                    this.chunks[i][j] = Bitmap.createBitmap(
                            bitmap,
                            i * CHUNK_SIZE_X,
                            j * CHUNK_SIZE_Y,
                            i == xChunks? xRest: CHUNK_SIZE_X,
                            j == yChunks? yRest: CHUNK_SIZE_Y
                    );
                }
            }
            this.stampSize = new int[]{ bitmap.getWidth(), bitmap.getHeight()};
        }

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
    public Body scale(double rate) {
        Body body = new Body();
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
