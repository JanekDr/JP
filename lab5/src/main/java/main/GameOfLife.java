package main;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.CyclicBarrier;

public class GameOfLife extends JPanel {
    private final int rows;
    private final int cols;
    private final boolean[][] initialGrid;
    private volatile boolean[][] grid;
    private volatile boolean[][] nextGrid;
    private boolean running = false;
    private int fps = 100;
    private final int FPS_MAX = 200;
    private JFrame frame;
    private CyclicBarrier barrier;
    private int[][] threadAssignments;
    private final Color[] threadColors = {
            Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.CYAN, Color.MAGENTA, Color.ORANGE, Color.PINK
    };

    public GameOfLife(int rows, int cols, boolean[][] initialGrid) {
        this.rows = rows;
        this.cols = cols;
        this.grid = initialGrid;
        this.initialGrid = initialGrid.clone();
        this.nextGrid = new boolean[rows][cols];
        this.threadAssignments = new int[rows][cols];
        setPreferredSize(new Dimension(cols * 10, rows * 10));
        initUI();
    }

    public void initUI() {
        frame = new JFrame("Game of Life");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        frame.add(this, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();
        controlPanel.setPreferredSize(new Dimension(200, 300));
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));

        JLabel statusLabel = new JLabel("Status: Stopped");
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton startStopButton = new JButton("Start/Stop");
        startStopButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startStopButton.addActionListener(e -> {
            toggleRunning();
            statusLabel.setText(isRunning() ? "Status: Running" : "Status: Stopped");
        });

        JLabel sliderLabel = new JLabel("Speed: " + fps);
        sliderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JSlider slider = new JSlider(0, FPS_MAX, fps);
        slider.setAlignmentX(Component.CENTER_ALIGNMENT);
        slider.setMajorTickSpacing(50);
        slider.setPaintTicks(true);
        slider.addChangeListener(e -> {
            fps = slider.getValue();
            String fpsText = String.valueOf(FPS_MAX-fps);
            sliderLabel.setText("Speed: " + fpsText);
        });

        controlPanel.add(Box.createVerticalStrut(20));
        controlPanel.add(statusLabel);
        controlPanel.add(Box.createVerticalStrut(20));
        controlPanel.add(startStopButton);
        controlPanel.add(Box.createVerticalStrut(20));
        controlPanel.add(sliderLabel);
        controlPanel.add(slider);

        frame.add(controlPanel, BorderLayout.EAST);
        frame.pack();
        frame.setVisible(true);
    }

    public void runGame(int iterations, int numberOfThreads) {
        barrier = new CyclicBarrier(numberOfThreads, this::applyNextGrid);
        int columnsPerThread = cols / numberOfThreads;
        int remainder = cols % numberOfThreads;

        for (int i = 0; i < numberOfThreads; i++) {
            int startCol = i * columnsPerThread;
            int endCol = startCol + columnsPerThread - 1;

            if (i == numberOfThreads - 1) {
                endCol += remainder;
            }

            for (int row = 0; row < rows; row++) {
                for (int col = startCol; col <= endCol; col++) {
                    threadAssignments[row][col] = i;
                }
            }

            new Thread(new Worker(i, startCol, endCol, iterations)).start();
        }
    }

    private synchronized void applyNextGrid() {
        for (int row = 0; row < rows; row++) {
            System.arraycopy(nextGrid[row], 0, grid[row], 0, cols);
        }
        System.out.println("Synchronizacja watkow i mapy sie zakonczyla");
        repaint();
    }


    private class Worker implements Runnable {
        private int threadID;
        private final int startCol;
        private final int endCol;
        private final int iterations;

        public Worker(int threadID, int startCol, int endCol, int iterations) {
            this.threadID = threadID;
            this.startCol = startCol;
            this.endCol = endCol;
            this.iterations = iterations;
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < iterations; i++) {
                    if (!running) {
                        Thread.sleep(fps);
                        i--;
                        continue;
                    }
                    processPartition();
                    System.out.println("watek: "+threadID+" czeka na synchronizacje.");
                    barrier.await(); // Synchronizacja wątków
                    Thread.sleep(fps);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void processPartition() {
            for (int row = 0; row < rows; row++) {
                for (int col = startCol; col <= endCol; col++) {
                    int liveNeighbors = countLiveNeighbors(row, col);
                    synchronized (GameOfLife.this) {
                        nextGrid[row][col] = (grid[row][col] && (liveNeighbors == 2 || liveNeighbors == 3))
                                || (!grid[row][col] && liveNeighbors == 3);
                    }
                }
            }
        }

        private int countLiveNeighbors(int row, int col) {
            int count = 0;
            for (int dr = -1; dr <= 1; dr++) {
                for (int dc = -1; dc <= 1; dc++) {
                    if (dr == 0 && dc == 0) continue;
                    int newRow = (row + dr + rows) % rows;
                    int newCol = (col + dc + cols) % cols;
                    synchronized (GameOfLife.this) {
                        if (grid[newRow][newCol]) {
                            count++;
                        }
                    }
                }
            }
            return count;
        }
    }

    public synchronized void toggleRunning() {
        running = !running;
    }

    public synchronized boolean isRunning() {
        return running;
    }

    @Override
    protected synchronized void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (grid[row][col]) {
                    g.setColor(Color.WHITE);
                    g.fillRect(col * 10, row * 10, 10, 10);
                }
                g.setColor(threadColors[threadAssignments[row][col] % threadColors.length]);
                g.drawRect(col * 10, row * 10, 10, 10);
            }
        }
    }
}
