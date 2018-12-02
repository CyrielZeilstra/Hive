package core.controllers;

import core.components.Model;
import core.components.Piece;
import nl.hanze.hive.Hive;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HiveMain implements Hive {

    public Model model;
    public HiveMain(Model m) {
        this.model = m;
    }

    public void showAvailableMovesSpots(Piece pe) {
        for (Point p : model.getAvailableMovesSelectedBoardPiece(pe)) {
            // [0] = Point in model aka 1,-1
            // [1] = Circle with board coards.
            Point modelPoint = new Point(p.x, p.y);
        }
    }

    public void showAvailablePlaySpots() throws IllegalMove {
        for (Point p : model.getAvailablePlaySpots()) {
            Point modelPoint = new Point(p.x, p.y);

            if (model.getCurrentPlayer() == Hive.Player.BLACK) {
                play((Tile) model.getSelectedPiece(), modelPoint.x, modelPoint.y);

                model.getBlack().getAvailablePieces().remove(model.getSelectedPiece());
            } else {
                play((Tile) model.getSelectedPiece(), modelPoint.x, modelPoint.y);

                if (model.getSelectedPiece() == Tile.QUEEN_BEE) {
                    model.getWhite().setHasPlayedQueen(true);
                }
                model.getWhite().getAvailablePieces().remove(model.getSelectedPiece());

            }
        }
    }

    public void swapTurn() {
        if (model.getCurrentPlayer() == Hive.Player.WHITE) {
            System.out.println("Black Player it's your turn!");
            model.setCurrentPlayer(Hive.Player.BLACK);
            model.getWhite().addMove();

            // check if player has placed the queen before move if not show only the queen
            if (model.getBlack().getAmountOfMovesMade() == 3 && !model.getBlack().hasPlayedQueen()) {
                List<Enum> blackQueen = Arrays.asList(Tile.QUEEN_BEE);
            }
        } else if (model.getCurrentPlayer() == Hive.Player.BLACK) {
            System.out.println("White Player it's your turn!");
            model.setCurrentPlayer(Hive.Player.WHITE);
            model.getBlack().addMove();
            if (model.getWhite().getAmountOfMovesMade() == 3 && !model.getWhite().hasPlayedQueen()) {
                List<Enum> whiteQueen = Arrays.asList(Tile.QUEEN_BEE);
            }

        }
    }

    @Override
    public void play(Tile tile, int q, int r) throws IllegalMove {
        Piece pec = model.createPiece(tile, q, r);
        if (model.getAvailableMovesSelectedBoardPiece(pec).contains(new Point(q, r)) || model.getAvailablePlaySpots().contains(new Point(q, r))) {
            // valid move.
            model.getBoard().add(pec);
            Player p = model.getCurrentPlayer();
            if (p == Hive.Player.BLACK) {
                if (model.getSelectedPiece() == Tile.QUEEN_BEE) {
                    model.getBlack().setHasPlayedQueen(true);
                }
                model.getBlack().getAvailablePieces().remove(tile);
            } else {
                if (model.getSelectedPiece() == Tile.QUEEN_BEE) {
                    model.getWhite().setHasPlayedQueen(true);
                }
                model.getWhite().getAvailablePieces().remove(tile);
            }
            swapTurn();
        } else {
            throw new IllegalMove("Not a valid move.");
        }
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

        if (!isWinner(model.getCurrentPlayer())) {
            swapTurn();
        } else {
            System.exit(1);
        }
    }

    public boolean playerCanPass() {
        int moves = model.getAvailablePlaySpots().size();

        if (model.getCurrentPlayer() == Hive.Player.WHITE && model.getWhite().hasPlayedQueen()) {
            for (Piece piece : model.getBoard()) {
                if (piece.getPlayer() == Hive.Player.WHITE) {
                    moves = +model.getAvailableMovesSelectedBoardPiece(piece).size();
                }
            }
        } else if (model.getBlack().hasPlayedQueen()) {
            for (Piece piece : model.getBoard()) {
                if (piece.getPlayer() == Hive.Player.BLACK) {
                    moves = +model.getAvailableMovesSelectedBoardPiece(piece).size();
                }
            }
        }

        return moves == 0;
    }

    @Override
    public void pass() throws IllegalMove {
        if (playerCanPass()) {
            if (!isWinner(model.getCurrentPlayer())) {
                swapTurn();
            }
        } else {
            throw new IllegalMove("You can't pass, because you have amountOfMovesMade to play!");
        }
    }

    @Override
    public boolean isWinner(Player player) {
        if (!isDraw()) {
            for (Piece piece : model.getBoard()) {
                if (piece.getPiece() == Tile.QUEEN_BEE) {
                    if (model.getNeighbours(piece.getCenter()).size() == 6) {
                        if (piece.getPlayer() == player) {
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
