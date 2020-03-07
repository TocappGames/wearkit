package com.tocapp.gamesdk.engine;


import android.view.View;

public class GameRunner extends Thread {

    private View view;
    private int fps;

    public GameRunner(View view, int fps) {
        this.view = view;
        this.fps = fps;
    }

    public void run(){
        while (true) {
            this.view.invalidate();
            try {
                Thread.sleep( 1000 / this.fps);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
