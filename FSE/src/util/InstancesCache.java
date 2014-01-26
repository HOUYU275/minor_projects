package util;

import weka.core.Instances;

import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 31/01/12
 * Time: 09:50
 */
public class InstancesCache {

    public static void main(String args[]) throws Exception {
        File file = new File(Registry.crossValidationCachePath);
        if (!file.exists()) {
            file.mkdir();
            System.out.println("Cache Directory Built");
        }
        String[] datasets = Dataset.CES;
        for (int i = 0; i < 10; i++) {
            buildRepeatedDirectories(datasets, i);
            generateRepeatedCache(datasets, i);
        }
        Instances instances = loadCache("cnae", true, 3);
        System.out.println(instances.numInstances());
    }

    private static void generateRepeatedCache(String[] datasets, int repeat) throws Exception {
        int numFold = 10;
        Random random = new Random();
        for (int d = 0; d < datasets.length; d++) {
            String fileName = Registry.crossValidationCachePath + repeat + System.getProperty("file.separator") + datasets[d];
            File file = new File(fileName);
            if ((file.isDirectory()) && (file.list().length == 0)) {
                String datasetName = datasets[d];
                random.setSeed(random.nextLong());
                CrossValidation crossValidation = new CrossValidation(Dataset.getDataset(datasetName), numFold, random);
                Instances instances;
                String foldType;
                for (int j = 0; j < crossValidation.getNumFolds(); j++) {
                    foldType = "train";
                    instances = crossValidation.getTrainingFold(j);
                    ObjectSerialisation.exportRepeatedInstances(instances, fileName, foldType + j);
                    foldType = "test";
                    instances = crossValidation.getTestingFold(j);
                    ObjectSerialisation.exportRepeatedInstances(instances, fileName, foldType + j);
                }
                System.out.println("(" + repeat + ") " + "Generated Cache for Dataset: " + datasetName);
            }
        }
    }

    private static void buildRepeatedDirectories(String[] datasets, int repeat) {
        for (int datasetIndex = 0; datasetIndex < datasets.length; datasetIndex++) {
            String topDirectory = Registry.crossValidationCachePath + repeat;
            File file = new File(topDirectory);
            if (!file.exists()) {
                file.mkdir();
                System.out.println("Top Directory Built (" + repeat + ")");
            }
            String fileName = topDirectory + System.getProperty("file.separator") + datasets[datasetIndex];
            file = new File(fileName);
            if (!file.exists()) {
                file.mkdir();
                System.out.println("(" + repeat + ") " + "Directory Built for Dataset: " + datasets[datasetIndex]);
            }
        }
    }

    public static Instances loadRepeatedCache(String datasetName, boolean isTrainFold, int foldNumber, int repeat)
            throws ClassNotFoundException, IOException {
        return ObjectSerialisation.importInstances(repeat + System.getProperty("file.separator") + datasetName + System.getProperty("file.separator") + (isTrainFold ? "train" : "test") + foldNumber);
    }

    public static Instances loadCache(String datasetName, boolean isTrainFold, int foldNumber)
            throws ClassNotFoundException, IOException {
        return ObjectSerialisation.importInstances(0 + System.getProperty("file.separator") + datasetName + "\\" + (isTrainFold ? "train" : "test") + foldNumber);
    }

    public static void generateCache(String[] datasets) throws Exception {
        for (int i = 0; i < datasets.length; i++) {
            String fileName = Registry.crossValidationCachePath + datasets[i];
            File file = new File(fileName);
            if ((file.isDirectory()) && (file.list().length == 0)) {
                String datasetName = datasets[i];
                CrossValidation crossValidation = new CrossValidation(Dataset.getDataset(datasetName));
                crossValidation.generateFolds();
                Instances instances;
                String foldType;
                for (int j = 0; j < crossValidation.getNumFolds(); j++) {
                    foldType = "train";
                    instances = crossValidation.getTrainingFold(j);
                    ObjectSerialisation.exportInstances(instances, datasetName, foldType + j);
                    foldType = "test";
                    instances = crossValidation.getTestingFold(j);
                    ObjectSerialisation.exportInstances(instances, datasetName, foldType + j);
                }
                System.out.println("Generated Cache for Dataset: " + datasetName);
            }
        }
    }

    public static void buildDirectories(String[] datasets) {
        for (int datasetIndex = 0; datasetIndex < datasets.length; datasetIndex++) {
            String fileName = Registry.crossValidationCachePath + datasets[datasetIndex];
            File file = new File(fileName);
            if (!file.exists()) {
                file.mkdir();
                System.out.println("Directory Built for Dataset: " + datasets[datasetIndex]);
            }
        }
    }
}
