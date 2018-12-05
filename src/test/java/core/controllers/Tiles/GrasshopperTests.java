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
        hive = new HiveMain(new Model());
    }

    @DisplayName("11a. Een sprinkhaan verplaatst zich door in een rechte lijn een sprong te maken naar een veld meteen achter een andere steen in de richting van de sprong.")
    @Test
    public void HopperHasToMoveInStraightLine() throws Hive.IllegalMove {

    }


    @DisplayName("11b. Een sprinkhaan mag zich niet verplaatsen naar het veld waar hij al staat.")
    @Test
    public void HopperCantMoveToSamePoint() throws Hive.IllegalMove {

    }


    @DisplayName("11c. Een sprinkhaan moet over minimaal één steen springen")
    @Test
    public void HopperHasToJumpAtleastOneTile() throws Hive.IllegalMove {

    }


    @DisplayName("11d. Een sprinkhaan mag niet naar een bezet veld springen")
    @Test
    public void HopperCantJumpToOccupiedPoint() throws Hive.IllegalMove {

    }

    @DisplayName("11e. Een sprinkhaan mag niet over lege velden springen. Dit betekent dat alle velden tussen de start- en eindpositie bezet moeten zijn")
    @Test
    public void HopperCantJumpOverEmptySpotsToReachEnd() throws Hive.IllegalMove {

    }

}
