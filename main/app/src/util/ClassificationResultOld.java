package util;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 07/03/12
 * Time: 14:29
 */
public class ClassificationResultOld {

    private String classifier;
    private int[] features;
    private String dataset;
    private double accuracy;
    private String evaluator;
    private double score;
    private int numFold;

    public ClassificationResultOld() {
    }

    public ClassificationResultOld(String dataset) {
        this.dataset = dataset;
    }

    public String getClassifier() {
        return classifier;
    }

    public void setClassifier(String classifier) {
        this.classifier = classifier;
    }

    public int[] getFeatures() {
        return features;
    }

    public void setFeatures(int[] features) {
        this.features = features;
    }

    public String getDataset() {
        return dataset;
    }

    public void setDataset(String dataset) {
        this.dataset = dataset;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    public String getEvaluator() {
        return evaluator;
    }

    public void setEvaluator(String evaluator) {
        this.evaluator = evaluator;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public int getNumFold() {
        return numFold;
    }

    public void setNumFold(int numFold) {
        this.numFold = numFold;
    }
}
