package fuzzy;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 04/02/11
 * Time: 11:46
 * To change this template use File | Settings | File Templates.
 */
public class FuzzyNumber implements Comparable {

    private double centroid;
    private double leftShoulder;
    private double rightShoulder;

    public FuzzyNumber() {
    }

    public FuzzyNumber(double leftShoulder, double centroid, double rightShoulder) {
        this.centroid = centroid;
        this.leftShoulder = leftShoulder;
        this.rightShoulder = rightShoulder;
    }

    public double getSupportLength() {
        return rightShoulder - leftShoulder;
    }

    public double distanceTo(FuzzyNumber fuzzyNumber) {
        return this.getRep() - fuzzyNumber.getRep();
    }

    public double absDistanceTo(FuzzyNumber fuzzyNumber) {
        return Math.abs(this.getRep() - fuzzyNumber.getRep());
    }

    public double getCentroid() {
        return centroid;
    }

    public void setCentroid(double centroid) {
        this.centroid = centroid;
    }

    public double getLeftShoulder() {
        return leftShoulder;
    }

    public void setLeftShoulder(double leftShoulder) {
        this.leftShoulder = leftShoulder;
    }

    public double getRightShoulder() {
        return rightShoulder;
    }

    public void setRightShoulder(double rightShoulder) {
        this.rightShoulder = rightShoulder;
    }

    public double getMembership(double value) {
        if ((value >= rightShoulder) || (value <= leftShoulder)) return 0d;
        if ((value > leftShoulder) & (value <= centroid)) return (value - leftShoulder) / (centroid - leftShoulder);
        else return (rightShoulder - value) / (rightShoulder - centroid);
    }

    public FuzzyNumber times(double value) {
        return new FuzzyNumber(leftShoulder * value, centroid * value, rightShoulder * value);
    }

    public FuzzyNumber times(FuzzyNumber fuzzyNumber) {
        /*double al = (centroid - leftShoulder) + leftShoulder;
        double ar = - (rightShoulder - centroid) + rightShoulder;
        double bl = (fuzzyNumber.getCentroid() - fuzzyNumber.getLeftShoulder()) + fuzzyNumber.getLeftShoulder();
        double br = - (fuzzyNumber.getRightShoulder() - fuzzyNumber.getCentroid()) + fuzzyNumber.getRightShoulder();*/
        return new FuzzyNumber(
                leftShoulder * fuzzyNumber.getLeftShoulder(),
                centroid * fuzzyNumber.getCentroid(),
                rightShoulder * fuzzyNumber.getRightShoulder());
    }

    public FuzzyNumber divides(FuzzyNumber fuzzyNumber) {
        return new FuzzyNumber(
                leftShoulder / fuzzyNumber.getRightShoulder(),
                centroid / fuzzyNumber.getCentroid(),
                rightShoulder / fuzzyNumber.getLeftShoulder());
    }

    public FuzzyNumber divides(double value) {
        return new FuzzyNumber(
                leftShoulder / value,
                centroid / value,
                rightShoulder / value);
    }

    public FuzzyNumber adds(FuzzyNumber fuzzyNumber) {
        return new FuzzyNumber(
                leftShoulder + fuzzyNumber.getLeftShoulder(),
                centroid + fuzzyNumber.getCentroid(),
                rightShoulder + fuzzyNumber.getRightShoulder());
    }

    public FuzzyNumber adds(double value) {
        return new FuzzyNumber(
                leftShoulder + value,
                centroid + value,
                rightShoulder + value);
    }

    public FuzzyNumber minus(double value) {
        return new FuzzyNumber(
                leftShoulder - value,
                centroid - value,
                rightShoulder - value);
    }

    public FuzzyNumber minus(FuzzyNumber fuzzyNumber) {
        return new FuzzyNumber(
                leftShoulder - fuzzyNumber.getLeftShoulder(),
                centroid - fuzzyNumber.getCentroid(),
                rightShoulder - fuzzyNumber.getRightShoulder());
    }

    public String toString() {
        return "(" + leftShoulder + "," + centroid + "," + rightShoulder + ")";
    }

    public int compareTo(Object o) {
        FuzzyNumber fn2 = (FuzzyNumber) o;
        //System.out.println(this + " v.s. " + fn2);
        FuzzyNumber min = getFuzzyMin(this, fn2);
        FuzzyNumber max = getFuzzyMax(this, fn2);
        //System.out.println(min + " v.s. " + max);
        double rank1 = getFuzzyRanking(this, min, max);
        double rank2 = getFuzzyRanking(fn2, min, max);

        if (rank1 > rank2) {
            return 1;
        } else if (rank1 < rank2) {
            return -1;
        } else {
            return 0;
        }
        /*FuzzyNumber fuzzyNumber = (FuzzyNumber) o;
        if ((leftShoulder == fuzzyNumber.getLeftShoulder()) &
                (centroid == fuzzyNumber.getCentroid()) &
                (rightShoulder == fuzzyNumber.getRightShoulder())) return 0;
        else if ((centroid <= fuzzyNumber.getCentroid()) &
                (centroid - leftShoulder <= fuzzyNumber.getCentroid() - fuzzyNumber.getLeftShoulder()) &
                (centroid + rightShoulder <= fuzzyNumber.getCentroid() + fuzzyNumber.getRightShoulder()))
            return -1;
        else return 1;*/
    }

    public FuzzyNumber inverse() {
        return new FuzzyNumber(0 - leftShoulder, 0 - centroid, 0 - rightShoulder);
    }

    public double getRep() {
        return (leftShoulder + centroid + rightShoulder) / 3;
    }

    public double getCentreOfGravityX() {
        /*double a1 = leftShoulder;
        double b1 = rightShoulder;
        double c1 = centroid;*/

        return (leftShoulder + centroid + rightShoulder) / 3;

        /*double x = (a1 + b1 + c1) / 3;
        double y = 1 / 3;

        if ((a1 == c1) && (b1 == c1)) return c1;

        double i1 = (b1 - a1) / 2;

        double j1 = (b1 - c1) / 2;
        double j2 = getMembership(j1);

        Line l1 = new Line(a1, 0, j1, j2);
        Line l2 = new Line(i1, 0, c1, 1);

        return Line.intersect(l1, l2);*/

        /* double x1 = 1 / (c1 - i1);
       double y1 = 1 - c1 * x1;

       double x2 = (0 - j2) / (a1 - j1);
       double y2 = 0 - a1 * x1;

       double s1 = (y2 - y1) / (x1 - x2);

       return s1;*/
    }

    public static double fuzzyLessThan(double left, double right, double threshold) {
        if (left < right) return 1d;
        else if (left < right + threshold) return (left - right) / threshold;
        else return 0d;
    }

    public static double fuzzyLessThanOrEqualTo(double left, double right, double threshold) {
        if (left <= right) return 1d;
        else if (left <= right + threshold) return (left - right) / threshold;
        else return 0d;
    }

    public static double fuzzyGreaterThan(double left, double right, double threshold) {
        if (left > right) return 1d;
        else if (left + threshold > right) return (right - left) / threshold;
        else return 0d;
    }

    public static double fuzzyGreaterThanOrEqualTo(double left, double right, double threshold) {
        if (left >= right) return 1d;
        else if (left + threshold >= right) return (right - left) / threshold;
        else return 0d;
    }

    public FuzzyNumber getFuzzyMax(FuzzyNumber fn1, FuzzyNumber fn2) {
        return new FuzzyNumber(
                Math.min(fn1.getLeftShoulder(), fn2.getLeftShoulder()),
                Math.max(fn1.getRightShoulder(), fn2.getRightShoulder()),
                Math.max(fn1.getRightShoulder(), fn2.getRightShoulder()));
    }

    public FuzzyNumber getFuzzyMin(FuzzyNumber fn1, FuzzyNumber fn2) {
        return new FuzzyNumber(
                Math.min(fn1.getLeftShoulder(), fn2.getLeftShoulder()),
                Math.min(fn1.getLeftShoulder(), fn2.getLeftShoulder()),
                Math.max(fn1.getRightShoulder(), fn2.getRightShoulder()));
    }

    public double getFuzzyRanking(FuzzyNumber fn, FuzzyNumber min, FuzzyNumber max) {
        double left = intersectMin(fn, min);
        double right = intersectMax(fn, max);
        //y1 = a1x1 + b1, y2 = a2x2 + b2
        return (fn.getMembership(right) + 1 - fn.getMembership(left)) / 2;

    }

    public double intersectMin(FuzzyNumber fn, FuzzyNumber min) {
        Line l1 = new Line(min.getCentroid(), 1, min.getRightShoulder(), 0);
        Line l2 = new Line(fn.getLeftShoulder(), 0, fn.getCentroid(), 1);
        return Line.intersect(l1, l2);
    }

    public double intersectMax(FuzzyNumber fn, FuzzyNumber max) {
        Line l1 = new Line(max.getLeftShoulder(), 0, max.getCentroid(), 1);
        Line l2 = new Line(fn.getCentroid(), 1, fn.getRightShoulder(), 0);
        return Line.intersect(l1, l2);
    }
}
