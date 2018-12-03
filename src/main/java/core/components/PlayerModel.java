package core.components;

import nl.hanze.hive.Hive;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marinkton on 30-10-18.
 */
public class PlayerModel {

    private boolean hasPlayedQueen = false;
    private int amountOfMovesMade = 0;
    private ArrayList<Hive.Tile> availableTiles;
    private ArrayList<Piece> playedPieces = new ArrayList<>();
    private Hive.Player playerColor;

    public PlayerModel(boolean hasPlayedQueen, int amountOfMovesMade, Hive.Player playerColor, ArrayList<Hive.Tile> availableTiles) {
        this.hasPlayedQueen = hasPlayedQueen;
        this.amountOfMovesMade = amountOfMovesMade;
        this.playerColor = playerColor;
        this.availableTiles = availableTiles;
    }

    public void setHasPlayedQueen(boolean hasPlayedQueen) {
        this.hasPlayedQueen = hasPlayedQueen;
    }

    public void addMove() {
        this.amountOfMovesMade++;
    }

    public void setAmountOfMovesMade(int amountOfMovesMade) {
        this.amountOfMovesMade = amountOfMovesMade;
    }

    public boolean hasPlayedQueen() {
        return hasPlayedQueen;
    }

    public int getAmountOfMovesMade() {
        return amountOfMovesMade;
    }

    public ArrayList<Hive.Tile> getAvailableTiles() {
        return availableTiles;
    }

    public void setAvailableTiles(ArrayList<Hive.Tile> availableTiles) {
        this.availableTiles = availableTiles;
    }

    public boolean isHasPlayedQueen() {
        return hasPlayedQueen;
    }

    public Hive.Player getPlayerColor() {
        return playerColor;
    }

    public List<Piece> getPlayedPieces() {
        return playedPieces;
    }
}