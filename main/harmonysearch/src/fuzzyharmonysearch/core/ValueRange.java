package fuzzyharmonysearch.core;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 06-Jul-2010
 * Time: 11:25:32
 * To change this template use File | Settings | File Templates.
 */
public class ValueRange {
    private double min;
    private double max;
    private boolean continuous;

    public ValueRange() {

    }

    public ValueRange(double min, double max, boolean continuous) {
        this.min = min;
        this.max = max;
        this.continuous = continuous;
    }

    public double getSpread() {
        return max - min;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public boolean isContinuous() {
        return continuous;
    }

    public void setContinuous(boolean continuous) {
        this.continuous = continuous;
    }

    public static ValueRange[] createParameterRanges(int minHMS, int maxHMS,
                                                     double minHMCR, double maxHMCR,
                                                     double minPAR, double maxPAR,
                                                     double minBW, double maxBW) {
        ValueRange[] parameterRanges = new ValueRange[4];
        parameterRanges[0] = new ValueRange(minHMS, maxHMS, false);
        parameterRanges[1] = new ValueRange(minHMCR, maxHMCR, true);
        parameterRanges[2] = new ValueRange(minPAR, maxPAR, true);
        parameterRanges[3] = new ValueRange(minBW, maxBW, true);
        return parameterRanges;
    }

    public static ValueRange[] createParameterRanges(int maxHMS,
                                                     double minHMCR,
                                                     double minPAR,
                                                     double maxBW) {
        ValueRange[] parameterRanges = new ValueRange[4];
        parameterRanges[0] = new ValueRange(maxHMS, maxHMS, false);
        parameterRanges[1] = new ValueRange(minHMCR, minHMCR, true);
        parameterRanges[2] = new ValueRange(minPAR, minPAR, true);
        parameterRanges[3] = new ValueRange(maxBW, maxBW, true);
        return parameterRanges;
    }

    public static ValueRange[] createParameterRanges(int maxHMS,
                                                     double minHMCR) {
        ValueRange[] parameterRanges = new ValueRange[4];
        parameterRanges[0] = new ValueRange(maxHMS / 2, maxHMS, false);
        parameterRanges[1] = new ValueRange(minHMCR, 1, true);
        parameterRanges[2] = new ValueRange(0, 0, true);
        parameterRanges[3] = new ValueRange(Double.MIN_VALUE, Double.MIN_VALUE, true);
        return parameterRanges;
    }
}
