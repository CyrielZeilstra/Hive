package core.components;

import nl.hanze.hive.Hive;

import java.awt.*;


public class Piece extends Polygon {

    private Point center;
    private Hive.Player player;
    private Hive.Tile piece;

    public Piece(int x, int y, Hive.Player player, Hive.Tile piece) {
        this.center = new Point(x, y);
        this.player = player;
        this.piece = piece;
    }

    public void setCenter(Point center) {
        this.center = center;
    }

    public Point getCenter() {
        return center;
    }

    public Hive.Player getPlayer() {
        return player;
    }

    public Hive.Tile getPiece() {
        return piece;
    }
}