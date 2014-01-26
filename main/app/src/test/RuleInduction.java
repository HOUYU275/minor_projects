package test;

import util.ClassifierCrossValidator;
import util.CrossValidation;
import util.Dataset;
import weka.core.Instances;

/**
 * Created by IntelliJ IDEA.
 * User: Ren
 * Date: 07/01/12
 * Time: 18:04
 * To change this template use File | Settings | File Templates.
 */
public class RuleInduction {
    public static void main(String[] args) throws Exception {
        Instances instances = Dataset.getDataset("heart");
        double correct;
        int numFolds = 10;
        String[] datasets = Dataset.simple;
        String classifier = "fuzzy.QuickRules";
        //BufferedWriter writer = new FileIOHandler().getWriter(classifier + "_accuracy_" + FormatConverter.getTimeStamp() + ".txt");

        double[] acc;
        for (int i = 0; i < datasets.length; i++) {
            String datasetName = datasets[i];
            System.out.println(datasetName);
            CrossValidation crossValidation = new CrossValidation(Dataset.getDataset(datasetName));
            acc = new double[numFolds];
            for (int j = 0; j < numFolds; j++) {
                Instances training = crossValidation.getTrainingFold(j);
                Instances testing = crossValidation.getTestingFold(j);
                double accuracy = ClassifierCrossValidator.validateClassifier("weka.classifiers." + classifier, training, testing);
                //System.out.println(datasetName + "\t" + j + "\t" + accuracy);
                acc[j] = accuracy;
                System.out.print(accuracy + ",");
            }
            System.out.println();
        }

    }
}
