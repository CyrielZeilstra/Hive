package core.components.Tiles;

import core.components.Model;
import core.components.Piece;
import core.controllers.HiveMain;
import nl.hanze.hive.Hive;
import org.junit.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.rules.ExpectedException;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by Marinkton on 04-11-18.
 */
public class TilesTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();
    private HiveMain hive;

    @Before
    public final void before() {
        hive = new HiveMain(new Model());
    }

//    @DisplayName("7a. Beetle can only shift once")
//    @Test
//    public void beetleCanShiftOnlyOnce() {
//        ArrayList<Point> beetleMoves = model.getBeetleMoves(p2.getCenter());
//        int distance = 1;
//        for(Point p: beetleMoves) {
//            assertEquals(distance, model.getDistanceBetweenPoints(p2.getCenter(),p));
//        }
//    }
//
//    @DisplayName("8a. Queen can only shift once")
//    @Test
//    public void queenCanShiftOnlyOnce() {
//        ArrayList<Point> queenMoves = model.getQueenMoves(p1.getCenter());
//        int distance = 1;
//        for(Point p: queenMoves) {
//            assertEquals(distance, model.getDistanceBetweenPoints(p1.getCenter(),p));
//        }
//    }
//
//    @DisplayName("9a. Ant can shift unlimited")
//    @Test
//    public void antCanShiftUnlimited() {
//        model.getBoard().add(ant);
//        ArrayList<Point> antMoves = model.getAntMoves(ant.getCenter());
//        assertTrue(antMoves.size() > 2);
//
//    }
//
//    @DisplayName("10a. Spider must shift three times")
//    @Test
//    public void spiderCanShiftThreeTimes() {
//        model.getBoard().add(spider);
//        ArrayList<Point> spiderMoves = model.getSpiderMoves(spider.getCenter());
//        System.out.println(spiderMoves);
//        int distance = 3;
//        for(Point p: spiderMoves) {
//            assertEquals(distance, model.getDistanceBetweenPoints(spider.getCenter(),p));
//        }
//    }
//
//    @DisplayName("10b. Spider can't move to current position")
//    @Test
//    public void spiderCantMoveToCurrentPos() {
//        ArrayList<Point> spiderMoves = model.getSpiderMoves(spider.getCenter());
//        assertTrue(!spiderMoves.contains(spider.getCenter()));
//    }
//
//    @DisplayName("10c. Spider can only move over empty fields")
//    @Test
//    public void spiderCanOnlyMoveOverEmptyFields() {
//        ArrayList<Point> spiderMoves = model.getSpiderMoves(spider.getCenter());
//        ArrayList<Point> pointsAlreadyOnBoard = model.getBoardAsPoints();
//
//        for (Point p : spiderMoves) {
//            assertTrue(!pointsAlreadyOnBoard.contains(p));
//        }
//    }
//
//    @DisplayName("10d. Spider can't move to visited fields")
//    @Test
//    public void spiderCantMoveToVisitedField() {
//        ArrayList<Point> spiderMoves = model.getSpiderMoves(spider.getCenter());
//        Point point = new Point(-1,-1);
//            assertTrue(!spiderMoves.contains(point));
//    }
//
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