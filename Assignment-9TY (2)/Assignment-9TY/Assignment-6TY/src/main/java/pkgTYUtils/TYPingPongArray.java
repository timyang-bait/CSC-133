package pkgTYUtils;

/**
 * Abstract base class for a 2D array that supports "ping-pong" logic (live vs. next state).
 */
public class TYPingPongArray {
    protected static final int DEAD = 0;
    protected static final int LIVE = 1;
    protected int[][] nextGrid;
    protected int[][] grid;
    protected int numRows, numCols;

    public TYPingPongArray(int numRows, int numCols) {
        this.numRows = numRows;
        this.numCols = numCols;
        grid = new int[numRows][numCols];
        nextGrid = new int[numRows][numCols];  // New line
    }
    protected int readStateAt(int row, int col) {
        return readFromLive(row, col);  // Reuse your safe logic
    }

    // Used to write to the next grid
    protected void writeNextStateAt(int row, int col, int val) {
        writeToNext(nextGrid, row, col, val);
    }

    // Call this after each update to advance the generation

    public TYPingPongArray() {

    }

    /**
     * To be overridden in child: counts the 8 live neighbors of a cell.
     */
    public int countNeighbors(int row, int col) {
        return 0;  // Default: no live neighbors
    }


    public void saveGridToFile(String fileName) {
        // Placeholder; overridden in subclass.
    }

    public void loadGridFromFile(String fileName) {
        // Placeholder; overridden in subclass.
    }

    /**
     * Protected getter for reading from the grid safely.
     */
    protected int readFromLive(int row, int col) {
        if (row >= 0 && row < numRows && col >= 0 && col < numCols) {
            return grid[row][col];
        }
        return DEAD;
    }

    /**
     * Protected setter to update a cell in the next array (to be used in swap logic).
     */
    protected void writeToNext(int[][] next, int row, int col, int val) {
        if (row >= 0 && row < numRows && col >= 0 && col < numCols) {
            next[row][col] = val;
        }
    }
    public int getNumRows() {
        return numRows;
    }

    public int getNumCols() {
        return numCols;
    }

}
