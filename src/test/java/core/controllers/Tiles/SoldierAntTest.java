package core.controllers.Tiles;

import core.components.Model;
import core.controllers.HiveMain;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

public class SoldierAntTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();
    private HiveMain hive;

    @Before
    public final void before() {
        hive = new HiveMain(new Model());
    }
    //    @DisplayName("9a. Ant can shift unlimited")
//    @Test
//    public void antCanShiftUnlimited() {
//        model.getBoard().add(ant);
//        ArrayList<Point> antMoves = model.getAntMoves(ant.getCenter());
//        assertTrue(antMoves.size() > 2);
//
//    }
}
