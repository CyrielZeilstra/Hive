package core.components;

import nl.hanze.hive.Hive;

import java.awt.*;
import java.util.*;
import java.util.List;

import static java.lang.Float.max;
import static java.lang.Integer.min;
import static nl.hanze.hive.Hive.Player.BLACK;
import static nl.hanze.hive.Hive.Player.WHITE;
import static nl.hanze.hive.Hive.Tile.*;

public class Model {

    private PlayerModel whitePlayer = new PlayerModel(false, 0, WHITE);
    private PlayerModel blackPlayer = new PlayerModel(false, 0, BLACK);
    private PlayerModel currentPlayer = whitePlayer;

    private ArrayList<Piece> board = new ArrayList<>();

    public ArrayList<Piece> getBoard() {
        return board;
    }

    public void swapTurn() {
        if (getCurrentPlayer().getPlayerColor() == WHITE) {
            System.out.println("Black PlayerModel it's your turn!");
            whitePlayer.addMove();
            setCurrentPlayer(blackPlayer);
            // check if player has placed the queen before move if not show only the queen
            if (getBlackPlayer().getAmountOfMovesMade() == 3 && !getBlackPlayer().hasPlayedQueen()) {
                List<Enum> blackQueen = Arrays.asList(Hive.Tile.QUEEN_BEE);
            }
        } else if (getCurrentPlayer().getPlayerColor() == BLACK) {
            System.out.println("White PlayerModel it's your turn!");
            getBlackPlayer().addMove();
            setCurrentPlayer(whitePlayer);

            if (getWhitePlayer().getAmountOfMovesMade() == 3 && !getWhitePlayer().hasPlayedQueen()) {
                List<Enum> whiteQueen = Arrays.asList(Hive.Tile.QUEEN_BEE);
            }

        }
    }

    public ArrayList<Point> getAvailablePlaySpots() {
        ArrayList<Point> moves = new ArrayList<>();
        Point firstWhiteMove = new Point(0, 0);
        if (board.size() < 2 && getCurrentPlayer().getPlayerColor() == WHITE) {
            moves.add(firstWhiteMove);
        } else if (board.size() < 2 && getCurrentPlayer().getPlayerColor() == BLACK) {
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

    public boolean breaksConnection(Point from) {
        ArrayList<Point> temp = getBoardAsPoints();
        temp.remove(from);
        if (temp.size() <= 2) {
            return false;
        }
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

    public boolean canSlideIn(Point from, Point to) {
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
        Point origin = p.getCenter();

        int amount = Collections.frequency(getBoardAsPoints(), origin);
        if (amount > 1 && p.getPiece() == BEETLE) {
            for (Point move : getSurroundingPoints(origin)) {
                tempMoves.add(move);
            }
            return tempMoves;
        } else {
            if (p.getPiece() == QUEEN_BEE) {
                tempMoves = getQueenMoves(origin);
            } else if (p.getPiece() == Hive.Tile.SOLDIER_ANT) {
                tempMoves = getAntMoves(origin);
            } else if (p.getPiece() == SPIDER) {
                tempMoves = getSpiderMoves(origin);
            } else if (p.getPiece() == GRASSHOPPER) {
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
        // remove not allowed amountOfMovesMade
        return surrounding;
    }

    public ArrayList<Point> getAvailablePlays() {
        ArrayList<Point> tempMoves = new ArrayList<>();
        ArrayList<Point> pointsAlreadyOnBoard = getBoardAsPoints();
        ArrayList<Point> otherPlayerMoves = new ArrayList<>();

        for (Piece piece : board) {
            ArrayList<Point> moves = getSurroundingPoints(piece.getCenter());
            for (Point p : moves) {
                if (getCurrentPlayer().getPlayerColor() != piece.getPlayer() && !pointsAlreadyOnBoard.contains(p)) {
                    otherPlayerMoves.add(p);
                }
            }
        }

        for (Piece piece : board) {
            ArrayList<Point> moves = getSurroundingPoints(piece.getCenter());
            for (Point p : moves) {
                if (getCurrentPlayer().getPlayerColor() == piece.getPlayer() && !otherPlayerMoves.contains(p) && !pointsAlreadyOnBoard.contains(p)) {
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
            double nx = (Math.abs(dy) + (1 - p)) / 2;

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

    public boolean isPlayAllowed(int q, int r) {
        return getAvailablePlaySpots().contains(new Point(q, r));
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

    public Piece createPiece(Hive.Tile tile, int q, int r) {
        Piece pieceToPlay;
        pieceToPlay = new Piece(q, r, currentPlayer.getPlayerColor(), tile);
        return pieceToPlay;
    }

    public PlayerModel getWhitePlayer() {
        return whitePlayer;
    }

    public PlayerModel getBlackPlayer() {
        return blackPlayer;
    }

    public PlayerModel getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(PlayerModel currentPlayer) {
        this.currentPlayer = currentPlayer;
    }
}