package DynamicFSEnsemble;

import featuresubsetensemble.FeatureSubsetEnsemble;
import online.DataSetEvaluator;
import online.InstanceCreator;

import java.util.Random;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 21/01/13
 * Time: 17:12
 */
public class DynamicFSEnsembleBuilder {

    DynamicFSSearcher[] searchers;
    public FeatureSubsetEnsemble build(InstanceCreator creator, DataSetEvaluator evaluator, int numIterations, Random random, int numEnsembles) {
        Vector<int[]> votes = new Vector<>();
        Vector<Double> merits = new Vector<>();
        searchers = new DynamicFSSearcher[numEnsembles];
        for (int i = 0; i < numEnsembles; i++) {
            searchers[i] = new DynamicFSSearcher(creator, evaluator, creator.getCreated().numAttributes(), numIterations, random);
            try {
                votes.add(searchers[i].call());
                merits.add(evaluator.evaluate(votes.get(i)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        FeatureSubsetEnsemble featureSubsetEnsemble = new FeatureSubsetEnsemble(creator.getCreated().numAttributes());
        featureSubsetEnsemble.convertFrom(votes, merits);
        //if (evaluation instanceof FuzzyRoughSubsetEval) ((WeakGamma)((FuzzyRoughSubsetEval)evaluation).getMeasure()).shutdown();
        return featureSubsetEnsemble;
    }

    public FeatureSubsetEnsemble adapt(InstanceCreator creator, DataSetEvaluator evaluator) {
        Vector<int[]> votes = new Vector<>();
        Vector<Double> merits = new Vector<>();
        for (int i = 0; i < searchers.length; i++) {
            try {
                votes.add(searchers[i].call());
                merits.add(evaluator.evaluate(votes.get(i)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        FeatureSubsetEnsemble featureSubsetEnsemble = new FeatureSubsetEnsemble(creator.getCreated().numAttributes());
        featureSubsetEnsemble.convertFrom(votes, merits);
        //if (evaluation instanceof FuzzyRoughSubsetEval) ((WeakGamma)((FuzzyRoughSubsetEval)evaluation).getMeasure()).shutdown();
        return featureSubsetEnsemble;
    }

}
