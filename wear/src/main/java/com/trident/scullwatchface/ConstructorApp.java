package com.trident.scullwatchface;

import android.app.Application;
import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

public class ConstructorApp extends Application {
    private WatchFaceManager watchFaceManager;

    private int displayWidth, displayHeight;

    @Override
    public void onCreate() {
        super.onCreate();
        watchFaceManager = new WatchFaceManager();

        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        displayWidth = size.x;
        displayHeight = size.y;
    }

    public WatchFaceManager getWatchFaceManager() { return this.watchFaceManager; }

    public int getDisplayWidth() { return this.displayWidth; }
    public int getDisplayHeight() { return this.displayHeight; }
}
