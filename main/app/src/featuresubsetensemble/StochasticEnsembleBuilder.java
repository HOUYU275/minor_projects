package featuresubsetensemble;

import util.ThreadPool;
import weka.core.Instances;

import java.util.Vector;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by IntelliJ IDEA.
 * User: Ren
 * Date: 23/10/12
 * Time: 11:26
 */
public class StochasticEnsembleBuilder {

	public static FeatureSubsetEnsemble build(Instances data, String evaluationKeyword, String searchKeyword, int numEnsembles, ThreadPool threadPool) {
		Vector<int[]> votes = new Vector<>();
		//threadPool.start();

        Vector<Future<int[]>> futures = new Vector<>();
		for (int i = 0; i < numEnsembles; i++) {
			try {
				futures.add(threadPool.submitEvaluator(new SingleStochasticEnsembleBuilder(evaluationKeyword, searchKeyword, votes, data)));
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
		//if (evaluation instanceof FuzzyRoughSubsetEval) ((WeakGamma)((FuzzyRoughSubsetEval)evaluation).getMeasure()).shutdown();
		return featureSubsetEnsemble;
	}

	/*public static Vector<int[]> buildFeatureSelectionEnsembleStochastic(Instances data, ASEvaluation evaluator, ASSearch search, int numEnsembles, boolean repeat) throws Exception {
		CrossValidation crossValidation;
		Vector<int[]> votes = new Vector<int[]>();
		int numRepeats = repeat ? 10 : 1;
		evaluator.buildEvaluator(data);
		for (int currentRepeat = 0; currentRepeat < numRepeats; currentRepeat++) {
			//crossValidation = new CrossValidation(data, numEnsembles, new Random());
			for (int i = 0; i < numEnsembles; i++) {
				votes.add(search.search(evaluator, data));
			}
		}
		return votes;
	}*/
}