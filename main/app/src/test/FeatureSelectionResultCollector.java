package test;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 23/11/11
 * Time: 14:37
 * To change this template use File | Settings | File Templates.
 */
public class FeatureSelectionResultCollector extends ExperimentalResultCollector {

    /* private int numFolds;
  private int repeat;
  private String dataset;*/

    public FeatureSelectionResultCollector(int repeat) {
        super(repeat);
        super.put("HarmonySearch", new FeatureSelectionResult[getNumFolds()][repeat]);
        super.put("GeneticSearch", new FeatureSelectionResult[getNumFolds()][repeat]);
        super.put("PSOSearch", new FeatureSelectionResult[getNumFolds()][repeat]);
        super.put("GreedyStepwise", new FeatureSelectionResult[getNumFolds()][repeat]);
    }

    public FeatureSelectionResultCollector(int repeat, int numFolds) {
        super(repeat, numFolds);
        super.put("HarmonySearch", new FeatureSelectionResult[getNumFolds()][repeat]);
        super.put("GeneticSearch", new FeatureSelectionResult[getNumFolds()][repeat]);
        super.put("PSOSearch", new FeatureSelectionResult[getNumFolds()][repeat]);
        super.put("GreedyStepwise", new FeatureSelectionResult[getNumFolds()][repeat]);
    }

    public FeatureSelectionResultCollector(String dataset, int repeat) {
        super(dataset, repeat);
        super.put("HarmonySearch", new FeatureSelectionResult[getNumFolds()][repeat]);
        super.put("GeneticSearch", new FeatureSelectionResult[getNumFolds()][repeat]);
        super.put("PSOSearch", new FeatureSelectionResult[getNumFolds()][repeat]);
        super.put("GreedyStepwise", new FeatureSelectionResult[getNumFolds()][repeat]);
    }
}
