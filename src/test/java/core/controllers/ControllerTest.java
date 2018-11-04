package core.controllers;

import core.components.Model;
import core.components.Piece;
import nl.hanze.hive.Hive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

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

    @DisplayName("4a can only play own pieces")
    @Test
    public void canOnlyPlayOwnPieces() {
        main.init();

    }



}