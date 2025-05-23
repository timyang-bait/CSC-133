package pkgCSC133test;

import org.junit.jupiter.api.Test;
import pkgCSC133.TYGoLArray;
import pkgTYRenderEngine.TYGeometryManager;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;
import static pkgCSC133.Driver.loadGoLArrayFromResource;

public class TYAssignment9DriverTest_1 {

    @Test
    public void testSwapGrids() {
        boolean[][] initialState = {
                {false, false, false},
                {true,  false, false},
                {false, false, false}
        };

        TYGoLArray goLArray = new TYGoLArray(initialState);

        // Initial state check
        assertEquals(1, goLArray.readStateAt(1, 0), "Fail: (1,0) should be live but was dead");
        assertEquals(0, goLArray.readStateAt(1, 1), "Fail: (1,1) should be dead but was live");

        // Perform an update
        goLArray.onTickUpdate();

        // After update, cell (1,0) has 0 live neighbors => it should die
        assertEquals(0, goLArray.readStateAt(1, 0), "Fail: (1,0) should be dead after update");
    }

    @Test
    public void testMyPPArraySetterGetter() {
        TYGoLArray goLArray = new TYGoLArray(3, 3, 1);
        TYGeometryManager gm = new TYGeometryManager(goLArray, 10, 5, 2);

        // Verify that the getter will return the correct array
        assertSame(goLArray, gm.getMyPPArray());
    }
    @Test
    public void testCountNeighbors() {
        boolean[][] initialState = {
                {false, true, false},
                {true, true, true},
                {false, true, false}
        };

        TYGoLArray goLArray = new TYGoLArray(initialState);

        // Center cell (1,1) has 4 live neighbors: (0,1), (1,0), (1,2), (2,1)
        assertEquals(4, goLArray.countNeighbors(1, 1), "Fail: (1,1) should have 4 live neighbors");

        // Top-middle cell (0,1) has 3 live neighbors: (1,0), (1,1), (1,2)
        assertEquals(3, goLArray.countNeighbors(0, 1), "Fail: (0,1) should have 3 live neighbors");
    }

    @Test
    public void testOnTickUpdate() {
        boolean[][] initialState = {
                {false, true, false},
                {false, true, false},
                {false, true, false}
        };

        TYGoLArray goLArray = new TYGoLArray(initialState);
        goLArray.onTickUpdate();  // Update the grid (one generation)

        // After one tick, the blinker should flip its state
        assertEquals(1, goLArray.readStateAt(1, 0), "Fail: (1,0) should be live after tick");
        assertEquals(1, goLArray.readStateAt(1, 1), "Fail: (1,1) should be live after tick");
        assertEquals(1, goLArray.readStateAt(1, 2), "Fail: (1,2) should be live after tick");
    }

    @Test
    // Tests to see if the gol_input_1.txt is found in resources
    public void testLoadGoLArrayFromResource() {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("gol_input_1.txt");

        if (inputStream == null) {
            fail("Fail: Resource file 'gol_input_1.txt' not found in resources");
        }
    }

    @Test
    public void testWriteNextStateAt() {
        boolean[][] initialState = {
                {false, false},
                {false, false}
        };

        TYGoLArray goLArray = new TYGoLArray(initialState);

        // Write to next grid
        goLArray.writeNextStateAt(0, 0, 1);  // Set (0,0) to live in next grid

        // Swap the grids so nextGrid becomes current
        goLArray.swapLiveAndNext();

        // Now read from the current grid (which was nextGrid)
        assertEquals(1, goLArray.readStateAt(0, 0), "Fail: (0,0) should be live after swap");
    }

    @Test
    // Test for isInEquilibrium
    public void testGoLArrayFromBooleanGrid() {
        boolean[][] initialState = {
                {true, false, false},
                {false, true, false},
                {false, false, true}
        };

        TYGoLArray goLArray = new TYGoLArray(initialState);

        // Validate that the grid has been correctly initialized
        assertEquals(1, goLArray.readStateAt(0, 0), "Fail: (0,0) should be live");
        assertEquals(0, goLArray.readStateAt(0, 1), "Fail: (0,1) should be dead");
        assertEquals(1, goLArray.readStateAt(1, 1), "Fail: (1,1) should be live");
        assertEquals(0, goLArray.readStateAt(2, 0), "Fail: (2,0) should be dead");
    }

}
