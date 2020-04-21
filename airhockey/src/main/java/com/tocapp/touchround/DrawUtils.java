package com.tocapp.touchround;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;

import dev.wearkit.core.engine.World;
import dev.wearkit.core.rendering.Ornament;
import dev.wearkit.core.rendering.shape.Rectangle;

public class DrawUtils {
    final private int MOBILE_WIDTH;
    final private int MOBILE_HEIGHT;
    private Bitmap backgroundImageBmp;
    private Bitmap backgroundImageBmpScaled;
    private final int BOX_HEIGHT;
    private final int SIDES_MARGIN;
    private final Context CONTEXT;
    private final int CENTER_WIDTH;
    private final int CENTER_HEIGHT;
    private final World WORLD;

    private Ornament goalsText;
    private Ornament userPunctuation;
    private Ornament iaPunctuation;
    private Ornament difficulty;
    private Paint goalsTextPaint = new Paint(  );
    private Paint userPuctuationPaint = new Paint(  );
    private Paint iaPunctuationPaint = new Paint();
    private Paint difficultyPaint = new Paint(  );

    private boolean isDynamicTextWrited = false;

    public DrawUtils(double mobileWidth, double mobileHeight, double boxHeight, double sidesMargin, Context context, World world) {
        this.MOBILE_WIDTH = (int) mobileWidth;
        this.MOBILE_HEIGHT = (int) mobileHeight;
        this.CENTER_WIDTH = (int) mobileWidth / 2;
        this.CENTER_HEIGHT = (int) mobileHeight / 2;
        this.BOX_HEIGHT = (int) boxHeight;
        this.SIDES_MARGIN = (int) sidesMargin;
        this.CONTEXT = context;
        this.WORLD = world;

        goalsText = new Ornament();
        userPunctuation = new Ornament();
        iaPunctuation = new Ornament();
        difficulty = new Ornament();
        goalsTextPaint.setColor( Color.WHITE );
        userPuctuationPaint.setColor( Color.WHITE );
        iaPunctuationPaint.setColor( Color.WHITE );
        difficultyPaint.setColor( Color.WHITE );
    }

    public void drawBackground(int backgroundImage) {
        backgroundImageBmp = null;
        if (backgroundImage != 0) {
            backgroundImageBmp = BitmapFactory.decodeResource(
                    CONTEXT.getResources(),
                    backgroundImage );

            backgroundImageBmpScaled = Bitmap.createScaledBitmap(
                    backgroundImageBmp,
                    MOBILE_WIDTH - ((SIDES_MARGIN) * 2 - (BOX_HEIGHT)),
                    MOBILE_HEIGHT - ((SIDES_MARGIN) * 2 - (BOX_HEIGHT)),
                    false );

        }
        drawImage( backgroundImageBmpScaled, WORLD.getSize().x / 2f, WORLD.getSize().y / 2f, MOBILE_WIDTH, MOBILE_HEIGHT, -10 );
    }

    public void drawImage(Bitmap background, double positionX, double positionY, int width, int height, int zIndex) {
        Paint paint = new Paint();
        Ornament backgroundOrnament = new Ornament();
        backgroundOrnament.paint( paint );
        backgroundOrnament.stamp( background );
        Rectangle rectangleBackgroundCircuit = new Rectangle( width, height );
        backgroundOrnament.addFixture( rectangleBackgroundCircuit );
        backgroundOrnament.translate(
                positionX,
                positionY );
        WORLD.addOrnament( backgroundOrnament );
    }

    public void initLabel(Ornament label, Paint paint, double positionX, double positionY, float textSize, int zIndex) {
        paint.setTextSize( textSize );
        label.paint( paint );
        label.translate(
                positionX,
                positionY );
        //label.print( text, 0, 0, paint );
        WORLD.addOrnament( label );
    }

    public void drawDifficulty(final int level) {
        final int textSize = MOBILE_WIDTH / 20;
        final int positionX = MOBILE_WIDTH / 8;
        final int positionY = MOBILE_HEIGHT / 8;
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
        initLabel(difficulty, difficultyPaint, positionX, positionY, textSize, -8 );
        updateLabel( difficulty, difficultyPaint, text );
    }

    public void initPunctuation() {
        final float CENTER_DISTANCE = MOBILE_HEIGHT / 8;
        final int POSITION_X = MOBILE_WIDTH / 8;
        final float TEXT_SIZE = MOBILE_HEIGHT / 8;
        final int POSITION_Y_USER = (int) (CENTER_HEIGHT + CENTER_DISTANCE + TEXT_SIZE);
        final int POSITION_Y_IA = (int) (CENTER_HEIGHT - CENTER_DISTANCE);
        initLabel(userPunctuation,userPuctuationPaint, POSITION_X, POSITION_Y_USER, TEXT_SIZE, -3 );
        initLabel(iaPunctuation,iaPunctuationPaint, POSITION_X, POSITION_Y_IA, TEXT_SIZE, -2 );

    }

    public void updateLabel(Ornament label, Paint paint, String text) {
        label.print(  text , 0,0 , paint ) ;
    }


    public void updatePunctuation(int userGoals, int iaGoals) {
        updateLabel( userPunctuation, userPuctuationPaint, Integer.toString( userGoals ) );
        updateLabel( iaPunctuation, iaPunctuationPaint, Integer.toString( iaGoals ) );
    }
    public void initGoalText() {
        final float TEXT_SIZE = MOBILE_HEIGHT / 18;
        final float POSITION_X = CENTER_WIDTH - TEXT_SIZE * 2;
        final float POSITION_Y;
        POSITION_Y = CENTER_HEIGHT;
        initLabel( goalsText, goalsTextPaint, POSITION_X, POSITION_Y, TEXT_SIZE, -1 );
    }

    public void showGoalText(final boolean userScores, final boolean iaScores, final boolean iaWin, final boolean userWin) {
        String text = "";
            // Render goal text
            if (userScores) text = "User goal";
            if (iaScores) text = "Ia goal";
            if (userWin) text = "User win";
            if (iaWin) text = "Ia win";
            System.out.println( text );
            updateLabel( goalsText, goalsTextPaint, text );
    }

    public void removeGoalText() {
        updateLabel( goalsText, goalsTextPaint, "" );
    }

}
