package model;

import java.awt.geom.Point2D;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 15/02/12
 * Time: 12:51
 */
public interface Movable {
    public abstract Point2D move(int totalSteps);
    public abstract int turn(int degree);
    public abstract Point2D stop();
}
