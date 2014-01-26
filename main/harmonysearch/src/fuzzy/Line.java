package fuzzy;

/**
 * User: rrd09, Date: 09/02/11, Time: 12:33
 */
public class Line {
    public double x1, y1, x2, y2;

    public Line(double x1, double y1, double x2, double y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public static double intersect(Line l1, Line l2) {
        double a1 = (l1.y1 - l1.y2) / (l1.x1 - l1.x2);
        double b1 = l1.y1 - a1 * l1.x1;
        double a2 = (l2.y1 - l2.y2) / (l2.x1 - l2.x2);
        double b2 = l2.y1 - a2 * l2.x1;
        return (b2 - b1) / (a1 - a2);
    }

    public double getX1() {
        return x1;
    }

    public void setX1(double x1) {
        this.x1 = x1;
    }

    public double getY1() {
        return y1;
    }

    public void setY1(double y1) {
        this.y1 = y1;
    }

    public double getX2() {
        return x2;
    }

    public void setX2(double x2) {
        this.x2 = x2;
    }

    public double getY2() {
        return y2;
    }

    public void setY2(double y2) {
        this.y2 = y2;
    }
}
