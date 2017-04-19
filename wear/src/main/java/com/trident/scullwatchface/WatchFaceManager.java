package com.trident.scullwatchface;

public class WatchFaceManager {

    private int bg_index;
    private int scull_index;
    private int clock_index;

    private boolean watchFaceUpdated;

    public boolean isWatchFaceUpdated() { return this.watchFaceUpdated; }
    public void setWatchFaceUpdated(boolean watchFaceUpdated) {
        this.watchFaceUpdated = watchFaceUpdated;
    }

    public void setBackground (int bg_index) { this.bg_index = bg_index; }
    public void setScull(int scull_index) { this.scull_index = scull_index; }
    public void setClockFace(int clock_index) { this.clock_index = clock_index; }


    public int getBackground() { return this.bg_index; }
    public int getScull() { return this.scull_index; }
    public int getClockFace() { return this.clock_index; }
}
