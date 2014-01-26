package test;

import util.ClassifierCrossValidator;
import util.CrossValidation;
import util.Dataset;
import weka.attributeSelection.ASEvaluation;
import weka.attributeSelection.CfsSubsetEval;
import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.trees.J48;
import weka.core.Instances;

/**
 * User: rrd09, Date: 19/01/12, Time: 13:21
 */
public class FeatureSelectionEnsemble {
    //String classifier = "meta.FeatureSelectionEnsemble";
    private static int numFolds = 10;

    public static void main(String[] args) throws Exception {

        ASEvaluation evaluation = new CfsSubsetEval();
        Classifier base = new J48();
        int poolSize = 100;
        boolean useFull = false;
        boolean useRandom = false;
        String[] datasets = Dataset.CES2;

        //compare();

        System.out.println("Evaluator\t" + evaluation.getClass().toString());
        System.out.println("Pool Size\t" + poolSize);
        System.out.println("Use Full\t" + useFull);
        System.out.println("Use Random\t" + useRandom);


        double[] acc;
        for (String datasetName : datasets) {
            System.out.println(datasetName);
            CrossValidation crossValidation = new CrossValidation(datasetName, false);
            acc = new double[numFolds];
            for (int j = 0; j < numFolds; j++) {
                Instances training = crossValidation.getTrainingFold(j);
                Instances testing = crossValidation.getTestingFold(j);
                double accuracy = ClassifierCrossValidator.validateCES(base, training, testing, evaluation, poolSize, useFull, useRandom);
                double baseAccuracy = ClassifierCrossValidator.validateClassifier("weka.classifiers.trees.J48", training, testing);
                acc[j] = accuracy;
                System.out.format("\tensemble acc\t%.4f\tbase acc\t%.4f\n", accuracy, baseAccuracy);
            }
            System.out.println();
            System.out.format("average\t%.4f%n", average(acc));
            System.gc();
        }
    }

    public static void compare() throws Exception {
        String classifier = "trees.J48";
        String[] datasets = Dataset.CES;

        double[] acc;
        for (String datasetName : datasets) {
            System.out.println(datasetName);
            CrossValidation crossValidation = new CrossValidation(Dataset.getDataset(datasetName));
            acc = new double[numFolds];
            for (int j = 0; j < numFolds; j++) {
                Instances training = crossValidation.getTrainingFold(j);
                Instances testing = crossValidation.getTestingFold(j);
                double accuracy = ClassifierCrossValidator.validateClassifier("weka.classifiers." + classifier, training, testing);
                acc[j] = accuracy;
                System.out.print(accuracy + ",");
            }
            System.out.println();
            System.out.println("average = " + average(acc));
            System.gc();
        }
    }

    public static double average(double[] inputs) {
        double sum = 0;
        for (double d : inputs) sum += d;
        return sum / inputs.length;
    }

}
