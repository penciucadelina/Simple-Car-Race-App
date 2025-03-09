package org.raceapp;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

class CarPanel extends JPanel {
    private static final int NUM_CARS = 4;
    private static final int CAR_SIZE = 30;
    private static final int INITIAL_Y_POS = 50;
    private static final int Y_POS_INCREMENT = 80;
    private static final int FINISH_LINE_DISTANCE = 400;

    private final int[] carPositions;
    private final String[] carNames;
    private final Color[] carColors;
    private final int[] carOrder;
    private final AtomicInteger index;

    private static final Logger logger = Logger.getLogger(CarPanel.class.getName());

    public CarPanel() {
        carPositions = new int[NUM_CARS];
        carNames = new String[]{"Red car", "Blue car", "Green car", "Yellow car"};
        carColors = new Color[]{Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW};
        carOrder = new int[NUM_CARS];
        index = new AtomicInteger(0);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int i = 0; i < NUM_CARS; i++) {
            int yPos = INITIAL_Y_POS + i * Y_POS_INCREMENT;
            int xPos = carPositions[i];

            g.setColor(carColors[i]);
            g.fillOval(xPos, yPos, CAR_SIZE, CAR_SIZE);
            g.setColor(Color.BLACK);
            g.drawString(carNames[i], xPos, yPos - 5);
        }
    }

    public void updateCarPosition(String carName, int distance) {
        int carIndex = getCarIndex(carName);
        if (carIndex != -1) {
            carPositions[carIndex] = distance;
            SwingUtilities.invokeLater(this::repaint);
        }
    }

    private int getCarIndex(String carName) {
        for (int i = 0; i < NUM_CARS; i++) {
            if (carNames[i].equals(carName)) {
                return i;
            }
        }
        return -1;
    }

    public void carFinished(String carName) {
        int pos = index.getAndIncrement();
        carOrder[pos] = getCarIndex(carName);
        final String[] positions = {"1st", "2nd", "3rd", "4th"};
        updateCarPosition(carName, FINISH_LINE_DISTANCE);
        logger.info(String.format("%s finished the race in %s place", carName, positions[pos]));
    }
}