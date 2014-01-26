package test;

import util.ClassifierCrossValidator;
import util.CrossValidation;
import util.Dataset;
import util.Registry;
import weka.attributeSelection.CfsSubsetEval;
import weka.attributeSelection.HarmonySearch;
import weka.attributeSelection.ReliabilityFS;
import weka.core.Instances;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 06/03/12
 * Time: 14:16
 */
public class TestReliabilityFS {
    public static void main(String[] args) throws Exception {
        String[] datasets = Dataset.hd;
        String classifier = "trees.J48";
        int numFolds = 10;
        for (int i = 0; i < datasets.length; i++) {

            String datasetName = datasets[i];
            System.out.println(datasetName);
            CrossValidation crossValidation = new CrossValidation(datasetName, false, true);
            double[] selectedAccuracies = new double[numFolds];
            double[] cfsSelectedAccuracies = new double[numFolds];
            double[] fullAccuracies = new double[numFolds];
            for (int j = 0; j < numFolds; j++) {
                Instances training = crossValidation.getTrainingFold(j);
                Instances testing = crossValidation.getTestingFold(j);
                ReliabilityFS reliabilityFS = new ReliabilityFS();
                CfsSubsetEval eval = new CfsSubsetEval();
                eval.buildEvaluator(training);
                HarmonySearch search = new HarmonySearch();
                int[] cfsAttributes = search.search(eval, training);
                int[] selectedAttributes = reliabilityFS.search(null, training);
                selectedAccuracies[j] = ClassifierCrossValidator.validateFilteredClassifier(Registry.PART, selectedAttributes, training, testing);
                cfsSelectedAccuracies[j] = ClassifierCrossValidator.validateFilteredClassifier(Registry.PART, cfsAttributes, training, testing);
                fullAccuracies[j] = ClassifierCrossValidator.validateClassifier(Registry.PART, training, testing);
            }
            System.out.format("selected average = %.4f%n", average(selectedAccuracies));
            System.out.format("cfs selected average = %.4f%n", average(cfsSelectedAccuracies));
            System.out.format("full average = %.4f%n", average(fullAccuracies));
        }
    }

    public static double average(double[] inputs) {
        double sum = 0;
        for (double d : inputs)
            sum += d;
        return sum / inputs.length;
    }
}
