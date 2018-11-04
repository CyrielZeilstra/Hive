package core.controllers;

import core.components.Model;
import core.components.Piece;
import nl.hanze.hive.Hive;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.rules.ExpectedException;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class ControllerTest {

    Controller main = new Controller(new Model());

    @DisplayName("1c Both players have correct pieces at the start of the game.")
    @Test
    public void whenBoardCreatedBothPlayersHaveCorrectPieces() {
        main.init();
        List<Enum> pieces = Arrays.asList(Hive.Tile.QUEEN_BEE,
                Hive.Tile.SPIDER, Hive.Tile.SPIDER,
                Hive.Tile.BEETLE, Hive.Tile.BEETLE,
                Hive.Tile.GRASSHOPPER, Hive.Tile.GRASSHOPPER, Hive.Tile.GRASSHOPPER,
                Hive.Tile.SOLDIER_ANT, Hive.Tile.SOLDIER_ANT, Hive.Tile.SOLDIER_ANT);
        main.model.getWhiteAvailablePieces().containsAll(pieces);
        main.model.getBlackAvailablePieces().containsAll(pieces);
        assertTrue("Black should have all pieces at new game", main.model.getBlackAvailablePieces().containsAll(pieces));
        assertTrue("White should have all pieces at new game", main.model.getWhiteAvailablePieces().containsAll(pieces));
    }

    @DisplayName("2c Empty playing field at the start of the game")
    @Test
    public void whenBoardCreatedNoPiecesShouldBePlaced() {
        main.init();
        assertEquals("Board is not empty.", 0, main.model.getBoard().size());
    }

    @DisplayName("2f Only top piece may move when pieces are stacked.")
    @Test
    public void whenTilesAreStackedOnlyTopTileCanMove() throws Hive.IllegalMove {
        main.init();
        Piece Queen = new Piece(0, 0, false, Hive.Player.WHITE, Hive.Tile.QUEEN_BEE);
        Piece Beetle = new Piece(0, 0, false, Hive.Player.WHITE, Hive.Tile.BEETLE);

        main.model.getBoard().add(Queen);
        main.model.getBoard().add(Beetle);
        assertEquals("Queen can move ", 0, main.model.getAvailableMovesSelectedBoardPiece(Queen).size());
        assertEquals("Beetle cant still move ", 6, main.model.getAvailableMovesSelectedBoardPiece(Beetle).size());
    }

    @DisplayName("3a White plays first")
    @Test
    public void whitePlaysFirst() {
        main.init();
        assertEquals("White moves first", Hive.Player.WHITE, main.model.getCurrentPlayer());
    }

    @DisplayName("3c player wins if Queen surrounded")
    @Test
    public void playerWinsIfQueenSurrounded() {
        main.init();
        Piece Queen = new Piece(0, 0, false, Hive.Player.WHITE, Hive.Tile.QUEEN_BEE);
        Piece Beetle1 = new Piece(-1, 0, false, Hive.Player.BLACK, Hive.Tile.BEETLE);
        Piece Beetle2 = new Piece(0, -1, false, Hive.Player.BLACK, Hive.Tile.BEETLE);
        Piece Beetle3 = new Piece(1, -1, false, Hive.Player.BLACK, Hive.Tile.BEETLE);
        Piece Beetle4 = new Piece(1, 0, false, Hive.Player.BLACK, Hive.Tile.BEETLE);
        Piece Beetle5 = new Piece(0, 1, false, Hive.Player.BLACK, Hive.Tile.BEETLE);
        Piece Beetle6 = new Piece(-1, 1, false, Hive.Player.BLACK, Hive.Tile.BEETLE);
        main.model.getBoard().add(Queen);
        main.model.getBoard().add(Beetle1);
        main.model.getBoard().add(Beetle2);
        main.model.getBoard().add(Beetle3);
        main.model.getBoard().add(Beetle4);
        main.model.getBoard().add(Beetle5);
        main.model.getBoard().add(Beetle6);
        assertTrue("black wins", main.isWinner(Hive.Player.BLACK));
    }

    @DisplayName("3d draw if both Queen's surrounded")
    @Test
    public void drawIfBothQueensSurrounded() {
        main.init();
        Piece Queen1 = new Piece(0, 0, false, Hive.Player.WHITE, Hive.Tile.QUEEN_BEE);
        Piece Queen2 = new Piece(0, 0, false, Hive.Player.BLACK, Hive.Tile.QUEEN_BEE);
        Piece Beetle1 = new Piece(-1, 0, false, Hive.Player.BLACK, Hive.Tile.BEETLE);
        Piece Beetle2 = new Piece(0, -1, false, Hive.Player.BLACK, Hive.Tile.BEETLE);
        Piece Beetle3 = new Piece(1, -1, false, Hive.Player.BLACK, Hive.Tile.BEETLE);
        Piece Beetle4 = new Piece(1, 0, false, Hive.Player.BLACK, Hive.Tile.BEETLE);
        Piece Beetle5 = new Piece(0, 1, false, Hive.Player.BLACK, Hive.Tile.BEETLE);
        Piece Beetle6 = new Piece(-1, 1, false, Hive.Player.BLACK, Hive.Tile.BEETLE);
        main.model.getBoard().add(Queen1);
        main.model.getBoard().add(Queen2);
        main.model.getBoard().add(Beetle1);
        main.model.getBoard().add(Beetle2);
        main.model.getBoard().add(Beetle3);
        main.model.getBoard().add(Beetle4);
        main.model.getBoard().add(Beetle5);
        main.model.getBoard().add(Beetle6);
        assertFalse(main.isWinner(Hive.Player.BLACK));
        assertFalse(main.isWinner(Hive.Player.WHITE));
    }

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @DisplayName("4a can only play own pieces")
    @Test
    public void canOnlyPlayOwnPieces() throws nl.hanze.hive.Hive.IllegalMove {
        main.init();
        main.play(Hive.Tile.QUEEN_BEE, 0, 0);
        main.play(Hive.Tile.QUEEN_BEE, 1, 0);
        expectedEx.expect(nl.hanze.hive.Hive.IllegalMove.class);
        expectedEx.expectMessage("Not a valid move.");
        main.play(Hive.Tile.QUEEN_BEE, 1, -1);
    }

    @DisplayName("4c can only play own pieces")
    @Test
    public void haveToPlayOnNewSpot() throws nl.hanze.hive.Hive.IllegalMove {
        main.init();
        main.play(Hive.Tile.QUEEN_BEE, 0, 0);
        expectedEx.expect(nl.hanze.hive.Hive.IllegalMove.class);
        expectedEx.expectMessage("Not a valid move.");
        main.play(Hive.Tile.QUEEN_BEE, 0, 0);
    }

    @DisplayName("4d can only play own pieces")
    @Test
    public void canOnlyPlayNextToOwnPieces() throws nl.hanze.hive.Hive.IllegalMove {
        main.init();
        main.play(Hive.Tile.QUEEN_BEE, 0, 0);
        main.play(Hive.Tile.QUEEN_BEE, 1, 0);
        expectedEx.expect(nl.hanze.hive.Hive.IllegalMove.class);
        expectedEx.expectMessage("Not a valid move.");
        main.play(Hive.Tile.QUEEN_BEE, 2, 0);
    }

    @DisplayName("4e player is forced to play queen after 3 moves.")
    @Test
    public void forcePlayerToPlayQueenAfterThreeMoves() throws nl.hanze.hive.Hive.IllegalMove {
        main.init();
        //1
        main.play(Hive.Tile.SOLDIER_ANT, 0, 0);
        main.play(Hive.Tile.SOLDIER_ANT, 1, 0);
        //2
        main.play(Hive.Tile.SOLDIER_ANT, -1, 0);
        main.play(Hive.Tile.SOLDIER_ANT, 2, 0);
        //3
        main.play(Hive.Tile.SOLDIER_ANT, -2, 0);
        main.play(Hive.Tile.SOLDIER_ANT, 3, 0);
        //4
        expectedEx.expect(nl.hanze.hive.Hive.IllegalMove.class);
        expectedEx.expectMessage("Not a valid move.");
        main.play(Hive.Tile.BEETLE, -3, 0);
    }

    @DisplayName("5a Player can only move played pieces")
    @Test
    public void cannotImmidiatlyMove() throws nl.hanze.hive.Hive.IllegalMove {
        main.init();
        //1
        main.play(Hive.Tile.SOLDIER_ANT, 0, 0);
        //4
        expectedEx.expect(nl.hanze.hive.Hive.IllegalMove.class);
        expectedEx.expectMessage("Not a valid move.");
        main.move(0, 0, 1, 0);
    }

    @DisplayName("12 Player can only pass if moves = 0")
    @Test
    public void passTest() throws Hive.IllegalMove {
        Hive.Player player = main.model.getCurrentPlayer();
        main.pass();
        assertTrue(main.model.getCurrentPlayer() == player);
    }


}