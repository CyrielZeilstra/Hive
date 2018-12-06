package core.controllers;

import core.components.Model;
import core.components.Piece;
import nl.hanze.hive.Hive;
import sun.font.TrueTypeFont;

import java.awt.*;
import java.util.ArrayList;

import static nl.hanze.hive.Hive.Player.BLACK;
import static nl.hanze.hive.Hive.Player.WHITE;

public class HiveMain implements Hive {

    public Model model;

    public HiveMain(Model m) {
        this.model = m;
    }

    @Override
    public void play(Tile tile, int q, int r) throws IllegalMove {
        if (!model.getCurrentPlayer().getAvailableTiles().contains(tile)) {
            throw new IllegalMove("Player does not have this piece");
        }
        if (!model.isPlayAllowed(q, r)) {
            throw new IllegalMove("Not a valid play");
        }
        if (model.getCurrentPlayer().getAmountOfMovesMade() == 3 && !model.getCurrentPlayer().hasPlayedQueen()) {
            throw new IllegalMove("Need to play Queen after 3 moves");
        }
        model.doPlay(tile, q, r);
    }


    @Override
    public void move(int fromQ, int fromR, int toQ, int toR) throws IllegalMove {
        // Player can only move his own pieces.
        if (!model.getBoardAsPoints().contains(new Point(fromQ,fromR))) {
            throw new IllegalMove("Piece does not exist on the board.");
        }

        if (model.getCorrespondingPlayerByPiece(fromQ, fromR) != model.getCurrentPlayer().getPlayerColor()) {
            throw new IllegalMove("Cannot move other player's pieces");
        }

        if (!model.getCurrentPlayer().hasPlayedQueen()) {
            throw new IllegalMove("Have to play Queen before move is allowed");
        }

        if (model.isFloatingPiece(new Point(fromQ, fromR), new Point(toQ, toR))) {
            throw new IllegalMove("Move breaks connection");
        }

        if (model.breaksConnection(new Point(fromQ, fromR), new Point(toQ, toR))) {
            throw new IllegalMove("Move creates floating island");
        }

//        if (!model.canSlideIn(new Point(fromQ, fromR), new Point(toQ, toR))) {
//            throw new IllegalMove("Move cannot slide in");
//        }
//
//        if (!model.isTouchingMove(new Point(fromQ, fromR), new Point(toQ, toR))) {
//            throw new IllegalMove("Not touching piece while moving");
//        }

        if (!model.canPieceMoveLikeThat(new Point(fromQ, fromR), new Point(toQ, toR))) {
            throw new IllegalMove("This piece is not allowed to move like that");
        }

        model.doMove(fromQ, fromR, toQ, toR);

        if (!isDraw()) {
            // No draw
            if (!isWinner(model.getCurrentPlayer().getPlayerColor())) {
                // No win, swap turn
                model.swapTurn();
            }
        }
    }

    public boolean playerCanPass() {
        int moves = model.getAvailablePlays().size();

        if (model.getCurrentPlayer().getPlayerColor() == WHITE && model.getWhitePlayer().hasPlayedQueen()) {
            for (Piece piece : model.getBoard()) {
                if (piece.getPlayer() == WHITE) {
                    moves = +model.getAvailableMovesSelectedBoardPiece(piece).size();
                }
            }
        } else if (model.getBlackPlayer().hasPlayedQueen()) {
            for (Piece piece : model.getBoard()) {
                if (piece.getPlayer() == BLACK) {
                    moves = +model.getAvailableMovesSelectedBoardPiece(piece).size();
                }
            }
        }

        return moves == 0;
    }

    @Override
    public void pass() throws IllegalMove {
        if (playerCanPass()) {
            if (!isWinner(model.getCurrentPlayer().getPlayerColor())) {
                model.swapTurn();
            }
        } else {
            throw new IllegalMove("You can't pass, because you have moves to play!");
        }
    }

    @Override
    public boolean isWinner(Player player) {
        for (Piece piece : model.getBoard()) {
            if (piece.getPiece() == Tile.QUEEN_BEE) {
                if (model.getNeighbours(piece.getCenter()).size() == 6) {
                    System.out.println(player + " won!");
                    return piece.getPlayer() != player;
                }
            }
        }
        return false;
    }

    @Override
    public boolean isDraw() {
        int count = 0;

        for (Piece piece : model.getBoard()) {
            if (piece.getPiece() == Tile.QUEEN_BEE) {
                if (model.getNeighbours(piece.getCenter()).size() == 6) {
                    count++;
                }
            }
        }
        if (count == 2) {
            System.out.println("Game was a draw!");
            return true;
        }
        return false;
    }
}
