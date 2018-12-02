package core.controllers;

import core.components.ActivePlayer;
import core.components.Model;
import core.components.Piece;
import nl.hanze.hive.Hive;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class HiveMain implements Hive {

    public Model model;

    public ActivePlayer whitePlayer = new ActivePlayer(false, 0);
    public ActivePlayer blackPlayer = new ActivePlayer(false, 0);


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

            if (model.getCurrentPlayer() == Player.BLACK) {
                play((Tile) model.getSelectedPiece(), modelPoint.x, modelPoint.y);

                model.getBlackAvailablePieces().remove(model.getSelectedPiece());
            } else {
                play((Tile) model.getSelectedPiece(), modelPoint.x, modelPoint.y);

                if (model.getSelectedPiece() == Tile.QUEEN_BEE) {
                    whitePlayer.setHasPlayedQueen(true);
                }
                model.getWhiteAvailablePieces().remove(model.getSelectedPiece());

            }
        }
    }

    public void swapTurn() {
        if (model.getCurrentPlayer() == Player.WHITE) {
            System.out.println("Black Player it's your turn!");
            model.setCurrentPlayer(Player.BLACK);
            whitePlayer.addMove();

            // check if player has placed the queen before move if not show only the queen
            if (blackPlayer.getMoves() == 3 && !blackPlayer.hasPlayedQueen()) {
                List<Enum> blackQueen = Arrays.asList(Tile.QUEEN_BEE);
            }
        } else if (model.getCurrentPlayer() == Player.BLACK) {
            System.out.println("White Player it's your turn!");
            model.setCurrentPlayer(Player.WHITE);
            blackPlayer.addMove();
            if (whitePlayer.getMoves() == 3 && !whitePlayer.hasPlayedQueen()) {
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
            if (p == Player.BLACK) {
                if (model.getSelectedPiece() == Tile.QUEEN_BEE) {
                    blackPlayer.setHasPlayedQueen(true);
                }
                model.getBlackAvailablePieces().remove(tile);
            } else {
                if (model.getSelectedPiece() == Tile.QUEEN_BEE) {
                    whitePlayer.setHasPlayedQueen(true);
                }
                model.getWhiteAvailablePieces().remove(tile);
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

        if (model.getCurrentPlayer() == Player.WHITE && whitePlayer.hasPlayedQueen()) {
            for (Piece piece : model.getBoard()) {
                if (piece.getPlayer() == Player.WHITE) {
                    moves = +model.getAvailableMovesSelectedBoardPiece(piece).size();
                }
            }
        } else if (blackPlayer.hasPlayedQueen()) {
            for (Piece piece : model.getBoard()) {
                if (piece.getPlayer() == Player.BLACK) {
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
            throw new IllegalMove("You can't pass, because you have moves to play!");
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
