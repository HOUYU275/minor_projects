package featuresubsetensemble;

import util.Registry;
import weka.attributeSelection.ASEvaluation;
import weka.attributeSelection.ASSearch;
import weka.attributeSelection.FuzzyRoughSubsetEval;
import weka.attributeSelection.HarmonySearch;
import weka.core.Instances;
import weka.fuzzy.measure.WeakGamma;

import java.util.Vector;
import java.util.concurrent.Callable;

/**
 * Created by IntelliJ IDEA.
 * User: Ren
 * Date: 23/10/12
 * Time: 11:28
 */
public class SingleStochasticEnsembleBuilder implements Callable {

	private String evaluationKeyword;
	private String searchKeyword;
	private Vector<int[]> votes;
	private Instances data;

	public SingleStochasticEnsembleBuilder(String evaluationKeyword, String searchKeyword, Vector<int[]> votes, Instances data) {
		this.evaluationKeyword = evaluationKeyword;
		this.searchKeyword = searchKeyword;
		this.votes = votes;
		this.data = data;
	}

	/*public void run() {
		ASEvaluation evaluation = Registry.getEvaluation(evaluationKeyword);
		ASSearch search = Registry.getSearch(searchKeyword);
		if (Registry.getMusicianHint(data.getName(), evaluation.getClass().getName()) != 0) {
			((HarmonySearch) search).setMusicianHint(Registry.getMusicianHint(data.getName(), evaluation.getClass().getName()));
			((HarmonySearch) search).setIterative(true);
			((HarmonySearch) search).setIteration(500);
		}
        try {
            evaluation.buildEvaluator(data);
            //System.out.println(".");
            int[] result;
            do {
                result = search.search(evaluation, data);
            }
            while (result.length == 0);
            votes.add(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
	}*/

	@Override
	public int[] call() throws Exception {
		ASSearch search = Registry.getSearch(searchKeyword);
        ASEvaluation evaluation = Registry.getEvaluation(evaluationKeyword);
		if (Registry.getMusicianHint(data.getName(), evaluation.getClass().getName()) != 0) {
			((HarmonySearch) search).setMusicianHint(Registry.getMusicianHint(data.getName(), evaluation.getClass().getName()));
			((HarmonySearch) search).setIterative(false);
			((HarmonySearch) search).setIteration(500);
		}
        int[] result = null;
        try {
            evaluation.buildEvaluator(data);
            //System.out.println(".");
            do {
                result = search.search(evaluation, data);
            }
            while (result.length == 0);
            votes.add(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //if (evaluation instanceof FuzzyRoughSubsetEval) ((WeakGamma)((FuzzyRoughSubsetEval)evaluation).getMeasure()).shutdown();
        return result;
	}
}
