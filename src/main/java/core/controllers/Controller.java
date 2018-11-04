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

public class Controller implements Hive {

    public Model model;

    public List<Enum> Pieces = Arrays.asList(Tile.QUEEN_BEE,
            Tile.SPIDER, Tile.SPIDER,
            Tile.BEETLE, Tile.BEETLE,
            Tile.GRASSHOPPER, Tile.GRASSHOPPER, Tile.GRASSHOPPER,
            Tile.SOLDIER_ANT, Tile.SOLDIER_ANT, Tile.SOLDIER_ANT);

    public ActivePlayer whitePlayer = new ActivePlayer(false, 0);
    public ActivePlayer blackPlayer = new ActivePlayer(false, 0);

    Scanner gameHandler;

    public Controller(Model m) {
        this.model = m;
    }

    private void handleMove() {
        System.out.print("What do you want to do : ");
        System.out.println("[0]Play [1]Move [2]Pass");
        System.out.print("Choice : ");
        int choice = gameHandler.nextInt();
        try {
            switch (choice) {
                case 0:
                    // play
                    if (model.getCurrentPlayer() == Player.BLACK) {
                        System.out.print("Please pick a piece : ");
                        int pieceIndex = gameHandler.nextInt();
                        model.setSelectedPiece(model.getBlackAvailablePieces().get(pieceIndex));

                        System.out.print("Please pick a playspot : ");
                        int moveIndex = gameHandler.nextInt();

                        int q = model.getAvailablePlaySpots().get(moveIndex).x;
                        int r = model.getAvailablePlaySpots().get(moveIndex).y;
                        Enum pieceToPlay = model.getSelectedPiece();
                        try {
                            play((Tile) pieceToPlay, q, r);
                        } catch (IllegalMove illegalMove) {
                            System.out.println(illegalMove.getMessage());
                        }
                    } else {
                        System.out.print("Please pick a piece : ");
                        int pieceIndex = gameHandler.nextInt();
                        model.setSelectedPiece(model.getWhiteAvailablePieces().get(pieceIndex));

                        System.out.print("Please pick a move : ");
                        int moveIndex = gameHandler.nextInt();

                        int q = model.getAvailablePlaySpots().get(moveIndex).x;
                        int r = model.getAvailablePlaySpots().get(moveIndex).y;
                        Enum pieceToPlay = model.getSelectedPiece();
                        try {
                            play((Tile) pieceToPlay, q, r);
                        } catch (IllegalMove illegalMove) {
                            System.out.println(illegalMove.getMessage());
                        }
                    }
                    break;
                case 1:
                    // move
                    try {
                        System.out.println("Please select a piece to move: ");
                        ArrayList<Piece> playerPieces = new ArrayList<>();
                        for (Piece p : model.getBoard()) {
                            if (p.getPlayer() == model.getCurrentPlayer()) {
                                playerPieces.add(p);
                            }
                        }

                        if (!playerPieces.isEmpty()) {
                            for (int i = 0; i < playerPieces.size(); i++) {
                                System.out.print("[" + i + "]" + "(" + playerPieces.get(i).getCenter().x + "," + playerPieces.get(i).getCenter().y + ")" + playerPieces.get(i).getPiece() + " ");
                            }
                            System.out.println();
                            System.out.println("Choice : ");
                            int movePieceIndex = gameHandler.nextInt();
                            Piece p = model.getBoard().get(movePieceIndex);
                            System.out.println("Moves for this piece: ");
                            ArrayList<Point> moves = model.getAvailableMovesSelectedBoardPiece(p);
                            if (!moves.isEmpty()) {
                                for (int i = 0; i < moves.size(); i++) {
                                    System.out.print("[" + i + "]" + "(" + moves.get(i).x + "," + moves.get(i).y + ") ");
                                }
                                System.out.print("Where to move ?");
                                int finalMoveIndex = gameHandler.nextInt();
                                int fromQ = p.getCenter().x;
                                int fromR = p.getCenter().y;
                                int toQ = moves.get(finalMoveIndex).x;
                                int toR = moves.get(finalMoveIndex).y;
                                move(fromQ, fromR, toQ, toR);
                            } else {
                                System.out.println("This piece has no available moves.");
                            }
                        } else {
                            System.out.println("This piece has no moves or you dont have any pieces that can move!");
                            break;
                        }
                    } catch (IllegalMove illegalMove) {
                        System.out.println(illegalMove.getMessage());
                    }
                    break;
                case 2:
                    try {
                        pass();
                    } catch (IllegalMove illegalMove) {
                        System.out.println(illegalMove.getMessage());
                    }
                    break;
            }
        } catch (Exception e) {
            System.out.println("Illegal input");
        }
        printGameState();

    }

    public void printBoard() {
        if (!model.getBoardAsPoints().isEmpty()) {
            System.out.println("-----------Board----------");
            for (int i = 0; i < model.getBoard().size(); i++) {
                System.out.println("(" + model.getBoard().get(i).getCenter().x
                        + "," + model.getBoard().get(i).getCenter().y + ") "
                        + model.getBoard().get(i).getPiece() + " Player : "
                        + model.getBoard().get(i).getPlayer());
            }
            System.out.println("--------------------------");
        } else {
            System.out.println("Board: Empty");
        }
    }

    private void printGameState() {

        printBoard();

        if (model.getCurrentPlayer() == Player.BLACK) {
            System.out.println("Current Player : " + model.getCurrentPlayer());

            System.out.print("Available playspots : ");
            for (int i = 0; i < model.getAvailablePlaySpots().size(); i++) {
                System.out.print("[" + i + "]" + "(" + model.getAvailablePlaySpots().get(i).x + "," + model.getAvailablePlaySpots().get(i).y + ") ");
            }
            System.out.println();
            System.out.print("Available pieces : ");
            for (int i = 0; i < model.getBlackAvailablePieces().size(); i++) {
                System.out.print("[" + i + "] " + model.getBlackAvailablePieces().get(i).toString());
            }
            System.out.println();
            handleMove();
        } else {
            System.out.println("Current Player : " + model.getCurrentPlayer());

            System.out.print("Available playspots : ");
            for (int i = 0; i < model.getAvailablePlaySpots().size(); i++) {
                System.out.print("[" + i + "]" + "(" + model.getAvailablePlaySpots().get(i).x + "," + model.getAvailablePlaySpots().get(i).y + ") ");
            }
            System.out.println();
            System.out.print("Available pieces : ");
            for (int i = 0; i < model.getWhiteAvailablePieces().size(); i++) {
                System.out.print("[" + i + "] " + model.getWhiteAvailablePieces().get(i).toString());
            }
            System.out.println();
            handleMove();
        }
    }

    public void newGame() {
        // Reset the model
        whitePlayer.setHasPlayedQueen(false);
        whitePlayer.setMoves(0);
        blackPlayer.setHasPlayedQueen(false);
        blackPlayer.setMoves(0);

        if (model.getCurrentPlayer() == Player.BLACK) {
            swapTurn();
        }

        model.setCurrentPlayer(Player.WHITE);
        model.getWhiteAvailablePieces().clear();
        model.getWhiteAvailablePieces().addAll(Pieces);

        model.getBlackAvailablePieces().clear();
        model.getBlackAvailablePieces().addAll(Pieces);
        model.getBoard().clear();

        gameHandler = new Scanner(System.in);
        System.out.println("New hive Game");
        printGameState();
    }

    public void showAvailableMovesSpots(Piece pe) {
        for (Point p : model.getAvailableMovesSelectedBoardPiece(pe)) {
            // [0] = Point in model aka 1,-1
            // [1] = Circle with board coards.
            Point modelPoint = new Point(p.x, p.y);
        }
    }

    public void test() {
        isWinner(model.getCurrentPlayer());
    }

    public void showAvailablePlaySpots() throws IllegalMove {
        for (Point p : model.getAvailablePlaySpots()) {
            Point modelPoint = new Point(p.x, p.y);

            if (model.getCurrentPlayer() == Player.BLACK) {
                play((Tile) model.getSelectedPiece(), modelPoint.x, modelPoint.y);
                if (model.getSelectedPiece() == Tile.QUEEN_BEE) {
                    blackPlayer.setHasPlayedQueen(true);
                }
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

//            check if player has placed the queen before move if not show only the queen
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
        Point point = new Point(q, r);
        if (model.getAvailableMovesSelectedBoardPiece(pec).contains(point) || model.getAvailablePlaySpots().contains(point)) {
            // valid move.
            model.getBoard().add(pec);
            Player p = model.getCurrentPlayer();
            if (p == Player.BLACK) {
                model.getBlackAvailablePieces().remove(tile);
            } else {
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
        lastpiece.setSelected(false);
        model.getBoard().add(lastpiece);

        if(!isWinner(model.getCurrentPlayer())){
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
                        if (piece.getPlayer() == player && piece.getPlayer() == player.BLACK) {
                            System.out.println("White player won the game!");
                        } else {
                            System.out.println("Black player won the game!");
                        }
                        return true;
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
