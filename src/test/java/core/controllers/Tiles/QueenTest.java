package core.controllers.Tiles;

import core.components.Model;
import core.controllers.HiveMain;
import nl.hanze.hive.Hive;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.rules.ExpectedException;

public class QueenTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();
    private HiveMain hive;

    @Before
    public final void before() {
        hive = new HiveMain();
    }

    @DisplayName("8a. De bijenkoningin verplaatst zich door precies één keer te verschuiven")
    @Test
    public void queenCanShiftOnlyOneSpot() throws Hive.IllegalMove {
        hive.play(Hive.Tile.SOLDIER_ANT, 0, 0);
        hive.play(Hive.Tile.SOLDIER_ANT, 1, 0);
        hive.play(Hive.Tile.QUEEN_BEE, -1, 0);
        hive.play(Hive.Tile.QUEEN_BEE, 2, 0);
        exception.expect(nl.hanze.hive.Hive.IllegalMove.class);
        exception.expectMessage("This piece is not allowed to move like that");
        hive.move(-1, 0, 1, -1);
    }

    @DisplayName("8b. De bijenkoningin mag alleen verplaatst worden naar een leeg veld")
    @Test
    public void QueenCanOnlyGoOnEmptySpot() throws Hive.IllegalMove {
        hive.play(Hive.Tile.SOLDIER_ANT, 0, 0);
        hive.play(Hive.Tile.SOLDIER_ANT, 1, 0);
        hive.play(Hive.Tile.QUEEN_BEE, -1, 0);
        hive.play(Hive.Tile.QUEEN_BEE, 2, 0);
        exception.expect(nl.hanze.hive.Hive.IllegalMove.class);
        exception.expectMessage("This piece is not allowed to move like that");
        hive.move(-1, 0, 0, 0);
    }
}
