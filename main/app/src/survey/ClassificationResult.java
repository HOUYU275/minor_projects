package survey;

import java.io.Serializable;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 07/12/12
 * Time: 13:50
 */
public class ClassificationResult implements Serializable {

    private static final long serialVersionUID = -7251943880032802269L;
    private ClassificationDetail[][][] classificationDetails;
    private ClassificationDetail summary;
    private Vector<FeatureSelectionResult> featureSelectionResults;

    private String dataset;
    private String search;
    private int topRepeats;
    private int folds;
    private int botRepeats;
    private String classifier;
    private String evaluator;

    public ClassificationResult(FeatureSelectionResult featureSelectionResult, String classifier) {
        this.featureSelectionResults = featureSelectionResults;
        this.classifier = classifier;
        this.dataset = featureSelectionResult.getDataset();
        this.topRepeats = featureSelectionResult.getTopRepeat();
        this.folds = featureSelectionResult.getFold();
        this.botRepeats = featureSelectionResult.getBotRepeats();
        this.classificationDetails = new ClassificationDetail[topRepeats][folds][botRepeats];
    }

    public ClassificationResult(String dataset, String search, int topRepeats, int folds, int botRepeats, String classifier, String evaluator) {
        this.dataset = dataset;
        this.search = search;
        this.topRepeats = topRepeats;
        this.folds = folds;
        this.botRepeats = botRepeats;
        this.classifier = classifier;
        this.evaluator = evaluator;
        this.classificationDetails = new ClassificationDetail[topRepeats][folds][botRepeats];
    }

    public String getEvaluator() {
        return evaluator;
    }

    public void setEvaluator(String evaluator) {
        this.evaluator = evaluator;
    }

    public ClassificationResult(String classifier, String dataset, int topRepeats, int folds, int botRepeats) {
        this.classifier = classifier;
        this.dataset = dataset;
        this.topRepeats = topRepeats;
        this.folds = folds;
        this.botRepeats = botRepeats;
        this.classificationDetails = new ClassificationDetail[topRepeats][folds][botRepeats];
    }

    public void set(int top, int fold, int bot, ClassificationDetail classificationDetail) {
        classificationDetails[top][fold][bot] = classificationDetail;
    }

    public ClassificationDetail[][][] getClassificationDetails() {
        return classificationDetails;
    }

    public void setClassificationDetails(ClassificationDetail[][][] classificationDetails) {
        this.classificationDetails = classificationDetails;
    }

    public Vector<FeatureSelectionResult> getFeatureSelectionResults() {
        return featureSelectionResults;
    }

    public void setFeatureSelectionResults(Vector<FeatureSelectionResult> featureSelectionResults) {
        this.featureSelectionResults = featureSelectionResults;
    }

    public String getDataset() {
        return dataset;
    }

    public void setDataset(String dataset) {
        this.dataset = dataset;
    }

    public int getTopRepeats() {
        return topRepeats;
    }

    public void setTopRepeats(int topRepeats) {
        this.topRepeats = topRepeats;
    }

    public int getFolds() {
        return folds;
    }

    public void setFolds(int folds) {
        this.folds = folds;
    }

    public int getBotRepeats() {
        return botRepeats;
    }

    public void setBotRepeats(int botRepeats) {
        this.botRepeats = botRepeats;
    }

    public String getClassifier() {
        return classifier;
    }

    public void setClassifier(String classifier) {
        this.classifier = classifier;
    }

    public void report() {
        for (int t = 0; t < topRepeats; t++) {
            for (int f = 0; f < folds; f++) {
                for (int b = 0; b < botRepeats; b++) {
                    System.out.println(dataset + "\t" + t + "\t" + f + "\t" + b + "\t" + classificationDetails[t][f][b]);
                }
            }
        }
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public ClassificationDetail summary() {
        this.summary = aggregate();
        return this.summary;
    }

    public ClassificationDetail aggregate() {
        double total = topRepeats * folds * botRepeats;
        ClassificationDetail detail = new ClassificationDetail();
        double percent_correct = 0;
        double root_mean_squared_error = 0;
        double true_positive_rate = 0;
        double true_negative_rate = 0;
        double precision = 0;
        double recall = 0;
        double f_measure = 0;
        double area_under_ROC = 0;
        double area_under_PRC = 0;
        for (int t = 0; t < topRepeats; t++) {
            for (int f = 0; f < folds; f++) {
                for (int b = 0; b < botRepeats; b++) {
                    percent_correct += classificationDetails[t][f][b].getPercent_correct();
                    root_mean_squared_error += classificationDetails[t][f][b].getRoot_mean_squared_error();
                    true_positive_rate += classificationDetails[t][f][b].getTrue_positive_rate();
                    true_negative_rate += classificationDetails[t][f][b].getTrue_negative_rate();
                    precision += classificationDetails[t][f][b].getPrecision();
                    recall += classificationDetails[t][f][b].getRecall();
                    f_measure += classificationDetails[t][f][b].getF_measure();
                    area_under_ROC += classificationDetails[t][f][b].getArea_under_ROC();
                    area_under_PRC += classificationDetails[t][f][b].getArea_under_PRC();
                }
            }
        }

        detail.setPercent_correct(percent_correct / total);
        detail.setRoot_mean_squared_error(root_mean_squared_error / total);
        detail.setTrue_positive_rate(true_positive_rate / total);
        detail.setTrue_negative_rate(true_negative_rate / total);
        detail.setPrecision(precision / total);
        detail.setRecall(recall / total);
        detail.setF_measure(f_measure / total);
        detail.setArea_under_ROC(area_under_ROC / total);
        detail.setArea_under_PRC(area_under_PRC / total);

        return detail;
    }

    public ClassificationDetail getSummary() {
        return summary;
    }

    public void setSummary(ClassificationDetail summary) {
        this.summary = summary;
    }
}
