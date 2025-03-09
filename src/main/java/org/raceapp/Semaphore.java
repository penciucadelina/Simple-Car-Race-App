package org.raceapp;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

class SemaphoreThread extends Thread {
    private final SemaphorePanel semaphorePanel;

    public SemaphoreThread(SemaphorePanel semaphorePanel) {
        this.semaphorePanel = semaphorePanel;
    }

    @Override
    public void run() {
        try {
            semaphorePanel.setGray();
            Thread.sleep(ThreadLocalRandom.current().nextInt(2000, 7000));

            semaphorePanel.setYellow();
            Thread.sleep(ThreadLocalRandom.current().nextInt(2000, 7000));

            semaphorePanel.setGreen();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }
}

class SemaphorePanel extends JPanel {
    private volatile Color color;

    public SemaphorePanel() {
        setPreferredSize(new Dimension(100, 300));
        color = Color.GRAY;
    }

    public void setGray() {
        updateColor(Color.GRAY);
    }

    public void setYellow() {
        updateColor(Color.YELLOW);
    }

    public void setGreen() {
        updateColor(Color.GREEN);
    }

    private void updateColor(Color newColor) {
        SwingUtilities.invokeLater(() -> {
            color = newColor;
            repaint();
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int diameter = 70;
        int x = (getWidth() - diameter) / 2;
        int y = (getHeight() - diameter) / 2;

        g.setColor(color);
        g.fillOval(x, y, diameter, diameter);

        g.setColor(Color.BLACK);
        g.drawOval(x, y, diameter, diameter);
    }
}