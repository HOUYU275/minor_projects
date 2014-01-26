package featuresubsetensemble;

import util.CrossValidation;
import util.ThreadPool;
import weka.core.Instances;

import java.util.Random;
import java.util.Vector;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by IntelliJ IDEA.
 * User: Ren
 * Date: 23/10/12
 * Time: 11:26
 */
public class DataPartitionEnsembleBuilder {


	public static FeatureSubsetEnsemble build(Instances data, String evaluationKeyword, String searchKeyword, int numEnsembles, ThreadPool threadPool) {
		Vector<int[]> votes = new Vector<>();
		Vector<Future<int[]>> futures = new Vector<>();
		//threadPool.start();
		CrossValidation crossValidation = new CrossValidation(data, numEnsembles, new Random());

		for (int i = 0; i < numEnsembles; i++) {
			try {
				futures.add(threadPool.submitEvaluator(new SingleDataPartitionEnsembleBuilder(evaluationKeyword, searchKeyword, votes, crossValidation.getTrainingFold(i))));
			} catch (ExecutionException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		//threadPool.waitForCompletion();
		for (int i = 0; i < futures.size(); i++) {
			try {
				votes.add(futures.get(i).get());
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
		FeatureSubsetEnsemble featureSubsetEnsemble = new FeatureSubsetEnsemble(data.numAttributes());
		featureSubsetEnsemble.convertFrom(votes);
		return featureSubsetEnsemble;
	}


	/*public static ArrayList<int[]> buildFeatureSelectionEnsemblePartition(Instances data, ASEvaluation evaluator, ASSearch search, int numEnsembles, boolean repeat) throws Exception {
		CrossValidation crossValidation;
		ArrayList<int[]> votes = new ArrayList<>();
		int numRepeats = repeat ? 10 : 1;
		for (int currentRepeat = 0; currentRepeat < numRepeats; currentRepeat++) {
			crossValidation = new CrossValidation(data, numEnsembles, new Random());
			for (int i = 0; i < crossValidation.getNumFolds(); i++) {
				evaluator.buildEvaluator(crossValidation.getTrainingFold(i));
				votes.add(search.search(evaluator, crossValidation.getTrainingFold(i)));
			}
		}
		return votes;
	}*/
}