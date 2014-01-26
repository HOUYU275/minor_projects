package test;

import util.*;
import weka.classifiers.AbstractClassifier;
import weka.classifiers.Classifier;
import weka.core.Instances;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 30/11/11
 * Time: 16:16
 * To change this template use File | Settings | File Templates.
 */
public class TestHSFSClassification {

    public static void main(String[] args) throws Exception {

        String evaluationMethod = "CON";
        //String classifier = "bayes.NaiveBayes";
        //String classifier = "trees.J48";
        //String classifier = "functions.SMO";
        String classifier = "trees.J48";

        BufferedWriter writer = new FileIOHandler().getWriter(evaluationMethod + "_" + evaluationMethod + "_" + classifier + "_accuracy_" + FormatConverter.getTimeStamp() + ".txt");

        for (int i = 0; i < Dataset.smcb.length; i++) {
            String datasetName = Dataset.smcb[i];
            writer.append(datasetName + "\t" + datasetName + "\t" + datasetName);
            writer.newLine();
            writer.newLine();
            int count = 5;
            CrossValidation crossValidation = new CrossValidation(Dataset.getDataset(datasetName));
            //validateFull(classifier, crossValidation, writer);
            validateBySearch(classifier, crossValidation, datasetName + "_" + evaluationMethod + ".txt", "HS", count, writer);
            writer.newLine();
            writer.newLine();
            /*validateBySearch(classifier, crossValidation, datasetName + "_" + evaluationMethod + ".txt", "GA", count, writer);
            writer.newLine();
            writer.newLine();
            validateBySearch(classifier, crossValidation, datasetName + "_" + evaluationMethod + ".txt", "PSO", count, writer);
            writer.newLine();
            writer.newLine();
            validateBySearch(classifier, crossValidation, datasetName + "_" + evaluationMethod + ".txt", "HC", count / 5, writer);
            writer.newLine();
            writer.newLine();*/
        }

        /*for (int i = 0; i < Dataset.count10.length; i++) {
            String datasetName = Dataset.count10[i];
            writer.append(datasetName + "\t" + datasetName + "\t" + datasetName);
            writer.newLine();
            writer.newLine();
            int count = 10;
            CrossValidation crossValidation = new CrossValidation(Dataset.getDataset(datasetName));

            validateBySearch(classifier, crossValidation, datasetName + "_" + evaluationMethod + ".txt", "HS", count, writer);
            writer.newLine();
            writer.newLine();
            //validateFull(classifier, crossValidation, writer);
            validateBySearch(classifier, crossValidation, datasetName + "_" + evaluationMethod + ".txt", "GA", count, writer);
            writer.newLine();
            writer.newLine();
            validateBySearch(classifier, crossValidation, datasetName + "_" + evaluationMethod + ".txt", "PSO", count, writer);
            writer.newLine();
            writer.newLine();
            validateBySearch(classifier, crossValidation, datasetName + "_" + evaluationMethod + ".txt", "HC", count / 5, writer);
            writer.newLine();
            writer.newLine();
        }*/
        writer.close();
    }

    private static void validateBySearch(String classifierName, CrossValidation crossValidation, String fileName, String key, int count, BufferedWriter writer) throws Exception {
        ArrayList<String[]> filterStringsHS = getFilterStrings("results\\HSO\\" + fileName, key, count);
        for (int i = 0; i < filterStringsHS.size(); i++) {
            Instances training = crossValidation.getTrainingFold(i);
            Instances testing = crossValidation.getTestingFold(i);
            //Classifier classifer = Classifier.forName("weka.classifiers." + classifierName, null);
            for (int j = 0; j < filterStringsHS.get(i).length; j++) {
                System.out.println(i + "\t" + j + "\t" + filterStringsHS.get(i)[j]);
                double accuracy = ClassifierCrossValidator.validateFilteredClassifier(
                        "weka.classifiers." + classifierName, filterStringsHS.get(i)[j], training, testing);
                writer.append(key + "\t" + i + "\t" + j + "\t" + filterStringsHS.get(i)[j] + "\t" + accuracy);
                writer.newLine();
                writer.flush();
            }
        }
    }

    private static void validateFull(String classifierName, CrossValidation crossValidation, BufferedWriter writer) throws Exception {

        for (int i = 0; i < 10; i++) {
            Instances training = crossValidation.getTrainingFold(i);
            Instances testing = crossValidation.getTestingFold(i);
            Classifier classifier = AbstractClassifier.forName("weka.classifiers." + classifierName, null);
            classifier.buildClassifier(training);
            int correct = 0;
            for (int j = 0; j < testing.numInstances(); j++) {
                if (classifier.classifyInstance(testing.instance(j)) == testing.instance(j).classValue()) {
                    correct++;
                }
            }
            writer.append("" + "\t" + i + "\t" + (correct + 0d) / testing.numInstances());
            writer.newLine();
            writer.flush();
        }
    }

    public static ArrayList<int[]> getSubsets(String s) {
        ArrayList<int[]> selectedFeatureSubsets = new ArrayList<int[]>();
        String[] lineSplit = s.split("\n");
        for (int i = 0; i < lineSplit.length; i++) {
            String[] tabSplit = lineSplit[i].split("\t");
            String[] spaceSplit = tabSplit[tabSplit.length - 1].split(" ");
            int[] selectedFeatures = new int[spaceSplit.length];
            for (int j = 0; j < spaceSplit.length; j++) {
                selectedFeatures[j] = Integer.parseInt(spaceSplit[j]);
            }
            selectedFeatureSubsets.add(selectedFeatures);
        }
        return selectedFeatureSubsets;
    }

    public static ArrayList<String[]> getFilterStrings(String fileName, String key, int numberInFold) throws IOException {
        //String fileName = fileName;
        BufferedReader reader = new FileIOHandler().getReader(fileName);

        ArrayList<String[]> groupedSubsets = new ArrayList<String[]>();

        String line;

        int counter = 0;
        String[] subGroup = new String[numberInFold];

        while ((line = reader.readLine()) != null) {
            if (line.contains(key)) {
                subGroup[counter] = stripSubsetString(line);
                counter++;
                if (counter == numberInFold) {
                    counter = 0;
                    groupedSubsets.add(subGroup);
                    subGroup = new String[numberInFold];
                }
            }
        }
        reader.close();

        return groupedSubsets;
    }

    public static String stripSubsetString(String s) {
        String[] tabSplit = s.split("\t");
        return tabSplit[tabSplit.length - 1];
    }

}
