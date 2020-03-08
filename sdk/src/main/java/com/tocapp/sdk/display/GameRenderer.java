package com.tocapp.sdk.display;


import android.view.View;

public class GameRenderer extends Thread {

    private View view;
    private int fps;

    public GameRenderer(View view, int fps) {
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
