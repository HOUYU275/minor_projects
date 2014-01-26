package featuresubsetensemble;

import util.Registry;
import weka.attributeSelection.*;
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
public class SingleMixedEnsembleBuilder implements Callable {

    private String evaluationKeyword;
    private String searchKeyword;
    private Vector<int[]> votes;
    private Instances data;

    public SingleMixedEnsembleBuilder(String evaluationKeyword, String searchKeyword, Vector<int[]> votes, Instances data) {
        this.evaluationKeyword = evaluationKeyword;
        this.searchKeyword = searchKeyword;
        this.votes = votes;
        this.data = data;
    }

    public void run() {
        AttributeSelection attributeSelection = new AttributeSelection();
        ASEvaluation evaluation = Registry.getEvaluation(evaluationKeyword);
        ASSearch search = Registry.getSearch(searchKeyword);
        try {
            attributeSelection.setEvaluator(evaluation);
            if (evaluation instanceof SubsetEvaluator) {
                attributeSelection.setSearch(search);
                if (Registry.getMusicianHint(data.getName(), evaluationKeyword) != 0)
                    ((HarmonySearch) search).setMusicianHint(Registry.getMusicianHint(data.getName(), evaluationKeyword));
            } else {
                Ranker ranker = new Ranker();

                ranker.setNumToSelect(Registry.getMusicianHint(data.getName(), evaluationKeyword));
                attributeSelection.setSearch(ranker);
            }
            attributeSelection.SelectAttributes(data);
            votes.add(attributeSelection.selectedAttributes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int[] call() throws Exception {
        AttributeSelection attributeSelection = new AttributeSelection();
        ASEvaluation evaluation = Registry.getEvaluation(evaluationKeyword);
        ASSearch search = Registry.getSearch(searchKeyword);
        int[] result = null;
        try {
            attributeSelection.setEvaluator(evaluation);
            if (evaluation instanceof SubsetEvaluator) {
                attributeSelection.setSearch(search);
                if (Registry.getMusicianHint(data.getName(), evaluation.getClass().getName()) != 0) {
                    ((HarmonySearch) search).setMusicianHint(Registry.getMusicianHint(data.getName(), evaluation.getClass().getName()));
                    ((HarmonySearch) search).setIterative(false);
                    ((HarmonySearch) search).setIteration(500);
                }
            } else {
                Ranker ranker = new Ranker();
                ranker.setNumToSelect(data.numAttributes() / 2);
                attributeSelection.setSearch(ranker);
            }
            attributeSelection.SelectAttributes(data);
            result = attributeSelection.selectedAttributes();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //if (evaluation instanceof FuzzyRoughSubsetEval)
        //    ((WeakGamma) ((FuzzyRoughSubsetEval) evaluation).getMeasure()).shutdown();
        return result;
    }
}
