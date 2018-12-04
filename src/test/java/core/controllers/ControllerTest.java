package core.controllers;

import core.components.Model;
import core.components.Piece;
import nl.hanze.hive.Hive;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.rules.ExpectedException;

import java.awt.*;

import static org.junit.Assert.*;

public class ControllerTest {


    @Rule
    public ExpectedException exception = ExpectedException.none();
    private HiveMain hive;

    @Before
    public final void before() {
        hive = new HiveMain(new Model());
    }

//    @DisplayName("1c Both players have correct pieces at the start of the game.")
//    @Test
//    public void whenBoardCreatedBothPlayersHaveCorrectPieces() {
//        List<Enum> pieces = Arrays.asList(Hive.Tile.QUEEN_BEE,
//                Hive.Tile.SPIDER, Hive.Tile.SPIDER,
//                Hive.Tile.BEETLE, Hive.Tile.BEETLE,
//                Hive.Tile.GRASSHOPPER, Hive.Tile.GRASSHOPPER, Hive.Tile.GRASSHOPPER,
//                Hive.Tile.SOLDIER_ANT, Hive.Tile.SOLDIER_ANT, Hive.Tile.SOLDIER_ANT);
//        hive.model.getWhiteAvailablePieces().containsAll(pieces);
//        hive.model.getBlackAvailablePieces().containsAll(pieces);
//        assertTrue("Black should have all pieces at new game", hive.model.getBlackAvailablePieces().containsAll(pieces));
//        assertTrue("White should have all pieces at new game", hive.model.getWhiteAvailablePieces().containsAll(pieces));
//    }

    @DisplayName("2c Aan het begin van het spel is het speelveld leeg")
    @Test
    public void whenBoardCreatedNoPiecesShouldBePlaced() {
        assertEquals("Board is not empty.", 0, hive.model.getBoard().size());
    }

    @DisplayName("2f In sommige gevallen mogen stenen op andere stenen liggen; in dat geval mag alleen de bovenste steen van een stapel verplaatst worden.")
    @Test
    public void whenTilesAreStackedOnlyTopTileCanMove() throws Hive.IllegalMove {
        hive.play(Hive.Tile.QUEEN_BEE, 0, 0);
        hive.play(Hive.Tile.QUEEN_BEE, 1, 0);
        hive.play(Hive.Tile.BEETLE, -1, 0);
        hive.play(Hive.Tile.BEETLE, 2, 0);
        hive.move(-1, 0, 0, 0);
        hive.play(Hive.Tile.SOLDIER_ANT, 1, 1);
        hive.move(0, 0, -1, 0);
        int counter = 0;
        for (Piece p : hive.model.getBoard()) {
            if (p.getCenter().x == -1 && p.getCenter().y == 0) {
                counter++;
            }
        }
        assertEquals(1, counter);
    }

    @DisplayName("3a Wit heeft de eerste beurt.")
    @Test
    public void whitePlaysFirst() {
        assertEquals("White amountOfMovesMade first", Hive.Player.WHITE, hive.model.getCurrentPlayer().getPlayerColor());
    }

    @DisplayName("3b Tijdens zijn beurt kan een speler een steen spelen, een steen verplaatsen of passen; daarna is de tegenstander aan de beurt.")
    @Test
    public void moveSwapsWhenNeeded() throws Hive.IllegalMove {
        hive.play(Hive.Tile.QUEEN_BEE, 0, 0);
        assertEquals(Hive.Player.BLACK, hive.model.getCurrentPlayer().getPlayerColor());
        hive.play(Hive.Tile.SOLDIER_ANT, 1, 0);
        assertEquals(Hive.Player.WHITE, hive.model.getCurrentPlayer().getPlayerColor());
    }

    @DisplayName("3c Een speler wint als alle zes velden naast de bijenkoningin van de tegenstander bezet zijn.")
    @Test
    public void playerWinsIfQueenSurrounded() throws Hive.IllegalMove {
        hive.play(Hive.Tile.QUEEN_BEE, 0, 0);
        hive.play(Hive.Tile.QUEEN_BEE, -1, 0);
        hive.play(Hive.Tile.SOLDIER_ANT, 1, 0);
        hive.play(Hive.Tile.SOLDIER_ANT, -1, -1);
        hive.play(Hive.Tile.SOLDIER_ANT, 0, 1);
        hive.play(Hive.Tile.SOLDIER_ANT, -2, 1);
        hive.play(Hive.Tile.SOLDIER_ANT, 1, -1);
        hive.move(-1, -1, 0, -1);
        hive.play(Hive.Tile.BEETLE, 2, 0);
        hive.move(-2, 1, -1, 1);
        assertTrue("black wins", hive.isWinner(Hive.Player.BLACK));
    }

    @DisplayName("3d Als beide spelers tegelijk zouden winnen is het in plaats daarvan een gelijkspel.")
    @Test
    public void gameIsDrawIfBothQueensAreSurrounded() throws Hive.IllegalMove {
        hive.play(Hive.Tile.QUEEN_BEE, 0, 0);
        hive.play(Hive.Tile.QUEEN_BEE, -1, 0);
        hive.play(Hive.Tile.SOLDIER_ANT, 1, 0);
        hive.play(Hive.Tile.SOLDIER_ANT, -1, -1);
        hive.play(Hive.Tile.SOLDIER_ANT, 0, 1);
        hive.play(Hive.Tile.SOLDIER_ANT, -2, 1);
        hive.play(Hive.Tile.SOLDIER_ANT, 1, -1);
        hive.play(Hive.Tile.SOLDIER_ANT, -2, 0);
        hive.play(Hive.Tile.BEETLE, 1, -2);
        hive.move(-2, 1, -1, 1);
        hive.play(Hive.Tile.BEETLE, 2, 0);
        hive.play(Hive.Tile.BEETLE, -2, 1);
        hive.move(1, -2, 0, -1);
        assertTrue("draw", hive.isDraw());
    }


    @DisplayName("4a Een speler mag alleen zijn eigen nog niet gespeelde stenen spelen.")
    @Test
    public void canOnlyPlayOwnPieces() throws Hive.IllegalMove {
        hive.play(Hive.Tile.QUEEN_BEE, 0, 0);
        hive.play(Hive.Tile.QUEEN_BEE, -1, 0);
        hive.play(Hive.Tile.SPIDER, 1, 0);
        hive.play(Hive.Tile.SOLDIER_ANT, -1, -1);
        hive.play(Hive.Tile.SPIDER, 0, 1);
        hive.play(Hive.Tile.SOLDIER_ANT, -2, 1);
        exception.expect(nl.hanze.hive.Hive.IllegalMove.class);
        exception.expectMessage("Player does not have this piece");
        hive.play(Hive.Tile.SPIDER, 1, -1);
    }

    @DisplayName("4b Een speler speelt een steen door deze op een leeg vlak in het speelveld te leggen.")
    @Test
    public void haveToPlayOnNewSpot() throws nl.hanze.hive.Hive.IllegalMove {
        hive.play(Hive.Tile.QUEEN_BEE, 5, 3);
        exception.expect(nl.hanze.hive.Hive.IllegalMove.class);
        exception.expectMessage("Not a valid play");
        hive.play(Hive.Tile.QUEEN_BEE, 5, 3);
    }

    @DisplayName("4c Als er al stenen op het bord liggen moet een naast een andere steen gespeeld worden")
    @Test
    public void canOnlyPlayNextToOwnPieces() throws nl.hanze.hive.Hive.IllegalMove {

        hive.play(Hive.Tile.QUEEN_BEE, 50, 50);
        exception.expect(nl.hanze.hive.Hive.IllegalMove.class);
        exception.expectMessage("Not a valid play");
        hive.play(Hive.Tile.QUEEN_BEE, 5, 5);
    }

    @DisplayName("4d Als er stenen van beide spelers op het bord liggen mag een steen niet naast een steen van de tegenstander geplaatst worden")
    @Test
    public void canOnlyPlayIfNotTouchingOtherPlayer() throws nl.hanze.hive.Hive.IllegalMove {
        hive.play(Hive.Tile.QUEEN_BEE, 0, 0);
        hive.play(Hive.Tile.QUEEN_BEE, -1, 0);
        hive.play(Hive.Tile.SOLDIER_ANT, 1, -1);
        exception.expect(nl.hanze.hive.Hive.IllegalMove.class);
        exception.expectMessage("Not a valid play");
        hive.play(Hive.Tile.SOLDIER_ANT, 1, 0);
    }

    @DisplayName("4e Als een speler al drie stenen gespeeld heeft maar zijn bijenkoningin nog niet, dan moet deze gespeeld worden..")
    @Test
    public void forcePlayerToPlayQueenAfterThreeMoves() throws nl.hanze.hive.Hive.IllegalMove {
        //1
        hive.play(Hive.Tile.SOLDIER_ANT, 0, 0);
        hive.play(Hive.Tile.SOLDIER_ANT, 1, 0);
        //2
        hive.play(Hive.Tile.SOLDIER_ANT, -1, 0);
        hive.play(Hive.Tile.SOLDIER_ANT, 2, 0);
        //3
        hive.play(Hive.Tile.SOLDIER_ANT, -2, 0);
        hive.play(Hive.Tile.SOLDIER_ANT, 3, 0);
        //4
        exception.expect(nl.hanze.hive.Hive.IllegalMove.class);
        exception.expectMessage("Need to play Queen after 3 moves");
        hive.play(Hive.Tile.BEETLE, -3, 0);
    }

    @DisplayName("5a Een speler mag alleen zijn eigen eerder gespeelde stenen verplaatsen.")
    @Test
    public void playerCanOnlyMoveOwnedPieces() throws nl.hanze.hive.Hive.IllegalMove {
        hive.play(Hive.Tile.QUEEN_BEE, 0, 0);
        hive.play(Hive.Tile.QUEEN_BEE, -1, 0);
        hive.play(Hive.Tile.SOLDIER_ANT, 1, 0);
        hive.play(Hive.Tile.SOLDIER_ANT, -1, -1);
        hive.play(Hive.Tile.SOLDIER_ANT, 0, 1);
        hive.play(Hive.Tile.SOLDIER_ANT, -2, 1);
        exception.expect(nl.hanze.hive.Hive.IllegalMove.class);
        exception.expectMessage("Cannot move other player's pieces");
        hive.move(-2, 1, -2, 0);
    }


    @DisplayName("5b Een speler mag pas stenen verplaatsen als zijn bijenkoningin gespeeld is")
    @Test
    public void canOnlyMovePiecesAfterQueenIsPlayed() throws nl.hanze.hive.Hive.IllegalMove {
        hive.play(Hive.Tile.QUEEN_BEE, 0, 0);
        hive.play(Hive.Tile.SOLDIER_ANT, -1, 0);
        hive.play(Hive.Tile.SOLDIER_ANT, 1, 0);
        hive.play(Hive.Tile.SOLDIER_ANT, -1, -1);
        hive.play(Hive.Tile.SOLDIER_ANT, 0, 1);
        exception.expect(nl.hanze.hive.Hive.IllegalMove.class);
        exception.expectMessage("Have to play Queen before move is allowed");
        hive.move(-1, -1, -2, 0);
        hive.play(Hive.Tile.QUEEN_BEE, -2, 0);
        hive.play(Hive.Tile.BEETLE, 1, 1);
        hive.move(-2, 0, -2, 1);
    }
//
//    @DisplayName("5d piece has to be in contact after move")
//    @Test
//    public void pieceMoveDoesNotCreateIsland() throws nl.hanze.hive.Hive.IllegalMove {
//
//        hive.play(Hive.Tile.QUEEN_BEE, 0, 0);
//        hive.play(Hive.Tile.QUEEN_BEE, 1, 0);
//        hive.move(1, 0, 5, 5);
//        assertTrue(hive.model.breaksConnection(new Point(0, 0)));
//    }
//
//
//    @DisplayName("6b can piece slip in")
//    @Test
//    public void pieceHasToSlideIn() throws nl.hanze.hive.Hive.IllegalMove {
//        hive.play(Hive.Tile.QUEEN_BEE, 0, 0);
//        hive.play(Hive.Tile.QUEEN_BEE, 1, -2);
//        hive.play(Hive.Tile.BEETLE, 0, -1);
//        hive.move(0, -1, 1, -1);
//        expectedEx.expect(nl.hanze.hive.Hive.IllegalMove.class);
//        expectedEx.expectMessage("Not a valid move.");
//        assertFalse(hive.model.canSlideIn(new Point(0, -1), new Point(1, -1)));
//    }

//    @DisplayName("12 PlayerModel can only pass if amountOfMovesMade = 0")
//    @Test
//    public void passTest() throws Hive.IllegalMove {
//        Hive.Player player = hive.model.getCurrentPlayer();
//        hive.pass();
//        assertTrue(hive.model.getCurrentPlayer() == player);
//    }
}