package gui;

import java.awt.geom.Point2D;

public class Target{
    private final Point2D.Double position = new Point2D.Double(150,100);

    public Target(double x, double y) {
        position.setLocation(x, y);
    }

    public Target() {}

    public Point2D.Double getPosition() {
        return position;
    }

}