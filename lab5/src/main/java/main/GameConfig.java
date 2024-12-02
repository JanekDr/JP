package main;

public class GameConfig {
    public final int rows;
    public final int cols;
    public final int iterations;
    public final boolean[][] initialGrid;

    public GameConfig(int rows, int cols, int iterations, boolean[][] initialGrid) {
        this.rows = rows;
        this.cols = cols;
        this.iterations = iterations;
        this.initialGrid = initialGrid;
    }
}
