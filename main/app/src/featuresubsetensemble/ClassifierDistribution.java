package featuresubsetensemble;

/**
 * Created by IntelliJ IDEA.
 * User: Ren
 * Date: 19/10/12
 * Time: 16:39
 */
public class ClassifierDistribution {
	private double threshold;
	private double[] distribution;

	public ClassifierDistribution(double threshold, double[] distribution) {
		this.threshold = threshold;
		this.distribution = distribution;
	}

	public double getThreshold() {
		return threshold;
	}

	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}

	public double[] getDistribution() {
		return distribution;
	}

	public void setDistribution(double[] distribution) {
		this.distribution = distribution;
	}
}
