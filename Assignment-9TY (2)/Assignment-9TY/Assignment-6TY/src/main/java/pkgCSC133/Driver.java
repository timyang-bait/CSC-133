package pkgCSC133;

import pkgTYRenderEngine.TYGeometryManager;
import pkgTYRenderEngine.TYRenderer;
import pkgTYUtils.TYWindowManager;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Driver {

    public static void main(String[] args) {
        final int DEFAULT_ROWS = 16;
        final int DEFAULT_COLS = 16;
        final int DEFAULT_NUM_LIVE_CELLS = (int) (DEFAULT_ROWS * DEFAULT_COLS * 0.2f + 0.5);

        // Use provided argument or default to gol_input_1.txt
        String inputFileName = args.length > 0 ? args[0] : "gol_input_1.txt";

        TYGoLArray myGoLArray = loadGoLArrayFromResource(inputFileName);
        if (myGoLArray == null) {
            System.err.println(" Failed to load GoL array from " + inputFileName + ". Using random initialization.");
            myGoLArray = new TYGoLArray(DEFAULT_ROWS, DEFAULT_COLS, DEFAULT_NUM_LIVE_CELLS);
        }

        int numRows = myGoLArray.getNumRows();
        int numCols = myGoLArray.getNumCols();
        int polyLength = 500;
        int polyOffset = 50;
        int polyPadding = 20;
        int winWidth = (polyLength + polyPadding) * numCols + 2 * polyOffset;
        int winHeight = (polyLength + polyPadding) * numRows + 2 * polyOffset;
        int winOrgX = 30, winOrgY = 30;

        TYWindowManager myWM = TYWindowManager.get(winWidth, winHeight, winOrgX, winOrgY);
        TYGeometryManager geomMgr = new TYGeometryManager(myGoLArray, polyLength, polyOffset, polyPadding);
        TYRenderer myRenderer = new TYRenderer(myWM, geomMgr);

        // Main loop
        while (myWM.isWindowOpen()) {
            myGoLArray.onTickUpdate(); // Update simulation
            myRenderer.run();          // Render frame

            // Check for equilibrium state
            if (myGoLArray.isInEquilibrium()) {
                System.out.println("The system has reached equilibrium.");
                break; // Exit the loop if equilibrium is reached
            }

            try {
                Thread.sleep(35);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        myWM.destroyGlfwWindow(); // Cleanup on exit
        System.out.println("Simulation finished. Exiting program.");
    }

    /**
     * Reads a Game of Life input file from the resources folder and builds a TYGoLArray.
     */
    public static TYGoLArray loadGoLArrayFromResource(String fileName) {
        try (InputStream inputStream = Driver.class.getClassLoader().getResourceAsStream(fileName)) {
            if (inputStream == null) {
                System.err.println(" File not found in resources: " + fileName);
                return null;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            // Skip header/comment line
            reader.readLine();

            // Read grid size line: e.g., "16 16"
            String sizeLine = reader.readLine();
            if (sizeLine == null || sizeLine.trim().isEmpty()) {
                System.err.println(" Invalid size line.");
                return null;
            }

            String[] sizeParts = sizeLine.trim().split("\\s+");
            if (sizeParts.length != 2) {
                System.err.println(" Size line must contain exactly two integers.");
                return null;
            }

            int rows = Integer.parseInt(sizeParts[0]);
            int cols = Integer.parseInt(sizeParts[1]);
            boolean[][] grid = new boolean[rows][cols];

            // Read the grid
            for (int r = 0; r < rows; r++) {
                String line = reader.readLine();
                if (line == null) {
                    System.err.println(" Not enough rows in input file.");
                    return null;
                }

                // Split by space, ignoring the row index
                String[] tokens = line.trim().split("\\s+");
                if (tokens.length < cols + 1) {
                    System.err.println(" Row " + r + " does not have enough values.");
                    return null;
                }

                // Fill grid based on the line
                for (int c = 0; c < cols; c++) {
                    grid[r][c] = tokens[c + 1].equals("1");
                }
            }

            return new TYGoLArray(grid);
        } catch (Exception e) {
            System.err.println(" Error reading file: " + e.getMessage());
            return null;
        }
    }
}
