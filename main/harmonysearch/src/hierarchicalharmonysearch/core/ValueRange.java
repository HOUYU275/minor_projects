package hierarchicalharmonysearch.core;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 26/01/11
 * Time: 12:56
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
}
