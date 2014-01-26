package ruleinduction;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 20/12/11
 * Time: 12:30
 * To change this template use File | Settings | File Templates.
 */
public class ValueRange {

    protected double min;
    protected double max;
    protected boolean continuous;

    public ValueRange() {
    }

    public ValueRange(double min, double max, boolean continuous) {
        this.min = min;
        this.max = max;
        this.continuous = continuous;
    }

    public static ValueRange[] createStaticParameters(int HMS, double HMCR, double PAR, double BW) {
        ValueRange[] parameterRanges = new ValueRange[]{
                new ValueRange(HMS, HMS, false),
                new ValueRange(HMCR, HMCR, true),
                new ValueRange(PAR, PAR, true),
                new ValueRange(BW, BW, true),};
        return parameterRanges;
    }
}