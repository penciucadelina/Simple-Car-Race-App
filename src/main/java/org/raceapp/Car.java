package org.raceapp;

import java.util.concurrent.ThreadLocalRandom;

class Car extends Thread {
    private final String name;
    private int distance = 0;
    private long raceDuration = 0;
    private final CarPanel carPanel;

    public Car(String name, CarPanel carPanel) {
        setName(name);
        this.name = name;
        this.carPanel = carPanel;
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        while (distance < 400) {
            int speed = ThreadLocalRandom.current().nextInt(1, 11);
            distance += speed;

            carPanel.updateCarPosition(name, distance);

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        }

        carPanel.carFinished(name);
        raceDuration = System.currentTimeMillis() - startTime;
        System.out.printf("%.2f sec%n", raceDuration / 1000.0f);
    }

    public int getDistance() {
        return distance;
    }

    public long getRaceDuration() {
        return raceDuration;
    }
}