package fuzzy;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 04/02/11
 * Time: 11:46
 * To change this template use File | Settings | File Templates.
 */
public abstract class FuzzyNumber implements Comparable, Cloneable {

    public FuzzyNumber() {
    }

    public FuzzyNumber(float[] points){

    }

    public abstract float distanceTo(FuzzyNumber fuzzyNumber);

    public abstract float absDistanceTo(FuzzyNumber fuzzyNumber);

    public abstract float[] getPoints();

    public abstract float getPoint(int index);

    public abstract void setPoints(float[] points);

    public abstract float getMembership(float value);

    public abstract FuzzyNumber times(float value);

    public abstract FuzzyNumber times(FuzzyNumber fuzzyNumber) throws IncompatibleFuzzyNumberTypeException;

    public abstract FuzzyNumber divides(FuzzyNumber fuzzyNumber) throws IncompatibleFuzzyNumberTypeException;

    public abstract FuzzyNumber divides(float value);

    public abstract FuzzyNumber adds(FuzzyNumber fuzzyNumber) throws IncompatibleFuzzyNumberTypeException;

    public abstract FuzzyNumber adds(double value);

    public abstract FuzzyNumber minus(float value);

    public abstract FuzzyNumber minus(FuzzyNumber fuzzyNumber) throws IncompatibleFuzzyNumberTypeException;

    public abstract String toString();

    public abstract String toStringInt();

    public abstract int compareTo(Object o);

    public abstract FuzzyNumber inverse();

    public abstract float getRepresentativeValue();

    public abstract FuzzyNumber getFuzzyMax(FuzzyNumber fn1, FuzzyNumber fn2);

    public abstract FuzzyNumber getFuzzyMin(FuzzyNumber fn1, FuzzyNumber fn2);

    public abstract FuzzyNumber clone();

    public static float fuzzyLessThan(double left, double right, double threshold) {
        if (left < right) return 1;
        else if (left < right + threshold) return (float) ((left - right) / threshold);
        else return 0;
    }

    public static float fuzzyLessThanOrEqualTo(double left, double right, double threshold) {
        if (left <= right) return 1;
        else if (left <= right + threshold) return (float) ((left - right) / threshold);
        else return 0;
    }

    public static float fuzzyGreaterThan(double left, double right, double threshold) {
        if (left > right) return 1;
        else if (left + threshold > right) return (float) ((right - left) / threshold);
        else return 0;
    }

    public static float fuzzyGreaterThanOrEqualTo(double left, double right, double threshold) {
        if (left >= right) return 1;
        else if (left + threshold >= right) return (float) ((right - left) / threshold);
        else return 0;
    }
}
