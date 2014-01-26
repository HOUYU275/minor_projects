package featuresubsetensemble;

import util.Registry;
import weka.attributeSelection.ASEvaluation;
import weka.attributeSelection.ASSearch;
import weka.attributeSelection.HarmonySearch;
import weka.core.Instances;

import java.util.concurrent.Callable;

/**
 * Created by IntelliJ IDEA.
 * User: Ren
 * Date: 01/11/12
 * Time: 12:44
 */
public class ThreadedEvaluator implements Callable {
	private ASEvaluation evaluation;
	private String searchKeyword;
	private Instances data;

	public ThreadedEvaluator(String evaluationKeyword, String searchKeyword, Instances data) {
		this.evaluation = Registry.getEvaluation(evaluationKeyword);
		this.searchKeyword = searchKeyword;
		this.data = data;
	}

	public ThreadedEvaluator(ASEvaluation evaluation, String searchKeyword, Instances data) {
		this.evaluation = evaluation;
		this.searchKeyword = searchKeyword;
		this.data = data;
	}

	@Override
	public int[] call() throws Exception {
		ASSearch search = Registry.getSearch(searchKeyword);
		evaluation.buildEvaluator(data);
		int[] result = null;
		if (Registry.getMusicianHint(data.getName(), evaluation.getClass().getName()) != 0) {
			((HarmonySearch) search).setMusicianHint(Registry.getMusicianHint(data.getName(), evaluation.getClass().getName()));
			((HarmonySearch) search).setIterative(false);
			((HarmonySearch) search).setIteration(500);
		}
		try {
			do {
				result = search.search(evaluation, data);
			}
			while (result.length == 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
