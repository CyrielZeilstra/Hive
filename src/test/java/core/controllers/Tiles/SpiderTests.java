package core.controllers.Tiles;

import core.components.Model;
import core.controllers.HiveMain;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

public class SpiderTests {
    @Rule
    public ExpectedException exception = ExpectedException.none();
    private HiveMain hive;

    @Before
    public final void before() {
        hive = new HiveMain(new Model());
    }


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
}
