package featuresubsetensemble;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Ren
 * Date: 19/10/12
 * Time: 16:53
 */
public class CombinedClassifierDistribution extends ArrayList<ClassifierDistribution> {
	public double[] aggregate() {
		double thresholdSum = 0d;
		int numberOfClasses = this.get(this.size() - 1).getDistribution().length;
		for (int i = 0; i < this.size(); i++) {
			ClassifierDistribution distribution = this.get(i);
			thresholdSum += distribution.getThreshold();
		}
		double[] aggregatedDistribution = new double[numberOfClasses];
		for (int i = 0; i < this.size(); i++) {
			ClassifierDistribution distribution = this.get(i);
			for (int j = 0; j < numberOfClasses; j++) {
				aggregatedDistribution[j] += distribution.getDistribution()[j]/* * distribution.getThreshold() / thresholdSum*/;
			}
		}
		return aggregatedDistribution;
	}

	public double classifyInstance() {
		double[] aggregatedDistribution = this.aggregate();
		int bestClassIndex = -1;
		double bestClassProbability = -1d;
		for (int i = 0; i < aggregatedDistribution.length; i++) {
			if (bestClassProbability < aggregatedDistribution[i]) {
				bestClassProbability = aggregatedDistribution[i];
				bestClassIndex = i;
			}
		}
		return (double) bestClassIndex;
	}
}
