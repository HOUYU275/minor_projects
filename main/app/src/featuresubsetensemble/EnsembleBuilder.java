package featuresubsetensemble;

import util.CrossValidation;
import weka.attributeSelection.*;
import weka.core.Instances;

import java.util.Random;
import java.util.Vector;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by IntelliJ IDEA.
 * User: Ren
 * Date: 23/10/12
 * Time: 11:17
 */
public class EnsembleBuilder {
	private static int maxThreads = 25;
	private static ThreadPoolExecutor threadPoolExecutor;

	public static Vector<int[]> buildFeatureSelectionEnsemblePartition(Instances data, ASEvaluation evaluator, ASSearch search, int numEnsembles, boolean repeat) throws Exception {
		CrossValidation crossValidation;
		Vector<int[]> votes = new Vector<>();
		int numRepeats = repeat ? 10 : 1;
		for (int currentRepeat = 0; currentRepeat < numRepeats; currentRepeat++) {
			crossValidation = new CrossValidation(data, numEnsembles, new Random());
			for (int i = 0; i < crossValidation.getNumFolds(); i++) {
				evaluator.buildEvaluator(crossValidation.getTrainingFold(i));
				votes.add(search.search(evaluator, crossValidation.getTrainingFold(i)));
			}
		}
		return votes;
	}

	public static Vector<int[]> buildFeatureSelectionEnsembleStochastic(Instances data, ASEvaluation evaluator, ASSearch search, int numEnsembles, boolean repeat) throws Exception {
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
	}

	public static Vector<int[]> buildFeatureSelectionEnsembleMixed(Instances data, ASSearch search, int numEnsembles, boolean repeat) throws Exception {
		AttributeSelection attributeSelection = new AttributeSelection();
		Vector<int[]> votes = new Vector<int[]>();
		final ASEvaluation[] evaluators = new ASEvaluation[] {
				new ChiSquaredAttributeEval(),
				new ReliefFAttributeEval(),
				new CfsSubsetEval(),
				new ConsistencySubsetEval(),
				//new FuzzyRoughSubsetEval(),
				new InfoGainAttributeEval(),
				new SymmetricalUncertAttributeEval(),
		};
		Random random = new Random();
		int numRepeats = repeat ? 10 * numEnsembles : 1 * numEnsembles;
		for (int currentRepeat = 0; currentRepeat < numRepeats; currentRepeat++) {
			ASEvaluation evaluator = evaluators[random.nextInt(evaluators.length)];
			//ASEvaluation evaluator = evaluators[5];
			attributeSelection.setEvaluator(evaluator);
			if (evaluator instanceof SubsetEvaluator) {
				attributeSelection.setSearch(search);
			}
			else {
				Ranker ranker = new Ranker();
				ranker.setNumToSelect(data.numAttributes() / 2);
				attributeSelection.setSearch(ranker);
			}
			attributeSelection.SelectAttributes(data);
			//votes.add(search.search(chiSquaredAttributeEval, data));
			votes.add(attributeSelection.selectedAttributes());
		}
		return votes;
	}

}
