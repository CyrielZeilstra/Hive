package core.controllers.Tiles;

import core.components.Model;
import core.controllers.HiveMain;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

public class GrasshopperTests {
    @Rule
    public ExpectedException exception = ExpectedException.none();
    private HiveMain hive;

    @Before
    public final void before() {
        hive = new HiveMain(new Model());
    }
    //    @DisplayName("11a. Hopper can only move in straight line")
//    @Test
//    public void hopperCanOnlyMoveInStraightLine() {
//        ArrayList<Point> hopperMoves = model.getGrasshopperMoves(hopper.getCenter());
//        for(Point p : hopperMoves) {
//            assertTrue(model.getLineDirection(hopper.getCenter(),p) != 6);
//        }
//    }
//
//    @DisplayName("11b. hopper can't move to current pos")
//    @Test
//    public void hopperCantMoveToCurrentPos() {
//        ArrayList<Point> hopperMoves = model.getGrasshopperMoves(hopper.getCenter());
//        assertTrue(!hopperMoves.contains(hopper.getCenter()));
//    }
//
//    @DisplayName("11c. hopper must jump over at least 1 field")
//    @Test
//    public void hopperMustJumpOverAtLeastOneTile() {
//        ArrayList<Point> hopperMoves = model.getGrasshopperMoves(hopper.getCenter());
//        ArrayList<Point> neighbours = model.getNeighbours(hopper.getCenter());
//        for (Point p: hopperMoves) {
//            assertTrue(!neighbours.contains(p));
//        }
//
//    }
//
//    @DisplayName("11d. hopper can't move to taken positions on board")
//    @Test
//    public void hopperCantMoveToTakenPos() {
//        ArrayList<Point> hopperMoves = model.getGrasshopperMoves(hopper.getCenter());
//        ArrayList<Point> pointsAlreadyOnBoard = model.getBoardAsPoints();
//
//        for (Point p : hopperMoves) {
//            assertTrue(!pointsAlreadyOnBoard.contains(p));
//        }
//    }
//
//    @DisplayName("11d. ")
//    @Test
//    public void hopperCantJumpOverEmptyFields() {
//        ArrayList<Point> hopperMoves = model.getGrasshopperMoves(hopper.getCenter());
//        ArrayList<Point> pointsAlreadyOnBoard = model.getBoardAsPoints();
//        ArrayList<Point> allMoves = model.getAllMoves();
//
//        for (Point p : hopperMoves) {
//            assertTrue(!pointsAlreadyOnBoard.contains(p));
//        }
//    }
}
