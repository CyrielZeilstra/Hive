package core.controllers.Tiles;

import core.components.Model;
import core.controllers.HiveMain;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

public class QueenTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();
    private HiveMain hive;

    @Before
    public final void before() {
        hive = new HiveMain(new Model());
    }

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
}
