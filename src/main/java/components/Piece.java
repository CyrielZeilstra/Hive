package nl.hanze.hive.components;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Polyline;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import nl.hanze.hive.interfaces.Hive;

import java.awt.*;
import java.util.ArrayList;

public class Piece extends Polygon {

    private Point[] points = new Point[6];
    private double[] polypoints = new double[12];
    private Point center;
    private Point boardCenter;
    private Text piecetext;
    private boolean selected;
    private Polygon hexagon;
    private Polyline selectionPoly;
    private Enum player;
    private Enum piece;
    private EventHandler<MouseEvent> event;

    public Piece(int x, int y, Point boardCenter, boolean selected, Enum player, Enum piece) {
        this.center = new Point(x, y);
        this.boardCenter = boardCenter;
        this.selected = selected;
        this.player = player;
        this.piece = piece;
        initializePiece();
    }

    public void initializePiece() {
        updatePoints();
        hexagon = new Polygon(polypoints);
        piecetext = new Text((String) getTileInfo((Hive.Tile) piece)[0]);
        selectionPoly = new Polyline(getPolyLinesArray(polypoints));

        selectionPoly.setStrokeWidth(3);

        piecetext.setFont(new Font("Verdana", 10));
        Paint p;
        if (player == Hive.Player.WHITE) {
            p = Color.WHITE;
            piecetext.setFill(Color.BLACK);
        } else {
            p = Color.BLACK;
            piecetext.setFill(Color.WHITE);
        }

        hexagon.setFill(p);

        piecetext.setX(boardCenter.getX() + (int) getTileInfo((Hive.Tile) piece)[1]);
        piecetext.setY(boardCenter.getY() + (int) getTileInfo((Hive.Tile) piece)[2]);

        p = Color.CYAN;
        selectionPoly.setStroke(p);

        hexagon.setOnMouseClicked(event);
        piecetext.setOnMouseClicked(event);
    }


    public void setEvent(EventHandler<MouseEvent> event) {
        this.event = event;
        hexagon.setOnMouseClicked(event);
        piecetext.setOnMouseClicked(event);
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Polygon getHexagon() {
        return hexagon;
    }

    public void setCenter(Point center) {
        this.center = center;
        updatePoints();
    }

    public Text getPiecetext() {
        return piecetext;
    }

    public void setCenter(int x, int y) {
        setCenter(new Point(x, y));

    }

    private double findAngle(double fraction) {
        return fraction * Math.PI * 2 + Math.toRadians((90 + 180) % 360);
    }

    private Point findPoint(double angle) {
        int x = (int) (boardCenter.x + Math.cos(angle) * 30);
        int y = (int) (boardCenter.y + Math.sin(angle) * 30);
        return new Point(x, y);
    }

    public Point getCenter() {
        return center;
    }

    public Object[] getTileInfo(Hive.Tile piece) {
        Object[] temp = new Object[3];
        switch (piece) {
            case QUEEN_BEE:
                temp[0] = "QUEEN" + "\n" + center.x + "|" + center.y;
                temp[1] = -19;
                temp[2] = 3;
                return temp;
            case SPIDER:
                temp[0] = "SPIDER" + "\n" + center.x + "|" + center.y;
                temp[1] = -19;
                temp[2] = 3;
                return temp;
            case BEETLE:
                temp[0] = "BEETLE" + "\n" + center.x + "|" + center.y;
                temp[1] = -19;
                temp[2] = 3;
                return temp;
            case GRASSHOPPER:
                temp[0] = "HOPPER" + "\n" + center.x + "|" + center.y;
                temp[1] = -20;
                temp[2] = 3;
                return temp;
            case SOLDIER_ANT:
                temp[0] = "ANT" + "\n" + center.x + "|" + center.y;
                temp[1] = -11;
                temp[2] = 3;
                return temp;
            default:
                temp[0] = "UNDEFINED";
                temp[1] = -19;
                temp[2] = 3;
                return temp;
        }
    }

    protected void updatePoints() {
        ArrayList<Double> templist = new ArrayList<>();
        for (int p = 0; p < 6; p++) {
            double angle = findAngle((double) p / 6);
            Point point = findPoint(angle);
            points[p] = point;
            templist.add((double) point.x);
            templist.add((double) point.y);
        }
        for (int i = 0; i < templist.size(); i++) {
            polypoints[i] = templist.get(i);
        }
    }

    public Enum getPlayer() {
        return player;
    }

    public Enum getPiece() {
        return piece;
    }

    public double[] getPolyLinesArray(double[] current) {
        double[] temp = new double[14];
        System.arraycopy(current, 0, temp, 0, current.length);
        // Close the polygon by setting first point as last aswell.
        temp[12] = current[0];
        temp[13] = current[1];
        return temp;
    }

    public void drawPiece(Pane gamefield) {
        gamefield.getChildren().addAll(hexagon, piecetext);
        if (selected) {
            gamefield.getChildren().add(selectionPoly);
        }
    }
}