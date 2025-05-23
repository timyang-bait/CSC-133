package pkgTYRenderEngine;

import pkgCSC133.TYGoLArray;
import pkgTYUtils.TYPingPongArray;

public class TYGeometryManager {

    private final int NUM_COLS, NUM_ROWS, SIZE, OFFSET, PADDING, TOTAL_PRIMS;
    private final int[] WinWidthHeight;
    private TYPingPongArray myPPArray;



    // Constructor used by Driver
    public TYGeometryManager(TYGoLArray myGoLArray, int polyLength, int polyOffset, int polyPadding) {
        this.NUM_ROWS = myGoLArray.getNumRows();
        this.NUM_COLS = myGoLArray.getNumCols();
        this.SIZE = polyLength;
        this.OFFSET = polyOffset;
        this.PADDING = polyPadding;
        this.TOTAL_PRIMS = NUM_ROWS * NUM_COLS;
        this.WinWidthHeight = new int[2];
        this.myPPArray = myGoLArray;

        // Calculate usable OpenGL window dimensions
        int winWidth = OFFSET * 2 + NUM_COLS * (SIZE + PADDING) - PADDING;
        int winHeight = OFFSET * 2 + NUM_ROWS * (SIZE + PADDING) - PADDING;
        this.WinWidthHeight[0] = winWidth;
        this.WinWidthHeight[1] = winHeight;
    }




    // Fill the array with tile vertices at a specific position
    protected boolean fillArrayWithTileVertices(float[] vertices, int startIndex, float xmin, float ymin) {
        float[] tileVerts = {
                xmin, ymin,
                xmin + SIZE, ymin,
                xmin + SIZE, ymin + SIZE,
                xmin, ymin,
                xmin + SIZE, ymin + SIZE,
                xmin, ymin + SIZE,
        };
        System.arraycopy(tileVerts, 0, vertices, startIndex, tileVerts.length);
        return true;
    }

    // Generate the vertices for all live cells in the Game of Life array
    protected boolean generateTilesVertices(TYGoLArray goLArray, float[] vertices) {
        int index = 0;

        // Loop through all cells in the grid and check if they are live
        for (int r = 0; r < NUM_ROWS; r++) {
            for (int c = 0; c < NUM_COLS; c++) {
                // Check if the cell at position (r, c) is alive
                if (goLArray.isLiveAt(r, c)) {
                    // Calculate the x and y position for the cell based on its row and column
                    float x = OFFSET + c * (SIZE + PADDING);
                    float y = OFFSET + r * (SIZE + PADDING);

                    // Fill the vertices array with the tile's vertex data
                    fillArrayWithTileVertices(vertices, index, x, y);

                    // Move the index forward by the number of vertices for one tile (6 vertices)
                    index += 12;  // 6 indices * 2 (for x, y)
                }
            }
        }
        return true;
    }


    // Setter and getter for myPPArray
    protected void setMyPPArray(TYPingPongArray array) {
        this.myPPArray = array;
    }

    public TYPingPongArray getMyPPArray() {
        return myPPArray;
    }

    public int getNumRows() {
        return NUM_ROWS;
    }

    public int getNumCols() {
        return NUM_COLS;
    }
    public int[] getWinWidthHeight() {
        return WinWidthHeight;
    }
}
