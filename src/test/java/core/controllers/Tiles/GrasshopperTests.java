package core.controllers.Tiles;

import core.components.Model;
import core.controllers.HiveMain;
import nl.hanze.hive.Hive;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.rules.ExpectedException;

public class GrasshopperTests {
    @Rule
    public ExpectedException exception = ExpectedException.none();
    private HiveMain hive;

    @Before
    public final void before() {
        hive = new HiveMain();
    }

    @DisplayName("11a. Een sprinkhaan verplaatst zich door in een rechte lijn een sprong te maken naar een veld meteen achter een andere steen in de richting van de sprong.")
    @Test
    public void HopperHasToMoveInStraightLine() throws Hive.IllegalMove {
        hive.play(Hive.Tile.QUEEN_BEE, 0, 0);
        hive.play(Hive.Tile.QUEEN_BEE, 1, 0);
        hive.play(Hive.Tile.GRASSHOPPER, -1, 0);
        hive.play(Hive.Tile.GRASSHOPPER, 1, 1);
        hive.play(Hive.Tile.SOLDIER_ANT, 0, -1);
        hive.play(Hive.Tile.SOLDIER_ANT, 2, -1);
        exception.expect(nl.hanze.hive.Hive.IllegalMove.class);
        exception.expectMessage("This piece is not allowed to move like that");
        hive.move(-1, 0, 0, 1);
    }


    @DisplayName("11b. Een sprinkhaan mag zich niet verplaatsen naar het veld waar hij al staat.")
    @Test
    public void HopperCantMoveToSamePoint() throws Hive.IllegalMove {
        hive.play(Hive.Tile.QUEEN_BEE, 0, 0);
        hive.play(Hive.Tile.QUEEN_BEE, 1, 0);
        hive.play(Hive.Tile.GRASSHOPPER, -1, 1);
        hive.play(Hive.Tile.GRASSHOPPER, 1, 1);
        exception.expect(nl.hanze.hive.Hive.IllegalMove.class);
        exception.expectMessage("This piece is not allowed to move like that");
        hive.move(-1, 1, -1, 1);
    }


    @DisplayName("11c. Een sprinkhaan moet over minimaal één steen springen")
    @Test
    public void HopperHasToJumpAtleastOneTile() throws Hive.IllegalMove {
        hive.play(Hive.Tile.QUEEN_BEE, 0, 0);
        hive.play(Hive.Tile.QUEEN_BEE, 1, 0);
        hive.play(Hive.Tile.GRASSHOPPER, -1, 1);
        hive.play(Hive.Tile.GRASSHOPPER, 1, 1);
        exception.expect(nl.hanze.hive.Hive.IllegalMove.class);
        exception.expectMessage("This piece is not allowed to move like that");
        hive.move(-1, 1, 0, -1);
    }


    @DisplayName("11d. Een sprinkhaan mag niet naar een bezet veld springen")
    @Test
    public void HopperCantJumpToOccupiedPoint() throws Hive.IllegalMove {
        hive.play(Hive.Tile.QUEEN_BEE, 0, 0);
        hive.play(Hive.Tile.QUEEN_BEE, 1, 0);
        hive.play(Hive.Tile.GRASSHOPPER, -1, 1);
        hive.play(Hive.Tile.GRASSHOPPER, 1, 1);
        hive.play(Hive.Tile.SOLDIER_ANT, 0, -1);
        hive.play(Hive.Tile.SOLDIER_ANT, 2, -1);
        hive.move(0, -1, 1, -1);
        hive.play(Hive.Tile.BEETLE, 2, 0);
        exception.expect(nl.hanze.hive.Hive.IllegalMove.class);
        exception.expectMessage("This piece is not allowed to move like that");
        hive.move(-1, 1, 1, -1);
    }

    @DisplayName("11e. Een sprinkhaan mag niet over lege velden springen. Dit betekent dat alle velden tussen de start- en eindpositie bezet moeten zijn")
    @Test
    public void HopperCantJumpOverEmptySpotsToReachEnd() throws Hive.IllegalMove {
        hive.play(Hive.Tile.QUEEN_BEE, 0, 0);
        hive.play(Hive.Tile.QUEEN_BEE, 1, 0);
        hive.play(Hive.Tile.GRASSHOPPER, -1, 1);
        hive.play(Hive.Tile.GRASSHOPPER, 1, 1);
        hive.play(Hive.Tile.SOLDIER_ANT, 0, -1);
        hive.play(Hive.Tile.SOLDIER_ANT, 2, -1);
        hive.play(Hive.Tile.SOLDIER_ANT, 1, -2);
        exception.expect(nl.hanze.hive.Hive.IllegalMove.class);
        exception.expectMessage("This piece is not allowed to move like that");
        hive.move(1, 1, 0, -2);
    }

}
