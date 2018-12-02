package core.components;

/**
 * Created by Marinkton on 30-10-18.
 */
public class ActivePlayer {

    public boolean hasPlayedQueen = false;
    public int moves = 0;

    public ActivePlayer(boolean hasPlayedQueen, int moves) {
        this.hasPlayedQueen = hasPlayedQueen;
        this.moves = moves;
    }

    public void setHasPlayedQueen(boolean hasPlayedQueen) {
        this.hasPlayedQueen = hasPlayedQueen;
    }

    public void addMove() {
        this.moves++;
    }

    public void setMoves(int moves) { this.moves = moves;}

    public boolean hasPlayedQueen() {
        return hasPlayedQueen;
    }

    public int getMoves() {
        return moves;
    }
}