package com.tocapp.touchround;

public class Config {
    double widthCm;
    double heightCm;
    int level;
    boolean sound;
    boolean displayIsRound;
    int backgroundImage;
    int ballColor;
    int sticksColor;
    int boxColor;
    int goalsColor;


    public Config(double widthCm, double heightCm, int level, boolean sound, boolean displayIsRound, int backgroundImage, int ballColor, int sticksColor, int boxColor, int goalsColor) {
        this.widthCm = widthCm;
        this.heightCm = heightCm;
        this.level = level;
        this.sound = sound;
        this.displayIsRound = displayIsRound;
        this.backgroundImage = backgroundImage;
        this.ballColor = ballColor;
        this.sticksColor = sticksColor;
        this.boxColor = boxColor;
        this.goalsColor = goalsColor;
    }

    public Config(int ballColor, int sticksColor, int boxColor, int goalsColor) {
        this.ballColor = ballColor;
        this.sticksColor = sticksColor;
        this.boxColor = boxColor;
        this.goalsColor = goalsColor;
    }



    public double getWidthCm() {
        return widthCm;
    }

    public double getHeightCm() {
        return heightCm;
    }

    public int getLevel() {
        return level;
    }

    public boolean isSound() {
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

    public void setWidthCm(double widthCm) {
        this.widthCm = widthCm;
    }

    public void setHeightCm(double heightCm) {
        this.heightCm = heightCm;
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

    public void setBackgroundImage(int backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public void setBallColor(int ballColor) {
        this.ballColor = ballColor;
    }

    public void setSticksColor(int sticksColor) {
        this.sticksColor = sticksColor;
    }

    public void setBoxColor(int boxColor) {
        this.boxColor = boxColor;
    }

    public void setGoalsColor(int goalsColor) {
        this.goalsColor = goalsColor;
    }

}
