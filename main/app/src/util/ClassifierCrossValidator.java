package util;

import featuresubsetensemble.ClassifierDistribution;
import featuresubsetensemble.CombinedClassifierDistribution;
import featuresubsetensemble.FeatureSubsetEnsemble;
import survey.ClassificationDetail;
import test.ExperimentalResult;
import weka.attributeSelection.ASEvaluation;
import weka.classifiers.AbstractClassifier;
import weka.classifiers.Classifier;
import weka.classifiers.meta.FeatureSelectionEnsemble;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.Instance;
import weka.core.Instances;
import weka.experiment.ClassifierSplitEvaluator;
import weka.filters.unsupervised.attribute.Remove;

import java.util.Arrays;
import java.util.Random;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 30/11/11
 * Time: 16:40
 * To change this template use File | Settings | File Templates.
 */
public class ClassifierCrossValidator extends CrossValidator {
    public static double validateFilteredClassifiers(
            String classifierName,
            Vector<int[]> subsets,
            Instances trainingFold,
            Instances testingFold)
            throws Exception {
        int correct = 0;
        Classifier[] classifiers = new Classifier[subsets.size()];
        for (int i = 0; i < subsets.size(); i++) {
            classifiers[i] = buildFilteredClassifier(classifierName, getFilteredString(subsets.get(i), trainingFold.classIndex()), trainingFold);
        }
        for (int j = 0; j < testingFold.numInstances(); j++) {
            if (distributionForInstanceMajorityVoting(testingFold.instance(j), classifiers) == testingFold.instance(j).classValue()) {
                correct++;
            }
        }
        //System.out.println((correct + 0d) / testingFold.numInstances());
        return (correct + 0d) / (testingFold.numInstances());
    }

    public static double[] validateFilteredClassifiers(
            String[] classifierNames,
            Vector<int[]> subsets,
            Instances trainingFold,
            Instances testingFold)
            throws Exception {
        double[] accuracies = new double[classifierNames.length];
        int correct = 0;
        for (int c = 0; c < classifierNames.length; c++) {
            correct = 0;
            Classifier[] classifiers = new Classifier[subsets.size()];
            for (int i = 0; i < subsets.size(); i++) {
                classifiers[i] = buildFilteredClassifier(classifierNames[c], getFilteredString(subsets.get(i), trainingFold.classIndex()), trainingFold);
            }
            for (int j = 0; j < testingFold.numInstances(); j++) {
                if (distributionForInstanceMajorityVoting(testingFold.instance(j), classifiers) == testingFold.instance(j).classValue()) {
                    correct++;
                }
            }
            accuracies[c] = (correct + 0d) / (testingFold.numInstances());
        }
        return accuracies;
    }

    public static double validateFeatureSubsetEnsemble(String classifier,
                                                       FeatureSubsetEnsemble featureSubsetEnsemble,
                                                       Instances training,
                                                       Instances testing) throws Exception {
        int threshold = 20;
        Vector<int[]> subsets = new Vector<>();
        do {
            int[] selectedSubset = featureSubsetEnsemble.convertTo((double) threshold / 20);
            subsets.add(selectedSubset);
            threshold -= 1;
        }
        while (threshold > 0);

        int correct = 0;
        Classifier[] classifiers = new Classifier[subsets.size()];
        for (int i = 0; i < subsets.size(); i++) {
            classifiers[i] = buildFilteredClassifier(classifier, getFilteredString(subsets.get(i), training.classIndex()), training);
        }
        for (int j = 0; j < testing.numInstances(); j++) {
            if (distributionForInstanceMajorityVoting(testing.instance(j), classifiers) == testing.instance(j).classValue()) {
                correct++;
            }
        }
        //System.out.println((correct + 0d) / testingFold.numInstances());
        return (correct + 0d) / (testing.numInstances());
        /*CombinedClassifierDistribution[] distributions = ClassifierCrossValidator.getCombinedDistributions(classifier, featureSubsetEnsemble, training, testing);
        double[] decisions = new double[distributions.length];
        int correct = 0;
        for (int k = 0; k < distributions.length; k++) {
            decisions[k] = distributions[k].classifyInstance();
            if (decisions[k] == testing.get(k).classValue()) correct++;
        }
        return (double) correct / testing.numInstances();*/
    }

    public static double validateFeatureSubsetEnsembleByCombinedDistribution(String classifier,
                                                                             FeatureSubsetEnsemble featureSubsetEnsemble,
                                                                             Instances training,
                                                                             Instances testing) throws Exception {
        CombinedClassifierDistribution[] distributions = ClassifierCrossValidator.getCombinedDistributions(classifier, featureSubsetEnsemble, training, testing);
        double[] decisions = new double[distributions.length];
        int correct = 0;
        for (int k = 0; k < distributions.length; k++) {
            decisions[k] = distributions[k].classifyInstance();
            if (decisions[k] == testing.get(k).classValue()) correct++;
        }
        return (double) correct / testing.numInstances();
    }

    public static CombinedClassifierDistribution[] getCombinedDistributions(String classifierName,
                                                                            FeatureSubsetEnsemble featureSubsetEnsemble,
                                                                            Instances trainingFold,
                                                                            Instances testingFold) throws Exception {
        CombinedClassifierDistribution[] combinedDistributions = new CombinedClassifierDistribution[testingFold.numInstances()];
        for (int i = 0; i < combinedDistributions.length; i++) {
            combinedDistributions[i] = new CombinedClassifierDistribution();
        }
        int threshold = 10;
        Classifier classifier = null;
        do {
            int[] selectedSubset = featureSubsetEnsemble.convertTo((double) threshold / 10);
            if (selectedSubset.length > 0) {
                classifier = buildFilteredClassifier(classifierName, getFilteredString(selectedSubset, trainingFold.classIndex()), trainingFold);
                for (int i = 0; i < testingFold.numInstances(); i++) {
                    combinedDistributions[i].add(new ClassifierDistribution((double) threshold / 10, classifier.distributionForInstance(testingFold.instance(i))));
                }
            }
            threshold -= 1;
        }
        while (threshold > 0);
        return combinedDistributions;
    }

    public static double[] getDistribution(Classifier classifier, Instance instance) throws Exception {
        return classifier.distributionForInstance(instance);
    }

    public static double validateFilteredClassifier(
            String classifierName,
            String filterSubset,
            Instances trainingFold,
            Instances testingFold)
            throws Exception {
        int correct = 0;
        //classifier.buildClassifier(trainingFold);
        Classifier classifier = buildFilteredClassifier(classifierName, filterSubset, trainingFold);
        for (int j = 0; j < testingFold.numInstances(); j++) {
            if (classifier.classifyInstance(testingFold.instance(j)) == testingFold.instance(j).classValue()) {
                correct++;
            }
        }
        //System.out.println((correct + 0d) / testingFold.numInstances());
        return (correct + 0d) / testingFold.numInstances();
    }

    public static double[] validateFilteredClassifier(
            String[] classifierNames,
            String filterSubset,
            Instances trainingFold,
            Instances testingFold)
            throws Exception {
        int correct = 0;
        double[] accuracies = new double[classifierNames.length];
        for (int c = 0; c < classifierNames.length; c++) {
            correct = 0;
            //classifier.buildClassifier(trainingFold);
            Classifier classifier = buildFilteredClassifier(classifierNames[c], filterSubset, trainingFold);
            for (int j = 0; j < testingFold.numInstances(); j++) {
                if (classifier.classifyInstance(testingFold.instance(j)) == testingFold.instance(j).classValue()) {
                    correct++;
                }
            }
            //System.out.println((correct + 0d) / testingFold.numInstances());
            accuracies[c] = (correct + 0d) / testingFold.numInstances();
        }
        return accuracies;
    }

    public static double[] validateFilteredClassifier(
            String[] classifierNames,
            int[] subset,
            Instances trainingFold,
            Instances testingFold)
            throws Exception {
        int correct = 0;
        double[] accuracies = new double[classifierNames.length];
        for (int c = 0; c < classifierNames.length; c++) {
            correct = 0;
            //classifier.buildClassifier(trainingFold);
            Classifier classifier = buildFilteredClassifier(classifierNames[c], getFilteredString(subset, trainingFold.classIndex()), trainingFold);
            for (int j = 0; j < testingFold.numInstances(); j++) {
                if (classifier.classifyInstance(testingFold.instance(j)) == testingFold.instance(j).classValue()) {
                    correct++;
                }
            }
            //System.out.println((correct + 0d) / testingFold.numInstances());
            accuracies[c] = (correct + 0d) / testingFold.numInstances();
        }
        return accuracies;
    }

    public static double validateFilteredClassifier(
            String classifierName,
            int[] subset,
            Instances trainingFold,
            Instances testingFold)
            throws Exception {
        int correct = 0;
        //System.out.println(filterSubset);
        Classifier classifier = buildFilteredClassifier(classifierName, getFilteredString(subset, trainingFold.classIndex()), trainingFold);
        for (int j = 0; j < testingFold.numInstances(); j++) {
            if (classifier.classifyInstance(testingFold.instance(j)) == testingFold.instance(j).classValue()) {
                correct++;
            }
        }
        //System.out.println((correct + 0d) / testingFold.numInstances());
        return (correct + 0d) / testingFold.numInstances();
    }

    public static ClassificationDetail validateFilteredClassifierComprehensive(
            String classifierName,
            int[] subset,
            Instances trainingFold,
            Instances testingFold)
            throws Exception {
        Classifier classifier = getFilteredClassifier(classifierName, getFilteredString(subset, trainingFold.classIndex()), trainingFold);
        //Classifier classifier = AbstractClassifier.forName(classifierName, null);
        ClassifierSplitEvaluator evaluator = new ClassifierSplitEvaluator();
        evaluator.setClassifier(classifier);
        Object[] results = evaluator.getResult(trainingFold, testingFold);
        //String[] names = evaluator.getResultNames();
        //Object[] types = evaluator.getResultTypes();
        //for (int i = 0; i < results.length; i++) System.out.println(i + "\t" + names[i] + "\t" + results[i]);
        ClassificationDetail detail = new ClassificationDetail(results);
        return detail;
    }

    public static ClassificationDetail validateRawClassifierComprehensive(
            String classifierName,
            Instances trainingFold,
            Instances testingFold)
            throws Exception {
        //Classifier classifier = getFilteredClassifier(classifierName, getFilteredString(subset, trainingFold.classIndex()), trainingFold);
        Classifier classifier = AbstractClassifier.forName(classifierName, null);
        ClassifierSplitEvaluator evaluator = new ClassifierSplitEvaluator();
        evaluator.setClassifier(classifier);
        Object[] results = evaluator.getResult(trainingFold, testingFold);
        //String[] names = evaluator.getResultNames();
        //Object[] types = evaluator.getResultTypes();
        //for (int i = 0; i < results.length; i++) System.out.println(i + "\t" + names[i] + "\t" + results[i]);
        ClassificationDetail detail = new ClassificationDetail(results);
        return detail;
    }

    protected static double distributionForInstanceMajorityVoting(Instance instance, Classifier[] classifiers) throws Exception {

        double[] probs = new double[instance.classAttribute().numValues()];
        double[] votes = new double[probs.length];

        Random m_Random = new Random();

        for (int i = 0; i < classifiers.length; i++) {
            probs = classifiers[i].distributionForInstance(instance);
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
        double majorityIndex = majorityIndexes.get(m_Random.nextInt(majorityIndexes.size()));

        return majorityIndex;
    }

    public static String getFilteredString(int[] subset, int classIndex) {
        if (subset.length == 0) return "" + new Random().nextInt(classIndex);
        Arrays.sort(subset);

        String filterSubset = "";
        for (int i : subset) {
            if (i < classIndex) filterSubset += i + " ";
        }
        filterSubset = filterSubset.substring(0, filterSubset.length() - 1);
        return filterSubset;
    }

    public static double validateClassifier(
            String classifierName,
            Instances trainingFold,
            Instances testingFold)
            throws Exception {
        int correct = 0;
        Classifier classifier = AbstractClassifier.forName(classifierName, null);
        classifier.buildClassifier(trainingFold);
        for (int j = 0; j < testingFold.numInstances(); j++) {
            if (classifier.classifyInstance(testingFold.instance(j)) == testingFold.instance(j).classValue()) {
                correct++;
            }
        }
        return (correct + 0d) / testingFold.numInstances();
    }

    public static double[] validateClassifier(
            String[] classifierNames,
            Instances trainingFold,
            Instances testingFold)
            throws Exception {
        int correct = 0;
        double[] accuracies = new double[classifierNames.length];
        for (int c = 0; c < classifierNames.length; c++) {
            correct = 0;
            Classifier classifier = AbstractClassifier.forName(classifierNames[c], null);
            classifier.buildClassifier(trainingFold);
            for (int j = 0; j < testingFold.numInstances(); j++) {
                if (classifier.classifyInstance(testingFold.instance(j)) == testingFold.instance(j).classValue()) {
                    correct++;
                }
            }
            accuracies[c] = (correct + 0d) / testingFold.numInstances();
        }
        return accuracies;
    }

    public static double validateCES(
            Instances trainingFold,
            Instances testingFold,
            ASEvaluation evaluation,
            int poolSize,
            boolean useFull,
            boolean useRandom)
            throws Exception {
        int correct = 0;
        FeatureSelectionEnsemble classifier = new FeatureSelectionEnsemble();
        classifier.setEvaluation(evaluation);
        classifier.setPoolSize(poolSize);
        classifier.setUseFull(useFull);
        classifier.setUseRandom(useRandom);
        classifier.buildClassifier(trainingFold);
        for (int j = 0; j < testingFold.numInstances(); j++) {
            if (classifier.classifyInstance(testingFold.instance(j)) == testingFold.instance(j).classValue()) correct++;
        }
        return (correct + 0d) / testingFold.numInstances();
    }

    public static double validateClassifier(
            Classifier classifier,
            Instances trainingFold,
            Instances testingFold)
            throws Exception {
        int correct = 0;
        classifier.buildClassifier(trainingFold);
        for (int j = 0; j < testingFold.numInstances(); j++) {
            if (classifier.classifyInstance(testingFold.instance(j)) == testingFold.instance(j).classValue()) {
                correct++;
            }
        }
        return (correct + 0d) / testingFold.numInstances();
    }

    public static Classifier filterClassifier(
            Classifier classifier,
            int[] selectedAttributes,
            int classIndex) throws Exception {
        String selectedAttributesString = "";
        for (int i = 0; i < selectedAttributes.length; i++) {
            selectedAttributesString = selectedAttributesString + " " + selectedAttributes[i];
        }
        FilteredClassifier filteredClassifier = new FilteredClassifier();
        filteredClassifier.setClassifier(classifier);
        Remove remove = new Remove();
        remove.setOptions(new String[]{/*"-V", */"-R", processSelectString(selectedAttributesString, classIndex)});
        filteredClassifier.setFilter(remove);
        return filteredClassifier;
    }

    public static Classifier buildFilteredClassifier(
            String classifier,
            String subset,
            Instances data) throws Exception {
        FilteredClassifier filteredClassifier = new FilteredClassifier();
        filteredClassifier.setClassifier(AbstractClassifier.forName(classifier, null));
        Remove remove = new Remove();
        remove.setOptions(new String[]{"-V", "-R", processSelectString(subset, data.classIndex())});
        filteredClassifier.setFilter(remove);
        //data.setClassIndex(data.num);
        filteredClassifier.buildClassifier(data);
        return filteredClassifier;
    }

    public static Classifier getFilteredClassifier(
            String classifier,
            String subset,
            Instances data) throws Exception {
        FilteredClassifier filteredClassifier = new FilteredClassifier();
        filteredClassifier.setClassifier(AbstractClassifier.forName(classifier, null));
        Remove remove = new Remove();
        remove.setOptions(new String[]{"-V", "-R", processSelectString(subset, data.classIndex())});
        filteredClassifier.setFilter(remove);
        return filteredClassifier;
    }

    public static String processSelectString(String selectedAttributeString, int classIndex) {
        StringBuffer stringBuffer = new StringBuffer();
        String[] splitString = selectedAttributeString.trim().split(" ");

        int startIndex = 0;
        if (splitString.length > 1) {
            if (splitString[0].equals(splitString[1])) startIndex = 1;
        }
        for (int i = startIndex; i < splitString.length; i++) {
            stringBuffer.append(Integer.parseInt(splitString[i]) + 1);
            stringBuffer.append(",");
        }
        //stringBuffer.append(splitString[splitString.length - 1]);
        stringBuffer.append(classIndex + 1);
        return stringBuffer.toString();
    }

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
        //stringBuffer.append(classIndex);
        return stringBuffer.toString();
    }

    @Override
    public void report(ExperimentalResult result, int repeat) {
        //TODO: Automatically Generated Implemented Method
    }

    @Override
    public ExperimentalResult validate() {
        return null;  //TODO: Automatically Generated Implemented Method
    }
}
