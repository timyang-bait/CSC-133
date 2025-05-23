package pkgCSC133;

import pkgTYUtils.TYPingPongArray;

public class TYGoLArray extends TYPingPongArray {

    private static int TOTAL_PRIMS = 0;
    // Grid to hold the state of cells, 1 for live, 0 for dead
    private int[][] grid;
    private int[][] nextGrid;
    private boolean[][] previousState;
    private int NUM_ROWS, NUM_COLS;

    // Constructor for initializing from a boolean[][] grid
    public TYGoLArray(boolean[][] grid) {
        super(grid.length, grid[0].length); // Initialize superclass with grid dimensions
        this.grid = new int[grid.length][grid[0].length]; // Convert boolean[][] to int[][] for compatibility
        this.nextGrid = new int[grid.length][grid[0].length]; // Initialize nextGrid as int[][]
        NUM_ROWS = grid.length;
        NUM_COLS = grid[0].length;
        // Populate the grid based on boolean values
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[0].length; c++) {
                this.grid[r][c] = grid[r][c] ? 1 : 0; // Convert true/false to 1/0
            }
        }
    }
    public boolean isLiveAt(int r, int c) {
        // Ensure the row and col are within bounds of the grid
        if (r >= 0 && r < NUM_ROWS && c >= 0 && c < NUM_COLS) {
            return grid[r][c] == 1;
        } else {
            return false;
        }
    }
    // Constructor with rows, cols, and numLiveCells to populate the grid randomly
    public TYGoLArray(int rows, int cols, int numLiveCells) {
        super(rows, cols);
        this.grid = new int[rows][cols]; // Initialize grid as int[][] instead of boolean
        this.nextGrid = new int[rows][cols]; // Initialize nextGrid as int[][] instead of boolean
        populateRandomCells(numLiveCells); // Optionally populate the grid with random cells
    }

    // Method to populate the grid with random live cells
    private void populateRandomCells(int numLiveCells) {
        int totalCells = grid.length * grid[0].length;
        for (int i = 0; i < numLiveCells; i++) {
            int row = (int)(Math.random() * grid.length);
            int col = (int)(Math.random() * grid[0].length);
            grid[row][col] = 1; // Mark as live (1)
        }
    }



    // Method to save the current state of the grid for equilibrium checks
    private void savePreviousState() {
        previousState = new boolean[grid.length][grid[0].length];
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[0].length; x++) {
                previousState[y][x] = grid[y][x] == 1;
            }
        }
    }

    // Method to check if the grid is in equilibrium (no change in state)
    public boolean isInEquilibrium() {
        if (previousState == null) return false;

        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[0].length; x++) {
                if (grid[y][x] != (previousState[y][x] ? 1 : 0)) {
                    return false;
                }
            }
        }
        return true;
    }
    // Method to update the grid on each tick (generation of the Game of Life)
    public void onTickUpdate() {
        savePreviousState(); // Save previous state for comparison
        TOTAL_PRIMS = 0;
        for (int r = 0; r < NUM_ROWS; r++) {
            for (int c = 0; c < NUM_COLS; c++) {
                int liveNeighbors = countNeighbors(r, c);

                // Apply Conway's Game of Life rules
                if (grid[r][c] == 1) {
                    if (liveNeighbors < 2 || liveNeighbors > 3) {
                        nextGrid[r][c] = 0; // Underpopulation or overpopulation
                    } else {
                        nextGrid[r][c] = 1; // Lives on to the next generation
                    }
                } else {
                    if (liveNeighbors == 3) {
                        nextGrid[r][c] = 1; // Cell becomes alive
                    } else {
                        nextGrid[r][c] = 0; // Remains dead
                    }
                }

                if (nextGrid[r][c] == 1) {
                    TOTAL_PRIMS++;
                }
            }
        }
        swapLiveAndNext(); // Swap grids after update
    }



    public void swapLiveAndNext() {
        int[][] temp = grid;
        grid = nextGrid;
        nextGrid = temp;
    }



    // Method to write the next state to the nextGrid
    public void writeNextStateAt(int row, int col, int val) {
        nextGrid[row][col] = val;
    }

    // Method to count the number of live neighbors around a given cell (row, col)
    @Override
    public int countNeighbors(int row, int col) {
        int count = 0;
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (dr == 0 && dc == 0) continue; // Skip the cell itself
                int newRow = row + dr;
                int newCol = col + dc;
                if (newRow >= 0 && newRow < NUM_ROWS && newCol >= 0 && newCol < NUM_COLS) {
                    if (grid[newRow][newCol] == 1) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    // Helper method to safely read the state at a specific cell (row, col)
    public int readStateAt(int row, int col) {
        // Ensure that row and col are within bounds, otherwise return 0 (dead)
        if (row < 0 || col < 0 || row >= grid.length || col >= grid[0].length) {
            return 0; // Outside bounds, return 0 (dead)
        }
        return grid[row][col]; // Return the state of the cell (1 for live, 0 for dead)
    }
}
