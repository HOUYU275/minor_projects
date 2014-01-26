package fuzzy;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 04/02/11
 * Time: 11:46
 * To change this template use File | Settings | File Templates.
 */
public class TrapezoidalFuzzyNumber extends FuzzyNumber {

    private float leftCentroid;
    private float rightCentroid;
    private float leftShoulder;
    private float rightShoulder;

    public TrapezoidalFuzzyNumber() {
        super();
    }

    public TrapezoidalFuzzyNumber(float[] points) {
        this.leftShoulder = points[0];
        this.leftCentroid = points[1];
        this.rightCentroid = points[2];
        this.rightShoulder = points[3];
    }

    public TrapezoidalFuzzyNumber(double leftShoulder, double leftCentroid, double rightCentroid, double rightShoulder) {
        this.leftCentroid = (float)leftCentroid;
        this.rightCentroid = (float)rightCentroid;
        this.leftShoulder = (float)leftShoulder;
        this.rightShoulder = (float)rightShoulder;
    }

    public TrapezoidalFuzzyNumber(float leftShoulder, float leftCentroid, float rightCentroid, float rightShoulder) {
        this.leftCentroid = leftCentroid;
        this.rightCentroid = rightCentroid;
        this.leftShoulder = leftShoulder;
        this.rightShoulder = rightShoulder;
    }



    public float getBottomSupportLength() {
        return rightShoulder - leftShoulder;
    }

    public float getTopSupportLength() {
        return rightCentroid - leftCentroid;
    }

    public float distanceTo(FuzzyNumber fuzzyNumber) {
        return this.getRepresentativeValue() - fuzzyNumber.getRepresentativeValue();
    }

    public float absDistanceTo(FuzzyNumber fuzzyNumber) {
        return Math.abs(this.getRepresentativeValue() - fuzzyNumber.getRepresentativeValue());
    }

    @Override
    public float[] getPoints() {
        return new float[]{leftShoulder, leftCentroid, rightCentroid, rightShoulder};
    }

    @Override
    public float getPoint(int index) {
        switch (index) {
            case 0:
                return leftShoulder;
            case 1:
                return leftCentroid;
            case 2:
                return rightCentroid;
            case 3:
                return rightShoulder;
        }
        return 0;
    }

    @Override
    public void setPoints(float[] points) {
        leftShoulder = points[0];
        leftCentroid = points[1];
        rightCentroid = points[2];
        rightShoulder = points[3];
    }

    public float getLeftCentroid() {
        return leftCentroid;
    }
    public float getRightCentroid() {
        return rightCentroid;
    }
    public float getLeftShoulder() {
        return leftShoulder;
    }

    public void setLeftShoulder(float leftShoulder) {
        this.leftShoulder = leftShoulder;
    }

    public float getRightShoulder() {
        return rightShoulder;
    }

    public void setRightShoulder(float rightShoulder) {
        this.rightShoulder = rightShoulder;
    }

    public void setRightCentroid(float rightCentroid) {
        this.rightCentroid = rightCentroid;
    }

    public void setLeftCentroid(float leftCentroid) {
        this.leftCentroid = leftCentroid;
    }

    public float getMembership(float value) {
        if ((value >= rightShoulder) || (value <= leftShoulder)) return 0f;
        if ((value > leftShoulder) & (value <= leftCentroid))
            return (value - leftShoulder) / (leftCentroid - leftShoulder);
        else return (rightShoulder - value) / (rightShoulder - rightCentroid);
    }

    public FuzzyNumber times(float value) {
        return new TrapezoidalFuzzyNumber(leftShoulder * value, leftCentroid * value, rightCentroid * value, rightShoulder * value);
    }

    public FuzzyNumber times(FuzzyNumber fuzzyNumber) throws IncompatibleFuzzyNumberTypeException {
        if (!(fuzzyNumber instanceof TrapezoidalFuzzyNumber)) throw new IncompatibleFuzzyNumberTypeException();
        return new TrapezoidalFuzzyNumber(
                leftShoulder * fuzzyNumber.getPoints()[0],
                leftCentroid * fuzzyNumber.getPoints()[1],
                rightCentroid * fuzzyNumber.getPoints()[2],
                rightShoulder * fuzzyNumber.getPoints()[3]);
    }

    public FuzzyNumber divides(FuzzyNumber fuzzyNumber) throws IncompatibleFuzzyNumberTypeException {
        if (!(fuzzyNumber instanceof TrapezoidalFuzzyNumber)) throw new IncompatibleFuzzyNumberTypeException();
        return new TrapezoidalFuzzyNumber(
                leftShoulder / fuzzyNumber.getPoints()[0],
                leftCentroid / fuzzyNumber.getPoints()[1],
                rightCentroid / fuzzyNumber.getPoints()[2],
                rightShoulder / fuzzyNumber.getPoints()[3]);
    }

    public FuzzyNumber divides(float value) {
        return new TrapezoidalFuzzyNumber(
                leftShoulder / value,
                leftCentroid / value,
                rightCentroid / value,
                rightShoulder / value);
    }

    public FuzzyNumber adds(FuzzyNumber fuzzyNumber) throws IncompatibleFuzzyNumberTypeException {
        if (!(fuzzyNumber instanceof TrapezoidalFuzzyNumber)) throw new IncompatibleFuzzyNumberTypeException();
        return new TrapezoidalFuzzyNumber(
                leftShoulder + fuzzyNumber.getPoints()[0],
                leftCentroid + fuzzyNumber.getPoints()[1],
                rightCentroid + fuzzyNumber.getPoints()[2],
                rightShoulder + fuzzyNumber.getPoints()[3]);
    }

    public FuzzyNumber adds(double value) {
        return new TrapezoidalFuzzyNumber(
                leftShoulder + value,
                leftCentroid + value,
                rightCentroid + value,
                rightShoulder + value);
    }

    public FuzzyNumber minus(float value) {
        return new TrapezoidalFuzzyNumber(
                leftShoulder - value,
                leftCentroid - value,
                rightCentroid - value,
                rightShoulder - value);
    }

    public FuzzyNumber minus(FuzzyNumber fuzzyNumber) throws IncompatibleFuzzyNumberTypeException {
        if (!(fuzzyNumber instanceof TrapezoidalFuzzyNumber)) throw new IncompatibleFuzzyNumberTypeException();
        return new TrapezoidalFuzzyNumber(
                leftShoulder - fuzzyNumber.getPoints()[0],
                leftCentroid - fuzzyNumber.getPoints()[1],
                rightCentroid - fuzzyNumber.getPoints()[2],
                rightShoulder - fuzzyNumber.getPoints()[3]);
    }

    public String toString() {
        return "(" + leftShoulder + "," + leftCentroid + "," + rightCentroid + "," + rightShoulder + ")";
    }

    @Override
    public String toStringInt() {
        return "(" + (int)leftShoulder + "," + (int)leftCentroid + "," + (int)rightCentroid + "," + (int)rightShoulder + ")";
    }

    public int compareTo(Object o) {
        FuzzyNumber fn2 = (FuzzyNumber) o;
        //System.out.println(this + " v.s. " + fn2);
        FuzzyNumber min = getFuzzyMin(this, fn2);
        FuzzyNumber max = getFuzzyMax(this, fn2);
        //System.out.println(min + " v.s. " + max);
        float rank1 = getFuzzyRanking(this, min, max);
        float rank2 = getFuzzyRanking(fn2, min, max);

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
                (rightShoulder == fuzzyNumber.getPoints()[3])) return 0;
        else if ((centroid <= fuzzyNumber.getCentroid()) &
                (centroid - leftShoulder <= fuzzyNumber.getCentroid() - fuzzyNumber.getLeftShoulder()) &
                (centroid + rightShoulder <= fuzzyNumber.getCentroid() + fuzzyNumber.getPoints()[3]))
            return -1;
        else return 1;*/
    }

    public FuzzyNumber inverse() {
        return new TrapezoidalFuzzyNumber(0 - leftShoulder, 0 - leftCentroid, 0 - rightCentroid, 0 - rightShoulder);
    }

    @Override
    public float getRepresentativeValue() {
        return (leftShoulder + (leftCentroid + rightCentroid) / 2 + rightShoulder) / 3;
    }

    /*public float getRep() {
        return (leftShoulder + (leftCentroid + rightCentroid) / 2 + rightShoulder) / 3;
    }*/

    public FuzzyNumber getFuzzyMax(FuzzyNumber fn1, FuzzyNumber fn2) {
        return new TrapezoidalFuzzyNumber(
                Math.min(fn1.getPoints()[0], fn2.getPoints()[0]),
                Math.min(fn1.getPoints()[1], fn2.getPoints()[1]),
                Math.max(fn1.getPoints()[2], fn2.getPoints()[2]),
                Math.max(fn1.getPoints()[3], fn2.getPoints()[3]));
    }

    public FuzzyNumber getFuzzyMin(FuzzyNumber fn1, FuzzyNumber fn2) {
        return new TrapezoidalFuzzyNumber(
                Math.min(fn1.getPoints()[0], fn2.getPoints()[0]),
                Math.min(fn1.getPoints()[0], fn2.getPoints()[0]),
                Math.max(fn1.getPoints()[3], fn2.getPoints()[3]),
                Math.max(fn1.getPoints()[3], fn2.getPoints()[3]));
    }

    @Override
    public FuzzyNumber clone() {
        return new TrapezoidalFuzzyNumber(leftShoulder, leftCentroid, rightCentroid, rightShoulder);
    }

    public float getFuzzyRanking(FuzzyNumber fn, FuzzyNumber min, FuzzyNumber max) {
        return 0;
        /*float left = intersectMin(fn, min);
        float right = intersectMax(fn, max);
        //y1 = a1x1 + b1, y2 = a2x2 + b2
        return (fn.getMembership(right) + 1 - fn.getMembership(left)) / 2;*/

    }

    public float getTopBottomSupportRatio() {
        return (rightCentroid - leftCentroid) / (rightShoulder - leftShoulder);
    }

    /*public float intersectMin(FuzzyNumber fn, FuzzyNumber min) {
        Line l1 = new Line(min.getRightCentroid(), 1, min.getRightShoulder(), 0);
        Line l2 = new Line(fn.getLeftShoulder(), 0, fn.getLeftCentroid(), 1);
        return Line.intersect(l1, l2);
    }

    public float intersectMax(FuzzyNumber fn, FuzzyNumber max) {
        Line l1 = new Line(max.getLeftShoulder(), 0, max.getLeftCentroid(), 1);
        Line l2 = new Line(fn.getRightCentroid(), 1, fn.getRightShoulder(), 0);
        return Line.intersect(l1, l2);
    }*/
}
