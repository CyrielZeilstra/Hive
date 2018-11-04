package core.components;

import java.awt.*;


public class Piece extends Polygon {

    private Point center;
    private boolean selected;
    private Enum player;
    private Enum piece;

    public Piece(int x, int y, boolean selected, Enum player, Enum piece) {
        this.center = new Point(x, y);
        this.selected = selected;
        this.player = player;
        this.piece = piece;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
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