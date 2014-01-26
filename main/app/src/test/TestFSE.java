package test;

import featuresubsetensemble.EnsembleBuilder;
import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;
import util.ClassificationResultOld;
import util.ClassifierCrossValidator;
import util.CrossValidation;
import util.Registry;
import weka.attributeSelection.ASEvaluation;
import weka.attributeSelection.ASSearch;
import weka.attributeSelection.SubsetEvaluator;
import weka.core.Instances;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 24/02/12
 * Time: 15:45
 */
public class TestFSE {

    private static int numFolds = 10;
    private static int numEnsembles = 10;
    private static int numRepeats = 10;
    private static double threshold = 0.5;

    public static void main(String[] args) throws Exception {

        String datasetsString = "simple";
        String classifierString = "J48";
        String evaluationKeyword = "CFS";
        String searchKeyword = "HS";

        String[] datasets = Registry.getDatasets(datasetsString);
        String classifier = Registry.getClassifier(classifierString);
        ASEvaluation evaluation = Registry.getEvaluation(evaluationKeyword);
        ASSearch search = Registry.getSearch(searchKeyword);

        double[] fullAccuracies;
        DescriptiveStatistics fullStatistics = new DescriptiveStatistics();
        double[] ensembleAccuracies;
        DescriptiveStatistics ensembleStatistics = new DescriptiveStatistics();
        double[] selectedAverageAccuracies;
        DescriptiveStatistics selectedStatistics = new DescriptiveStatistics();

        int[] fullSize;
        int[] ensembleSize;
        int[] selectedSize;

        double[] finalFullAccuracies = new double[numRepeats];
        double[] finalEnsembleAccuracies = new double[numRepeats];
        double[] finalSelectedAccuracies = new double[numRepeats];

        for (int i = 0; i < datasets.length; i++) {
            String datasetName = datasets[i];
            System.out.println("\n<<<" + datasetName + ">>>");

            for (int repeat = 0; repeat < numRepeats; repeat++) {

                CrossValidation crossValidation = new CrossValidation(datasetName, false);

                fullAccuracies = new double[numFolds];
                ensembleAccuracies = new double[numFolds];
                selectedAverageAccuracies = new double[numFolds];

                fullSize = new int[numFolds];
                ensembleSize = new int[numFolds];
                selectedSize = new int[numFolds];

                for (int j = 0; j < numFolds; j++) {
                    Instances training = crossValidation.getTrainingFold(j);
                    Instances testing = crossValidation.getTestingFold(j);
                    ClassificationResultOld result = new ClassificationResultOld(datasetName);
                    result.setClassifier(classifier);
                    evaluation.buildEvaluator(training);
                    result.setEvaluator(search.getClass().getName());
					Vector<int[]> votes = EnsembleBuilder.buildFeatureSelectionEnsemblePartition(training, evaluation, search, numEnsembles, false);
                    //ArrayList<int[]> votes = buildFeatureSelectionEnsembleMixed(training, search, numEnsembles, false);
                    //ArrayList<int[]> votes = buildFeatureSelectionEnsembleSingle(training, eval, search, numEnsembles, false);

                    double[] selectedAccuracies = new double[votes.size()];
                    int selectedSizeSum = 0;
                    for (int k = 0; k < votes.size(); k++) {
                        selectedAccuracies[k] = ClassifierCrossValidator.validateFilteredClassifier(classifier, votes.get(k), training, testing);
                        selectedStatistics.addValue(selectedAccuracies[k]);
                        selectedSizeSum += votes.get(k).length;
                    }
                    int averageSize = selectedSizeSum / votes.size();

                    threshold = 0.5;
                    int[] votedSubset = aggregate(votes, threshold);
                    while ((votedSubset.length + 0d) / averageSize < 0.75) {
                        threshold -= 0.1;
                        votedSubset = aggregate(votes, threshold);
                    }

                    ensembleAccuracies[j] = ClassifierCrossValidator.validateFilteredClassifier(classifier, votedSubset, training, testing);
                    ensembleStatistics.addValue(ensembleAccuracies[j]);
                    fullAccuracies[j] = ClassifierCrossValidator.validateClassifier(classifier, training, testing);
                    fullStatistics.addValue(fullAccuracies[j]);
                    selectedAverageAccuracies[j] = average(selectedAccuracies);

                    fullSize[j] = training.numAttributes();
                    selectedSize[j] = selectedSizeSum / votes.size();
                    ensembleSize[j] = votedSubset.length;
                }

                finalFullAccuracies[repeat] = average(fullAccuracies);

                finalEnsembleAccuracies[repeat] = average(ensembleAccuracies);
                finalSelectedAccuracies[repeat] = average(selectedAverageAccuracies);

                System.out.printf("[EA]\t%.4f\t", finalEnsembleAccuracies[repeat]);
                System.out.printf("%d\t", average(ensembleSize));
                System.out.printf("%.4f\t", ensembleStatistics.getStandardDeviation());
                System.out.printf("[SA]\t%.4f\t", finalSelectedAccuracies[repeat]);
                System.out.printf("%d\t", average(selectedSize));
                System.out.printf("%.4f\t", selectedStatistics.getStandardDeviation());
                System.out.printf("[FULL]\t%.4f\t", finalFullAccuracies[repeat]);
                System.out.printf("%d\t", average(fullSize));
                System.out.printf("%.4f\t%n", fullStatistics.getStandardDeviation());
                System.gc();
                ensembleStatistics.clear();
                selectedStatistics.clear();
                fullStatistics.clear();
            }
            System.out.printf("Ensemble\t%.4f\t", average(finalEnsembleAccuracies));

            System.out.printf("Selected\t%.4f\t", average(finalSelectedAccuracies));

            System.out.printf("Full\t%.4f\t", average(finalFullAccuracies));

        }
    }

    public static BitSet climb(BitSet input, HashSet<Integer> candidates, SubsetEvaluator evaluator) throws Exception {

        double baseScore = 0;
        double currentBest = evaluator.evaluateSubset(input);
        System.out.println("Input Score = " + currentBest);
        double currentScore = 0;
        int bestIndex = -1;
        while (currentBest > baseScore) {
            baseScore = evaluator.evaluateSubset(input);
            currentBest = -1;
            bestIndex = -1;
            currentScore = 0;
            for (Integer candidate : candidates) {
                input.set(candidate);
                currentScore = evaluator.evaluateSubset(input);
                if (currentScore > currentBest) {
                    bestIndex = candidate;
                    currentBest = currentScore;
                }
                input.clear(candidate);
            }
            input.set(bestIndex);
            candidates.remove(bestIndex);
        }
        return input;
    }

    private static BitSet toBitSet(int[] subset, int numAttributes) throws Exception {
        BitSet bs = new BitSet(numAttributes);
        int[] count = new int[numAttributes];
        for (int i = 0; i < subset.length; i++) {
            if ((subset[i] >= 0) && (subset[i] < numAttributes)) {
                count[subset[i]] = count[subset[i]] + 1;
            }
        }
        for (int i = 0; i < count.length; i++) {
            if (count[i] > 0) {
                bs.set(i);
            }
        }
        //System.out.println(bs.toString());
        return bs;
    }

    private static int[] toIntArray(BitSet set) {
        int[] returnArray = new int[set.cardinality()];
        int index = 0;
        for (int i = set.nextSetBit(0); i < set.length() - 1; i = set.nextSetBit(i + 1)) {
            if (i == -1)
                return returnArray;
            returnArray[index] = i;
            index++;
        }
        return returnArray;
    }



    public static ArrayList<int[]> buildFeatureSelectionEnsemble(Instances data, ASEvaluation[] evaluators, ASSearch search, int numEnsembles, boolean repeat) throws Exception {
        for (int i = 0; i < evaluators.length; i++) {
            evaluators[i].buildEvaluator(data);
        }
        ArrayList<int[]> votes = new ArrayList<>();
        Random random = new Random();
        int numRepeats = repeat ? 10 : 1;
        for (int currentRepeat = 0; currentRepeat < numRepeats; currentRepeat++) {
            for (int i = 0; i < numEnsembles; i++) {
                int index = random.nextInt(evaluators.length);
                votes.add(search.search(evaluators[index], data));
            }
        }
        return votes;
    }

    public static double average(double[] inputs) {
        double sum = 0;
        for (double d : inputs)
            sum += d;
        return sum / inputs.length;
    }

    public static int average(int[] inputs) {
        int sum = 0;
        for (int d : inputs)
            sum += d;
        return sum / inputs.length;
    }

    public static double average(ArrayList<Double> inputs) {
        double sum = 0;
        for (Double d : inputs)
            sum += d;
        return sum / inputs.size();
    }

    public static int[] aggregate(Vector<int[]> subsets, double threshold) {
        HashMap<Integer, Integer> votes = new HashMap();
        ArrayList<Integer> topVotes = new ArrayList<>();
        int sizeSum = 0;
        for (int i = 0; i < subsets.size(); i++) {
            sizeSum += subsets.get(i).length;
        }
        sizeSum = sizeSum /= subsets.size();
        //System.out.println("Selected Size = " + sizeSum);
        for (int i = 0; i < subsets.size(); i++) {
            for (int j = 0; j < subsets.get(i).length; j++) {
                if (votes.containsKey(subsets.get(i)[j])) {
                    votes.put(subsets.get(i)[j], votes.get(subsets.get(i)[j]) + 1);
                } else {
                    votes.put(subsets.get(i)[j], 1);
                }
            }
        }
        for (Integer i : votes.keySet()) {
            //System.out.println(i + " - " + votes.get(i));
            if ((double) votes.get(i) / (double) subsets.size() > threshold)
                topVotes.add(i);
        }
        Integer[] integers = topVotes.toArray(new Integer[topVotes.size()]);
        int[] intVotes = new int[integers.length];
        for (int i = 0; i < integers.length; i++)
            intVotes[i] = integers[i];
        return intVotes;
    }

    public static HashSet<Integer> findCandidates(ArrayList<int[]> subsets) {
        HashMap<Integer, Integer> votes = new HashMap();
        HashSet<Integer> candidateVotes = new HashSet<>();
        for (int i = 0; i < subsets.size(); i++) {
            for (int j = 0; j < subsets.get(i).length; j++) {
                if (!votes.containsKey(subsets.get(i)[j])) {
                    votes.put(subsets.get(i)[j], 1);
                }
            }
        }
        for (Integer i : votes.keySet()) {
            if ((double) votes.get(i) / (double) subsets.size() < 0.5)
                candidateVotes.add(i);
        }
        return candidateVotes;
    }

    /*public static void main(String[] args) throws Exception {
        int numFolds = 10;
        int numEnsembles = 5;
        int numRepeats = 10;
        String[] datasets = Dataset.simple;
        String classifier = Registry.J48;
        double[] fullAccuracies;
        double[] ensembleAccuracies;
        double[] ensembleClimbedAccuracies;
        double[] selectedAverageAccuracies;
        for (int i = 0; i < datasets.length; i++) {
            String datasetName = datasets[i];
            System.out.println("\n<<<" + datasetName + ">>>");

            for (int repeat = 0; repeat < 5; repeat++) {
                CrossValidation crossValidation = new CrossValidation(datasetName, false);
                fullAccuracies = new double[numFolds];
                ensembleAccuracies = new double[numFolds];
                ensembleClimbedAccuracies = new double[numFolds];
                selectedAverageAccuracies = new double[numFolds];
                for (int j = 0; j < numFolds; j++) {
                    Instances training = crossValidation.getTrainingFold(j);
                    Instances testing = crossValidation.getTestingFold(j);
                    ClassificationResultOld result = new ClassificationResultOld(datasetName);
                    result.setClassifier(classifier);

                    CfsSubsetEval eval = new CfsSubsetEval();
                    eval.buildEvaluator(training);
                    ASSearch search = new GreedyStepwise();

                    ASEvaluation[] evaluators = new ASEvaluation[]{new CfsSubsetEval(), new ConsistencySubsetEval(), new FuzzyRoughSubsetEval(),};
                    search = new HarmonySearch();

                    result.setEvaluator(search.getClass().getName());
                    ArrayList<int[]> votes = buildFeatureSelectionEnsemble(training, eval, search, numEnsembles, numRepeats);
                    //ArrayList<int[]> votes = buildFeatureSelectionEnsemble(training, evaluators, search, numEnsembles, numRepeats);
                    double[] selectedAccuracies = new double[votes.size()];
                    double[] selectedScores = new double[votes.size()];
                    for (int k = 0; k < votes.size(); k++) {
                        selectedAccuracies[k] = ClassifierCrossValidator.validateFilteredClassifier(classifier, votes.get(k), training, testing);
                        //selectedScores[k] = eval.evaluateSubset(toBitSet(votes.get(k), training.numAttributes()));
                    }
                    int[] votedSubset = aggregate(votes);
                    //System.out.println("Ensemble Size = " + votedSubset.length);
                    //System.out.println("Aggregated Subset Score = " + eval.evaluateSubset(toBitSet(votedSubset, training.numAttributes())));

                    //double selectedAverageScore = average(selectedScores);
                    //System.out.format("selected average score = %.4f%n", selectedAverageScore);

                    *//*HashSet<Integer> candidates = findCandidates(votes);
             BitSet climbed = climb(toBitSet(votedSubset, training.numAttributes()), candidates, eval);
             System.out.println("Aggregated Subset Score (climbed) = " + eval.evaluateSubset(climbed));*//*

                    double ensembleAccuracy = ClassifierCrossValidator.validateFilteredClassifier(classifier, votedSubset, training, testing);
                    ensembleAccuracies[j] = ensembleAccuracy;
                    //System.out.format("ensemble accuracy = %.4f%n", ensembleAccuracy);
                    *//*double ensembleClimbedAccuracy = ClassifierCrossValidator.validateFilteredClassifier(Registry.J48, toIntArray(climbed), training, testing);
               ensembleClimbedAccuracies[j] = ensembleClimbedAccuracy;
               System.out.format("ensemble climbed accuracy = %.4f%n", ensembleClimbedAccuracy);*//*
                    double fullAccuracy = ClassifierCrossValidator.validateClassifier(classifier, training, testing);
                    fullAccuracies[j] = fullAccuracy;
                    //System.out.format("full accuracy = %.4f%n", fullAccuracy);
                    double selectedAverageAccuracy = average(selectedAccuracies);
                    selectedAverageAccuracies[j] = selectedAverageAccuracy;
                    //System.out.format("selected full accuracy = %.4f%n", selectedAverageAccuracy);
                }
                System.out.println();
                System.out.format("ensemble average = %.4f%n", average(ensembleAccuracies));
                System.out.format("selected average = %.4f%n", average(selectedAverageAccuracies));
                System.out.format("full average = %.4f%n", average(fullAccuracies));
                System.gc();
            }
        }
    }*/
}
