package de.merlin.utils;

public class Timer {

    private long startTime;
    private long stopTime;
    private long pauseTime;
    private boolean isPaused;
    private boolean isRunning;

    public Timer() {
        this.startTime = 0;
        this.stopTime = 0;
        this.pauseTime = 0;
        this.isPaused = false;
        this.isRunning = false;
    }

    public void start() {
        this.startTime = System.currentTimeMillis();
        this.isRunning = true;
    }

    public void stop() {
        this.stopTime = System.currentTimeMillis();
        this.isRunning = false;
    }

    public void pause() {
        if (this.isRunning && !this.isPaused) {
            this.pauseTime = System.currentTimeMillis();
            this.isPaused = true;
        }
    }

    public void resume() {
        if (this.isRunning && this.isPaused) {
            long pausedDuration = System.currentTimeMillis() - this.pauseTime;
            this.startTime += pausedDuration;
            this.isPaused = false;
        }
    }

    public long getElapsedTime() {
        if (this.isRunning) {
            if (this.isPaused) {
                return this.pauseTime - this.startTime;
            } else {
                return System.currentTimeMillis() - this.startTime;
            }
        } else {
            return this.stopTime - this.startTime;
        }
    }

    public long getElapsedTimeSecs() {
        return this.getElapsedTime() / 1000;
    }

    public long getElapsedTimeMins() {
        return this.getElapsedTimeSecs() / 60;
    }

    public long getElapsedTimeHours() {
        return this.getElapsedTimeMins() / 60;
    }

    public long getElapsedTimeDays() {
        return this.getElapsedTimeHours() / 24;
    }

    public long getElapsedTimeWeeks() {
        return this.getElapsedTimeDays() / 7;
    }

    public long getElapsedTimeMonths() {
        return this.getElapsedTimeDays() / 30;
    }

    public long getElapsedTimeYears() {
        return this.getElapsedTimeDays() / 365;
    }

    public boolean isPaused() {
        return this.isPaused;
    }

    public boolean isRunning() {
        return this.isRunning;
    }

    public long getStartTime() {
        return this.startTime;
    }

    public long getStopTime() {
        return this.stopTime;
    }

    public long getPauseTime() {
        return this.pauseTime;
    }

    public String getTimeString() {
        long elapsedTimeSecs = this.getElapsedTimeSecs();

        long hours = elapsedTimeSecs / 3600;
        long minutes = (elapsedTimeSecs % 3600) / 60;
        long seconds = elapsedTimeSecs % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
