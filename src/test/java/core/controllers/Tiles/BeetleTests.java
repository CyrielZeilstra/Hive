package core.controllers.Tiles;

import core.components.Model;
import core.controllers.HiveMain;
import nl.hanze.hive.Hive;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BeetleTests {

    @Rule
    public ExpectedException exception = ExpectedException.none();
    private HiveMain hive;

    @Before
    public final void before() {
        hive = new HiveMain();
    }

    @DisplayName("7a. Een kever verplaatst zich door precies één keer te verschuiven")
    @Test
    public void beetleCanShiftOnlyOneSpot() throws Hive.IllegalMove {
        hive.play(Hive.Tile.QUEEN_BEE, 0, 0);
        hive.play(Hive.Tile.QUEEN_BEE, 1, 0);
        hive.play(Hive.Tile.BEETLE, -1, 0);
        hive.play(Hive.Tile.BEETLE, 2, 0);
        hive.move(-1, 0, 0, 0);
        hive.play(Hive.Tile.BEETLE, 2, -1);
        hive.move(0, 0, 0, -1);
        exception.expect(nl.hanze.hive.Hive.IllegalMove.class);
        exception.expectMessage("This piece is not allowed to move like that");
        hive.move(2, -1, 0, -1);
    }
}
