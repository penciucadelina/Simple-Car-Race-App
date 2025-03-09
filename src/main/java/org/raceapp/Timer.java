package org.raceapp;

public class Timer extends Thread {
    private long duration;
    private boolean running;

    public Timer() {
        this.duration = 0;
        this.running = false;
    }

    public synchronized void startCount() {
        running = true;
        start();
    }

    public synchronized void stopCount() {
        running = false;
    }

    @Override
    public void run() {
        long startTime = System.nanoTime();
        while (isRunning()) {
            try {
                Thread.sleep(10);
                updateDuration(System.nanoTime() - startTime);
                startTime = System.nanoTime();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        }
    }

    private synchronized boolean isRunning() {
        return running;
    }

    private synchronized void updateDuration(long elapsedNanos) {
        duration += elapsedNanos / 1_000_000;
    }

    public synchronized long getDuration() {
        return duration;
    }
}