package survey;

import java.util.Arrays;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 01/01/13
 * Time: 21:58
 */
public class FeatureSelectionDetailDouble {
    double merit;
    double size;
    long time;

    public FeatureSelectionDetailDouble(double merit, double size, long time, int[] subset) {
        this.merit = merit;
        this.size = size;
        this.time = time;
    }

    public FeatureSelectionDetailDouble() {
    }

    public double getMerit() {
        return merit;
    }

    public void setMerit(double merit) {
        this.merit = merit;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return merit + "\t" + size;
    }

    public static FeatureSelectionDetailDouble merge(Vector<FeatureSelectionDetailDouble> details) {
        FeatureSelectionDetailDouble detail = new FeatureSelectionDetailDouble();
        if (details.size() == 0) return detail;
        double sumMerit = 0;
        double sumSize = 0d;
        long sumTime = 0;
        for (FeatureSelectionDetailDouble detail1 : details) {
            sumMerit += detail1.merit;
            sumSize += detail1.size;
            sumTime += detail1.time;
        }
        detail.setMerit(sumMerit / details.size());
        detail.setSize(sumSize / details.size());
        detail.setTime(sumTime / details.size());
        return detail;
    }
}