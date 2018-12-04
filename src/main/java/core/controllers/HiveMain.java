package core.controllers;

import core.components.Model;
import core.components.Piece;
import nl.hanze.hive.Hive;

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
        Piece pec = model.createPiece(tile, q, r);
        if (!model.getCurrentPlayer().getAvailableTiles().contains(tile)) {
            throw new IllegalMove("Player does not have this piece");
        }
        if (!model.isPlayAllowed(q, r)) {
            throw new IllegalMove("Not a valid play");
        }
        if (model.getCurrentPlayer().getAmountOfMovesMade() == 3 && !model.getCurrentPlayer().hasPlayedQueen()) {
            throw new IllegalMove("Need to play Queen after 3 moves");
        }
        // valid move.
        model.getBoard().add(pec);
        if (model.getCurrentPlayer().getPlayerColor() == BLACK) {
            model.getBlackPlayer().getAvailableTiles().remove(tile);
            model.getBlackPlayer().getPlayedPieces().add(pec);
            if (tile == Tile.QUEEN_BEE) {
                model.getBlackPlayer().setHasPlayedQueen(true);
            }
        } else {
            model.getWhitePlayer().getAvailableTiles().remove(tile);
            model.getWhitePlayer().getPlayedPieces().add(pec);
            if (tile == Tile.QUEEN_BEE) {
                model.getWhitePlayer().setHasPlayedQueen(true);
            }
        }
        model.swapTurn();
    }


    @Override
    public void move(int fromQ, int fromR, int toQ, int toR) throws IllegalMove {
        // Player can only move his own pieces.
        if (model.getCorrespondingPlayerByPiece(fromQ, fromR) != model.getCurrentPlayer().getPlayerColor()) {
            throw new IllegalMove("Cannot move other player's pieces");
        }

        if (!model.getCurrentPlayer().hasPlayedQueen()) {
            throw new IllegalMove("Have to play Queen before move is allowed");
        }

        if (model.isFloatingPiece(new Point(fromQ, fromR), new Point(toQ, toR))) {
            throw new IllegalMove("Move breaks connection");
        }

        ArrayList<Piece> piecesOnLocation = new ArrayList<>();
        for (Piece p : model.getBoard()) {
            if (p.getCenter().x == fromQ && p.getCenter().y == fromR) {
                piecesOnLocation.add(p);
            }
        }
        // Last piece is highest on the stack on that position
        Piece lastpiece = piecesOnLocation.get(piecesOnLocation.size() - 1);
        model.getBoard().remove(lastpiece);
        Point newPosition = new Point(toQ, toR);
        lastpiece.setCenter(newPosition);
        model.getBoard().add(lastpiece);

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
        return count == 2;
    }
}
