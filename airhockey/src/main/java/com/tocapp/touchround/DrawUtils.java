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
    final private int MOBILE_WIDTH;
    final private int MOBILE_HEIGHT;
    private Bitmap backgroundImageBmp;
    private Bitmap backgroundImageBmpScaled;
    private final int BOX_HEIGHT;
    private final int SIDES_MARGIN;
    private final int SCALE;
    private final Context CONTEXT;
    private final int CENTER_WIDTH;
    private final int CENTER_HEIGHT;

    public DrawUtils(int mobileWidth, int mobileHeight, double boxHeight, double sidesMargin, Context context, double scale) {
        this.MOBILE_WIDTH = mobileWidth;
        this.MOBILE_HEIGHT = mobileHeight;
        this.CENTER_WIDTH = mobileWidth / 2;
        this.CENTER_HEIGHT = mobileHeight / 2;
        this.BOX_HEIGHT = (int) boxHeight;
        this.SIDES_MARGIN = (int) sidesMargin;
        this.CONTEXT = context;
        this.SCALE = (int) scale;

    }

    public void drawBackground(List<Renderable> background, int backgroundImage) {
        backgroundImageBmp = null;
        if (backgroundImage != 0) {
            backgroundImageBmp = BitmapFactory.decodeResource(
                    CONTEXT.getResources(),
                    backgroundImage );

            backgroundImageBmpScaled = Bitmap.createScaledBitmap(
                    backgroundImageBmp,
                    MOBILE_WIDTH - ((SIDES_MARGIN * SCALE) * 2 - (BOX_HEIGHT * SCALE)),
                    MOBILE_HEIGHT - ((SIDES_MARGIN * SCALE) * 2 - (BOX_HEIGHT * SCALE)),
                    false );

        }
        background.add( new Renderable() {
            @Override
            public void render(Canvas canvas, double scale) {
                if (backgroundImageBmp != null) {
                    canvas.drawBitmap(
                            backgroundImageBmpScaled,
                            (int) (SIDES_MARGIN * scale) + (int) (BOX_HEIGHT * scale / 2),
                            (int) (SIDES_MARGIN * scale) + (int) (BOX_HEIGHT * scale / 2),
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
        final int textSize = MOBILE_WIDTH / 20;
        final int positionX = MOBILE_WIDTH / 8;
        final int positionY = MOBILE_HEIGHT / 8;
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
        final float CENTER_DISTANCE = MOBILE_HEIGHT / 8;
        final int POSITION_X = MOBILE_WIDTH / 8;
        final float TEXT_SIZE =MOBILE_HEIGHT / 20;
        final int POSITION_Y_USER = (int) (CENTER_HEIGHT + CENTER_DISTANCE + TEXT_SIZE);
        final int POSITION_Y_IA = (int) (CENTER_HEIGHT - CENTER_DISTANCE );

        landscape.add( new Renderable() {
            @Override
            public void render(Canvas canvas, double scale) {
                // Render punctuation
                Paint paint = new Paint();
                paint.setColor( Color.WHITE );
                paint.setTextSize( TEXT_SIZE );
                canvas.drawText( userGoals.toString(), POSITION_X, POSITION_Y_USER, paint );
                canvas.drawText( iaGoals.toString(), POSITION_X, POSITION_Y_IA, paint );
            }

            @Override
            public void render(Canvas canvas, Paint paint, double scale) {

            }
        } );
    }

    public void drawGoals(List<Renderable> landscape, final boolean userScores, final boolean iaScores, final boolean iaWin, final boolean userWin, final double goalTime) {
        final float TEXT_SIZE = MOBILE_HEIGHT / 18;
        final float POSITION_X = CENTER_WIDTH - TEXT_SIZE * 2;
        final float POSITION_Y = CENTER_HEIGHT;
        final int TIME_IN_SCREEN = 1000;
        landscape.add( new Renderable() {
            @Override
            public void render(Canvas canvas, double scale) {
                String text = "";
                Paint paint = new Paint();
                paint.setColor( Color.WHITE );
                paint.setTextSize( TEXT_SIZE );

                // Render goal text
                if (userScores) text = "User goal";
                if (iaScores) text = "Ia goal";
                if (userWin) text = "User win";
                if (iaWin) text = "Ia win";

                if (System.currentTimeMillis() - goalTime < TIME_IN_SCREEN) {
                    canvas.drawText( text, POSITION_X, POSITION_Y, paint );
                }

            }

            @Override
            public void render(Canvas canvas, Paint paint, double scale) {

            }

        } );
    }
}
