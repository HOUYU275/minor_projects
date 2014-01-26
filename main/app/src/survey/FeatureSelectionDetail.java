package survey;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 06/12/12
 * Time: 18:59
 */
public class FeatureSelectionDetail implements Serializable {
    private static final long serialVersionUID = -1737157564331697340L;
    double merit;
	int size;
	long time;
	int[] subset;

	public FeatureSelectionDetail(double merit, int size, long time, int[] subset) {
		this.merit = merit;
		this.size = size;
		this.time = time;
		this.subset = subset;
	}

    public FeatureSelectionDetail() {
    }

    public double getMerit() {
		return merit;
	}

	public void setMerit(double merit) {
		this.merit = merit;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public int[] getSubset() {
		return subset;
	}

	public void setSubset(int[] subset) {
		this.subset = subset;
	}

    @Override
    public String toString() {
        return merit + "\t" + size + "\t" + Arrays.toString(subset);
    }

/*    public static FeatureSelectionDetail merge(Vector<FeatureSelectionDetail> details) {
        FeatureSelectionDetail detail = new FeatureSelectionDetail();
        if (details.size() == 0) return detail;
        double sumMerit = 0;
        int sumSize = 0;
        long sumTime = 0;
        for (FeatureSelectionDetail detail1 : details) {
            sumMerit += detail1.merit;
            sumSize += detail1.size;
            sumTime += detail1.time;
        }
        detail.setMerit(sumMerit / details.size());
        detail.setSize(sumSize / details.size());
        detail.setTime(sumTime / details.size());
        return detail;
    }*/
}
