package lms.grid;

import lms.logistics.belts.Belt;

import org.junit.*;

import java.util.Map;

import static org.junit.Assert.*;

public class GameGridTest {

    private GameGrid test4;
    private GameGrid test4Again;
    private GameGrid test6;
    private GameGrid test99;

    @Before
    public void setUp() throws Exception {
        test6 = new GameGrid(6);
        test4 = new GameGrid(4);
        test4Again = new GameGrid(4);
        test99 = new GameGrid(99);
    }

    @Test
    public void getGridWithEntry() {
        Map<Coordinate, GridComponent> gridEmpty = test4.getGrid();
        test4.setCoordinate(new Coordinate(0, 0, 0), () -> "TEST");
        Map<Coordinate, GridComponent> gridFull = test4.getGrid();
        assertNotEquals(gridEmpty, gridFull);
    }

    @Test
    public void getGridWithEntryPos() {
        Map<Coordinate, GridComponent> gridEmpty = test4.getGrid();
        test4.setCoordinate(new Coordinate(1, 2, 0), () -> "TEST");
        Map<Coordinate, GridComponent> gridFull = test4.getGrid();
        assertNotEquals(gridEmpty, gridFull);
    }

    @Test
    public void getGridWithEntryNeg() {
        Map<Coordinate, GridComponent> gridEmpty = test4.getGrid();
        test4.setCoordinate(new Coordinate(-1, -2, 0), () -> "TEST");
        Map<Coordinate, GridComponent> gridFull = test4.getGrid();
        assertNotEquals(gridEmpty, gridFull);
    }

    @Test
    public void getGridWithEntryMix() {
        Map<Coordinate, GridComponent> gridEmpty = test4.getGrid();
        test4.setCoordinate(new Coordinate(-1,2, 0), () -> "TEST");
        Map<Coordinate, GridComponent> gridFull = test4.getGrid();
        assertNotEquals(gridEmpty, gridFull);
    }


    @Test
    public void getGridFail() {
        test4.setCoordinate(new Coordinate(0, 0, 0), () -> "TEST");
        assertNotEquals(test4Again.getGrid().hashCode(),test4.getGrid().hashCode());
    }
    @Test
    public void getGrid() {
        assertEquals(test4Again.getGrid().hashCode(),test4.getGrid().hashCode());
    }

    @Test
    public void getRangePass() {
        assertEquals(4,test4.getRange());
    }
    @Test
    public void getRangeFailDouble() {
        assertNotEquals(4.0,test4.getRange());
    }
    @Test
    public void getRangeLarge() {
        assertEquals(99,test99.getRange());
    }

    @Test
    public void setCoordinate() {
        Coordinate coordinate = new Coordinate(0, 0, 0);
        GridComponent component = () -> "TEST";

        test6.setCoordinate(coordinate, component);
        Map<Coordinate, GridComponent> grid = test6.getGrid();
        GridComponent actualComponent = grid.get(coordinate);

        assertEquals(component, actualComponent);

    }
    @Test
    public void setCoordinatePos() {
        Coordinate coordinate = new Coordinate(2, 3);
        GridComponent component = () -> "TEST";

        test6.setCoordinate(coordinate, component);
        Map<Coordinate, GridComponent> grid = test6.getGrid();
        GridComponent actualComponent = grid.get(coordinate);

        assertEquals(component, actualComponent);

    }
    @Test
    public void setCoordinateNeg() {
        Coordinate coordinate = new Coordinate(-2,-3);
        GridComponent component = () -> "TEST";

        test6.setCoordinate(coordinate, component);
        Map<Coordinate, GridComponent> grid = test6.getGrid();
        GridComponent actualComponent = grid.get(coordinate);

        assertEquals(component, actualComponent);

    }
    @Test
    public void setCoordinateMix() {
        Coordinate coordinate = new Coordinate(-2,3);
        GridComponent component = () -> "TEST";

        test6.setCoordinate(coordinate, component);
        Map<Coordinate, GridComponent> grid = test6.getGrid();
        GridComponent actualComponent = grid.get(coordinate);

        assertEquals(component, actualComponent);

    }

}