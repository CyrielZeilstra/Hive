package core.components;

import nl.hanze.hive.Hive;

import java.util.List;

/**
 * Created by Marinkton on 30-10-18.
 */
public class PlayerModel {

    private boolean hasPlayedQueen = false;
    private int amountOfMovesMade = 0;
    private List<Hive.Tile> availablePieces;
    private Hive.Player playerColor;

    public PlayerModel(boolean hasPlayedQueen, int amountOfMovesMade, Hive.Player playerColor, List<Hive.Tile> availablePieces) {
        this.hasPlayedQueen = hasPlayedQueen;
        this.amountOfMovesMade = amountOfMovesMade;
        this.playerColor = playerColor;
        this.availablePieces = availablePieces;
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

    public List<Hive.Tile> getAvailablePieces() {
        return availablePieces;
    }

    public void setAvailablePieces(List<Hive.Tile> availablePieces) {
        this.availablePieces = availablePieces;
    }

    public boolean isHasPlayedQueen() {
        return hasPlayedQueen;
    }

    public Hive.Player getPlayerColor() {
        return playerColor;
    }
}