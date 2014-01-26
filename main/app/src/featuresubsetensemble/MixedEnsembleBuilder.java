package featuresubsetensemble;

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
public class MixedEnsembleBuilder {

	public static FeatureSubsetEnsemble build(Instances data, String searchKeyword, int numEnsembles, ThreadPool threadPool) {
		Vector<int[]> votes = new Vector<>();
		final String[] evaluatorKeywords = new String[]{
				"CFS",
				"PCFS",
				//"FRFS",
				"CHI",
				"RELIEF",
				"IG",
				"SU",
		};
		//String evaluationKeyword = evaluatorKeywords[new Random().nextInt(evaluatorKeywords.length)];
		//threadPool.start();
		/*for (int i = 0; i < numEnsembles; i++) {
			threadPool.submit(new SingleMixedEnsembleBuilder(evaluationKeyword, searchKeyword, votes, data));
		}*/
		Vector<Future<int[]>> futures = new Vector<>();
		for (int i = 0; i < numEnsembles; i++) {
			String evaluationKeyword = evaluatorKeywords[new Random().nextInt(evaluatorKeywords.length)];
			try {
				futures.add(threadPool.submitEvaluator(new SingleMixedEnsembleBuilder(evaluationKeyword, searchKeyword, votes, data)));
			} catch (ExecutionException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
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

	/*public static Vector<int[]> buildFeatureSelectionEnsembleMixed(Instances data, ASSearch search, int numEnsembles,
	boolean repeat) throws Exception {
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
	}*/
}