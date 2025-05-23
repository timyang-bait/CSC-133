package pkgTYUtils;

import java.io.*;
import java.util.Random;


public class TYPingPongArrayLive extends TYPingPongArray {

    private boolean[][] grid;  // Use boolean for state representation
    private boolean[][] nextGrid;  // Store next generation

    public TYPingPongArrayLive(int numRows, int numCols) {
        super(numRows, numCols);
        this.grid = new boolean[numRows][numCols];
        this.nextGrid = new boolean[numRows][numCols];
    }

    public TYPingPongArrayLive(String filePath) {
        // Load grid from file if needed
        loadGridFromFile(filePath);
    }

    //Randomly places a given number of LIVE cells in the grid
    private void initializeArray(int numLiveCells) {
        Random rand = new Random();
        int placed = 0;

        while (placed < numLiveCells) {
            int r = rand.nextInt(numRows);
            int c = rand.nextInt(numCols);
            if (!grid[r][c]) {  // Ensure only placing in DEAD cells
                grid[r][c] = true;
                placed++;
            }
        }
    }

    public int countNeighbors(int row, int col) {
        int count = 0;
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (dr == 0 && dc == 0) continue;
                int r = row + dr;
                int c = col + dc;
                if (r >= 0 && r < numRows && c >= 0 && c < numCols && grid[r][c]) {
                    count++;
                }
            }
        }
        return count;
    }



    //Saves the current grid state to a file.

    public void saveGridToFile(String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("Conway's GoL Save\n");  // First line (header)
            writer.write(numRows + " " + numCols + "\n");  // Second line: dimensions

            for (int r = 0; r < numRows; r++) {
                for (int c = 0; c < numCols; c++) {
                    writer.write((grid[r][c] ? 1 : 0) + " ");
                }
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


     // Loads a grid from a file with dimensions starting on the second line.

    public void loadGridFromFile(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            reader.readLine();  // Skip header

            String[] dims = reader.readLine().trim().split("\\s+");
            numRows = Integer.parseInt(dims[0]);
            numCols = Integer.parseInt(dims[1]);
            grid = new boolean[numRows][numCols];
            nextGrid = new boolean[numRows][numCols];

            for (int r = 0; r < numRows; r++) {
                String[] line = reader.readLine().trim().split("\\s+");
                for (int c = 0; c < numCols; c++) {
                    grid[r][c] = (Integer.parseInt(line[c]) == 1);
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }
}
