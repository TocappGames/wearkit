package com.tocapp.touchround;

import android.content.Context;

public class Config {

    private double area;
    private int level;
    private boolean sound;
    private boolean displayIsRound;
    private int backgroundImage;
    private int ballColor;
    private int sticksColor;
    private int boxColor;
    private int goalsColor;
    private boolean haveMap;
    private Context context;

    public Config() {
        haveMap = false;
        sound = true;
    }

    public void setMap0() {
        ballColor = R.color.white;
        sticksColor = R.color.blue;
        boxColor = R.color.white;
        goalsColor = R.color.red;
        backgroundImage = 0;
        haveMap = true;
    }

    public void setMap1() {
        ballColor = R.color.yellow;
        sticksColor = R.color.green;
        boxColor = R.color.purple;
        goalsColor = R.color.cyan;
        backgroundImage = R.drawable.fondo_2;
        haveMap = true;

    }

    public void setMap2() {
        ballColor = R.color.cyan;
        sticksColor = R.color.yellow;
        boxColor = R.color.purple;
        goalsColor = R.color.green;
        backgroundImage = R.drawable.fondo_3;
        haveMap = true;

    }

    public void setMap3() {
        ballColor = R.color.green;
        sticksColor = R.color.purple;
        boxColor = R.color.white;
        goalsColor = R.color.red;
        backgroundImage = R.drawable.fondo_4;
        haveMap = true;

    }

    public void setMap4() {
        ballColor = R.color.black;
        sticksColor = R.color.green;
        boxColor = R.color.yellow;
        goalsColor = R.color.purple;
        backgroundImage = R.drawable.fondo_5;
        haveMap = true;

    }

    public void setArea(double area){
        this.area = area;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

   public double getArea() {
        return this.area;
   }


    public int getLevel() {
        return level;
    }

    public boolean haveSound() {
        return sound;
    }

    public boolean isDisplayIsRound() {
        return displayIsRound;
    }

    public int getBackgroundImage() {
        return backgroundImage;
    }

    public int getBallColor() {
        return ballColor;
    }

    public int getSticksColor() {
        return sticksColor;
    }

    public int getBoxColor() {
        return boxColor;
    }

    public int getGoalsColor() {
        return goalsColor;
    }

    public boolean haveMap() {
        return haveMap;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setSound(boolean sound) {
        this.sound = sound;
    }

    public void setDisplayIsRound(boolean displayIsRound) {
        this.displayIsRound = displayIsRound;
    }


}
