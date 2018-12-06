package core.controllers.Tiles;

import core.components.Model;
import core.controllers.HiveMain;
import nl.hanze.hive.Hive;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.rules.ExpectedException;
import sun.font.TrueTypeFont;

public class SpiderTests {
    @Rule
    public ExpectedException exception = ExpectedException.none();
    private HiveMain hive;

    @Before
    public final void before() {
        hive = new HiveMain(new Model());
    }

    @DisplayName("10a. Een spin verplaatst zich door precies drie keer te verschuiven")
    @Test
    public void SpiderHasToMoveThreeTimesAtOnce() throws Hive.IllegalMove {
        hive.play(Hive.Tile.QUEEN_BEE, 0, 0);
        hive.play(Hive.Tile.QUEEN_BEE, 1, 0);
        hive.play(Hive.Tile.SPIDER, -1, 0);
        hive.play(Hive.Tile.SPIDER, 2, 0);
        hive.move(-1, 0, 1, 1);
    }


    @DisplayName("10b. Een spin mag zich niet verplaatsen naar het veld waar hij al staat")
    @Test
    public void SpiderCantMoveToSamePoint() throws Hive.IllegalMove {
        hive.play(Hive.Tile.QUEEN_BEE, 0, 0);
        hive.play(Hive.Tile.QUEEN_BEE, 1, 0);
        hive.play(Hive.Tile.SPIDER, -1, 0);
        hive.play(Hive.Tile.SPIDER, 2, 0);
        exception.expect(nl.hanze.hive.Hive.IllegalMove.class);
        exception.expectMessage("This piece is not allowed to move like that");
        hive.move(-1, 0, -1, 0);
    }


    @DisplayName("10c. Een spin mag alleen verplaatst worden over en naar lege velden")
    @Test
    public void SpiderCanOnlyMoveToAndOverEmptyPoints() throws Hive.IllegalMove {
        hive.play(Hive.Tile.QUEEN_BEE, 0, 0);
        hive.play(Hive.Tile.QUEEN_BEE, 1, 0);
        hive.play(Hive.Tile.SPIDER, -1, 0);
        hive.play(Hive.Tile.SPIDER, 2, 0);
        hive.play(Hive.Tile.SOLDIER_ANT, 0, -1);
        hive.play(Hive.Tile.SOLDIER_ANT, 2, -1);
        exception.expect(nl.hanze.hive.Hive.IllegalMove.class);
        exception.expectMessage("This piece is not allowed to move like that");
        hive.move(-1, 0, 2, -1);
    }


    @DisplayName("10d. Een spin mag tijdens zijn verplaatsing geen stap maken naar een veld waar hij tijdens de verplaatsing al is geweest")
    @Test
    public void SpiderCannotMoveToASpotWhereItHasAlreadyBeenBeforeDuringMove() throws Hive.IllegalMove {
        hive.play(Hive.Tile.QUEEN_BEE, 0, 0);
        hive.play(Hive.Tile.QUEEN_BEE, 1, 0);
        hive.play(Hive.Tile.SPIDER, -1, 0);
        hive.play(Hive.Tile.SPIDER, 2, 0);
        hive.play(Hive.Tile.SOLDIER_ANT, 0, -1);
        hive.play(Hive.Tile.SOLDIER_ANT, 2, -1);
        exception.expect(nl.hanze.hive.Hive.IllegalMove.class);
        exception.expectMessage("This piece is not allowed to move like that");
        hive.move(-1, 0, 0, 1);
    }
}
