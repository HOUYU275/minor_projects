package test;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 24/11/11
 * Time: 11:37
 * To change this template use File | Settings | File Templates.
 */
public class ExperimentalResultCollector extends ConcurrentHashMap<String, ExperimentalResult[][]> {
    private int numFolds;
    private int repeat;
    private String dataset;

    public ExperimentalResultCollector(int repeat) {
        this.repeat = repeat;
        this.numFolds = 10;
        /*super.put("HarmonySearch", new FeatureSelectionResult[numFolds][repeat]);
        super.put("GeneticSearch", new FeatureSelectionResult[numFolds][repeat]);
        super.put("PSOSearch", new FeatureSelectionResult[numFolds][repeat]);
        super.put("GreedyStepwise", new FeatureSelectionResult[numFolds][repeat]);*/
    }

    public ExperimentalResultCollector(int repeat, int numFolds) {
        this.repeat = repeat;
        this.numFolds = numFolds;
        /*FeatureSelectionResult[][] results = new FeatureSelectionResult[numFolds][repeat];
        for (int i = 0; i < numFolds; i++) results[i] = new FeatureSelectionResult[repeat];
        super.put("HarmonySearch", results);
        results = new FeatureSelectionResult[numFolds][repeat];
        for (int i = 0; i < numFolds; i++) results[i] = new FeatureSelectionResult[repeat];
        super.put("GeneticSearch", results);
        results = new FeatureSelectionResult[numFolds][repeat];
        for (int i = 0; i < numFolds; i++) results[i] = new FeatureSelectionResult[repeat];
        super.put("PSOSearch", results);
        results = new FeatureSelectionResult[numFolds][repeat];
        for (int i = 0; i < numFolds; i++) results[i] = new FeatureSelectionResult[repeat];
        super.put("GreedyStepwise", results);*/
    }

    public ExperimentalResultCollector(String dataset, int repeat) {
        this.dataset = dataset;
        this.repeat = repeat;
        this.numFolds = 10;
        /*super.put("HarmonySearch", new FeatureSelectionResult[numFolds][repeat]);
        super.put("GeneticSearch", new FeatureSelectionResult[numFolds][repeat]);
        super.put("PSOSearch", new FeatureSelectionResult[numFolds][repeat]);
        super.put("GreedyStepwise", new FeatureSelectionResult[numFolds][repeat]);*/
    }

    public synchronized void put(String key, int numFold, int repeat, ExperimentalResult result) {
        result.setDatasetName(dataset);
        super.get(key)[numFold][repeat] = result;
    }

    public int getNumFolds() {
        return numFolds;
    }

    public void setNumFolds(int numFolds) {
        this.numFolds = numFolds;
    }

    public int getRepeat() {
        return repeat;
    }

    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }

    public String getDataset() {
        return dataset;
    }

    public void setDataset(String dataset) {
        this.dataset = dataset;
    }
}
