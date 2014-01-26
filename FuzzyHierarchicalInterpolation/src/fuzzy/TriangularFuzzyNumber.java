package fuzzy;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 12/10/11
 * Time: 11:55
 * To change this template use File | Settings | File Templates.
 */
public class TriangularFuzzyNumber extends FuzzyNumber {
    private float centroid;
    private float leftShoulder;
    private float rightShoulder;

    public TriangularFuzzyNumber() {
        super();
    }

 /*   public TriangularFuzzyNumber(FuzzyNumber points) {
        this.leftShoulder = points[0];
        this.centroid = points[1];
        this.rightShoulder = points[2];
    }*/

    public TriangularFuzzyNumber(double leftShoulder, double centroid, double rightShoulder) {
        this.centroid = (float) centroid;
        this.leftShoulder = (float) leftShoulder;
        this.rightShoulder = (float) rightShoulder;
    }

    public TriangularFuzzyNumber(float leftShoulder, float centroid, float rightShoulder) {
        this.centroid = centroid;
        this.leftShoulder = leftShoulder;
        this.rightShoulder = rightShoulder;
    }

    public float getSupportLength() {
        return rightShoulder - leftShoulder;
    }

    public float distanceTo(FuzzyNumber fuzzyNumber) {
        return (float) (this.getRepresentativeValue() - fuzzyNumber.getRepresentativeValue());
    }

    public float absDistanceTo(FuzzyNumber fuzzyNumber) {
        return (float) Math.abs(this.getRepresentativeValue() - fuzzyNumber.getRepresentativeValue());
    }

    @Override
    public float[] getPoints() {
        return new float[]{leftShoulder, centroid, rightShoulder};
    }

    @Override
    public float getPoint(int index) {
        switch (index) {
            case 0:
                return leftShoulder;
            case 1:
                return centroid;
            case 2:
                return rightShoulder;
        }
        return 0;
    }

    @Override
    public void setPoints(float[] points) {
        leftShoulder = points[0];
        centroid = points[1];
        rightShoulder = points[2];
    }

    @Override
    public float getMembership(float value) {
        if ((value >= rightShoulder) || (value <= leftShoulder)) return 0;
        if ((value > leftShoulder) & (value <= centroid)) return (value - leftShoulder) / (centroid - leftShoulder);
        else return (rightShoulder - value) / (rightShoulder - centroid);
    }

    public double getCentroid() {
        return centroid;
    }

    public void setCentroid(float centroid) {
        this.centroid = centroid;
    }

    public double getLeftShoulder() {
        return leftShoulder;
    }

    public void setLeftShoulder(float leftShoulder) {
        this.leftShoulder = leftShoulder;
    }

    public double getRightShoulder() {
        return rightShoulder;
    }

    public void setRightShoulder(float rightShoulder) {
        this.rightShoulder = rightShoulder;
    }

    public FuzzyNumber times(float value) {
        return new TriangularFuzzyNumber(leftShoulder * value, centroid * value, rightShoulder * value);
    }

    public FuzzyNumber times(FuzzyNumber fuzzyNumber) throws IncompatibleFuzzyNumberTypeException {
        if (!(fuzzyNumber instanceof TriangularFuzzyNumber))
            throw new IncompatibleFuzzyNumberTypeException();
        return new TriangularFuzzyNumber(
                leftShoulder * fuzzyNumber.getPoints()[0],
                centroid * fuzzyNumber.getPoints()[1],
                rightShoulder * fuzzyNumber.getPoints()[2]);
    }

    public FuzzyNumber divides(FuzzyNumber fuzzyNumber) throws IncompatibleFuzzyNumberTypeException {
        if (!(fuzzyNumber instanceof TriangularFuzzyNumber))
            throw new IncompatibleFuzzyNumberTypeException();
        return new TriangularFuzzyNumber(
                leftShoulder / fuzzyNumber.getPoints()[0],
                centroid / fuzzyNumber.getPoints()[1],
                rightShoulder / fuzzyNumber.getPoints()[2]);
    }

    public FuzzyNumber divides(float value) {
        return new TriangularFuzzyNumber(
                leftShoulder / value,
                centroid / value,
                rightShoulder / value);
    }

    public FuzzyNumber adds(FuzzyNumber fuzzyNumber) throws IncompatibleFuzzyNumberTypeException {
        if (!(fuzzyNumber instanceof TriangularFuzzyNumber))
            throw new IncompatibleFuzzyNumberTypeException();
        return new TriangularFuzzyNumber(
                leftShoulder + fuzzyNumber.getPoints()[0],
                centroid + fuzzyNumber.getPoints()[1],
                rightShoulder + fuzzyNumber.getPoints()[2]);
    }

    public FuzzyNumber adds(double value) {
        return new TriangularFuzzyNumber(
                leftShoulder + value,
                centroid + value,
                rightShoulder + value);
    }

    public FuzzyNumber minus(float value) {
        return new TriangularFuzzyNumber(
                leftShoulder - value,
                centroid - value,
                rightShoulder - value);
    }

    public FuzzyNumber minus(FuzzyNumber fuzzyNumber) throws IncompatibleFuzzyNumberTypeException {
        if (!(fuzzyNumber instanceof TriangularFuzzyNumber))
            throw new IncompatibleFuzzyNumberTypeException();
        return new TriangularFuzzyNumber(
                leftShoulder - fuzzyNumber.getPoints()[0],
                centroid - fuzzyNumber.getPoints()[1],
                rightShoulder - fuzzyNumber.getPoints()[2]);
    }

    public String toString() {
        return "(" + leftShoulder + "," + centroid + "," + rightShoulder + ")";
    }

    public String toStringInt() {
        return "(" + (int) leftShoulder + "," + (int) centroid + "," + (int) rightShoulder + ")";
    }

    public int compareTo(Object o) {
        return 0;
        /*FuzzyNumber fn2 = (FuzzyNumber) o;
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
        }*/
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
        return new TriangularFuzzyNumber(0 - leftShoulder, 0 - centroid, 0 - rightShoulder);
    }

    /*public double getRep() {
        return (leftShoulder + centroid + rightShoulder) / 3;
    }*/

    public float getRepresentativeValue() {
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

    public FuzzyNumber getFuzzyMax(FuzzyNumber fn1, FuzzyNumber fn2) {
        return new TriangularFuzzyNumber(
                Math.min(fn1.getPoints()[0], fn2.getPoints()[0]),
                Math.max(fn1.getPoints()[1], fn2.getPoints()[1]),
                Math.max(fn1.getPoints()[2], fn2.getPoints()[2]));
    }

    public FuzzyNumber getFuzzyMin(FuzzyNumber fn1, FuzzyNumber fn2) {
        return new TriangularFuzzyNumber(
                Math.min(fn1.getPoints()[0], fn2.getPoints()[0]),
                Math.min(fn1.getPoints()[1], fn2.getPoints()[1]),
                Math.max(fn1.getPoints()[2], fn2.getPoints()[2]));
    }

    @Override
    public FuzzyNumber clone() {
        return new TriangularFuzzyNumber(leftShoulder, centroid, rightShoulder);
    }

    /*public float getFuzzyRanking(FuzzyNumber fn, FuzzyNumber min, FuzzyNumber max) {
        double left = intersectMin(fn, min);
        double right = intersectMax(fn, max);
        //y1 = a1x1 + b1, y2 = a2x2 + b2
        return (fn.getMembership(right) + 1 - fn.getMembership(left)) / 2;

    }*/
}