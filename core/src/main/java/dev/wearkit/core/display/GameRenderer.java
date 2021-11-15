package dev.wearkit.core.display;


import android.app.Activity;
import android.view.View;

public class GameRenderer extends Thread {

    public static int DEFAULT_FPS = 60;

    private View view;
    private Activity activity;
    private int fps;

    public GameRenderer(View view, Activity activity) {
        this(view, activity, DEFAULT_FPS);
    }

    public GameRenderer(View view, Activity activity, int fps) {
        this.view = view;
        this.activity = activity;
        this.fps = fps;
    }

    public void run(){
        while (true) {
            this.activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    GameRenderer.this.view.invalidate();
                }
            });
            try {
                Thread.sleep( 1000 / this.fps);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
