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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class ExtensiveBoardTests {

    @Rule
    public ExpectedException exception = ExpectedException.none();
    private HiveMain hive;

    @Before
    public final void before() {
        hive = new HiveMain(new Model());
    }

    @DisplayName("Very large game test")
    @Test
    public void ElaborateBoardTest() throws Hive.IllegalMove {
        hive.play(Hive.Tile.SOLDIER_ANT, 0, 0);
        hive.play(Hive.Tile.SOLDIER_ANT, 1, 0);

        hive.play(Hive.Tile.QUEEN_BEE, 0, -1);
        hive.play(Hive.Tile.QUEEN_BEE, 2, 0);

        assertEquals(2, hive.model.getQueenMoves(new Point(0, -1)).size());
        assertEquals(0, hive.model.getAntMoves(new Point(0, 0)).size());

        hive.play(Hive.Tile.GRASSHOPPER, 1, -2);
        hive.play(Hive.Tile.SPIDER, 2, -1);

        assertEquals(1, hive.model.getGrasshopperMoves(new Point(1, -2)).size());
        assertEquals(6, hive.model.getAvailablePlays().size());

        hive.play(Hive.Tile.SOLDIER_ANT, 2, -3);
        assertEquals(2, hive.model.getSpiderMoves(new Point(2, -1)).size());
        hive.play(Hive.Tile.BEETLE, 1, 1);

        assertEquals(13, hive.model.getAntMoves(new Point(2, -3)).size());
        hive.move(2, -3, 2, -2);
        hive.move(1, 1, 2, 0);
        hive.move(1, -2, -1, 0);
        hive.play(Hive.Tile.BEETLE, 3, -1);

        hive.move(2, -2, -2, 1);
        hive.move(3, -1, 2, 0);

        hive.play(Hive.Tile.BEETLE, -3, 2);
        hive.play(Hive.Tile.SPIDER, 3, -1);

        assertEquals(10, hive.model.getAvailablePlays().size());
        hive.play(Hive.Tile.SPIDER, -2, 0);
    }

    @DisplayName("Very large game test")
    @Test
    public void SecondElaborateBoardTest() throws Hive.IllegalMove {
        hive.play(Hive.Tile.QUEEN_BEE, 50, 50);
        hive.play(Hive.Tile.QUEEN_BEE, 51, 50);

        hive.play(Hive.Tile.SOLDIER_ANT, 49, 50);
        hive.play(Hive.Tile.SOLDIER_ANT, 52, 50);

        hive.play(Hive.Tile.SOLDIER_ANT, 49, 49);
        hive.play(Hive.Tile.SOLDIER_ANT, 53, 49);

        hive.play(Hive.Tile.SOLDIER_ANT, 50, 48);
        hive.play(Hive.Tile.SOLDIER_ANT, 53, 48);

        hive.play(Hive.Tile.SPIDER, 51, 47);

        assertEquals(19, hive.model.getAntMoves(new Point(53, 48)).size());

        hive.play(Hive.Tile.SPIDER, 53, 47);
        hive.move(51, 47, 51, 49);
        hive.move(53, 47, 51, 47);


        hive.play(Hive.Tile.GRASSHOPPER, 50, 49);
        hive.play(Hive.Tile.SPIDER, 52, 47);

        assertEquals(16, hive.model.getAntMoves(new Point(49, 49)).size());
        assertEquals(2, hive.model.getQueenMoves(new Point(50, 50)).size());
        assertEquals(5, hive.model.getGrasshopperMoves(new Point(50, 49)).size());
        assertEquals(5, hive.model.getGrasshopperMoves(new Point(50, 49)).size());

        hive.move(50,49,50,51);
    }
}
