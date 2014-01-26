package test;

import util.ClassifierCrossValidator;
import util.CommonMaths;
import util.CrossValidation;
import util.Dataset;
import weka.attributeSelection.ASEvaluation;
import weka.attributeSelection.ConsistencySubsetEval;
import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.meta.FeatureSelectionEnsemble;
import weka.core.Instances;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 19/01/12
 * Time: 13:21
 * To change this template use File | Settings | File Templates.
 */
public class TestFeatureSelectionEnsemble {

    private static String[] datasets = Dataset.turing_high;
    private static Classifier classifier = new NaiveBayes();
    private static ASEvaluation evaluation = new ConsistencySubsetEval();
    private static String ensembleMethod = "bagging";
    private static int poolSize = 200;
    private static int numFolds = 10;
    private static String mode = "fs";

    public static void main(String[] args) throws Exception {

        reportMode();
        testEnsemble(datasets, classifier, evaluation, ensembleMethod, poolSize, numFolds, mode);

        /*Instances instances = Dataset.getDataset("heart");
        double correct;
        int numFolds = 10;
        String[] datasets = Dataset.hd;
        String classifier = "meta.FeatureSelectionEnsemble";
        double[] acc;
        for (int i = 0; i < datasets.length; i++) {
            String datasetName = datasets[i];
            System.out.println(datasetName);
            CrossValidation crossValidation = new CrossValidation(datasetName, true);
            acc = new double[numFolds];
            for (int j = 0; j < numFolds; j++) {
                Instances training = crossValidation.getTrainingFold(j);
                Instances testing = crossValidation.getTestingFold(j);
                double accuracy = ClassifierCrossValidator.validateClassifier("weka.classifiers." + classifier, training, testing);
                acc[j] = accuracy;
                System.out.format("%.4f, ", accuracy);
            }
            System.out.println();
            System.out.format("average = %.4f%n", average(acc));
            System.gc();
        }

        classifier = "trees.J48";
        for (int i = 0; i < datasets.length; i++) {
            String datasetName = datasets[i];
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
        }*/
    }

    public static void reportMode() {
        System.out.println("Test Feature Selection Ensemble");
        System.out.println("Classifier = \t" + classifier.getClass().getName());
        System.out.println("Ensemble Method = \t" + ensembleMethod);
        System.out.println("Pool Size = \t" + poolSize);
        System.out.println("Num Folds = \t" + numFolds);
        System.out.println("Mode = \t" + mode);
    }

    public static void testEnsemble(String[] datasets, Classifier classifier, ASEvaluation evaluation, String ensembleMethod, int poolSize, int numFolds, String mode) throws Exception {
        double[] accuracies;
        for (int i = 0; i < datasets.length; i++) {
            String datasetName = datasets[i];
            System.out.println(datasetName);
            CrossValidation crossValidation = new CrossValidation(datasetName, true);
            accuracies = new double[numFolds];
            for (int j = 0; j < numFolds; j++) {
                Instances training = crossValidation.getTrainingFold(j);
                Instances testing = crossValidation.getTestingFold(j);
                double accuracy = 0;
                if (mode.equals("single")) {
                    accuracy = ClassifierCrossValidator.validateClassifier(classifier, training, testing);
                }
                else if (mode.equals("fs")) {
                    FeatureSelectionEnsemble ensemble = new FeatureSelectionEnsemble();
                    ensemble.setCacheProperties(datasetName, classifier, ensembleMethod, poolSize, j);
                    ensemble.setEvaluation(evaluation);
                    accuracy = ClassifierCrossValidator.validateClassifier(ensemble, training, testing);
                }
                else if (mode.equals("full")) {
                    FeatureSelectionEnsemble ensemble = new FeatureSelectionEnsemble();
                    ensemble.setCacheProperties(datasetName, classifier, ensembleMethod, poolSize, j);
                    ensemble.setEvaluation(evaluation);
                    ensemble.setUseFull(true);
                    accuracy = ClassifierCrossValidator.validateClassifier(ensemble, training, testing);
                }
                accuracies[j] = accuracy;
                System.out.format("%.4f\n", accuracy);
            }
            System.out.println();
            System.out.format("average =\t%.4f%n", CommonMaths.average(accuracies));
            System.gc();
        }
    }




}
