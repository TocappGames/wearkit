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
    private int backgroundImage;
    private Bitmap backgroundImageBmp;
    private Bitmap backgroundImageBmpScaled;
    private int boxHeight;
    private int sidesMargin;
    private int scale;
    private Context context;

    public DrawUtils(int mobileWidth, int mobileHeight, double boxHeight, double sidesMargin, Context context, double scale) {
        this.mobileWidth = mobileWidth;
        this.mobileHeight = mobileHeight;
        this.backgroundImage = backgroundImage;
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
                    mobileWidth - ( (sidesMargin * scale) * 2 -  (boxHeight * scale)),
                    mobileHeight - ( (sidesMargin * scale) * 2 -  (boxHeight * scale)),
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
        landscape.add(new Renderable() {
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
                paint2.setTextSize( 5 / (int) scale );
                canvas.drawText( text, mobileWidth / 8, mobileHeight / 8, paint2 );

            }

            @Override
            public void render(Canvas canvas, Paint paint, double scale) {

            }
        });
    }

            public void drawPuncuation(List<Renderable> landscape, final Integer userGoals, final Integer iaGoals) {
                landscape.add( new Renderable() {
                    @Override
                    public void render(Canvas canvas, double scale) {
                        // Render punctuation
                        Paint paint = new Paint();
                        paint.setColor( Color.WHITE );
                        paint.setTextSize( 2000 / (float) scale );
                        canvas.drawText( userGoals.toString(), mobileWidth / 8, mobileHeight / 2 + 4000 / (float) scale, paint );
                        canvas.drawText( iaGoals.toString(), mobileWidth / 8, (float) mobileHeight / 2 - 4000 / (float) scale, paint );
                    }

                    @Override
                    public void render(Canvas canvas, Paint paint, double scale) {

                    }
                } );
            }
            public void drawGoals(List<Renderable> landscape, final boolean userScores, final boolean iaScores, final boolean iaWin, final boolean userWin, final double goalTime) {
        landscape.add(new Renderable() {
            @Override
            public void render(Canvas canvas, double scale) {
                Paint paint = new Paint();
                paint.setColor(Color.WHITE);
                paint.setTextSize(2000 / (float) scale);

                // Render goal text
                if (userScores) {
                    if (System.currentTimeMillis() - goalTime < 1000) {
                        paint.setTextSize(1800 / (float) scale);
                        canvas.drawText("USER GOAL", mobileWidth / 3 + 1000 / (int) scale, mobileHeight / 2 + 3000 / (int) scale, paint);
                    }
                }

                if (iaScores) {
                    if (System.currentTimeMillis() - goalTime < 1000) {
                        paint.setTextSize(1800 / (float) scale);
                        canvas.drawText("MACHINE GOAL", mobileWidth / 3 + 1000 / (int) scale, mobileHeight / 2 - 3000 / (int) scale, paint);
                    }
                }

                if (userWin) {
                    paint.setTextSize(1800 / (float) scale);
                    canvas.drawText("User win!", mobileWidth / 3 + 1000 / (int) scale, mobileHeight / 2 - 3000 / (int) scale, paint);
                }

                if (iaWin) {
                    paint.setTextSize(1800 / (float) scale);
                    canvas.drawText("Ia Win!", mobileWidth / 3 + 1000 / (int) scale, mobileHeight / 2 - 3000 / (int) scale, paint);
                }

            }

            @Override
            public void render(Canvas canvas, Paint paint, double scale) {

            }

        });
    }
}
