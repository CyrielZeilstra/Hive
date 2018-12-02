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
import java.util.Arrays;
import java.util.List;

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

    @DisplayName("2c Empty playing field at the start of the game")
    @Test
    public void whenBoardCreatedNoPiecesShouldBePlaced() {
        assertEquals("Board is not empty.", 0, hive.model.getBoard().size());
    }

    @DisplayName("2f Only top piece may move when pieces are stacked.")
    @Test
    public void whenTilesAreStackedOnlyTopTileCanMove() throws Hive.IllegalMove {
        hive.play(Hive.Tile.QUEEN_BEE, 0, 0);
        hive.play(Hive.Tile.BEETLE, 0, 0);

        hive.move(0,0,1,1);
        expectedEx.expect(nl.hanze.hive.Hive.IllegalMove.class);
        expectedEx.expectMessage("Not a valid move.");

    }

    @DisplayName("3a White plays first")
    @Test
    public void whitePlaysFirst() {

        assertEquals("White amountOfMovesMade first", Hive.Player.WHITE, hive.model.getCurrentPlayerModel());
    }

    @DisplayName("3c player wins if Queen surrounded")
    @Test
    public void playerWinsIfQueenSurrounded() {

        Piece Queen = new Piece(0, 0, Hive.Player.WHITE, Hive.Tile.QUEEN_BEE);
        Piece Beetle1 = new Piece(-1, 0, Hive.Player.BLACK, Hive.Tile.BEETLE);
        Piece Beetle2 = new Piece(0, -1, Hive.Player.BLACK, Hive.Tile.BEETLE);
        Piece Beetle3 = new Piece(1, -1, Hive.Player.BLACK, Hive.Tile.BEETLE);
        Piece Beetle4 = new Piece(1, 0, Hive.Player.BLACK, Hive.Tile.BEETLE);
        Piece Beetle5 = new Piece(0, 1, Hive.Player.BLACK, Hive.Tile.BEETLE);
        Piece Beetle6 = new Piece(-1, 1, Hive.Player.BLACK, Hive.Tile.BEETLE);
        hive.model.getBoard().add(Queen);
        hive.model.getBoard().add(Beetle1);
        hive.model.getBoard().add(Beetle2);
        hive.model.getBoard().add(Beetle3);
        hive.model.getBoard().add(Beetle4);
        hive.model.getBoard().add(Beetle5);
        hive.model.getBoard().add(Beetle6);
        assertTrue("black wins", hive.isWinner(Hive.Player.BLACK));
    }

    @DisplayName("3d draw if both Queen's surrounded")
    @Test
    public void drawIfBothQueensSurrounded() {

        Piece Queen1 = new Piece(0, 0, Hive.Player.WHITE, Hive.Tile.QUEEN_BEE);
        Piece Queen2 = new Piece(0, 0, Hive.Player.BLACK, Hive.Tile.QUEEN_BEE);
        Piece Beetle1 = new Piece(-1, 0, Hive.Player.BLACK, Hive.Tile.BEETLE);
        Piece Beetle2 = new Piece(0, -1, Hive.Player.BLACK, Hive.Tile.BEETLE);
        Piece Beetle3 = new Piece(1, -1, Hive.Player.BLACK, Hive.Tile.BEETLE);
        Piece Beetle4 = new Piece(1, 0, Hive.Player.BLACK, Hive.Tile.BEETLE);
        Piece Beetle5 = new Piece(0, 1, Hive.Player.BLACK, Hive.Tile.BEETLE);
        Piece Beetle6 = new Piece(-1, 1, Hive.Player.BLACK, Hive.Tile.BEETLE);
        hive.model.getBoard().add(Queen1);
        hive.model.getBoard().add(Queen2);
        hive.model.getBoard().add(Beetle1);
        hive.model.getBoard().add(Beetle2);
        hive.model.getBoard().add(Beetle3);
        hive.model.getBoard().add(Beetle4);
        hive.model.getBoard().add(Beetle5);
        hive.model.getBoard().add(Beetle6);
        assertFalse(hive.isWinner(Hive.Player.BLACK));
        assertFalse(hive.isWinner(Hive.Player.WHITE));
    }

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @DisplayName("4a can only play own pieces")
    @Test
    public void canOnlyPlayOwnPieces() throws nl.hanze.hive.Hive.IllegalMove {

        hive.play(Hive.Tile.QUEEN_BEE, 0, 0);
        hive.play(Hive.Tile.QUEEN_BEE, 1, 0);
        expectedEx.expect(nl.hanze.hive.Hive.IllegalMove.class);
        expectedEx.expectMessage("Not a valid move.");
        hive.play(Hive.Tile.QUEEN_BEE, 1, -1);
    }

    @DisplayName("4c can only play own pieces")
    @Test
    public void haveToPlayOnNewSpot() throws nl.hanze.hive.Hive.IllegalMove {

        hive.play(Hive.Tile.QUEEN_BEE, 0, 0);
        expectedEx.expect(nl.hanze.hive.Hive.IllegalMove.class);
        expectedEx.expectMessage("Not a valid move.");
        hive.play(Hive.Tile.QUEEN_BEE, 0, 0);
    }

    @DisplayName("4d can only play own pieces")
    @Test
    public void canOnlyPlayNextToOwnPieces() throws nl.hanze.hive.Hive.IllegalMove {

        hive.play(Hive.Tile.QUEEN_BEE, 0, 0);
        hive.play(Hive.Tile.QUEEN_BEE, 1, 0);
        expectedEx.expect(nl.hanze.hive.Hive.IllegalMove.class);
        expectedEx.expectMessage("Not a valid move.");
        hive.play(Hive.Tile.QUEEN_BEE, 2, 0);
    }

    @DisplayName("4e player is forced to play queen after 3 amountOfMovesMade.")
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
        expectedEx.expect(nl.hanze.hive.Hive.IllegalMove.class);
        expectedEx.expectMessage("Not a valid move.");
        hive.play(Hive.Tile.BEETLE, -3, 0);
    }

    @DisplayName("5a PlayerModel can only move played pieces")
    @Test
    public void cannotImmidiatlyMove() throws nl.hanze.hive.Hive.IllegalMove {

        hive.play(Hive.Tile.SOLDIER_ANT, 0, 0);
        expectedEx.expect(nl.hanze.hive.Hive.IllegalMove.class);
        expectedEx.expectMessage("Not a valid move.");
        hive.move(0, 0, 1, 0);
    }

    @DisplayName("5c piece has to be in contact after move")
    @Test
    public void pieceIsHasToBeInContactAfterMove() throws nl.hanze.hive.Hive.IllegalMove {

        hive.play(Hive.Tile.SOLDIER_ANT, 0, 0);
        expectedEx.expect(nl.hanze.hive.Hive.IllegalMove.class);
        expectedEx.expectMessage("Not a valid move.");
        hive.move(0, 0, 5, 5);
    }

    @DisplayName("5d piece has to be in contact after move")
    @Test
    public void pieceMoveDoesNotCreateIsland() throws nl.hanze.hive.Hive.IllegalMove {

        hive.play(Hive.Tile.QUEEN_BEE, 0, 0);
        hive.play(Hive.Tile.QUEEN_BEE, 1, 0);
        hive.move(1, 0, 5, 5);
        assertTrue(hive.model.breaksConnection(new Point(0, 0)));
    }


    @DisplayName("6b can piece slip in")
    @Test
    public void pieceHasToSlideIn() throws nl.hanze.hive.Hive.IllegalMove {
        hive.play(Hive.Tile.QUEEN_BEE, 0, 0);
        hive.play(Hive.Tile.QUEEN_BEE, 1, -2);
        hive.play(Hive.Tile.BEETLE, 0, -1);
        hive.move(0, -1, 1, -1);
        expectedEx.expect(nl.hanze.hive.Hive.IllegalMove.class);
        expectedEx.expectMessage("Not a valid move.");
        assertFalse(hive.model.canSlideIn(new Point(0, -1), new Point(1, -1)));
    }

//    @DisplayName("12 PlayerModel can only pass if amountOfMovesMade = 0")
//    @Test
//    public void passTest() throws Hive.IllegalMove {
//        Hive.Player player = hive.model.getCurrentPlayerModel();
//        hive.pass();
//        assertTrue(hive.model.getCurrentPlayerModel() == player);
//    }
}