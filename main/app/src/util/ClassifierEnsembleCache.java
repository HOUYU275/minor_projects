package util;

import weka.classifiers.Classifier;

import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 31/01/12
 * Time: 15:06
 * To change this template use File | Settings | File Templates.
 */
public class ClassifierEnsembleCache {

    public static void main(String args[]) throws Exception {
        String[] datasets = Dataset.hd;
        buildDirectories(datasets);
        generateAllClassifiers(datasets);
    }

    public static void generateAllClassifiers(String[] datasets) {
        for (int datasetIndex = 0; datasetIndex < datasets.length; datasetIndex++) {
            String datasetName = datasets[datasetIndex];
            ClassifierEnsembleCacheGenerator generator = new ClassifierEnsembleCacheGenerator(datasetName);
            for (Classifier classifier : Registry.ensembleClassifiers) {
                generator.setClassifier(classifier);
                for (String method : Registry.ensembleMethods) {
                    generator.setMethod(method);
                    for (int size : Registry.ensembleSizes) {
                        generator.setSize(size);
                        generator.generateCache();
                    }
                }
            }
            System.gc();
            System.out.println("Classifiers Generated for Dataset: " + datasetName);
        }
    }

    public static void buildDirectories(String[] datasets) {
        for (int datasetIndex = 0; datasetIndex < datasets.length; datasetIndex++) {
            String datasetName = datasets[datasetIndex];
            new File(Registry.ensembleCachePath + datasetName).mkdir();
            for (Classifier classifier : Registry.ensembleClassifiers) {
                makeClassifierDirectories(datasetName, classifier.getClass().getName());
            }
            System.out.println("Directory Built for Dataset: " + datasetName);
        }
    }

    public static void makeClassifierDirectories(String datasetName, String classifierName) {
        new File(Registry.ensembleCachePath + datasetName + "\\" + classifierName).mkdir();
        for (String method : Registry.ensembleMethods) {
            new File(Registry.ensembleCachePath + datasetName + "\\" + classifierName + "\\" + method).mkdir();
            makeSizeDirectories(Registry.ensembleCachePath + datasetName + "\\" + classifierName + "\\" + method);
        }
    }

    public static void makeSizeDirectories(String currentPath) {
        for (int size : Registry.ensembleSizes) {
            new File(currentPath + "\\" + size).mkdir();
            for (int i = 0; i < 10; i++) {
                new File(currentPath + "\\" + size + "\\" + i).mkdir();
            }
        }
    }

}
