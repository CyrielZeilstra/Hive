package core.components;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marinkton on 30-10-18.
 */
public class Player {

    public boolean hasPlayedQueen = false;
    public int amountOfMovesMade = 0;
    List<Piece> availablePieces;
    Enum color;

    public Player(boolean hasPlayedQueen, int amountOfMovesMade, Enum color, List availablePieces) {
        this.hasPlayedQueen = hasPlayedQueen;
        this.amountOfMovesMade = amountOfMovesMade;
        this.color = color;
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

    public List<Piece> getAvailablePieces() {
        return availablePieces;
    }

    public void setAvailablePieces(List<Piece> availablePieces) {
        this.availablePieces = availablePieces;
    }
}