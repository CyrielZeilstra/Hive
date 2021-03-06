package core.controllers;

import core.components.Model;
import core.components.Piece;
import nl.hanze.hive.Hive;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GamelogicTests {

    @Rule
    public ExpectedException exception = ExpectedException.none();
    private HiveMain hive;

    @Before
    public final void before() {
        hive = new HiveMain();
    }

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
        assertTrue(!hive.isWinner(Hive.Player.WHITE));
        assertTrue(!hive.isWinner(Hive.Player.BLACK));
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
        hive.play(Hive.Tile.QUEEN_BEE, -3, 0);

        //4
//        exception.expect(nl.hanze.hive.Hive.IllegalMove.class);
//        exception.expectMessage("Need to play Queen after 3 moves");
//        hive.play(Hive.Tile.BEETLE, -3, 0);
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

    @DisplayName("5c Een steen moet na het verplaatsen in contact zijn met minstens één andere steen.")
    @Test
    public void pieceMoveDoesNotCreateSingleFloatingPiece() throws nl.hanze.hive.Hive.IllegalMove {
        hive.play(Hive.Tile.QUEEN_BEE, 0, 0);
        hive.play(Hive.Tile.QUEEN_BEE, -1, 0);
        hive.play(Hive.Tile.SOLDIER_ANT, 1, 0);
        hive.play(Hive.Tile.SOLDIER_ANT, -1, -1);
        exception.expect(nl.hanze.hive.Hive.IllegalMove.class);
        exception.expectMessage("Move breaks connection");
        hive.move(1, 0, 2, 0);
    }

    @DisplayName("5d Een steen mag niet verplaatst worden als er door het weghalen van de steen twee niet onderling verbonden groepen stenen ontstaan.")
    @Test
    public void pieceMoveDoesNotCreateIsland() throws nl.hanze.hive.Hive.IllegalMove {
        hive.play(Hive.Tile.QUEEN_BEE, 0, 0);
        hive.play(Hive.Tile.QUEEN_BEE, -1, 0);
        hive.play(Hive.Tile.SOLDIER_ANT, 1, 0);
        hive.play(Hive.Tile.SOLDIER_ANT, -2, 0);
        exception.expect(nl.hanze.hive.Hive.IllegalMove.class);
        exception.expectMessage("Move creates floating island");
        hive.move(0, 0, 1, -1);
    }

    @DisplayName("6b Een verschuiving moet schuivend uitgevoerd kunnen worden.")
    @Test
    public void pieceHasToSlideIn() throws nl.hanze.hive.Hive.IllegalMove {
        hive.play(Hive.Tile.QUEEN_BEE, 0, 0);
        hive.play(Hive.Tile.QUEEN_BEE, 1, 0);
        hive.play(Hive.Tile.SOLDIER_ANT, 0, -1);
        hive.play(Hive.Tile.SOLDIER_ANT, 2, -1);
        hive.play(Hive.Tile.SOLDIER_ANT, 0, -2);
        hive.play(Hive.Tile.SOLDIER_ANT, 2, -2);
        hive.move(0,-2, 1, -2);
        exception.expect(nl.hanze.hive.Hive.IllegalMove.class);
        exception.expectMessage("This piece is not allowed to move like that");
        hive.move(2, -2, 1, -1);
    }

    @DisplayName("6b Een verschuiving moet schuivend uitgevoerd kunnen worden Beetle can move in anyway.")
    @Test
    public void pieceHasToSlideInTwo() throws nl.hanze.hive.Hive.IllegalMove {
        //W
        hive.play(Hive.Tile.QUEEN_BEE, 0, 0);

        //B
        hive.play(Hive.Tile.QUEEN_BEE, 1, 0);

        //W
        hive.play(Hive.Tile.SOLDIER_ANT, 0, -1);

        //B
        hive.play(Hive.Tile.SOLDIER_ANT, 2, -1);

        //W
        hive.play(Hive.Tile.SOLDIER_ANT, 0, -2);

        //B
        hive.play(Hive.Tile.SOLDIER_ANT, 3, -2);

        //W
        hive.move(0,-2, 1, -1);
//        hive.play(Hive.Tile.BEETLE, 1, -2);
//        hive.move(3,-2, 2, -2);
//        exception.expect(nl.hanze.hive.Hive.IllegalMove.class);
//        exception.expectMessage("This piece is not allowed to move like that");
//        hive.move(1, -2, 1, -1);
    }


    @DisplayName("6c Tijdens een verschuiving moet de steen continu in contact blijven met minstens één andere steen.")
    @Test
    public void pieceHasToKeepTouching() throws nl.hanze.hive.Hive.IllegalMove {
        hive.play(Hive.Tile.QUEEN_BEE, 0, 0);
        hive.play(Hive.Tile.QUEEN_BEE, -1, 0);
        hive.play(Hive.Tile.SOLDIER_ANT, 0, 1);
        hive.play(Hive.Tile.SOLDIER_ANT, -2, 1);
        hive.play(Hive.Tile.BEETLE, -1, 2);
        hive.play(Hive.Tile.SOLDIER_ANT, -2, 0);
        exception.expect(nl.hanze.hive.Hive.IllegalMove.class);
        exception.expectMessage("This piece is not allowed to move like that");
        hive.move(-1, 2, -2, 2);
    }

    @DisplayName("12.1 Een speler mag alleen passen als hij geen enkele steen kan spelen of verplaatsen")
    @Test
    public void PlayerCanOnlyPassIfNoPieceCanBePlayedOrMoved1() throws nl.hanze.hive.Hive.IllegalMove {
        hive.play(Hive.Tile.QUEEN_BEE, 0, 0);
        hive.play(Hive.Tile.QUEEN_BEE, -1, 0);
        exception.expect(nl.hanze.hive.Hive.IllegalMove.class);
        exception.expectMessage("You can't pass, because you have pieces to play or you can move a piece");
        hive.pass();
    }

    @DisplayName("12.2 Een speler mag alleen passen als hij geen enkele steen kan spelen of verplaatsen")
    @Test
    public void PlayerCanOnlyPassIfNoPieceCanBePlayedOrMoved2() throws nl.hanze.hive.Hive.IllegalMove {
        hive.play(Hive.Tile.QUEEN_BEE, 0, 0);
        hive.play(Hive.Tile.QUEEN_BEE, -1, 0);

        hive.play(Hive.Tile.SOLDIER_ANT,1,0);
        hive.play(Hive.Tile.SOLDIER_ANT,-2,0);
        hive.play(Hive.Tile.SOLDIER_ANT,2,0);
        hive.play(Hive.Tile.SOLDIER_ANT,-3,0);
        hive.play(Hive.Tile.SOLDIER_ANT,3,0);
        hive.play(Hive.Tile.SOLDIER_ANT,-4,0);

        hive.play(Hive.Tile.BEETLE,4,0);
        hive.play(Hive.Tile.BEETLE,-5,0);
        hive.play(Hive.Tile.BEETLE,5,0);
        hive.play(Hive.Tile.BEETLE,-6,0);

        hive.play(Hive.Tile.GRASSHOPPER,6,0);
        hive.play(Hive.Tile.GRASSHOPPER,-7,0);
        hive.play(Hive.Tile.GRASSHOPPER,7,0);
        hive.play(Hive.Tile.GRASSHOPPER,-8,0);

        hive.play(Hive.Tile.SPIDER,8,0);
        hive.play(Hive.Tile.SPIDER,-9,0);
        hive.play(Hive.Tile.SPIDER,9,0);
        hive.play(Hive.Tile.SPIDER,-10,0);

        exception.expect(nl.hanze.hive.Hive.IllegalMove.class);
        exception.expectMessage("You can't pass, because you have pieces to play or you can move a piece");
        hive.pass();
    }
}
