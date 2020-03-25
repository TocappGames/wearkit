package com.tocapp.dyn4jtest;

public class Config {
    int level;
    int ballColor;
    int sticksColor;
    int boxColor;
    int goalsColor;


    public Config(int level, int ballColor, int sticksColor, int boxColor, int goalsColor) {
        this.level = level;
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



    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getBallColor() {
        return ballColor;
    }

    public void setBallColor(int ballColor) {
        this.ballColor = ballColor;
    }

    public int getSticksColor() {
        return sticksColor;
    }

    public void setSticksColor(int sticksColor) {
        this.sticksColor = sticksColor;
    }

    public int getBoxColor() {
        return boxColor;
    }

    public void setBoxColor(int boxColor) {
        this.boxColor = boxColor;
    }

    public int getGoalsColor() {
        return goalsColor;
    }

    public void setGoalsColor(int goalsColor) {
        this.goalsColor = goalsColor;
    }

}
