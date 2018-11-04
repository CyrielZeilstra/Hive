package controllers;

public class Controller {
    package nl.hanze.hive.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Pair;
import nl.hanze.hive.components.ActivePlayer;
import nl.hanze.hive.components.Model;
import nl.hanze.hive.components.Piece;
import nl.hanze.hive.interfaces.Hive;

import java.awt.*;

import javafx.scene.control.Label;

import java.util.*;
import java.util.List;

    public class Controller implements Hive {

        public ListView blackPiecesListView;
        public ListView whitePiecesListView;
        public Label hint;
        public Pane boardView;
        public Model model;

        public List<Enum> Pieces = Arrays.asList(Tile.QUEEN_BEE,
                Tile.SPIDER, Tile.SPIDER,
                Tile.BEETLE, Tile.BEETLE,
                Tile.GRASSHOPPER, Tile.GRASSHOPPER, Tile.GRASSHOPPER,
                Tile.SOLDIER_ANT, Tile.SOLDIER_ANT, Tile.SOLDIER_ANT);

        public Button blackPassBtn;
        public Button whitePassBtn;

        public ActivePlayer whitePlayer = new ActivePlayer(false, 0);
        public ActivePlayer blackPlayer = new ActivePlayer(false, 0);

        HashMap<Point, Point> boardRepresentations = new HashMap<>();

        public Controller(Model m) {
            this.model = m;
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
            model.getWhiteAvailablePieces().setAll(Pieces);
            model.getBlackAvailablePieces().setAll(Pieces);
            blackPiecesListView.setDisable(true);
            whitePiecesListView.setDisable(false);
            model.getBoard().clear();

            resetView();

            ChangeListener e = new ChangeListener() {
                @Override
                public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                    model.setSelectedPiece((Enum) newValue);
                    showAvailablePlaySpots();
                }
            };

            EventHandler passBtnEvent = new EventHandler() {
                @Override
                public void handle(Event event) {
                    try {
                        String id = ((Button) event.getSource()).getId();
                        if (model.getCurrentPlayer() == Player.BLACK && id.equals("blackPassBtn")) {
                            pass();
                        } else if (model.getCurrentPlayer() == Player.WHITE && id.equals("whitePassBtn"))
                            pass();
                    } catch (IllegalMove illegalMove) {
                        illegalMove.printStackTrace();
                    }
                }
            };

            blackPassBtn.setOnAction(passBtnEvent);
            whitePassBtn.setOnAction(passBtnEvent);

            blackPiecesListView.getSelectionModel().selectedItemProperty().addListener(e);
            whitePiecesListView.getSelectionModel().selectedItemProperty().addListener(e);

            redrawBoard();
            whitePiecesListView.getSelectionModel().select(0);
        }

        public void showAvailableMovesSpots(Piece pe) {
            for (Point p : model.getAvailableMovesSelectedBoardPiece(pe)) {
                // [0] = Point in model aka 1,-1
                // [1] = Circle with board coards.
                Point boardPoint = model.calculateBoardRepresentation(p.x, p.y);
                Point modelPoint = new Point(p.x, p.y);
                Circle moveMarker = new Circle(boardPoint.x, boardPoint.y, 8, Color.CYAN);
                boardRepresentations.put(boardPoint, modelPoint);
                moveMarker.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        try {
                            move(pe.getCenter().x, pe.getCenter().y, p.x, p.y);
                        } catch (IllegalMove illegalMove) {
                            illegalMove.printStackTrace();
                        }
                    }
                });
                boardView.getChildren().add(moveMarker);
            }
        }

        public void resetView() {
            blackPiecesListView.setItems(model.getBlackAvailablePieces());
            whitePiecesListView.setItems(model.getWhiteAvailablePieces());
        }


        public void test() {
            isWinner(model.getCurrentPlayer());
        }

        public Point getBoardPoint(Point modelPoint) {
            // find point
            return boardRepresentations.get(modelPoint);
        }

        public void showAvailablePlaySpots() {
            for (Point p : model.getAvailablePlaySpots()) {
                Point boardPoint = model.calculateBoardRepresentation(p.x, p.y);
                Point modelPoint = new Point(p.x, p.y);
                boardRepresentations.put(boardPoint, modelPoint);
                Circle moveMarker = new Circle(boardPoint.x, boardPoint.y, 8, Color.CYAN);
                moveMarker.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        try {
                            if (model.getCurrentPlayer() == Player.BLACK) {
                                play((Tile) model.getSelectedPiece(), modelPoint.x, modelPoint.y);
                                redrawBoard();
                                if (model.getSelectedPiece() == Tile.QUEEN_BEE) {
                                    blackPlayer.setHasPlayedQueen(true);
                                }
                                model.getBlackAvailablePieces().remove(model.getSelectedPiece());
                                blackPiecesListView.refresh();
                                blackPiecesListView.getSelectionModel().clearSelection();
                            } else {
                                play((Tile) model.getSelectedPiece(), modelPoint.x, modelPoint.y);
                                redrawBoard();
                                if (model.getSelectedPiece() == Tile.QUEEN_BEE) {
                                    whitePlayer.setHasPlayedQueen(true);
                                }
                                model.getWhiteAvailablePieces().remove(model.getSelectedPiece());
                                whitePiecesListView.refresh();
                                whitePiecesListView.getSelectionModel().clearSelection();
                            }
                        } catch (IllegalMove e) {
                            System.out.println("Illegal move... No piece selected?");
                        } catch (NullPointerException e) {
                            hint.setText("Select Piece First");
                        }

                    }
                });
                boardView.getChildren().add(moveMarker);
            }
        }

        public void swapTurn() {
            resetView();
            if (model.getCurrentPlayer() == Player.WHITE) {
                hint.setText("Black Player it's your turn!");
                model.setCurrentPlayer(Player.BLACK);
                whitePlayer.addMove();

//            check if player has placed the queen before move if not show only the queen
                if (blackPlayer.getMoves() == 3 && !blackPlayer.hasPlayedQueen()) {
                    List<Enum> blackQueen = Arrays.asList(Tile.QUEEN_BEE);
                    blackPiecesListView.setItems(FXCollections.observableArrayList(blackQueen));
                }

                blackPiecesListView.setDisable(false);
                whitePiecesListView.setDisable(true);

            } else if (model.getCurrentPlayer() == Player.BLACK) {
                hint.setText("White Player it's your turn!");
                model.setCurrentPlayer(Player.WHITE);
                blackPlayer.addMove();

                if (whitePlayer.getMoves() == 3 && !whitePlayer.hasPlayedQueen()) {
                    List<Enum> whiteQueen = Arrays.asList(Tile.QUEEN_BEE);
                    whitePiecesListView.setItems(FXCollections.observableArrayList(whiteQueen));
                }

                whitePiecesListView.setDisable(false);
                blackPiecesListView.setDisable(true);
            }
        }

        public void redrawBoard() {
            if (boardView.getChildren().size() > 0) {
                boardView.getChildren().clear();
            }
            for (Piece p : model.getBoard()) {
                p.drawPiece(boardView);
            }
        }

        public void unselectAllPieces() {
            for (Piece p : model.getBoard()) {
                p.setSelected(false);
            }
            redrawBoard();
        }

        @Override
        public void play(Tile tile, int q, int r) throws IllegalMove {
            Piece pec = model.createPiece(tile, q, r);
            Point boardPoint = model.calculateBoardRepresentation(q, r);
            Point modelPoint = new Point(q, r);
            boardRepresentations.put(boardPoint, modelPoint);

            EventHandler<MouseEvent> pEvent = new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (pec.isSelected() && (pec.getPlayer() == model.getCurrentPlayer())) {
                        pec.setSelected(false);
                        redrawBoard();
                    } else if (pec.getPlayer() == model.getCurrentPlayer()) {
                        for (Piece p : model.getBoard()) {
                            p.setSelected(false);
                        }
                        if (model.getCurrentPlayer() == Player.BLACK && blackPlayer.hasPlayedQueen()) {
                            pec.setSelected(true);
                            whitePiecesListView.getSelectionModel().clearSelection();
                            blackPiecesListView.getSelectionModel().clearSelection();
                            redrawBoard();
                            showAvailableMovesSpots(pec);
                        } else if (model.getCurrentPlayer() == Player.WHITE && whitePlayer.hasPlayedQueen()) {
                            pec.setSelected(true);
                            whitePiecesListView.getSelectionModel().clearSelection();
                            blackPiecesListView.getSelectionModel().clearSelection();
                            redrawBoard();
                            showAvailableMovesSpots(pec);
                        }
                    }
                }
            };
            pec.setEvent(pEvent);
            pec.drawPiece(boardView);
            model.getBoard().add(pec);
            redrawBoard();
            swapTurn();
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
            lastpiece.initializePiece();
            model.getBoard().add(lastpiece);
            redrawBoard();
//        if( !isWinner(model.getCurrentPlayer())) { swapTurn(); }
            swapTurn();
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
                hint.setText("You can't pass, because you have moves to play!");
            }

        }

        @Override
        public boolean isWinner(Player player) {
            if (!isDraw()) {
                for (Piece piece : model.getBoard()) {
                    if (piece.getPiece() == Tile.QUEEN_BEE) {
                        if (model.getNeighbours(piece.getCenter()).size() == 6) {
                            if (piece.getPlayer() == player && piece.getPlayer() == player.BLACK) {
                                hint.setText("White player won the game!");
                            } else {
                                hint.setText("Black player won the game!");
                            }
                            return true;
                        }
                    }
                }
            } else if (isDraw()) {
                hint.setText("It's a draw!");
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
}
