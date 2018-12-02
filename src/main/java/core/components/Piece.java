package core.components;

import java.awt.*;


public class Piece extends Polygon {

    private Point center;
    private Enum player;
    private Enum piece;

    public Piece(int x, int y, Enum player, Enum piece) {
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

    public Enum getPlayer() {
        return player;
    }

    public Enum getPiece() {
        return piece;
    }
}