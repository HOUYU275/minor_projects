package controller;

import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 15/02/12
 * Time: 13:58
 */
public class Tracker {
    private ArrayList<Point2D> path;

    public Tracker() {
        this.path = new ArrayList<Point2D>();
    }

    public void record(Point2D point2D) {
        this.path.add((Point2D)point2D.clone());
    }

    public ArrayList<Point2D> getPath() {
        return path;
    }
}
