package com.tocapp.touchround;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.tocapp.sdk.rendering.Renderable;

import java.util.List;

public class DrawUtils {
    int mobileWidth;
    int mobileHeight;
    private Bitmap backgroundImageBmp;
    private Bitmap backgroundImageBmpScaled;
    private int boxHeight;
    private int sidesMargin;
    private int scale;
    private Context context;
    private int centerWidth = mobileWidth / 2;
    private int centerHeight = mobileHeight / 2;

    public DrawUtils(int mobileWidth, int mobileHeight, double boxHeight, double sidesMargin, Context context, double scale) {
        this.mobileWidth = mobileWidth;
        this.mobileHeight = mobileHeight;

        this.boxHeight = (int) boxHeight;
        this.sidesMargin = (int) sidesMargin;
        this.context = context;
        this.scale = (int) scale;

    }

    public void drawBackground(List<Renderable> background, int backgroundImage) {
        backgroundImageBmp = null;
        if (backgroundImage != 0) {
            backgroundImageBmp = BitmapFactory.decodeResource(
                    context.getResources(),
                    backgroundImage );

            backgroundImageBmpScaled = Bitmap.createScaledBitmap(
                    backgroundImageBmp,
                    mobileWidth - ((sidesMargin * scale) * 2 - (boxHeight * scale)),
                    mobileHeight - ((sidesMargin * scale) * 2 - (boxHeight * scale)),
                    false );

        }
        background.add( new Renderable() {
            @Override
            public void render(Canvas canvas, double scale) {
                if (backgroundImageBmp != null) {
                    canvas.drawBitmap(
                            backgroundImageBmpScaled,
                            (int) (sidesMargin * scale) + (int) (boxHeight * scale / 2),
                            (int) (sidesMargin * scale) + (int) (boxHeight * scale / 2),
                            null
                    );
                }
            }

            @Override
            public void render(Canvas canvas, Paint paint, double scale) {

            }
        } );
    }

    public void drawDifficulty(List<Renderable> landscape, final int level) {
        final int textSize = mobileWidth / 20;
        final int positionX = mobileWidth / 8;
        final int positionY = mobileHeight / 8;
        landscape.add( new Renderable() {
            @Override
            public void render(Canvas canvas, double scale) {
                // Render difficulty on screen
                String text = "";
                switch (level) {
                    case 1:
                        text = "Easy";
                        break;
                    case 2:
                        text = "Medium";
                        break;
                    case 3:
                        text = "Hard";
                        break;
                }

                Paint paint2 = new Paint();
                paint2.setColor( Color.WHITE );
                paint2.setTextSize( textSize );
                canvas.drawText( text, positionX, positionY, paint2 );

            }

            @Override
            public void render(Canvas canvas, Paint paint, double scale) {

            }
        } );
    }

    public void drawPuncuation(List<Renderable> landscape, final Integer userGoals, final Integer iaGoals) {
        final float centerDistance = 4000;
        final int positionX = mobileWidth / 8;
        final int positionY = (int) (centerHeight + centerDistance / scale);
        landscape.add( new Renderable() {
            @Override
            public void render(Canvas canvas, double scale) {
                // Render punctuation
                Paint paint = new Paint();
                paint.setColor( Color.WHITE );
                paint.setTextSize( 2000 / (float) scale );
                canvas.drawText( userGoals.toString(), positionX, positionY, paint );
                canvas.drawText( iaGoals.toString(), positionX, positionY, paint );
            }

            @Override
            public void render(Canvas canvas, Paint paint, double scale) {

            }
        } );
    }

    public void drawGoals(List<Renderable> landscape, final boolean userScores, final boolean iaScores, final boolean iaWin, final boolean userWin, final double goalTime) {
        final float centerDistance = 3000;
        final float textSize = 1800 / (float) scale;
        final float xPosition = 80 / scale;
        final float yPosition = centerHeight -centerDistance / scale;
        landscape.add( new Renderable() {
            @Override
            public void render(Canvas canvas, double scale) {
                String text = "";
                Paint paint = new Paint();
                paint.setColor( Color.WHITE );
                paint.setTextSize( textSize );

                // Render goal text
                if (userScores) text = "User goal";
                if (iaScores) text = "Ia goal";
                if (userWin) text = "User win";
                if (iaWin) text = "Ia win";

                if (System.currentTimeMillis() - goalTime < 1000) {
                    canvas.drawText( text, xPosition, yPosition, paint );
                }

            }

            @Override
            public void render(Canvas canvas, Paint paint, double scale) {

            }

        } );
    }
}
