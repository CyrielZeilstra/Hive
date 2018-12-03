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

//    public void showAvailableMovesSpots(Piece pe) {
//        for (Point p : model.getAvailableMovesSelectedBoardPiece(pe)) {
//            // [0] = Point in model aka 1,-1
//            // [1] = Circle with board coards.
//            Point modelPoint = new Point(p.x, p.y);
//        }
//    }
//
//    public void showAvailablePlaySpots() throws IllegalMove {
//        for (Point p : model.getAvailablePlaySpots()) {
//            Point modelPoint = new Point(p.x, p.y);
//
//            if (model.getCurrentPlayer() == Hive.PlayerModel.BLACK) {
//                play((Tile) model.getSelectedPiece(), modelPoint.x, modelPoint.y);
//
//                model.getBlackPlayer().getAvailableTiles().remove(model.getSelectedPiece());
//            } else {
//                play((Tile) model.getSelectedPiece(), modelPoint.x, modelPoint.y);
//
//                if (model.getSelectedPiece() == Tile.QUEEN_BEE) {
//                    model.getWhitePlayer().setHasPlayedQueen(true);
//                }
//                model.getWhitePlayer().getAvailableTiles().remove(model.getSelectedPiece());
//
//            }
//        }
//    }

    @Override
    public void play(Tile tile, int q, int r) throws IllegalMove {
        Piece pec = model.createPiece(tile, q, r);
        if (!model.getCurrentPlayer().getAvailableTiles().contains(tile)) {
            throw new IllegalMove("Player does not have this piece");
        }
        if (!model.isPlayAllowed(q, r)) {
            throw new IllegalMove("Not a valid play.");
        }
        // valid move.
        model.getBoard().add(pec);
        if (model.getCurrentPlayer().getPlayerColor() == BLACK) {
            model.getBlackPlayer().getAvailableTiles().remove(tile);
            model.getBlackPlayer().getPlayedPieces().add(pec);
        } else {
            model.getWhitePlayer().getAvailableTiles().remove(tile);
            model.getWhitePlayer().getPlayedPieces().add(pec);
        }
        model.swapTurn();
    }


    @Override
    public void move(int fromQ, int fromR, int toQ, int toR) throws IllegalMove {
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

        if (!isWinner(model.getCurrentPlayer().getPlayerColor())) {
            model.swapTurn();
        } else {
            System.out.println(model.getCurrentPlayer().getPlayerColor() + " wins!");
        }
    }

    public boolean playerCanPass() {
        int moves = model.getAvailablePlaySpots().size();

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
        if (!isDraw()) {
            for (Piece piece : model.getBoard()) {
                if (piece.getPiece() == Tile.QUEEN_BEE) {
                    if (model.getNeighbours(piece.getCenter()).size() == 6) {
                        if (piece.getPlayer() != player) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                }
            }
        } else if (isDraw()) {
            System.out.println("It's a draw!");
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
