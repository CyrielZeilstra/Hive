package core.components;

import nl.hanze.hive.Hive;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static java.lang.Float.max;
import static java.lang.Integer.min;

public class Model {

    private Enum selectedPiece;
    private Hive.Player currentPlayer;
    private ArrayList<Enum> whiteAvailablePieces = new ArrayList<>();
    private ArrayList<Enum> blackAvailablePieces = new ArrayList<>();
    public ArrayList<Piece> board = new ArrayList<>();

    public ArrayList<Piece> getBoard() {
        return board;
    }

    public ArrayList<Point> getAvailablePlaySpots() {
        ArrayList<Point> moves = new ArrayList<>();
        Point firstWhiteMove = new Point(0, 0);
        if (board.size() < 2 && currentPlayer == Hive.Player.WHITE) {
            moves.add(firstWhiteMove);
        } else if (board.size() < 2 && currentPlayer == Hive.Player.BLACK) {
            moves.addAll(getSurroundingPoints(firstWhiteMove));
        } else {
            moves.addAll(getAvailablePlays());
        }
        return moves;
    }

    public int getAmountOfStonesOnPoint(Point p) {
        int amount = 0;
        for (Piece piece : board) {
            if (piece.getCenter() == p) {
                amount++;
            }
        }
        return amount;
    }

    public ArrayList<Point> getBoardAsPoints() {
        ArrayList<Point> points = new ArrayList<>();
        for (Piece b : board) {
            points.add(b.getCenter());
        }
        return points;
    }

    private boolean breaksConnection(Point from) {
        ArrayList<Point> temp = getBoardAsPoints();
        temp.remove(from);
        Set<Point> boardAfterMove = new HashSet<Point>(temp);
        ArrayList<Point> connections = new ArrayList<Point>();

        Set<Point> listOfConnections = new HashSet<>(recursiveConnectionSearch(temp.get(0), boardAfterMove, connections));

        return listOfConnections.size() != boardAfterMove.size();
    }

    private ArrayList<Point> recursiveConnectionSearch(Point currentPoint, Set<Point> board, ArrayList<Point> completeListOfConnections) {
        ArrayList<Point> surroundingPoints = getSurroundingPoints(currentPoint);
        for (Point p2 : surroundingPoints) {
            if (board.contains(p2)) {
                // Board has the surrounding piece.
                if (!completeListOfConnections.contains(p2)) {
                    completeListOfConnections.add(p2);
                    recursiveConnectionSearch(p2, board, completeListOfConnections);
                }
            }
        }
        return completeListOfConnections;
    }

    private boolean canSlideIn(Point from, Point to) {
        ArrayList<Point> tempList = new ArrayList<>();
        Set<Point> uniqueBoard = new HashSet<>(getBoardAsPoints());
        for (Point checkPoint : uniqueBoard) {
            if (getSurroundingPoints(to).contains(checkPoint)) {
                tempList.add(checkPoint);
            }
            if (getSurroundingPoints(from).contains(checkPoint)) {
                tempList.add(checkPoint);
            }
        }
        Set<Point> s = new HashSet<>();
        Set<Point> connectedPieces = new HashSet<>();
        for (Point p : tempList) {
            if (!s.add(p)) {
                connectedPieces.add(p);
            }
        }
        if (connectedPieces.size() == 2) {
            Point n1 = (Point) connectedPieces.toArray()[0];
            Point n2 = (Point) connectedPieces.toArray()[1];
            return min(getAmountOfStonesOnPoint(n1), getAmountOfStonesOnPoint(n2)) <=
                    max(getAmountOfStonesOnPoint(from) - 1, getAmountOfStonesOnPoint(to));
        } else {
            return true;
        }
    }

    public boolean isTouchingMove(Point from, Point to) {
        ArrayList<Point> neighboursFrom = getNeighbours(from);
        ArrayList<Point> neighboursTo = getNeighbours(to);
        neighboursFrom.remove(from);
        neighboursTo.remove(from);
        for (Point p : neighboursTo) {
            if (neighboursFrom.contains(p)) {
                return true;
            }
        }
        return false;
    }

    public boolean isTouchingMoveCustomBoard(Point from, Point to, ArrayList<Point> board) {
        ArrayList<Point> neighboursFrom = getSurroundingPoints(from);
        ArrayList<Point> neighboursTo = getSurroundingPoints(to);
        neighboursFrom.remove(from);
        neighboursTo.remove(from);
        ArrayList<Point> neighbors = new ArrayList<>();
        for (Point p : neighboursTo) {
            if (neighboursFrom.contains(p) && board.contains(p)) {
                neighbors.add(p);
            }
        }

        if (neighbors.size() == 1) {
            return true;
        }
        return false;
    }

    public ArrayList<Point> getNeighbours(Point point) {
        ArrayList<Point> neighbours = new ArrayList<>();
        ArrayList<Point> pointsAlreadyOnBoard = getBoardAsPoints();
        for (Point p : getSurroundingPoints(point)) {
            if (pointsAlreadyOnBoard.contains(p)) {
                neighbours.add(p);
            }
        }
        return neighbours;
    }

    public ArrayList<Point> getAvailableMovesSelectedBoardPiece(Piece p) {
        ArrayList<Point> tempMoves = new ArrayList<>();
        // Probably have to do someting with currentplayer here later....
        Point origin = p.getCenter();

        int amount = Collections.frequency(getBoardAsPoints(), origin);
        if (amount > 1 && p.getPiece() == Hive.Tile.BEETLE) {
            for (Point move : getSurroundingPoints(origin)) {
                tempMoves.add(move);
            }
            return tempMoves;
        } else {
            if (p.getPiece() == Hive.Tile.QUEEN_BEE) {
                tempMoves = getQueenMoves(origin);
            } else if (p.getPiece() == Hive.Tile.SOLDIER_ANT) {
                tempMoves = getAntMoves(origin);
            } else if (p.getPiece() == Hive.Tile.SPIDER) {
                tempMoves = getSpiderMoves(origin);
            } else if (p.getPiece() == Hive.Tile.GRASSHOPPER) {
                tempMoves = getGrasshopperMoves(origin);
            } else {
                tempMoves = getBeetleMoves(origin);
            }
            return tempMoves;
        }
    }

    public ArrayList<Point> getSurroundingPoints(Point p) {
        ArrayList<Point> surrounding = new ArrayList<>();
        // Probably have to do someting with currentplayer here later....

        int[][] surroundingPointsXYOffsets = {{-1, 0}, {0, -1}, {1, -1}, {1, 0}, {0, 1}, {-1, 1}};
        for (int[] surroundingPointsXYOffset : surroundingPointsXYOffsets) {
            int x = surroundingPointsXYOffset[0];
            int y = surroundingPointsXYOffset[1];
            Point movePoint = new Point((p.x + x), (p.y + y));
            if (!surrounding.contains(movePoint)) {
                surrounding.add(movePoint);
            }
        }
        // tempmoves now contains all surrounding points
        // remove not allowed moves
        return surrounding;
    }

    public ArrayList<Point> getAvailablePlays() {
        ArrayList<Point> tempMoves = new ArrayList<>();
        ArrayList<Point> pointsAlreadyOnBoard = getBoardAsPoints();
        ArrayList<Point> otherPlayerMoves = new ArrayList<>();

        for (Piece piece : board) {
            ArrayList<Point> moves = getSurroundingPoints(piece.getCenter());
            for (Point p : moves) {
                if (getCurrentPlayer() != piece.getPlayer() && !pointsAlreadyOnBoard.contains(p)) {
                    otherPlayerMoves.add(p);
                }
            }
        }

        for (Piece piece : board) {
            ArrayList<Point> moves = getSurroundingPoints(piece.getCenter());
            for (Point p : moves) {
                if (getCurrentPlayer() == piece.getPlayer() && !otherPlayerMoves.contains(p) && !pointsAlreadyOnBoard.contains(p)) {
                    tempMoves.add(p);
                }
            }
        }

        return tempMoves;
    }

    public int getDistanceBetweenPoints(Point from, Point to) {
        return (int) Math.hypot(Math.abs(to.getY() - from.getY()), Math.abs(to.getX() - from.getX()));
    }

    public int getLineDirection(Point from, Point to) {
        double dx = to.getX() - from.getX();
        double dy = to.getY() - from.getY();
        double p = from.getY() % 2;

        //Move Directions: W = 0, E = 1, NW = 2, NE = 3, SW = 4, SE = 5, invalid move = 6
        if (dy == 0) {
            if (dx < 0) {
                return 0;
            } else {
                return 1;
            }
        } else {
            double nx = (Math.abs(dy) + (1 - p)) / 1.8;

            if ((int) Math.abs(dx) != (int) Math.abs(nx)) {
                return 6;
            }
            if (dx < 0) {
                if (dy < 0) {
                    return 2;
                } else {
                    return 4;
                }
            } else {
                if (dy < 0) {
                    return 3;
                } else {
                    return 5;
                }
            }
        }
    }

    public ArrayList<Point> getAllMoves() {
        ArrayList<Point> moves = new ArrayList<>();
        ArrayList<Point> pointsAlreadyOnBoard = getBoardAsPoints();

        for (Piece piece : board) {
            ArrayList<Point> neighbours = getSurroundingPoints(piece.getCenter());
            for (Point p : neighbours) {
                if (!moves.contains(p) && !pointsAlreadyOnBoard.contains(p)) {
                    moves.add(p);
                }
            }
        }
        return moves;
    }

    public Boolean isFloatingPiece(Point p, ArrayList<Point> board) {
        for (Point e : getSurroundingPoints(p)) {
            if (board.contains(e)) {
                return false;
            }
        }
        return true;
    }

    public ArrayList<Point> getSpiderMoves(Point origin) {
        Set<Point> firstPoints = new HashSet<>();
        for (Point p1 : getSurroundingPoints(origin)) {
            if (!breaksConnection(origin) && isTouchingMove(origin, p1) && canSlideIn(origin, p1) && !getBoardAsPoints().contains(p1)) {
                firstPoints.add(p1);
            }
        }

        Set<Point> secondPoints = new HashSet<>();
        for (Point p2 : firstPoints) {
            for (Point p3 : getSurroundingPoints(p2)) {
                // remove all currently excisting pieces on board from list
                if (!getBoardAsPoints().contains(p3)) {
                    ArrayList<Point> tempboard = new ArrayList<>(getBoardAsPoints());
                    tempboard.remove(origin);
                    tempboard.add(p3);
                    if (!isFloatingPiece(p3, tempboard) && !firstPoints.contains(p3) && isTouchingMoveCustomBoard(p2, p3, tempboard)) {
                        secondPoints.add(p3);
                    }
                }
            }
        }

        Set<Point> thirdPoints = new HashSet<>();
        for (Point p3 : secondPoints) {
            for (Point p4 : getSurroundingPoints(p3)) {
                // remove all currently excisting pieces on board from list
                if (!getBoardAsPoints().contains(p4)) {
                    ArrayList<Point> tempboard = new ArrayList<>(getBoardAsPoints());
                    tempboard.remove(origin);
                    tempboard.add(p4);
                    if (!isFloatingPiece(p4, tempboard) && !firstPoints.contains(p4) && !secondPoints.contains(p4) && isTouchingMoveCustomBoard(p3, p4, tempboard)) {
                        thirdPoints.add(p4);
                    }
                }
            }
        }

        return new ArrayList(thirdPoints);
    }

    public ArrayList<Point> getBeetleMoves(Point origin) {
        ArrayList<Point> moves = new ArrayList<>();
        for (Point move : getSurroundingPoints(origin)) {
            ArrayList<Point> tempboard = new ArrayList<>(getBoardAsPoints());
            tempboard.remove(origin);
            tempboard.add(move);
            if (!breaksConnection(origin) && !isFloatingPiece(move, tempboard)) {
                if (Collections.frequency(getBoardAsPoints(), origin) > 1) {
                    moves.add(move);
                } else {
                    if (isTouchingMoveCustomBoard(origin, move, tempboard) || getBoardAsPoints().contains(move)) {
                        moves.add(move);
                    }
                }

            }
        }
        return moves;
    }

    public ArrayList<Point> getQueenMoves(Point origin) {
        ArrayList<Point> moves = new ArrayList<>();

        for (Point p : getSurroundingPoints(origin)) {
            ArrayList<Point> tempboard = new ArrayList<>(getBoardAsPoints());
            tempboard.remove(origin);
            tempboard.add(p);
            if (!getBoardAsPoints().contains(p) && canSlideIn(origin, p) && !isFloatingPiece(p, tempboard) && isTouchingMoveCustomBoard(origin, p, tempboard) && !breaksConnection(origin)) {
                moves.add(p);
            }
        }
        return moves;
    }

    public ArrayList<Point> getAntMoves(Point origin) {
        ArrayList<Point> moves = new ArrayList<>();
        ArrayList<Point> validSurroundingPoints = new ArrayList<>();

        for (Point p : getSurroundingPoints(origin)) {
            ArrayList<Point> tempboard = new ArrayList<>(getBoardAsPoints());
            tempboard.remove(origin);
            tempboard.add(p);
            if (!getBoardAsPoints().contains(p) && canSlideIn(origin, p) && !isFloatingPiece(p, tempboard) && isTouchingMoveCustomBoard(origin, p, tempboard) && !breaksConnection(origin)) {
                validSurroundingPoints.add(p);
            }
        }

        if (validSurroundingPoints.size() == 0) {
            // we cant move from current spot.
            return moves;
        } else {
            // we can move from current spot!
            for (Point p : validSurroundingPoints) {
                ArrayList<Point> tempboard = new ArrayList<>(getBoardAsPoints());
                tempboard.remove(origin);
                moves.addAll(recursiveAntSearch(p, tempboard, new HashSet<>(validSurroundingPoints)));
            }
            return moves;
        }

    }

    public ArrayList<Point> recursiveAntSearch(Point curPos, ArrayList<Point> board, Set<Point> validPoints) {
        for (Point x : getSurroundingPoints(curPos)) {
            if (!getBoardAsPoints().contains(x) && !isFloatingPiece(x, board) && !validPoints.contains(x) && !isFloatingPiece(x, board) && canSlideIn(curPos, x)) {
                validPoints.add(x);
                recursiveAntSearch(x, board, validPoints);
            }
        }
        return new ArrayList(validPoints);
    }

    public ArrayList<Point> getGrasshopperMoves(Point origin) {
        ArrayList<Point> moves = new ArrayList<>();
        ArrayList<Point> pointsAlreadyOnBoard = getBoardAsPoints();
        ArrayList<Point> invalidMoves = new ArrayList<>();
        ArrayList<Integer> invalidLines = new ArrayList<>();

        for (Point invalidPoint : getSurroundingPoints(origin)) {
            if (!pointsAlreadyOnBoard.contains(invalidPoint)) {
                invalidMoves.add(invalidPoint);
                invalidLines.add(getLineDirection(origin, invalidPoint));
            }
        }

        for (Point p : getAllMoves()) {
            if (!invalidMoves.contains(p)) {
                if (getLineDirection(origin, p) == 6) {
                    invalidMoves.add(p);
                    invalidLines.add(getLineDirection(origin, p));
                } else {
                    if (moves.isEmpty()) {
                        if (!invalidLines.contains(getLineDirection(origin, p)) && !breaksConnection(origin)) {
                            moves.add(p);
                        }
                    } else {
                        int pointsOnSameLine = 0;
                        for (Point pointOnSameLine : moves) {
                            if (getLineDirection(origin, p) == getLineDirection(origin, pointOnSameLine)) {
                                pointsOnSameLine++;
                                if (getDistanceBetweenPoints(origin, p) < getDistanceBetweenPoints(origin, pointOnSameLine)) {
                                    if (!invalidLines.contains(getLineDirection(origin, p)) && !breaksConnection(origin)) {
                                        moves.add(p);
                                    }
                                    moves.remove(pointOnSameLine);
                                }
                            }
                        }
                        if (pointsOnSameLine == 0) {
                            if (!invalidLines.contains(getLineDirection(origin, p)) && !breaksConnection(origin)) {
                                moves.add(p);
                            }
                        }
                    }
                }
            }
        }
        return moves;
    }

    public Piece createPiece(Enum tile, int q, int r) {
        Piece pieceToPlay;
        pieceToPlay = new Piece(q, r, false, currentPlayer, tile);
        return pieceToPlay;
    }

    public Enum getSelectedPiece() {
        return selectedPiece;
    }

    public void setSelectedPiece(Enum selectedPiece) {
        this.selectedPiece = selectedPiece;
    }

    public Hive.Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Hive.Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public ArrayList<Enum> getWhiteAvailablePieces() {
        return whiteAvailablePieces;
    }

    public void setWhiteAvailablePieces(ArrayList<Enum> whiteAvailablePieces) {
        this.whiteAvailablePieces = whiteAvailablePieces;
    }

    public ArrayList<Enum> getBlackAvailablePieces() {
        return blackAvailablePieces;
    }

    public void setBlackAvailablePieces(ArrayList<Enum> blackAvailablePieces) {
        this.blackAvailablePieces = blackAvailablePieces;
    }
}