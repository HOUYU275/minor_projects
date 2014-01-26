package test;

import weka.classifiers.Classifier;
import weka.classifiers.meta.FeatureSelectionEnsemble;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.Instances;
import weka.filters.unsupervised.attribute.Remove;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 06-Oct-2010
 * Time: 15:01:33
 * To change this template use File | Settings | File Templates.
 */
public class CV {

    private static double correctCount = 0;
    private static int instanceCount = 0;
    private static int completionCount = 0;

    public static void format() {
        String line;
        BufferedReader reader = new BufferedReader(new StringReader(""));
        try {
            while ((line = reader.readLine()) != null) {
                String[] splitLine = line.split(" ");
                if (splitLine.length > 1) {
                    for (int i = 0; i < splitLine.length - 1; i++) {
                        System.out.print((Integer.parseInt(splitLine[i]) + 1) + ",");
                    }
                    System.out.println(Integer.parseInt(splitLine[splitLine.length - 1]) + 1);
                } else {
                    System.out.println(splitLine[0]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    /**
     * @param classifier
     * @param selectedAttributes
     * @return
     * @throws Exception
     */
    public Classifier filterClassifier(Classifier classifier, String selectedAttributes) throws Exception {
        FilteredClassifier filteredClassifier = new FilteredClassifier();
        filteredClassifier.setClassifier(classifier);
        Remove remove = new Remove();
        remove.setOptions(new String[]{/*"-V", */"-R", selectedAttributes});
        filteredClassifier.setFilter(remove);
        return filteredClassifier;
    }

    public static Classifier filterClassifier(Classifier classifier, int[] selectedAttributes, int classIndex) throws Exception {
        String selectedAttributesString = "";
        for (int i = 0; i < selectedAttributes.length; i++) {
            selectedAttributesString = selectedAttributesString + " " + selectedAttributes[i];
        }
        FilteredClassifier filteredClassifier = new FilteredClassifier();
        filteredClassifier.setClassifier(classifier);
        Remove remove = new Remove();
        remove.setOptions(new String[]{/*"-V", */"-R", processRemoveString(selectedAttributesString, classIndex)});
        filteredClassifier.setFilter(remove);
        return filteredClassifier;
    }

    //start from index 0

    public static String processRemoveString(String selectedAttributeString, int classIndex) {
        StringBuffer stringBuffer = new StringBuffer();

        String[] splitString = selectedAttributeString.trim().split(" ");

        int currentStringIndex = 0;
        for (int i = 0; i < classIndex - 1; i++) {
            if (currentStringIndex < splitString.length) {
                if (i != Integer.parseInt(splitString[currentStringIndex])) {
                    stringBuffer.append(i + 1);
                    stringBuffer.append(",");
                } else {
                    currentStringIndex++;
                }
            } else {
                stringBuffer.append(i + 1);
                stringBuffer.append(",");
            }

        }
        stringBuffer.append(classIndex);
        //System.out.println(stringBuffer.toString());
        //stringBuffer = stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        return stringBuffer.toString();
    }

/*    public static Instances getDataset(String fileName) throws IOException {
        //File file = new File("C:\\Documents and Settings\\rrd09\\My Documents\\data\\" + fileName + ".arff");
        File file = new File("C:\\Documents and Settings\\rrd09\\My Documents\\academic-work-2011\\data\\" + fileName + ".arff");
        BufferedReader reader = new BufferedReader(
                new FileReader(file));
        Instances data = new Instances(reader);
        reader.close();
        return data;
    }*/

    public static void generateCrossValidationFoldsGrouped(Instances data, Instances[] trainingFolds, Instances[] testingFolds, int groupSize) throws Exception {
        if ((data.numInstances() % groupSize) != 0) throw new Exception("Instances are not in Group of " + groupSize);
        if (trainingFolds.length != testingFolds.length)
            throw new Exception("Unequal Training Testing Number of Folds");

        System.out.println(data.numInstances());

        data = shuffleInstancesGrouped(data, groupSize);

        int numberOfFolds = trainingFolds.length;
        int numberOfInstances = data.numInstances();
        int numberOfInstanceGroups = data.numInstances() / groupSize;
        int numberOfInstanceGroupsInFold = numberOfInstanceGroups / numberOfFolds;
        //int numberOfInstancesInFold = numberOfInstances / numberOfFolds;

        for (int i = 0; i < numberOfFolds; i++) {

            int numberOfTestingInstances = (i < numberOfInstanceGroups % numberOfFolds) ?
                    (numberOfInstanceGroupsInFold + 1) * groupSize : numberOfInstanceGroupsInFold * groupSize;
            int beginIndex = (i < numberOfInstanceGroups % numberOfFolds) ?
                    numberOfInstances - (i + 1) * numberOfTestingInstances : (numberOfFolds - i - 1) * numberOfInstanceGroupsInFold * groupSize;

            trainingFolds[i] = new Instances(data, numberOfInstances - numberOfTestingInstances);
            testingFolds[i] = new Instances(data, numberOfTestingInstances);

            for (int j = 0; j < numberOfInstances; j++) {
                if ((j >= beginIndex) && (j < beginIndex + numberOfTestingInstances)) {
                    testingFolds[i].add(data.instance(j));
                } else {
                    trainingFolds[i].add(data.instance(j));
                }
            }
        }
    }

    public static Instances[] generateCrossValidationFolds(int numFolds, Instances data) throws Exception {
        Instances[] trainingTestingFolds = new Instances[numFolds * 2];
        data.stratify(numFolds);
        for (int i = 0; i < numFolds; i++) {
            trainingTestingFolds[i] = data.trainCV(numFolds, i);
            trainingTestingFolds[numFolds + i] = data.testCV(numFolds, i);
        }
        return trainingTestingFolds;
    }

    public static Instances shuffleInstancesGrouped(Instances data, int groupSize) {
        ArrayList<Instances> dataArray = new ArrayList<Instances>();
        for (int i = 0; i < data.numInstances() / groupSize; i++) {
            Instances instances = new Instances(data, groupSize);
            for (int j = i * groupSize; j < i * groupSize + groupSize; j++) instances.add(data.instance(j));
            dataArray.add(i, instances);
        }
        Collections.shuffle(dataArray);
        Instances shuffledData = new Instances(data, data.numInstances());
        for (int i = 0; i < dataArray.size(); i++) {
            for (int j = 0; j < groupSize; j++) shuffledData.add(dataArray.get(i).instance(j));
        }
        return shuffledData;
    }

    public static double crossValidateGrouped(Classifier classifier, Instances[] trainingFolds, Instances[] testingFolds, int groupSize) throws Exception {
        double[][] predictions = null;
        double[][] results = null;
        double[] actualLabel = null;
        double accumulatedScore = 0d;
        for (int i = 0; i < trainingFolds.length; i++) {
            predictions = new double[testingFolds[i].numInstances()][];
            results = new double[testingFolds[i].numInstances() / groupSize][];
            actualLabel = new double[testingFolds[i].numInstances() / groupSize];
            classifier.buildClassifier(trainingFolds[i]);
            for (int j = 0; j < testingFolds[i].numInstances(); j++) {
                predictions[j] = classifier.distributionForInstance(testingFolds[i].instance(j));
                //System.out.println(j + "-" + testingFolds[i].instance(j).classValue());

                actualLabel[j / groupSize] = testingFolds[i].instance(j).classValue();
            }
            for (int j = 0; j < testingFolds[i].numInstances(); j += groupSize) {
                //System.out.println(testingFolds[i].numInstances());
                results[j / groupSize] = distributionForGroupedAverage(predictions, j, groupSize);
                //results[j / groupSize] = distributionForGroupProduct(predictions, j, groupSize);
                //results[j / groupSize] = distributionForGroupMajorityVoting(predictions, j, groupSize);
            }
            System.out.println("Fold: " + i);
            double[] aggregatedLabel = new double[actualLabel.length];
            for (int j = 0; j < results.length; j++) {
                aggregatedLabel[j] = analyseDistribution(results[j]);
            }
            accumulatedScore = accumulatedScore + scoringResults(aggregatedLabel, actualLabel);
            System.out.println("Score: " + scoringResults(aggregatedLabel, actualLabel));
            System.out.println("------------------");
        }
        System.gc();
        return accumulatedScore / trainingFolds.length;
    }

    private static double scoringResults(double[] result, double[] actualLabel) {
        double scoreCount = 0;
        double resultCount = 0;
        for (int i = 0; i < result.length; i++) {

            //System.out.println(result[i] + " vs " + actualLabel[i]);

            /*if (result[i] == actualLabel[i]) {
                    scoreCount = scoreCount + 1d;
                }
                resultCount = resultCount + 1d;*/

            if (actualLabel[i] == 0) {
                if (result[i] == actualLabel[i]) {
                    scoreCount = scoreCount + 1d;
                }
                resultCount = resultCount + 1d;
            } else {
                /*if (result[i] == actualLabel[i]) {
                    scoreCount = scoreCount + 1d;
                }
                resultCount = resultCount + 1d;*/
            }
        }
        return scoreCount / resultCount;
    }

    private static double analyseDistribution(double[] distribution) {
        double high = Double.MIN_VALUE;
        int highIndex = 0;
        for (int i = 0; i < distribution.length; i++) {
            if (distribution[i] > high) {
                high = distribution[i];
                highIndex = i;
            }
        }
        //System.out.println(highIndex + " - " + high);
        return (double) highIndex;
    }

    private static double[] distributionForGroupedAverage(double[][] predictions, int beginIndex, int groupSize) throws Exception {

        double[] probs = predictions[beginIndex];
        for (int i = beginIndex; i < beginIndex + groupSize; i++) {
            double[] dist = predictions[i];
            for (int j = 0; j < dist.length; j++) {
                probs[j] += dist[j];
            }
        }
        for (int j = 0; j < probs.length; j++) {
            probs[j] /= (double) groupSize;
        }
        return probs;
    }

    private static double[] distributionForGroupProduct(double[][] predictions, int beginIndex, int groupSize) throws Exception {

        double[] probs = predictions[beginIndex];
        for (int i = beginIndex; i < beginIndex + groupSize; i++) {
            double[] dist = predictions[i];
            for (int j = 0; j < dist.length; j++) {
                probs[j] *= dist[j];
            }
        }

        return probs;
    }

    private static double[] distributionForGroupMajorityVoting(double[][] predictions, int beginIndex, int groupSize) throws Exception {

        double[] probs = new double[predictions[beginIndex].length];//new double[instance.classAttribute().numValues()];
        double[] votes = new double[probs.length];

        for (int i = beginIndex; i < beginIndex + groupSize; i++) {
            probs = predictions[i];
            int maxIndex = 0;
            for (int j = 0; j < probs.length; j++) {
                if (probs[j] > probs[maxIndex])
                    maxIndex = j;
            }

            // Consider the cases when multiple classes happen to have the same probability
            for (int j = 0; j < probs.length; j++) {
                if (probs[j] == probs[maxIndex])
                    votes[j]++;
            }
        }

        int tmpMajorityIndex = 0;
        for (int k = 1; k < votes.length; k++) {
            if (votes[k] > votes[tmpMajorityIndex])
                tmpMajorityIndex = k;
        }

        // Consider the cases when multiple classes receive the same amount of votes
        Vector<Integer> majorityIndexes = new Vector<Integer>();
        for (int k = 0; k < votes.length; k++) {
            if (votes[k] == votes[tmpMajorityIndex])
                majorityIndexes.add(k);
        }
        // Resolve the ties according to a uniform random distribution
        int majorityIndex = majorityIndexes.get((new Random()).nextInt(majorityIndexes.size()));

        //set probs to 0
        for (int k = 0; k < probs.length; k++)
            probs[k] = 0;
        probs[majorityIndex] = 1; //the class that have been voted the most receives 1

        return probs;
    }

    public static double crossValidate(Classifier classifier, Instances[] trainingTestingFolds) throws Exception {
        correctCount = 0;
        completionCount = 0;
        instanceCount = 0;
        Instances trainingFold = null;
        Instances testingFold = null;
        for (int i = 0; i < trainingTestingFolds.length / 2; i++) {
            trainingFold = trainingTestingFolds[i];
            testingFold = trainingTestingFolds[trainingTestingFolds.length / 2 + i];
            classifier.buildClassifier(trainingFold);
            for (int j = 0; j < testingFold.numInstances(); j++) {
                try {
                    if (classifier.classifyInstance(testingFold.instance(j)) == testingFold.instance(j).classValue()) {
                        CV.increaseCorrectCount(1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            CV.increaseInstanceCount(testingFold.numInstances());
        }
        System.gc();
        return ((correctCount) / (instanceCount));
    }

    public static double crossValidateFeatureSelectionEnsemble(
            ArrayList<Classifier[]> baseClassifiers,
            int selectionMethod,
            Instances[] trainingTestingFolds) throws Exception {
        if (baseClassifiers.size() != trainingTestingFolds.length / 2)
            throw new Exception("Incompatible Validation Settings");
        int numFolds = trainingTestingFolds.length / 2;
        correctCount = 0;
        completionCount = 0;
        instanceCount = 0;
        FeatureSelectionEnsemble featureSelectionEnsemble = new FeatureSelectionEnsemble();
        //featureSelectionEnsemble.setSelectionMethod(selectionMethod);
        for (int i = 0; i < numFolds; i++) {
            featureSelectionEnsemble.setPool(baseClassifiers.get(i));
            featureSelectionEnsemble.buildClassifier(trainingTestingFolds[i]);
            for (int j = 0; j < trainingTestingFolds[numFolds + i].numInstances(); j++) {
                try {
                    if (featureSelectionEnsemble.classifyInstance(trainingTestingFolds[numFolds + i].instance(j)) == trainingTestingFolds[numFolds + i].instance(j).classValue()) {
                        CV.increaseCorrectCount(1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            CV.increaseInstanceCount(trainingTestingFolds[numFolds + i].numInstances());
        }
        return ((correctCount) / (instanceCount));
    }

    public static double crossValidateFeatureSelectionEnsemble(
            Classifier[] prebuiltClassifiers,
            int selectionMethod,
            Instances[] trainingTestingFolds) throws Exception {
        if (prebuiltClassifiers.length != trainingTestingFolds.length / 2)
            throw new Exception("Incompatible Validation Settings");
        int numFolds = trainingTestingFolds.length / 2;
        correctCount = 0;
        completionCount = 0;
        instanceCount = 0;

        for (int i = 0; i < numFolds; i++) {
            //((FeatureSelectionEnsemble) prebuiltClassifiers[i]).setSelectionMethod(selectionMethod);
            prebuiltClassifiers[i].buildClassifier(trainingTestingFolds[i]);
            for (int j = 0; j < trainingTestingFolds[numFolds + i].numInstances(); j++) {
                try {
                    if (prebuiltClassifiers[i].classifyInstance(trainingTestingFolds[numFolds + i].instance(j)) == trainingTestingFolds[numFolds + i].instance(j).classValue()) {
                        CV.increaseCorrectCount(1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            CV.increaseInstanceCount(trainingTestingFolds[numFolds + i].numInstances());
        }
        return ((correctCount) / (instanceCount));
    }

    public static double crossValidate(int numFolds, Classifier classifier, Instances data) throws Exception {
        //double accuracy = 0d;
        Instances trainingFold = null;
        Instances testingFold = null;
        correctCount = 0;
        completionCount = 0;
        instanceCount = 0;
        data.stratify(numFolds);
        //new CVT(data.trainCV(numFolds, 1), data.testCV(numFolds, 1), classifier).start();
        //new CVT(data.trainCV(numFolds, 2), data.testCV(numFolds, 2), classifier).run();
        for (int i = 0; i < numFolds; i++) {
            trainingFold = data.trainCV(numFolds, i);
            testingFold = data.testCV(numFolds, i);
            classifier.buildClassifier(trainingFold);
            for (int j = 0; j < testingFold.numInstances(); j++) {
                try {
                    if (classifier.classifyInstance(testingFold.instance(j)) == testingFold.instance(j).classValue()) {
                        CV.increaseCorrectCount(1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
                //System.out.println(a++);
            }
            //CV.increaseInstanceCount(testingFold.numInstances());
            //new CVT(trainingFold, testingFold, classifier).start();
            //System.out.println("Fold: " + i + " of " + numFolds + " " + );
            //accuracy += correctCount;
        }
        //while (completionCount != 10) Thread.sleep(1000);
        System.gc();
        //System.out.println(instanceCount + " vs " + data.numInstances());        
        return ((correctCount) / (data.numInstances()));
    }

    public static synchronized void increaseCorrectCount(int amount) {
        correctCount += amount;
    }

    public static synchronized void increaseInstanceCount(int amount) {
        instanceCount += amount;
    }

    public static synchronized void increaseCompletionCount() {
        completionCount += 1;
    }

    public boolean validate(String s) {
        if (s.contains("+") || !s.contains(" ")) return false;
        return true;
    }

    public void validate(Classifier classifier, Instances data, String attributeLists) throws Exception {
        //processRemoveString
        String line;
        BufferedReader reader = new BufferedReader(new StringReader(attributeLists));
        try {
            while ((line = reader.readLine()) != null) {
                System.out.print(line + "\t");
                if (validate(line)) {
                    System.out.print("" +
                            crossValidate(
                                    10, filterClassifier(
                                    classifier, processRemoveString(
                                    line, data.classIndex())), data) +
                            "\n");
                } else {
                    System.out.println();
                }
                //System.out.println(acc);

            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

}

class CVT extends Thread {

    //private int currentFold;
    private Instances trainingFold;
    private Instances testingFold;
    private Classifier classifier;
    private int a;

    public CVT(Instances trainingFold, Instances testingFold, Classifier classifier) {
        this.trainingFold = trainingFold;
        this.testingFold = testingFold;
        this.classifier = classifier;
    }

    public void run() {
        System.out.println("Started");
        try {
            classifier.buildClassifier(trainingFold);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        for (int j = 0; j < testingFold.numInstances(); j++) {
            try {
                if (classifier.classifyInstance(testingFold.instance(j)) == testingFold.instance(j).classValue()) {
                    CV.increaseCorrectCount(1);
                }
            } catch (Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            //System.out.println(a++);
        }
        CV.increaseCompletionCount();
    }
}
