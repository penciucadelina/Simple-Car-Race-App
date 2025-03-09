package org.raceapp;

import javax.swing.*;
import java.awt.*;

public class CarRace {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame mainFrame = new JFrame("Car Race Championship");
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainFrame.setSize(1200, 700);
            mainFrame.setLayout(new BorderLayout());
            mainFrame.setLocationRelativeTo(null);

            // Header Panel
            JPanel headerPanel = new JPanel(new GridLayout(2, 1));
            JLabel titleLabel = new JLabel("Welcome to the Car Race Championship", SwingConstants.CENTER);
            titleLabel.setFont(new Font("Verdana", Font.BOLD, 32));
            titleLabel.setForeground(Color.BLUE);
            headerPanel.add(titleLabel);

            JLabel subtitleLabel = new JLabel("Let the race begin!", SwingConstants.CENTER);
            subtitleLabel.setFont(new Font("Verdana", Font.ITALIC, 20));
            subtitleLabel.setForeground(Color.DARK_GRAY);
            headerPanel.add(subtitleLabel);

            mainFrame.add(headerPanel, BorderLayout.NORTH);

            // Control Panel
            JPanel controlPanel = new JPanel();
            JButton startButton = new JButton("Go!");
            startButton.setFont(new Font("Verdana", Font.BOLD, 18));
            startButton.setBackground(Color.GREEN);
            startButton.setForeground(Color.WHITE);
            controlPanel.add(startButton);
            mainFrame.add(controlPanel, BorderLayout.SOUTH);

            // Main Container Panel
            JPanel containerPanel = new JPanel(new GridLayout(1, 2, 10, 10));
            containerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            mainFrame.add(containerPanel, BorderLayout.CENTER);

            // Left Panel (Race Track and Semaphore)
            JPanel leftPanel = new JPanel(new BorderLayout());
            containerPanel.add(leftPanel);

            JPanel raceTrackPanel = new JPanel(new BorderLayout());
            JLabel raceTrackLabel = new JLabel("Race Track", SwingConstants.CENTER);
            raceTrackLabel.setFont(new Font("Verdana", Font.BOLD, 22));
            raceTrackPanel.add(raceTrackLabel, BorderLayout.NORTH);

            CarPanel carPanel = new CarPanel();
            raceTrackPanel.add(carPanel, BorderLayout.CENTER);

            JPanel semaphorePanelWrapper = new JPanel(new BorderLayout());
            JLabel semaphoreLabel = new JLabel("Race Signal", SwingConstants.CENTER);
            semaphoreLabel.setFont(new Font("Verdana", Font.BOLD, 22));
            semaphorePanelWrapper.add(semaphoreLabel, BorderLayout.NORTH);

            SemaphorePanel semaphorePanel = new SemaphorePanel();
            semaphorePanelWrapper.add(semaphorePanel, BorderLayout.CENTER);

            leftPanel.add(raceTrackPanel, BorderLayout.CENTER);
            leftPanel.add(semaphorePanelWrapper, BorderLayout.EAST);

            // Right Panel (Race Results)
            JPanel rightPanel = new JPanel(new BorderLayout());
            containerPanel.add(rightPanel);

            JLabel resultsLabel = new JLabel("Results", SwingConstants.CENTER);
            resultsLabel.setFont(new Font("Verdana", Font.BOLD, 22));
            rightPanel.add(resultsLabel, BorderLayout.NORTH);

            JTextArea resultsText = new JTextArea();
            resultsText.setEditable(false);
            resultsText.setFont(new Font("Monospaced", Font.PLAIN, 14));
            JScrollPane resultsScrollPane = new JScrollPane(resultsText);
            rightPanel.add(resultsScrollPane, BorderLayout.CENTER);

            // Start Button Action
            startButton.addActionListener(e -> new Thread(() -> startRace(semaphorePanel, carPanel, resultsText)).start());

            mainFrame.setVisible(true);
        });
    }

    private static void startRace(SemaphorePanel semaphorePanel, CarPanel carPanel, JTextArea resultsText) {
        Timer timer = new Timer();
        timer.startCount();

        SemaphoreThread semaphoreThread = new SemaphoreThread(semaphorePanel);
        semaphoreThread.start();

        PlaySound ps = new PlaySound();

        Car car1 = new Car("Red car", carPanel);
        Car car2 = new Car("Blue car", carPanel);
        Car car3 = new Car("Green car", carPanel);
        Car car4 = new Car("Yellow car", carPanel);

        try {
            semaphoreThread.join();
            ps.playSound();

            car1.start();
            car2.start();
            car3.start();
            car4.start();

            car1.join();
            car2.join();
            car3.join();
            car4.join();

            ps.stopSound();
            timer.stopCount();

            showResults(resultsText, timer.getDuration(), car1, car2, car3, car4);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    private static void showResults(JTextArea resultsText, long raceDuration, Car... cars) {
        StringBuilder results = new StringBuilder("Race Results:\n");
        results.append("Total race duration: ").append(raceDuration).append(" ms\n\n");

        for (Car car : cars) {
            results.append(car.getName()).append(": ").append(car.getRaceDuration()).append(" ms\n");
        }

        resultsText.setText(results.toString());
    }
}