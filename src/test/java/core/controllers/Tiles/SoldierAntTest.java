package core.controllers.Tiles;

import core.components.Model;
import core.controllers.HiveMain;
import nl.hanze.hive.Hive;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.rules.ExpectedException;

public class SoldierAntTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();
    private HiveMain hive;

    @Before
    public final void before() {
        hive = new HiveMain();
    }

    @DisplayName("9a. Een soldatenmier verplaatst zich door een onbeperkt aantal keren te verschuiven")
    @Test
    public void queenCanShiftOnlyOneSpot() throws Hive.IllegalMove {
        hive.play(Hive.Tile.QUEEN_BEE, 0, 0);
        hive.play(Hive.Tile.QUEEN_BEE, 1, 0);
        hive.play(Hive.Tile.SOLDIER_ANT, -1, 0);
        hive.play(Hive.Tile.SOLDIER_ANT, 2, 0);
        hive.move(-1, 0, 3, 0);
    }

    @DisplayName("9b. Een soldatenmier mag zich niet verplaatsen naar het veld waar hij al staat")
    @Test
    public void cannotMoveAntToSameSpot() throws Hive.IllegalMove {
        hive.play(Hive.Tile.QUEEN_BEE, 0, 0);
        hive.play(Hive.Tile.QUEEN_BEE, 1, 0);
        hive.play(Hive.Tile.SOLDIER_ANT, -1, 0);
        hive.play(Hive.Tile.SOLDIER_ANT, 2, 0);
        exception.expect(nl.hanze.hive.Hive.IllegalMove.class);
        exception.expectMessage("This piece is not allowed to move like that");
        hive.move(-1, 0, -1, 0);
    }

    @DisplayName("9c. Een soldatenmier mag alleen verplaatst worden over en naar lege velden")
    @Test
    public void QueenCanOnlyGoOnEmptySpot() throws Hive.IllegalMove {
        hive.play(Hive.Tile.QUEEN_BEE, 0, 0);
        hive.play(Hive.Tile.QUEEN_BEE, 1, 0);
        hive.play(Hive.Tile.SOLDIER_ANT, -1, 0);
        hive.play(Hive.Tile.SOLDIER_ANT, 2, 0);
        exception.expect(nl.hanze.hive.Hive.IllegalMove.class);
        exception.expectMessage("This piece is not allowed to move like that");
        hive.move(-1, 0, 0, 0);
    }
}
