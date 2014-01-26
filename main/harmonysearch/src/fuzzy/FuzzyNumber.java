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
        return this.getRepresentativeValue() - fuzzyNumber.getRepresentativeValue();
    }

    public double absDistanceTo(FuzzyNumber fuzzyNumber) {
        return Math.abs(this.getRepresentativeValue() - fuzzyNumber.getRepresentativeValue());
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
        if (value == leftShoulder) return 0d;
        if (value == rightShoulder) return 0d;
        if (value == centroid) return 1d;
        if ((value > rightShoulder) || (value < leftShoulder)) return 0d;
        if ((value > leftShoulder) & (value < centroid)) return (value - leftShoulder) / (centroid - leftShoulder);
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
        if (leftShoulder * fuzzyNumber.getLeftShoulder() > rightShoulder * fuzzyNumber.getRightShoulder())
            return new FuzzyNumber(
                    rightShoulder * fuzzyNumber.getRightShoulder(),
                    centroid * fuzzyNumber.getCentroid(),
                    leftShoulder * fuzzyNumber.getLeftShoulder());
        else
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

    public String toStringInt() {
        return "(" + (int) leftShoulder + "," + (int) centroid + "," + (int) rightShoulder + ")";
    }

    public int compareTo(Object o) {
        FuzzyNumber fn2 = (FuzzyNumber) o;

        FuzzyNumber min = getFuzzyMin(this, fn2);
        FuzzyNumber max = getFuzzyMax(this, fn2);

        double rank1 = getFuzzyRanking(this, min, max);
        double rank2 = getFuzzyRanking(fn2, min, max);

        if (rank1 > rank2) {
            return 1;
        } else if (rank1 < rank2) {
            return -1;
        } else {
            return 0;
        }
    }

    public FuzzyNumber inverse() {
        return new FuzzyNumber(0 - rightShoulder, 0 - centroid, 0 - leftShoulder);
    }

    /*public double getRep() {
        return (leftShoulder + centroid + rightShoulder) / 3;
    }*/

    public double getRepresentativeValue() {
        return (leftShoulder + centroid + rightShoulder) / 3;
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

    public static FuzzyNumber getFuzzyMax(FuzzyNumber fn1, FuzzyNumber fn2) {
        return new FuzzyNumber(
                Math.min(fn1.getLeftShoulder(), fn2.getLeftShoulder()),
                Math.max(fn1.getRightShoulder(), fn2.getRightShoulder()),
                Math.max(fn1.getRightShoulder(), fn2.getRightShoulder()));
    }

    public static FuzzyNumber getFuzzyMin(FuzzyNumber fn1, FuzzyNumber fn2) {
        return new FuzzyNumber(
                Math.min(fn1.getLeftShoulder(), fn2.getLeftShoulder()),
                Math.min(fn1.getLeftShoulder(), fn2.getLeftShoulder()),
                Math.max(fn1.getRightShoulder(), fn2.getRightShoulder()));
    }

    public static double getFuzzyRanking(FuzzyNumber fn, FuzzyNumber min, FuzzyNumber max) {
        double left = intersectMin(fn, min);
        double right = intersectMax(fn, max);
        //y1 = a1x1 + b1, y2 = a2x2 + b2
        return (fn.getMembership(right) + 1 - fn.getMembership(left)) / 2;

    }

    public static double intersectMin(FuzzyNumber fn, FuzzyNumber min) {
        Line l1 = new Line(min.getCentroid(), 1, min.getRightShoulder(), 0);
        Line l2 = new Line(fn.getLeftShoulder(), 0, fn.getCentroid(), 1);
        return Line.intersect(l1, l2);
    }

    public static double intersectMax(FuzzyNumber fn, FuzzyNumber max) {
        Line l1 = new Line(max.getLeftShoulder(), 0, max.getCentroid(), 1);
        Line l2 = new Line(fn.getCentroid(), 1, fn.getRightShoulder(), 0);
        return Line.intersect(l1, l2);
    }
}
